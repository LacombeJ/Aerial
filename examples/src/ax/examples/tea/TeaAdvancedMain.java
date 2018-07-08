package ax.examples.tea;

import ax.aui.Align;
import ax.aui.Button;
import ax.aui.ComboBox;
import ax.aui.Dial;
import ax.aui.Label;
import ax.aui.LineEdit;
import ax.aui.List;
import ax.aui.ListLayout;
import ax.aui.Menu;
import ax.aui.MenuBar;
import ax.aui.Panel;
import ax.aui.ScrollPanel;
import ax.aui.SizePolicy;
import ax.aui.Slider;
import ax.aui.SplitPanel;
import ax.aui.TabPanel;
import ax.aui.Timer;
import ax.aui.ToolBar;
import ax.aui.ToolButton;
import ax.aui.Tree;
import ax.aui.TreeItem;
import ax.aui.UIManager;
import ax.aui.Widget;
import ax.aui.Window;
import ax.commons.func.Function0D;
import ax.commons.io.Console;
import ax.tea.TUIManager;

public class TeaAdvancedMain {

    public static void main(String[]args) {
        new TeaAdvancedMain().run();
    }
    
    Window window;
    Widget widget;
    
    void run() {
        
        TUIManager ui = TUIManager.instance();
        
        window = ui.window();
        
        window.call("SET_COLOR (3f, 1, 0.5, 1) ");
        
        window.setWidth(1024);
        window.setHeight(576);
        
        window.setResizable(true);
        
        Panel panel = ui.panel();
        ListLayout layout = ui.listLayout(Align.VERTICAL);
        layout.setMargin(0, 0, 0, 0);
        layout.setSpacing(0);
        panel.setLayout(layout);
        
        MenuBar mb = createMenuBar(ui);
        ToolBar tb = createToolBar(ui);
        SplitPanel sp = createSplitPanel(ui);
        Panel status = createStatusPanel(ui);
        
        layout.add(mb);
        layout.add(tb);
        layout.add(sp);
        layout.add(status);
        window.setWidget(panel);
        window.create();
        
        window.setVisible(true);
        
        
        
        //app(window, widget);
        
    }
    
    MenuBar createMenuBar(UIManager ui) {
        MenuBar mb = ui.menuBar();
        
        Menu file = ui.menu("File"); {
            file.add(ui.menu("Entering some text here..."));
            file.add(ui.menu("Entering some text here..."));
            file.add(ui.menu("Entering some text here..."));
            file.addSeparator();
            file.add(ui.menu("Entering some text here..."));
            file.add(ui.menu("Entering some text here..."));
            file.add(ui.menu("Entering some text here..."));
            file.add(ui.menu("Done..."));
        }
        Menu edit = ui.menu("Edit");
        Menu source = ui.menu("Source");
        Menu refactor = ui.menu("refactor");
        
        mb.add(file);
        mb.add(edit);
        mb.add(source);
        mb.add(refactor);
        
        return mb;
    }
    
    ToolBar createToolBar(UIManager ui) {
        ToolBar tb = ui.toolBar();
        
        ToolButton button = ui.toolButton();
        button.clicked().connect(()->{
            Console.log("Tool Button clicked");
        });
        
        ToolButton create = ui.toolButton();
        ToolButton save = ui.toolButton();
        ToolButton saveAll = ui.toolButton();
        
        tb.add(button);
        tb.add(create);
        tb.add(save);
        tb.add(saveAll);
        
        return tb;
    }
    
    SplitPanel createSplitPanel(UIManager ui) {
        
        SplitPanel split = ui.splitPanel();
        
        Panel sp = createSidePanel(ui);
        TabPanel tp = createTabPanel(ui);
        
        split.setWidgetOne(sp);
        split.setWidgetTwo(tp);
        split.setAlign(Align.HORIZONTAL);
        split.setRatio(0.2f);
        
        return split;
    }
    
