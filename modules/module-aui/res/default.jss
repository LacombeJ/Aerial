
.root {
    background: white;
    color: black;
    font: calibri;
}

Widget {
    background: transparent;
}

Window {
    background: rgb(27, 32, 36);
}

Button {
    background: linear-gradient(rgb(32,32,32), rgb(48,48,48));
    color: white;
    border-color: darkgray;
    text-align: center;
    text-offset: (0,0);
    border: 1;
    radius: 6;
}

Button:hover {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border-color: gray;
}

Button:hover:down {
    background: linear-gradient(rgb(48,48,48), rgb(70,70,70));
}

CheckBox {
    background: darkgray;
    color: white;
    border-color: gray;
    border: 2;
    radius: 4;
    box-size: 18;
    box-align: left;
    box-offset: (0,0);
    text-align: left;
    text-offset: (24,0);
    image-color: white;
    image-align: left;
    image-offset: (3,0);
}

CheckBox:hover {
    border-color: rgb(120,130,180);
}

CheckBox:checked {
    image: "ui/check";
}

CheckBox:checked:hover {
    border-color: rgb(120,130,180);
}

ComboBox {
    background: linear-gradient(rgb(32,32,32), rgb(48,48,48));
    color: white;
    image: "ui/caret";
    image-align: right;
    image-offset: (-6,0);
    text-align: left;
    text-offset: (4,0);
    border-color: darkgray;
    border: 1;
}

ComboBox:hover {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border-color: gray;
}

ComboBox.Bar {
    background: transparent;
    border-color: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border: 2;
}

ComboBox.Button {
    background: rgba(0,0,0,128);
    color:white;
    text-align: left;
    text-offset: (4,0);
}

ComboBox.Button:hover {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    color:white;
    text-align: left;
    text-offset: (4,0);
    
}

Dial {
    background: linear-gradient(rgb(32,32,32), rgb(48,48,48));
    border-color: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border: 2;
    dial-size: 80%;
    knob-color: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    knob-border-color: gray;
    knob-border: 2;
    knob-size: 20%;
    knob-distance: 70%;
}

Dial:hover {
    background: linear-gradient(rgb(48,48,48), rgb(64,64,64));
}

Dial:hover:down {
    background: linear-gradient(rgb(64,64,64), rgb(96,96,96));
    border-color: linear-gradient(rgb(128,128,128), rgb(164,164,164));
    knob-border-color: linear-gradient(rgb(128,128,128), rgb(164,164,164));
}

Label {
    background: transparent;
    color: white;
    text-align: left;
    text-offset: (4,0);
}

LineEdit {
    background: darkgray;
    color: white;
    border: 2;
    border-color: gray;
    radius: 4;
    text-align: left;
    text-offset: (2,0);
}

LineEdit:hover {
    border-color: rgb(120,130,180);
}


Menu {
    background: rgba(0,0,0,128);
    border-color: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border: 2;
}

Menu.Bar {
    background: rgb(32,32,32);
}

Menu.Button {
    background: transparent;
    color:white;
}

Menu.Button:hover {
    background: rgb(64,64,64);
}

ScrollPanel.Bar {
    background: lightgray;
}

ScrollPanel.Button {
    background: linear-gradient(rgb(32,32,32), rgb(48,48,48));
    color: white;
    border-color: darkgray;
    text-align: center;
    text-offset: (0,0);
    border: 1;
    radius: 0;
}

ScrollPanel.Button:hover {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border-color: gray;
    image-align: left;
}

ScrollPanel.Button:down {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border-color: gray;
    image-align: left;
}

Slider {
    width: 4;
    background: gray;
    border: 1;
    border-color: black;
}

Slider.Button {
    background: linear-gradient(rgb(32,32,32), rgb(48,48,48));
    color: white;
    border-color: darkgray;
    text-align: center;
    text-offset: (0,0);
    border: 1;
    radius: 0;
}

Slider.Button:hover {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border-color: gray;
    image-align: left;
}

Slider.Button:down {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border-color: gray;
    image-align: left;
}

SplitPanel {
    background: rgba(32,32,32,128);
    border-color: linear-gradient(rgb(48,48,48), rgb(96,96,96));
    border: 1;
}

TabPanel {
    background: transparent;
}

TabPanel.Bar {
    background: transparent;
}

TabPanel.Button {
    background: linear-gradient(rgb(32,32,32), rgb(48,48,48));
    color: white;
    border-color: darkgray;
    text-align: left;
    text-offset: (4,0);
}

TabPanel.Button:checked {
    background: gray;
}

TabPanel.Button:hover {
    background: linear-gradient(rgb(64,64,64), rgb(96,96,96));
}

TabPanel.Content {
    background: transparent;
    border-color: gray;
    border: 2;
}

ToolBar {
    background: rgba(0,0,0,64);
    border-color: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    border: 2;
}

ToolButton {
    background: linear-gradient(rgb(32,32,32), rgb(48,48,48));
    border-color: darkgray;
    radius: 6;
    border: 1;
}

ToolButton:hover {
    background: linear-gradient(rgb(64,64,64), rgb(130,130,130));
    image-color: rgb(196,196,196);
}

ToolButton:hover:down {
    background: linear-gradient(rgb(48,48,48), rgb(70,70,70));
    image-color: rgb(164,164,164);
}

Tree.Item {
    background:transparent;
    color: white;
}

Tree.Item:hover {
    background: linear-gradient(rgba(64,64,64,128), rgba(130,130,130,128));
    color: white;
    border-color: darkgray;
    text-align: center;
    text-offset: (0,0);
    border: 1;
    radius: 6;
}

Tree.Drop {
    background: transparent;
}

Tree.Drop:hover {
    image-color: rgb(120,130,180);
}
