package jonl.ge.base.app;

import jonl.aui.Align;
import jonl.aui.HAlign;
import jonl.aui.Panel;
import jonl.aui.ScrollPanel;
import jonl.aui.SplitPanel;
import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.aui.UIManager;
import jonl.aui.VAlign;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TUIManager;
import jonl.aui.tea.TWindow;
import jonl.ge.core.Editor;
import jonl.vmath.Vector4;

public class EditorGUI {

	public Editor editor;
    
	public UIManager ui;
    
    public TWindow window;
    public Panel menuBarAndRest;
    public Panel menuBar;
    public Panel toolBarAndMainSplit;
    public Panel toolBar;
    public SplitPanel mainSplit;
    public ScrollPanel hierarchyScroll;
    public Tree tree;
    public SplitPanel editorSplit;
    public SplitPanel consoleSplit;
    public Panel editorViewer;
    public Panel tabPanel;
    public Panel propertyPanel;
    
    public boolean resizable = true;
    public boolean fullscreen = false;
    
    public EditorGUI(Editor editor) {
        this.editor = editor;
    }
    
    public void create() {
        
        ui = TUIManager.instance();
        
        //Window
        window = (TWindow) ui.window();
        window.setTitle("Engine");
        window.setWidth(1024);
        window.setHeight(576);
        window.setPosition(HAlign.CENTER,VAlign.MIDDLE);
        window.setResizable(resizable);
        window.create();
        {
            //menuBarAndRest = ui.fixedSplitPanel();
            {
                menuBar = ui.panel();
                //toolBarAndMainSplit = ui.fixedSplitPanel();
                {
                    toolBar = ui.panel();
                    mainSplit = ui.splitPanel();
                    {
                        hierarchyScroll = ui.scrollPanel();
                        {
                            tree = ui.tree();
                        }
                        //hierarchyScroll.setScroll(tree,0,0,200,800);
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
                //toolBarAndMainSplit.setSplit(toolBar,mainSplit,Border.TOP,32);
            }
            //menuBarAndRest.setSplit(menuBar,toolBarAndMainSplit,Border.TOP,20);
        }
        window.setWidget(menuBarAndRest);
        
        //TODO remove TGraphics cast with an implementation in graphics
        
        menuBar.paint().connect((g)->{
            ((TGraphics)g).renderRect(0,0,menuBar.width(),menuBar.height(),new Vector4(0.7f,0.7f,0.8f,1));
        });
        
        toolBar.paint().connect((g)->{
            ((TGraphics)g).renderRect(0,0,toolBar.width(),toolBar.height(),new Vector4(0.9f,0.9f,0.9f,1));
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
        
    }
    
    
}