    TabPanel createTabPanel(UIManager ui) {
        TabPanel tabPanel = ui.tabPanel();
        
        tabPanel.setAddable(true);
        tabPanel.setCloseable(true);
        tabPanel.newTab().connect(()->{
            tabPanel.add(ui.panel(),"NewTab");
        });
        
        ScrollPanel scrollPanel = createScrollPanel(ui);
        
        Function0D<Panel> nested = () -> {
            Panel level0 = ui.panel();
            
            level0.addStyle(
                "Widget {"
                + "     background: rgba(128,0,0,16);"
                + "     border-color: rgba(255,128,128,128);"
                + "     border: 1;"
                + "     radius: 2;"
                + "}");
            
            return level0;
        };
        
        // Nested panels validation
        Panel level0 = nested.f();
        Panel level1 = nested.f();
        Panel level2 = nested.f();
        level0.add(level1);
        level1.add(level2);
        
        Panel panel = nested.f();
        Button button = ui.button("Nested Button");
        button.clicked().connect(()->{
            Console.log("Nested button clicked");
            tabPanel.setIndex(0);
        });
        panel.add(button);
        
        level2.add(panel);
        
        Tree tree = createTree(ui);
        
        Panel listTest = createListPanel(ui);
        
        tabPanel.add(scrollPanel, "ScrollPanel Tab");
        tabPanel.add(level0, "Nested panels");
        tabPanel.add(tree, "Tree");
        tabPanel.add(ui.button("Button"), "Button Panel");
        tabPanel.add(listTest, "List Test");
        
        Timer t0 = ui.timer(scrollPanel,1000,true);
        t0.tick().connect(()->{
            Console.log("Timer 0");
        });
        
        Timer t1 = ui.timer(1000);
        t1.tick().connect(()->{
            Console.log("Timer 1");
        });
        
        return tabPanel;
    }
    
    Tree createTree(UIManager ui) {
        Tree tree = ui.tree();
        
        TreeItem alpha = ui.treeItem("Alpha"); {
            alpha.addItem(ui.treeItem("A"));
            alpha.addItem(ui.treeItem("B"));
            alpha.addItem(ui.treeItem("C"));
            alpha.addItem(ui.treeItem("D"));
            alpha.addItem(ui.treeItem("E"));
            alpha.addItem(ui.treeItem("F"));
            alpha.addItem(ui.treeItem("G"));
            alpha.addItem(ui.treeItem("H"));
            alpha.addItem(ui.treeItem("I"));
        }
        TreeItem beta = ui.treeItem("Beta");
        TreeItem gamma = ui.treeItem("Gamma");
        TreeItem delta = ui.treeItem("Delta");
        TreeItem epsilon = ui.treeItem("Epsilon");
        TreeItem zeta = ui.treeItem("Zeta");
        TreeItem eta = ui.treeItem("Eta");
        TreeItem theta = ui.treeItem("Theta");
        TreeItem iota = ui.treeItem("Iota");
        TreeItem kappa = ui.treeItem("Kappa");
        TreeItem lambda = ui.treeItem("Lamda");
        TreeItem mu = ui.treeItem("Mu");
        TreeItem nu = ui.treeItem("Nu");
        TreeItem xi = ui.treeItem("Xi");
        TreeItem omicron = ui.treeItem("Omicron");
        TreeItem pi = ui.treeItem("Pi");
        TreeItem rho = ui.treeItem("Rho");
        TreeItem sigma = ui.treeItem("Sigma");
        TreeItem tau = ui.treeItem("Tau");
        TreeItem upsilon = ui.treeItem("Upsilon");
        TreeItem phi = ui.treeItem("Phi");
        TreeItem chi = ui.treeItem("Chi");
        TreeItem psi = ui.treeItem("Psi");
        TreeItem omega = ui.treeItem("Omega");
        
        tree.addItem(alpha);
        tree.addItem(beta);
        tree.addItem(gamma);
        tree.addItem(delta);
        tree.addItem(epsilon);
        tree.addItem(zeta);
        tree.addItem(eta);
        tree.addItem(theta);
        tree.addItem(iota);
        tree.addItem(kappa);
        tree.addItem(lambda);
        tree.addItem(mu);
        tree.addItem(nu);
        tree.addItem(xi);
        tree.addItem(omicron);
        tree.addItem(pi);
        tree.addItem(rho);
        tree.addItem(sigma);
        tree.addItem(tau);
        tree.addItem(upsilon);
        tree.addItem(phi);
        tree.addItem(chi);
        tree.addItem(psi);
        tree.addItem(omega);
        return tree;
    }
    
