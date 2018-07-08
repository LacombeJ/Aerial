package ax.examples.engine.grass;

import ax.aui.Align;
import ax.aui.ArrayLayout;
import ax.aui.Justify;
import ax.aui.Label;
import ax.aui.LineEdit;
import ax.aui.Overlay;
import ax.aui.Panel;
import ax.aui.Slider;
import ax.aui.TabPanel;
import ax.aui.Window;
import ax.tea.TUIManager;

public class MovingGrassMain {

    public static void main(String[] args) {
        new MovingGrassMain().start();
    }
    
    Window window;
    TabPanel panel;
    Overlay gPanel;
    Panel menuPanel;
    Overlay texturePanel;
    Panel textureMenu;
    
    MovingGrassTextureScene mgtScene;
    
    void start() {
        ui();
        mgtScene = new MovingGrassTextureScene(window,texturePanel);
        new MovingGrassScene(window,gPanel);
    }
    
    void ui() {
        
        TUIManager ui = TUIManager.instance();
        
        ui.setDarkStyle();
        
        window = ui.window();
        window.setResizable(true);
        window.setWidth(1024);
        window.setHeight(576);
        
        panel = ui.tabPanel();
        
        gPanel = ui.overlay(); {
            menuPanel = ui.panel(ui.listLayout(Align.VERTICAL));
            
            gPanel.add(menuPanel,10,10,200,150,Justify.TOP_LEFT);
        }
        
        texturePanel = ui.overlay(); {
            ArrayLayout array = ui.arrayLayout();
            textureMenu = ui.panel(array);
            
            Label freqLabel = ui.label("Frequency");
            Slider freqSlider = ui.slider(Align.HORIZONTAL,0,100);
            LineEdit freqEdit = ui.lineEdit("---");
            freqSlider.changed().connect((v)->{
                float f = v/5000f;
                mgtScene.mgt.frequency = f;
                freqEdit.setText(f+"");
            });
            
            
            
            array.add(freqLabel,0,0);
            array.add(freqSlider,0,1);
            array.add(freqEdit,0,2);
            
            texturePanel.add(textureMenu,10,10,200,150,Justify.TOP_LEFT);
        }
        
        panel.add(gPanel,"Scene");
        
        panel.add(texturePanel,"Texture");
        
        window.setWidget(panel);
        window.create();
        window.setVisible(true);
        
    }
    

}
