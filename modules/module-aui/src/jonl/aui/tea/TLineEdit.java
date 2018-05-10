package jonl.aui.tea;

import jonl.aui.LineEdit;
import jonl.aui.Signal;
import jonl.aui.SizePolicy;
import jonl.aui.tea.event.TFocusEvent;
import jonl.aui.tea.event.TKeyEvent;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.LineEditRenderer;
import jonl.aui.tea.graphics.TFont;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;

public class TLineEdit extends TWidget implements LineEdit {

    private String text = "";
    
    private Signal<Callback<String>> changed = new Signal<>();
    
    private Signal<Callback<String>> finished = new Signal<>();
    
    public TLineEdit() {
        super();
        setSizePolicy(new SizePolicy(SizePolicy.EXPANDING, SizePolicy.FIXED));
        
        TFillLayout layout = new TFillLayout();
        layout.setMargin(2,2,0,0);
        TScrollArea area = new TScrollArea(false,true);
        TextArea textArea = new TextArea(this);
        
        area.setScrollWidget(textArea);
        
        layout.add(area);
        setWidgetLayout(layout);
        
    }
    
    public TLineEdit(String text) {
        this();
        this.text = text;
    }
    
    @Override
    public String text() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        changed().emit((cb)->cb.f(text));
        invalidateSizeHint();
    }
    
    @Override
    public Signal<Callback<String>> changed() { return changed; }
    
    @Override
    public Signal<Callback<String>> finished() { return finished; }

    // ------------------------------------------------------------------------
    
    protected void paint(TGraphics g) {
        LineEditRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseEnter(TMouseEvent event) {
        info().put("bIsMouseWithin", true);
        setCursor(TCursor.IBEAM);
        return true;
    }
    
    @Override
    protected boolean handleMouseExit(TMouseEvent event) {
        info().put("bIsMouseWithin", false);
        setCursor(TCursor.ARROW);
        return true;
    }
    
    
    public static class TextArea extends TWidget {
        
        TFont font = TSizeReasoning.FONT;
        
        TLineEdit edit;
        int caret;
        
        TextArea(TLineEdit edit) {
            this.edit = edit;
            setKeyFocusSupport(true);
        }
        
        public TLineEdit edit() {
            return edit;
        }
        
        @Override
        protected TSizeHint sizeHint() {
            return TSizeReasoning.lineEdit(edit);
        }
        
        @Override
        protected void paint(TGraphics g) {
            LineEditRenderer.paint(this,g,edit.info());
            paint().emit(cb->cb.f(g));
        }
        
        @Override
        protected boolean handleMouseButtonPress(TMouseEvent event) {
            if (event.button==Input.MB_LEFT) {
                caret = index(event.x);
                edit.info().put("bCaretVisible", true);
                edit.info().put("iBlinkValue",0);
                return true;
            }
            return false;
        }
        
        private boolean handleKeyTyped(TKeyEvent event) {
            if (event.charValid) {
                String text = edit.text();
                text = text.substring(0, caret) + event.character + text.substring(caret, text.length());
                caret++;
                edit.setText(text);
            }
            if (event.key==TInput.K_BACKSPACE) {
                if (caret > 0) {
                    String text = edit.text();
                    text = text.substring(0, caret-1) + text.substring(caret, text.length());
                    caret--;
                    edit.setText(text);
                }
            }
            if (event.key==TInput.K_DELETE) {
                if (caret < edit.text.length()) {
                    String text = edit.text();
                    text = text.substring(0, caret) + text.substring(caret+1, text.length());
                    edit.setText(text);
                }
            }
            if (event.key==TInput.K_LEFT) {
                if (caret>0) {
                    caret--;
                }
                edit.info().put("bCaretVisible", true);
                edit.info().put("iBlinkValue",0);
            }
            if (event.key==Input.K_RIGHT) {
                if (caret < edit.text.length()) {
                    caret++;
                }
                edit.info().put("bCaretVisible", true);
                edit.info().put("iBlinkValue",0);
            }
            if (event.key==Input.K_ENTER || event.key==Input.K_NP_ENTER) {
                releaseKeyFocus();
            }
            return true;
        }
        
        @Override
        protected boolean handleKeyPress(TKeyEvent event) {
            return handleKeyTyped(event);
        }
        
        @Override
        protected boolean handleKeyRepeat(TKeyEvent event) {
            return handleKeyTyped(event);
        }
        
        @Override
        protected boolean handleFocusLost(TFocusEvent event) {
            edit.finished().emit((cb)->cb.f(edit.text));
            return true;
        }
        
        public int caret() {
            return caret;
        }
        
        public int caretX() {
            String sub = edit.text.substring(0,caret);
            return font.getWidth(sub);
        }
        
        public int caretHeight() {
            return font.getHeight();
        }
        
        public int index(int x) {
            if (x < 0) {
                return 0;
            }
            if (x > font.getWidth(edit.text)) {
                return edit.text.length();
            }
            
            int index = 0;
            int length = edit.text.length();
            int width = 0;
            
            for (int i=0; i<length; i++) {
                char c = edit.text.charAt(i);
                int cwidth = font.getWidth(c+"");
                int prevWidth = width;
                width += cwidth;
                if (x <= width) {
                    int halfWidth = (prevWidth + width) / 2;
                    if (x < halfWidth) {
                        return index;
                    } else {
                        return index+1;
                    }
                }
                index++;
            }
            
            return index;
        }
        
    }
    
}