    Panel createListPanel(UIManager ui) {
        Panel panel = ui.panel();
        
        List list = ui.list();
        
        list.add(ui.button("Hello world"));
        list.add(ui.button("Another button"));
        list.add(ui.button("And another one"));
        String text = "And some more...";
        for (int i=0; i<15; i++) {
            list.add(ui.button(text));
            text += ".";
        }
        
        panel.add(list);
        
        return panel;
    }
    
    Panel createSidePanel(UIManager ui) {
        Panel panel = ui.panel(ui.listLayout(Align.VERTICAL));
        
        Dial dial = ui.dial();
        
        dial.setMin(100);
        dial.setMax(110);
        
        
        LineEdit dialValue = ui.lineEdit("0");
        dialValue.setSizePolicy(new SizePolicy(SizePolicy.FIXED, SizePolicy.FIXED));
        dialValue.setSizeConstraint(32,-1);
        
        dial.changed().connect((value)->{
            dialValue.setText(value+"");
        });
        
        Slider vert = ui.slider(Align.VERTICAL);
        
        Slider slider = ui.slider();
        
        slider.pressed().connect(()->{
            Console.log("Slider pressed");
        });
        
        slider.changed().connect((v)->{
            Console.log("Value:", v);
            vert.setValue(v);
        });
        
        panel.add(dial);
        panel.add(dialValue);
        panel.add(slider);
        panel.add(vert);
        
        return panel;
    }
    
    ScrollPanel createScrollPanel(UIManager ui) {
        ScrollPanel scrollPanel = ui.scrollPanel();
        
        Panel p = ui.panel(ui.listLayout(Align.VERTICAL));
        
        Button refresh = ui.button("Refresh class path default.jss");
        refresh.clicked().connect(()->{
            ((TUIManager)ui).setDefaultStyle();
        });
        p.add(refresh);
        
        Button refresh1 = ui.button("Set style light.jss");
        refresh1.clicked().connect(()->{
            ((TUIManager)ui).setLightStyle();
        });
        p.add(refresh1);
        
        Button refresh3 = ui.button("Set style dark.jss");
        refresh3.clicked().connect(()->{
            ((TUIManager)ui).setDarkStyle();
        });
        p.add(refresh3);
        
        Button abutton = ui.button("A button");
        p.add(abutton);
        
        Button another = ui.button("Another button");
        another.setEnabled(false);
        p.add(another);
        
        p.add(ui.button("And another one"));
        
        p.add(ui.checkBox("A check Box"));
        
        ComboBox comboBox = ui.comboBox();
        comboBox.add("The first combobox option");
        comboBox.add("Hello, world");
        comboBox.add("More options");
        p.add(comboBox);
        
        Panel h = ui.panel(ui.listLayout(Align.HORIZONTAL));
        h.layout().setMargin(0, 0, 0, 0);
        h.layout().setSpacing(0);
        
        LineEdit lineEdit = ui.lineEdit("Hello, here's some text");
        
        Dial dial = ui.dial();
        
        dial.changed().connect((value)->{
            lineEdit.setText("Here's some text: "+value);
        });
        
        dial.setMinSize(300, 300);
        
        h.add(lineEdit);
        
        h.add(dial);
        
        p.add(h);
        
        //p.add(ui.spacerItem());
        
        scrollPanel.setWidget(p);
        
        widget = p;
        
        return scrollPanel;
    }
    
    Panel createStatusPanel(UIManager ui) {
        Panel panel = ui.panel(ui.listLayout(Align.HORIZONTAL));
        
        panel.setMaxSize(Integer.MAX_VALUE, 32);
        
        Label label = ui.label("Jonathan's Level Editor UI - 3/14/18");
        
        panel.add(label);
        
        return panel;
    }
    
}
