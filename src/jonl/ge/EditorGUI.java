package jonl.ge;

import jonl.aui.Align;
import jonl.aui.Border;
import jonl.aui.HAlign;
import jonl.aui.TreeItem;
import jonl.aui.VAlign;
import jonl.aui.sui.SFixedSplitPanel;
import jonl.aui.sui.SPanel;
import jonl.aui.sui.SScrollPanel;
import jonl.aui.sui.SSplitPanel;
import jonl.aui.sui.STree;
import jonl.aui.sui.SUIManager;
import jonl.aui.sui.SWindow;
import jonl.vmath.Vector4;

class EditorGUI {

    Editor editor;
    
    SUIManager ui;
    
    SWindow window;
        SFixedSplitPanel menuBarAndRest;
            SPanel menuBar;
            SFixedSplitPanel toolBarAndMainSplit;
                SPanel toolBar;
                SSplitPanel mainSplit;
                    SScrollPanel hierarchyScroll;
                        STree tree;
                    SSplitPanel editorSplit;
                        SSplitPanel consoleSplit;
                            SPanel editorViewer;
                            SPanel tabPanel;
                        SPanel propertyPanel;
    
    boolean resizable = true;
    boolean fullscreen = false;
    
    EditorGUI(Editor editor) {
        this.editor = editor;
    }
    
    void create() {
        
        ui = new SUIManager();
        
        //Window
        window = ui.window();
        window.setTitle("Nexus Engine");
        window.setWidth(1024);
        window.setHeight(576);
        window.setPosition(HAlign.CENTER,VAlign.MIDDLE);
        window.setResizable(resizable);
        window.create();
        {
            menuBarAndRest = ui.fixedSplitPanel();
            {
                menuBar = ui.panel();
                toolBarAndMainSplit = ui.fixedSplitPanel();
                {
                    toolBar = ui.panel();
                    mainSplit = ui.splitPanel();
                    {
                        hierarchyScroll = ui.scrollPanel();
                        {
                            tree = ui.tree();
                        }
                        hierarchyScroll.setScroll(tree,0,0,500,800);
                        editorSplit = ui.splitPanel();
                        {
                            consoleSplit = ui.splitPanel();
                            {
                                editorViewer = ui.panel();
                                tabPanel = ui.panel();
                            }
                            consoleSplit.setSplit(tabPanel,editorViewer,Align.VERTICAL,0.2);
                            propertyPanel = ui.panel();
                        }
                        editorSplit.setSplit(consoleSplit,propertyPanel,Align.HORIZONTAL,0.7);
                    }
                    mainSplit.setSplit(hierarchyScroll,editorSplit,Align.HORIZONTAL,0.2);
                }
                toolBarAndMainSplit.setSplit(toolBar,mainSplit,Border.TOP,32);
            }
            menuBarAndRest.setSplit(menuBar,toolBarAndMainSplit,Border.TOP,20);
        }
        window.setWidget(menuBarAndRest);
        
        
        menuBar.addPainter((g)->{
            g.renderRect(0,0,menuBar.getWidth(),menuBar.getHeight(),new Vector4(0.7f,0.7f,0.8f,1));
        });
        
        toolBar.addPainter((g)->{
            
            g.renderRect(0,0,toolBar.getWidth(),toolBar.getHeight(),new Vector4(0.9f,0.9f,0.9f,1));
        });
        
        TreeItem item = ui.treeItem("TreeRoot");
        {
            TreeItem child0 = ui.treeItem("Child0");
            {
                TreeItem child0_0 = ui.treeItem("Leaf0");
                child0.addItem(child0_0);
            }
            TreeItem child1 = ui.treeItem("Child1");
            TreeItem child2 = ui.treeItem("Child2");
            item.addItem(child0);
            item.addItem(child1);
            item.addItem(child2);
        }
        
        TreeItem item2 = ui.treeItem("AnotherTreeRoot");
        {
            TreeItem child0 = ui.treeItem("Child0");
            {
                TreeItem child0_0 = ui.treeItem("Leaf0");
                child0.addItem(child0_0);
            }
            TreeItem child1 = ui.treeItem("Child1");
            TreeItem child2 = ui.treeItem("Child2");
            item2.addItem(child0);
            item2.addItem(child1);
            item2.addItem(child2);
        }
        
        tree.addItem(item);
        tree.addItem(item2);
        
        window.setVisible(true);
        
        /*
        
        Button b0 = ui.button("Press Button");
        b0.addAction(()->{
            Console.print(b0.getText());
        });
        
        Button b1 = ui.button("Button 2");
        b1.addAction(()->{
            Console.print(b1.getText());
        });
        
        Button b2 = ui.button("Button 3");
        b2.addAction(()->{
            Console.print(b2.getText());
        });
        Button b3 = ui.button("abcdefghijklm");
        Button b4 = ui.button("nopqrstuvwxyz");
        Button b5 = ui.button("ABCDEFGHIJKLM");
        Button b6 = ui.button("NOPQRSTUVWXYZ");
        
        Dial d = ui.dial();
        Label label = ui.label("Dial value: "+d.getValue());
        d.addValueChangedListener((v,ov)->{
            label.setText("Dial value: "+d.getValue());
        });
        
        Panel p = ui.panel();
        WeightedListLayout sl = new WeightedListLayout(Align.VERTICAL,2,2,2,2,2,2,2,12,1);
        p.setLayout(sl);
        
        p.add(b0);
        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        p.add(b5);
        p.add(b6);
        p.add(d);
        p.add(label);
        
        ScrollPanel scroll = ui.scrollPanel();
        scroll.setWidget(p);
        scroll.setScrollWidth(400);
        scroll.setScrollHeight(768);
        
        editorViewer = ui.panel();
        
        SplitPanel total = ui.splitPanel(scroll,editorViewer,Align.HORIZONTAL,0.4f);
        
        window.setWidget(total);
        
        window.setVisible(true);
        */
    }
    
    
}
