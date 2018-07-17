package ax.examples.tea;

import ax.aui.*;
import ax.aui.Menu;
import ax.aui.MenuBar;
import ax.aui.Panel;
import ax.aui.Window;
import ax.commons.io.Console;
import ax.tea.TUIManager;

public class TeaDemoMain {

    public static void main(String[]args) {
        new TeaDemoMain().run();
    }

    void run() {

        TUIManager ui = TUIManager.instance();
        ui.setDarkStyle();

        Window window = ui.window();
        window.setWidth(1024);
        window.setHeight(576);

        window.setResizable(true);

        ListLayout layout = ui.listLayout(Align.VERTICAL, new Margin(0,0,0,0), 0);
        Panel panel = ui.panel(layout);

        Timer timer = ui.timer(panel, 10000, true);
        timer.tick().connect(()->{
            Console.log("10 seconds have passed");
        });

        MenuBar menuBar = ui.menuBar();
        {
            Menu fileMenu = ui.menu("File");
            {
                Menu openMenu = ui.menu("Open File");
                Menu closeMenu = ui.menu("Exit Program");
                fileMenu.add(openMenu);
                fileMenu.addSeparator();
                fileMenu.add(closeMenu);
                openMenu.clicked().connect(()->{
                    FileDialog fileDialog = ui.fileDialog();
                    FileDialog.State state = fileDialog.showOpenDialog();
                    String path = fileDialog.selected();
                    Console.log("File path:", path);
                    Console.log("File Dialog Return State:",state);
                });
                closeMenu.clicked().connect(()->{
                    System.exit(0);
                });
            }
            Menu editMenu = ui.menu("Edit");
            menuBar.add(fileMenu);
            menuBar.add(editMenu);
        }
        panel.add(menuBar);

        ToolBar toolBar = ui.toolBar(); {
            ToolButton newButton = ui.toolButton();
            ToolButton openButton = ui.toolButton();
            ToolButton saveButton = ui.toolButton();
            toolBar.add(newButton);
            toolBar.add(openButton);
            toolBar.add(saveButton);

            newButton.clicked().connect(()->{
                Console.log("New ToolButton clicked");
                MessageDialog messageDialog = ui.messageDialog();
                MessageDialog.State state = messageDialog.showDialog(
                        MessageDialog.Message.INFORMATION,
                        "Message Dialog title",
                        "Message Dialog Text",
                        MessageDialog.Option.OK_CANCEL_OPTION);
                Console.log("Message Dialog Return State:", state);
            });
        }
        panel.add(toolBar);

        TabPanel tabPanel = ui.tabPanel();
        {
            SplitPanel primary = ui.splitPanel(Align.HORIZONTAL); {
                ArrayLayout arrayLayout = ui.arrayLayout();
                Panel leftPanel = ui.panel(arrayLayout); {
                    Button lightStyle = ui.button("Click to change style to light.jss");
                    Button darkStyle = ui.button("Click to change style to light.jss");

                    Button button = ui.button("Button");
                    Button customButton = ui.button("Custom Button");
                    customButton.addStyle(
                        "Button {"
                        + "     background: linear-gradient(rgb(32,16,16), rgb(48,24,24));"
                        + "     border-color: rgba(64,32,32,255);"
                        + "     border: 2;"
                        + "     radius: 12;"
                        + "}"
                        + "Button:hover {"
                        + "     background: linear-gradient(rgb(64,32,32), rgb(96,48,48));"
                        + "     border-color: rgba(128,64,64,255);"
                        + "}"
                        + "Button:hover:down {"
                        + "     background: linear-gradient(rgb(48,24,24), rgb(64,36,36));"
                        + "     border-color: rgba(96,48,48,255);"
                        + "}");
                    customButton.setMinSize(0,50);
                    CheckBox checkBox = ui.checkBox("Check Box");
                    ComboBox comboBox = ui.comboBox(); {
                        comboBox.add("ComboBox Entry #0");
                        comboBox.add("ComboBox Entry #1");
                    }
                    Dial dial = ui.dial();
                    Label label = ui.label("Label");
                    LineEdit lineEdit = ui.lineEdit("Line Edit");
                    RadioButton radioButton = ui.radioButton("Radio Button");
                    RadioButton radioButton2 = ui.radioButton("Another Radio Button");
                    Slider slider = ui.slider(Align.HORIZONTAL);
                    Spacer spacer = ui.spacer(Align.VERTICAL);

                    int row = 0;
                    arrayLayout.add(darkStyle,row++,0);
                    arrayLayout.add(ui.label("Click to change style to light.jss"), row, 0);
                    arrayLayout.add(lightStyle,row++,1);
                    arrayLayout.add(ui.label("Click to change style to dark.jss"), row, 0);
                    arrayLayout.add(darkStyle,row++,1);
                    arrayLayout.add(ui.label("This is a button:"), row, 0);
                    arrayLayout.add(button, row++, 1);
                    arrayLayout.add(ui.label("This is a custom button:"), row, 0);
                    arrayLayout.add(customButton, row++, 1);
                    arrayLayout.add(ui.label("This is a checkBox:"), row, 0);
                    arrayLayout.add(checkBox, row++, 1);
                    arrayLayout.add(ui.label("This is a comboBox:"), row, 0);
                    arrayLayout.add(comboBox, row++, 1);
                    arrayLayout.add(ui.label("This is a dial:"), row, 0);
                    arrayLayout.add(dial, row++, 1);
                    arrayLayout.add(ui.label("This is a label:"), row, 0);
                    arrayLayout.add(label, row++, 1);
                    arrayLayout.add(ui.label("This is a lineEdit:"), row, 0);
                    arrayLayout.add(lineEdit, row++, 1);
                    arrayLayout.add(ui.label("This is a radioButton:"), row, 0);
                    arrayLayout.add(radioButton, row++, 1);
                    arrayLayout.add(ui.label("This is another radioButton:"), row, 0);
                    arrayLayout.add(radioButton2, row++, 1);
                    arrayLayout.add(ui.label("This is a slider:"), row, 0);
                    arrayLayout.add(slider, row++, 1);
                    arrayLayout.add(ui.label("This is a spacer:"), row, 0);
                    arrayLayout.add(spacer, row++, 1);

                    lightStyle.clicked().connect(()->{
                        ui.setLightStyle();
                    });
                    darkStyle.clicked().connect(()->{
                        ui.setDarkStyle();
                    });

                    button.clicked().connect(()->{
                        Console.log("Button clicked.");
                    });
                    customButton.clicked().connect(()->{
                        Console.log("Custom Button clicked.");
                    });
                    checkBox.toggled().connect((toggled)->{
                        Console.log("CheckBox toggled:", toggled);
                    });
                    comboBox.changed().connect((index,text)->{
                        Console.log("ComboBox changed:", index, text);
                    });
                    dial.changed().connect((value)->{
                        Console.log("Dial changed:", value);
                    });
                    lineEdit.changed().connect((text)->{
                        Console.log("LineEdit changed:", text);
                    });
                    radioButton.clicked().connect(()->{
                        Console.log("RadioButton #1 clicked");
                    });
                    radioButton2.clicked().connect(()->{
                        Console.log("RadioButton #2 clicked");
                    });
                    slider.changed().connect((value)->{
                        Console.log("Slider changed:", value);
                    });
                }
                Tree tree = ui.tree(); {
                    TreeItem treeItem = ui.treeItem("Click to expand me.");
                    for (int i=0; i<10; i++) {
                        TreeItem subItem = ui.treeItem("SubItem #"+i);
                        treeItem.addItem(subItem);
                    }
                    tree.addItem(treeItem);
                    for (int i=0; i<20; i++) {
                        TreeItem anotherItem = ui.treeItem("TreeItem #"+i);
                        tree.addItem(anotherItem);
                    }
                }
                primary.setWidgets(leftPanel,tree);
            }
            Panel secondary = ui.panel();
            tabPanel.add(primary,"Primary");
            tabPanel.add(secondary,"Secondary");
        }
        panel.add(tabPanel);

        window.setWidget(panel);
        window.create();
        window.setVisible(true);

    }

}
