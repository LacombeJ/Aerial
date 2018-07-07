
/*
Light stylesheet
*/

// Variable Definitions
// ----------------------------------------------------------------------------
.define {
    c_back:             white;
    c_text:             black;
    c_background:       rgb(240,240,240);
    c_plain:            rgb(225,225,225);
    c_plain_border:     rgb(173,173,173);
    c_hover:            rgb(229,241,251);
    c_hover_border:     rgb(0,120,215);
    c_select:           rgb(204,228,247);
    c_select_border:    rgb(0,84,153);
    c_primary:          lightgray;
    c_secondary:        gray;
    c_tertiary:         darkgray;
    c_frame_button:     rgb(173,173,173);
    c_minmax:           rgba(164,164,164,128);
    c_minmax_down:      rgba(96,96,96,128);
    c_close:            rgba(255,0,0,128);
    c_close_down:       rgba(196,0,0,128);
    c_check:            black;
    c_combo:            white;
    c_combo_border:     black;
}

// Root style
// ----------------------------------------------------------------------------
.root {
    background: back;
    color: c_text;
    font: calibri;
}

// "Abstract" item style
// ----------------------------------------------------------------------------
_Item {
    background: c_plain;
    border-color: c_plain_border;
    border: 1;
    color: c_text;
}
_Item:disabled {
    background: c_primary;
    color: c_secondary;
    border-color: c_secondary;
}
_Item:hover {
    background: c_hover;
    border-color: c_hover_border;
}
_Item:hover:down {
    background: c_select;
    border-color: c_select_border;
}



// ----------------------------------------------------------------------------
Widget {
    background: transparent;
}
// ----------------------------------------------------------------------------
Window {
    background: c_background;
}
// ----------------------------------------------------------------------------
Button {
    @extend _Item;
    text-align: center;
    text-offset: (0,0);
}
Button:disabled {
    @extend _Item:disabled;
}
Button:hover {
    @extend _Item:hover;
}
Button:hover:down {
    @extend _Item:down;
}
// ----------------------------------------------------------------------------
CheckBox {
    @extend _Item;
    box-size: 18;
    box-align: left;
    box-offset: (0,0);
    text-align: left;
    text-offset: (24,0);
    image-color: c_check;
    image-align: left;
    image-offset: (3,0);
}
CheckBox:disabled {
    @extend _Item:disabled;
}
CheckBox:hover {
    @extend _Item:hover;
}
CheckBox:checked {
    image: "ui/check";
}
CheckBox:checked:hover {
    @extend _Item:hover;
}
// ----------------------------------------------------------------------------
ComboBox {
    @extend _Item;
    image: "ui/caret";
    image-align: right;
    image-offset: (-6,0);
    image-color: c_plain_border;
    text-align: left;
    text-offset: (4,0);
}
ComboBox:disabled {
    @extend _Item:disabled;
}
ComboBox:hover:hover {
    @extend _Item:hover;
}
ComboBox:hover:hover:down {
    @extend _Item:down;
}
ComboBox.Bar {
    background: c_combo;
    border-color: c_combo_border;
    border: 2;
}
ComboBox.Button {
    background: c_combo;
    color: c_text;
    text-align: left;
    text-offset: (4,0);
}
ComboBox.Button:hover {
    background: c_hover_border;
    color: c_back;
}
// ----------------------------------------------------------------------------
Dial {
    background: c_plain;
    border-color: c_plain_border;
    border: 1;
    dial-size: 80%;
    knob-color: c_plain;
    knob-border-color: c_plain_border;
    knob-border: 1;
    knob-size: 20%;
    knob-distance: 70%;
}
Dial:disabled {
    @extend _Item:disabled;
}
Dial:hover {
    background: c_hover;
    border-color: c_hover_border;
    knob-color: c_select;
    knob-border-color: c_select_border;
}
Dial:hover:down {
    background: c_select;
    border-color: c_select_border;
    knob-color: c_select;
    knob-border-color: c_select_border;
}
// ----------------------------------------------------------------------------
Frame {
    window-override: true;
    background: c_primary;
    color: c_text;
}
Frame.Minimize.Button {
    background: transparent;
    image: "ui/subtract";
}
Frame.Maximize.Button {
    background: transparent;
    image: "ui/maximize";
}
Frame.Close.Button {
    background: transparent;
    image: "ui/remove";
}

