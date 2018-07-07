package ax.aui.tea;

import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.UnsupportedLookAndFeelException;

import ax.aui.Align;
import ax.aui.Font;
import ax.aui.Icon;
import ax.aui.Layout;
import ax.aui.Margin;
import ax.aui.Resource;
import ax.aui.Timer;
import ax.aui.UIManager;
import ax.aui.Widget;
import ax.commons.io.FileUtils;
import ax.commons.jss.Style;
import ax.commons.jss.StyleSheet;
import ax.commons.misc.ImageUtils;

public class TUIManager implements UIManager {
    
    private static TUIManager ui = null;
    
    public static TUIManager instance() {
        if (ui==null) {
            ui = new TUIManager();
        }
        return ui;
    }
    
    static {
        // Because we are using JDialogs, we will set the LAF to the system
        // If ui no longer has awt dependency, remove this
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    
    private Style styleSheet = defaultStyle();
    
    private TResourceMap map = new TResourceMap();
    
    private TUIManager() {
        loadResources();
    }
    
    private ArrayList<TTimer> timers = new ArrayList<>();
    
    ArrayList<TTimer> timers() {
        return timers;
    }
    
    @Override
    public void setStyle(String style) {
        styleSheet = StyleSheet.fromString(style);
    }
    
    @Override
    public void setStyle(Style style) {
        styleSheet = style.copy();
    }
    
    @Override
    public void addStyle(String style) {
        styleSheet.append(StyleSheet.fromString(style));
    }
    
    @Override
    public void addStyle(Style style) {
        styleSheet.append(style);
    }
    
    public Style styleSheet() {
        return styleSheet;
    }
    
    public void setDefaultStyle() {
        styleSheet = defaultStyle();
    }
    
    public void setLightStyle() {
        styleSheet = lightStyle();
    }
    
    public void setDarkStyle() {
        styleSheet = darkStyle();
    }
    
    @Override
    public TResource resource(String key) {
        return map.getResource(key);
    }
    
    @Override
    public void resource(String key, Object data) {
        map.putResource(key,data);
    }
    
    @Override
    public TFileDialog fileDialog() {
        return new TFileDialog();
    }
    
    @Override
    public TMessageDialog messageDialog() {
        return new TMessageDialog();
    }
    
    @Override
    public TWindow window() {
        return new TWindow();
    }
    
    @Override
    public TFrame frame() {
        return new TFrame();
    }

    @Override
    public TButton button() {
        return new TButton();
    }

    @Override
    public TButton button(String text) {
        return new TButton(text);
    }
    
    @Override
    public TCheckBox checkBox() {
        return new TCheckBox();
    }

    @Override
    public TCheckBox checkBox(String text) {
        return new TCheckBox(text);
    }

    @Override
    public TLabel label() {
        return new TLabel();
    }

    @Override
    public TLabel label(String text) {
        return new TLabel(text);
    }
    
    @Override
    public TLineEdit lineEdit() {
        return new TLineEdit();
    }

    @Override
    public TLineEdit lineEdit(String text) {
        return new TLineEdit(text);
    }
    
    @Override
    public TList list() {
        return new TList();
    }
    
    @Override
    public TList list(Align align) {
        return new TList(align);
    }
    
    @Override
    public TList list(Align align, int scrollType) {
        return new TList(align, scrollType);
    }
    
    @Override
    public TComboBox comboBox() {
        return new TComboBox();
    }

    @Override
    public TDial dial() {
        return new TDial();
    }
    
    @Override
    public TDial dial(int min, int max) {
        return new TDial(min,max);
    }
    
    @Override
    public TSlider slider() {
        return new TSlider();
    }
    
    @Override
    public TSlider slider(Align align) {
        return new TSlider(align);
    }
    
    @Override
    public TSlider slider(Align align, int min, int max) {
        return new TSlider(align, min, max);
    }

    @Override
    public TPanel panel() {
        return new TPanel();
    }

    @Override
    public TPanel panel(Layout layout) {
        return new TPanel(layout);
    }
    
    @Override
    public TRadioButton radioButton() {
        return new TRadioButton();
    }

    @Override
    public TRadioButton radioButton(String text) {
        return new TRadioButton(text);
    }
    
    @Override
    public TTitlePanel titlePanel(String title) {
        return new TTitlePanel(title);
    }

    @Override
    public TTitlePanel titlePanel(String title, Layout layout) {
        return new TTitlePanel(title,layout);
    }

    @Override
    public TSplitPanel splitPanel() {
        return new TSplitPanel();
    }
    
    @Override
    public TSplitPanel splitPanel(Align align) {
        return new TSplitPanel(align);
    }

    @Override
    public TSplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio) {
        return new TSplitPanel(w1,w2,align,ratio);
    }

