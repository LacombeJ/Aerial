package ax.examples.engine.postproc;

import java.util.ArrayList;

import ax.aui.ArrayLayout;
import ax.aui.Justify;
import ax.aui.Label;
import ax.aui.LineEdit;
import ax.aui.Margin;
import ax.aui.Overlay;
import ax.aui.Panel;
import ax.aui.RadioButton;
import ax.aui.Signal;
import ax.aui.Slider;
import ax.aui.UIManager;
import ax.aui.Window;
import ax.engine.core.ui.SubApp;
import ax.std.StandardSubApp;
import ax.std.misc.CanvasObject;
import ax.std.misc.FPCToggle;
import ax.std.misc.FirstPersonControl;
import ax.std.render.*;
import ax.std.render.pass.Blur;
import ax.std.render.pass.DeferredPassSSAO;
import ax.std.render.pass.SSAO;
import ax.std.text.Align;
import ax.std.text.Text;
import ax.std.text.TextMesh;
import ax.tea.TPanel;
import ax.tea.TUIManager;
import ax.commons.func.Callback;
import ax.commons.func.Callback2D;
import ax.engine.core.Camera;
import ax.engine.core.Geometry;
import ax.engine.core.Material;
import ax.engine.core.Mesh;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Texture;
import ax.engine.core.geometry.BoxGeometry;
import ax.engine.core.geometry.ConeGeometry;
import ax.engine.core.geometry.CylinderGeometry;
import ax.engine.core.geometry.TorusGeometry;
import ax.engine.core.material.BasicMaterialBuilder;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.ShaderLanguage.SLTexU;
import ax.engine.core.material.ShaderLanguage.SLVec2;
import ax.engine.core.material.ShaderLanguage.SLVec4;
import ax.math.vector.Color;
import ax.math.vector.Mathi;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class PostProcessingMain {

    public static void main(String[] args) {
        new PostProcessingMain().start();
    }
    
    Window window;
    Overlay gPanel;
    
    Panel menuPanel;
    RadioButton colorRadio;
    RadioButton ambientRadio;
    Color cColor;
    Color cAmbient;
    PickerWidget pickerWidget;
    
    class Picker {
        Label label;
        Slider slider;
        LineEdit edit;
        
        Signal<Callback<Integer>> valueChanged = new Signal<>();
        
        Picker(UIManager ui, String text, int min, int max) {
            label = ui.label(text);
            slider = ui.slider(ax.aui.Align.HORIZONTAL,min,max);
            edit = ui.lineEdit(min+"");
            edit.setSizeConstraint(30,-1);
            
            slider.changed().connect((v)->{
                edit.setText(v+"");
                valueChanged.emit((cb)->cb.f(v));
            });
            
            edit.finished().connect((t)->{
                int v = Integer.parseInt(t);
                slider.setValue(v);
                valueChanged.emit((cb)->cb.f(v));
            });
        }
        
        void setValue(int v) {
            edit.setText(v+"");
            slider.setValue(v);
        }
        
    }
    
    class PickerWidget extends TPanel {
        ArrayList<Picker> pickers;
        PickerWidget(UIManager ui, ArrayList<Picker> pickers) {
            super();
            
            ArrayLayout layout = ui.arrayLayout();
            layout.setMargin(new Margin(0,0,0,0));
            
            this.pickers = pickers;
            
            for (int i=0; i<pickers.size(); i++) {
                Picker picker = pickers.get(i);
                layout.add(picker.label,i,0);
                layout.add(picker.slider,i,1);
                layout.add(picker.edit,i,2);
            }
            
            setLayout(layout);
        }
    }
    
    void start() {
        ui();
        app();
    }
    
    void ui() {
        
        TUIManager ui = TUIManager.instance();
        
        ui.setDarkStyle();
        
        window = ui.window();
        window.setResizable(true);
        window.setWidth(1024);
        window.setHeight(576);
        
        gPanel = ui.overlay(); {
            menuPanel = ui.panel(ui.listLayout(ax.aui.Align.VERTICAL)); {
                
                Panel radioButtonPanel = ui.panel(ui.listLayout(ax.aui.Align.HORIZONTAL,new Margin(0,0,0,0),6));
                
                colorRadio = ui.radioButton("Color");
                ambientRadio = ui.radioButton("Ambient");
                
                cColor = Color.fromInt(0,0,0);
                cAmbient = Color.fromInt(0,0,0);
                
                colorRadio.setChecked(true);
                
                radioButtonPanel.add(colorRadio);
                radioButtonPanel.add(ambientRadio);
                
                colorRadio.toggled().connect((e)->{
                    updateColor();
                });
                ambientRadio.toggled().connect((e)->{
                    updateColor();
                });
                
                menuPanel.add(radioButtonPanel);
                
                ArrayList<Picker> pickers = new ArrayList<>();
                pickers.add(new Picker(ui,"Red",0,255));
                pickers.add(new Picker(ui,"Green",0,255));
                pickers.add(new Picker(ui,"Blue",0,255));
                
                pickers.get(0).valueChanged.connect((v)->{
                    updateRgb();
                });
                pickers.get(1).valueChanged.connect((v)->{
                    updateRgb();
                });
                pickers.get(2).valueChanged.connect((v)->{
                    updateRgb();
                });
                
                pickerWidget = new PickerWidget(ui, pickers);
                
                menuPanel.add(pickerWidget);
            }
            
            gPanel.add(menuPanel,10,10,200,150,Justify.TOP_LEFT);
        }
        
        menuPanel.addStyle(
            "Widget {" +
            "   background: rgba(32,32,32,128);"       +
            "   border: 1;" +
            "   border-color: white;" +
            "   radius: 12;" +
            "}");
        
        window.setWidget(gPanel);
        window.create();
        window.setVisible(true);
        
    }
    
    CanvasObject canvas;
    StandardMaterialBuilder smb;
    Light light;
    
    void app() {
        
        SubApp app = new StandardSubApp(window,gPanel);
        app.setResizable(true);
        
        Scene scene = new Scene();
        
        canvas = new CanvasObject();
        
        buildMaterial();
        
        SceneObject top = new SceneObject();
        
        SceneObject player = player();
        SceneObject light = light();
        SceneObject text = text();
        SceneObject group = group();
        
        canvas.add(text);
        
        scene.add(canvas.get());
        scene.add(top);
        scene.add(player);
        scene.add(light);
        scene.add(text);
        scene.add(group);
        
        app.addScene(scene);
        
        app.start();
        
    }
    
    void updateRgb() {
        
        int r = pickerWidget.pickers.get(0).slider.value();
        int g = pickerWidget.pickers.get(1).slider.value();
        int b = pickerWidget.pickers.get(2).slider.value();
        Color rgb = Color.fromInt(r,g,b);
        Vector3 color = rgb.toVector().xyz();
        
        if (colorRadio.checked()) {
            cColor = rgb;
            light.setColor(color);
        } else {
            cAmbient = rgb;
            light.setAmbient(color);
        }
        
        
    }
    
    void updateColor() {
        
        Color rgb = null;
        if (colorRadio.checked()) {
            rgb = cColor;
        } else {
            rgb = cAmbient;
        }
        Color.ColorInt color = Color.toInt(rgb);
        
        pickerWidget.pickers.get(0).setValue(color.r);
        pickerWidget.pickers.get(1).setValue(color.g);
        pickerWidget.pickers.get(2).setValue(color.b);
        
    }
    
    void buildMaterial() {
        StandardMaterialBuilder sl = new StandardMaterialBuilder();
        
        sl.diffuse = sl.vec3(1.0f);
        sl.specular = sl.vec3(0.0f);
        
        smb = sl;
    }
    
    Material flippedTexture() {
        BasicMaterialBuilder sl = new BasicMaterialBuilder();
        
        SLVec2 texCoord = sl.attributeIn(SLVec2.class, "vTexCoord");
        
        SLVec2 flippedCoord = sl.vec2(texCoord.x(), sl.sub(1,texCoord.y()));
        SLTexU texture = sl.texture("texture");
        
        SLVec4 sample = sl.sample(texture,flippedCoord);
        
        SLVec4 fragColor = sl.vec4(sample);
        
        sl.gl_FragColor(fragColor);
        
        return sl.build();
    }
    
    ShaderLanguage vs() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        
        sl.uniform("mat4 MVP");
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        return sl;
    }
    
    ShaderLanguage fs() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.gl_FragColor(sl.vec4(1,0,0,1));
        
        return sl;
    }
    
    SceneObject player() {
        SceneObject go = new SceneObject();
        
        Camera camera = null;
        
        boolean deferred = true;
        
        if (deferred) {

            DeferredMaterialBuilder dmb = new DeferredMaterialBuilder();
            
            DeferredMaterial dm = dmb.build();
            
            DeferredCamera cam = new DeferredCamera(1024,576,dm);

            camera = cam;
            
            RenderPass rp = new RenderPass();
            SSAO ssaoPass = rp.attach(new SSAO(), () -> new Texture[]{cam.position(),cam.normal(),cam.stencil()});
            Blur horBlur = rp.attach(new Blur(true), () -> new Texture[]{ssaoPass.ssao()});
            Blur verBlur = rp.attach(new Blur(false), () -> new Texture[]{horBlur.blur()});
            DeferredPassSSAO dp = rp.attach(new DeferredPassSSAO(), () -> new Texture[]{cam.position(),cam.normal(),cam.texCoord(),cam.stencil(),verBlur.blur()});
            rp.finish(()->dp.texture());
            
            cam.setRenderPass(rp);
            
        } else {
            
            Camera cam = new Camera();
            camera = cam;
        }
        
        
        Vector4 color = Color.WHITE.toVector();
        camera.setClearColor(color);
        
        go.transform().translation.set(0,6f,10);
        go.transform().rotation.set(0.33f,0.00f,0.00f,0.94f).nor();
        
        FirstPersonControl fpc = new FirstPersonControl();
        FPCToggle mgt = new FPCToggle(true);
        Light light = new Light();
        
        go.addComponent(light);
        go.addComponent(mgt);
        go.addComponent(fpc);
        go.addComponent(camera);
        
        return go;
    }
    
    SceneObject obj(Vector3 pos, Geometry geometry) {
        SceneObject so = new SceneObject();
        Material material = smb.build();
        Mesh mesh = new Mesh(geometry, material);
        so.transform().translation.set(pos);
        so.addComponent(mesh);
        return so;
    }
    
    SceneObject cone(Vector3 pos, float radius, float height) {
        return obj(pos,new ConeGeometry(radius, height));
    }
    
    SceneObject cylinder(Vector3 pos, float radius, float height) {
        return obj(pos,new CylinderGeometry(radius, height));
    }
    
    SceneObject box(Vector3 pos, float width, float height, float depth) {
        return obj(pos,new BoxGeometry(width,height,depth));
    }
    
    SceneObject torus(Vector3 pos, float radius, float tube) {
        return obj(pos,new TorusGeometry(radius,tube));
    }
    
    SceneObject group() {
        SceneObject so = new SceneObject();
        
        Callback2D<Integer,Vector3> create = (t,p) -> {
            if (t==0) {
                so.addChild(box(p, 1,1,1));
            } else if (t==1) {
                so.addChild(cylinder(p, 1,2));
            } else if (t==2) {
                so.addChild(cone(p, 1,2));
            } else if (t==3) {
                so.addChild(torus(p, 1,1));
            }
        };
        
        Vector3 origin = new Vector3(0,0,0);
        int n = 50;
        float space = 5;
        for (int i=0; i<n; i++) {
            int type = Mathi.randInt(0,3);
            Vector3 pos = Vector3.random(-0.5f,0.5f).multiply(space).add(origin);
            create.f(type,pos);
        }
        
        
        return so;
    }
    
    SceneObject light() {
        SceneObject go = new SceneObject();
        
        light = new Light();
        light.setType(Light.DIRECTIONAL);
        light.setColor(Color.RED.toVector().xyz());
        light.setDirection(new Vector3(-1.3f,-1.2f,-1.9f));
        light.setAmbient(Color.fromInt(196,16,42).toVector().scale(0.1f).xyz());
        
        go.addComponent(light);
        
        go.transform().translation.set(3,3,-3);
        
        return go;
    }
    
    SceneObject text() {
        SceneObject go = new SceneObject();
        
        Text text = new Text("Hello world!\nMy name is Jonathan Lacombe.\nSome more text here...");
        text.setAlign(Text.LEFT);
        
        TextMesh mesh = new TextMesh(text);
        mesh.setColor(new Vector4(0.2f, 0.2f, 0.2f ,1));
        mesh.setHAlign(Align.HA_LEFT);
        mesh.setVAlign(Align.VA_BOTTOM);
        
        //CameraCull cull = new CameraCull();
        //cull.addTargets(canvas);
        //canvas.addTargets(go);
        
        go.addComponent(mesh);
        //go.addComponent(cull);
        
        go.transform().translation.set(0, 0, 0);
        
        return go;
    }
    
    
    
}