Frame.Minimize.Button:hover {
    background: c_minmax;
}
Frame.Minimize.Button:hover:down {
    background: c_minmax_down;
}

Frame.Maximize.Button:hover {
    background: c_minmax;
}
Frame.Maximize.Button:hover:down {
    background: c_minmax_down;
}

Frame.Close.Button:hover {
    background: c_close;
}
Frame.Close.Button:hover:down {
    background: c_close_down;
}
// ----------------------------------------------------------------------------
Label {
    background: transparent;
    color: c_text;
    text-align: left;
    text-offset: (4,0);
}
// ----------------------------------------------------------------------------
LineEdit {
    background: white;
    color: c_text;
    border: 1;
    border-color: c_secondary;
    text-align: left;
}
LineEdit:hover {
    border-color: c_hover_border;
}
// ----------------------------------------------------------------------------
Menu {
    background: c_plain;
    border-color: c_secondary;
    border: 1;
}
Menu.Bar {
    background: c_back;
}
Menu.Button {
    background: transparent;
    color: c_text;
}
Menu.Button:hover {
    background: c_hover;
}
// ----------------------------------------------------------------------------
RadioButton {
    @extend CheckBox;
    radius: 9;
}
RadioButton:disabled {
    @extend CheckBox:disabled;
}
RadioButton:hover {
    @extend CheckBox:hover;
}
RadioButton:checked {
    image: "ui/dot";
}
RadioButton:checked:hover {
    @extend CheckBox:checked:hover;
}
// ----------------------------------------------------------------------------
ScrollPanel.Bar {
    background: c_plain;
}
ScrollPanel.Button {
    background: c_primary;
}
ScrollPanel.Button:hover {
    background: c_secondary;
}
ScrollPanel.Button:hover:down {
    background: c_tertiary;
}
// ----------------------------------------------------------------------------
Slider {
    width: 4;
    background: c_secondary;
}
Slider.Button {
    @extend _Item;
}
Slider.Button:hover {
    @extend _Item:hover;
}
Slider.Button:hover:down {
    @extend _Item:down;
}
// ----------------------------------------------------------------------------
SplitPanel {
    background: c_plain;
    border-color: c_plain_border;
    border: 1;
}
// ----------------------------------------------------------------------------
TabPanel {
    background: transparent;
}
TabPanel.Bar {
    background: transparent;
}
TabPanel.Button {
    @extend _Item;
    text-align: left;
    text-offset: (4,0);
}
TabPanel.Button:checked {
    background: c_secondary;
    color: white;
}
TabPanel.Button:hover {
    @extend _Item:hover;
}
TabPanel.Content {
    background: transparent;
    border-color: c_secondary;
    border: 1;
}
TabPanel.New.Button {
    @extend TabPanel.Button;
    image: "ui/add";
    image-color: c_secondary;
    border: 1;
    border-color: c_tertiary;
}
TabPanel.Close.Button {
    background: transparent;
    image: "ui/remove";
    image-color: c_primary;
}
TabPanel.Close.Button:hover {
    image-color: red;
}
// ----------------------------------------------------------------------------
TitlePanel {
    background: transparent;
    color: c_text;
}
// ----------------------------------------------------------------------------
ToolBar {
    background: transparent;
}
ToolButton {
    @extend Button;
}
ToolButton:disabled {
    @extend Button:disabled;
}
ToolButton:hover {
    @extend Button:hover;
}
ToolButton:hover:down {
    @extend Button:hover:down;
}
// ----------------------------------------------------------------------------
Tree.Item {
    background:transparent;
    color: c_text;
}
Tree.Item:hover {
    background: c_select;
    text-align: center;
    text-offset: (0,0);
}
Tree.Drop {
    background: transparent;
    image: "ui/caret";
    image-color: c_primary;
    image-align: center;
}
Tree.Drop:hover {
    image-color: c_secondary;
}
// ----------------------------------------------------------------------------