    @Override
    public TScrollPanel scrollPanel() {
        return new TScrollPanel();
    }
    
    @Override
    public TSwitchWidget switchWidget() {
        return new TSwitchWidget();
    }
    
    @Override
    public TTabPanel tabPanel() {
        return new TTabPanel();
    }

    @Override
    public TTree tree() {
        return new TTree();
    }

    @Override
    public TTreeItem treeItem() {
        return new TTreeItem();
    }

    @Override
    public TTreeItem treeItem(String text) {
        return new TTreeItem(text);
    }
    
    @Override
    public TMenuBar menuBar() {
        return new TMenuBar();
    }
    
    @Override
    public TMenu menu(String text) {
        return new TMenu(text);
    }
    
    @Override
    public TToolBar toolBar() {
        return new TToolBar();
    }
    
    @Override
    public TToolButton toolButton() {
        return new TToolButton();
    }
    
    @Override
    public TToolButton toolButton(Icon icon) {
        return new TToolButton(icon);
    }
    
    @Override
    public TToolButton toolButton(Resource icon) {
        return new TToolButton(icon);
    }
    
    @Override
    public TOverlay overlay() {
        return new TOverlay();
    }
    
    @Override
    public Timer timer(long interval) {
        return new TTimer(interval);
    }
    
    @Override
    public Timer timer(Widget widget, long interval) {
        return new TTimer((TWidget)widget,interval);
    }
    
    @Override
    public Timer timer(Widget widget, long interval, boolean singleShot) {
        return new TTimer((TWidget)widget,interval,singleShot);
    }
    
    @Override
    public TIcon icon(String file) {
        return new TIcon(file);
    }

    @Override
    public Font font(String font, int size) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public TSpacer spacer() {
        return new TSpacer();
    }
    
    @Override
    public TSpacer spacer(Align align) {
        return new TSpacer(align);
    }

    @Override
    public TListLayout listLayout() {
        return new TListLayout();
    }

    @Override
    public TListLayout listLayout(Align align) {
        return new TListLayout(align);
    }
    
    @Override
    public TListLayout listLayout(Align align, Margin margin, int spacing) {
        return new TListLayout(align, margin, spacing);
    }
    
    @Override
    public TArrayLayout arrayLayout() {
        return new TArrayLayout();
    }
    
    private Style setResourceStyle(String style) {
        InputStream in = getClass().getResourceAsStream(style);
        Style jss = StyleSheet.fromString(FileUtils.readFromStream(in));
        return jss;
    }
    
    private Style defaultStyle() {
        return lightStyle();
    }
    
    private Style lightStyle() {
        return setResourceStyle("/light.jss");
    }
    
    private Style darkStyle() {
        return setResourceStyle("/dark.jss");
    }
    
    private void resourceIcon(String loc, String resource) {
        InputStream in = getClass().getResourceAsStream(loc);
        if (in != null) {
            resource(resource, new TIcon(ImageUtils.load(in)));
        } else {
            System.err.println("Failed to find icon resource: "+loc);
        }
    }
    
    private void loadResources() {
        resourceIcon("/check.png",      "ui/check");
        resourceIcon("/caret.png",      "ui/caret");
        resourceIcon("/dot.png",        "ui/dot");
        resourceIcon("/add.png",        "ui/add");
        resourceIcon("/subtract.png",   "ui/subtract");
        resourceIcon("/remove.png",     "ui/remove");
        resourceIcon("/maximize.png",   "ui/maximize");
    }
    
}
