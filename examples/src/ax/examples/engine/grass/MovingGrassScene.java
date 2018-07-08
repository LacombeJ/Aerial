package ax.examples.engine.grass;

import ax.aui.Widget;
import ax.aui.Window;
import ax.engine.core.Camera;
import ax.engine.core.Geometry;
import ax.engine.core.Material;
import ax.engine.core.Mesh;
import ax.engine.core.Property;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Texture;
import ax.engine.core.TextureUniform;
import ax.engine.core.Mesh.Mode;
import ax.engine.core.geometry.BoxGeometry;
import ax.engine.core.geometry.GeometryBuilder;
import ax.engine.core.geometry.PlaneGeometry;
import ax.engine.core.geometry.SphereGeometry;
import ax.engine.core.material.GeneratedShader;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.SolidMaterial;
import ax.engine.core.material.TextureMaterial;
import ax.engine.core.material.ShaderLanguage.SLArray;
import ax.engine.core.material.ShaderLanguage.SLFloat;
import ax.engine.core.material.ShaderLanguage.SLMat4;
import ax.engine.core.material.ShaderLanguage.SLStruct;
import ax.engine.core.material.ShaderLanguage.SLStructBuilder;
import ax.engine.core.material.ShaderLanguage.SLTexU;
import ax.engine.core.material.ShaderLanguage.SLVec2;
import ax.engine.core.material.ShaderLanguage.SLVec3;
import ax.engine.core.material.ShaderLanguage.SLVec4;
import ax.engine.core.ui.SubApp;
import ax.math.vector.Color;
import ax.math.vector.Mathf;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;
import ax.std.StandardSubApp;
import ax.std.misc.CanvasObject;
import ax.std.misc.FPCToggle;
import ax.std.misc.FirstPersonControl;
import ax.std.render.Light;
import ax.std.render.StandardMaterialBuilder;

public class MovingGrassScene {

    Window window;
    Widget widget;
    
    MovingGrassScene(Window window, Widget widget) {
        this.window = window;
        this.widget = widget;
        app();
    }
    
    Light light;
    MovingGrassTexture mgt;
    
    int gn = 20000;
    int gdim = 50;
    
    void app() {
        
        SubApp app = new StandardSubApp(window,widget);
        app.setResizable(true);
        
        mgt = new MovingGrassTexture();
        
        Scene scene = new Scene();
        
        SceneObject player = player();
        
        SceneObject plane = plane();
        
        SceneObject light = light();
        
        SceneObject grass = grass();
        
        SceneObject perlinDisplay = perlinDisplay();
        
        scene.add(mgt.sceneObject);
        scene.add(player);
        scene.add(plane);
        scene.add(light);
        scene.add(grass);
        scene.add(perlinDisplay);
        
        app.addScene(scene);
        
        app.start();
        
    }
    
    SceneObject player() {
        SceneObject go = new SceneObject();
        
        Camera cam = new Camera();
        
        Vector4 c0 = Color.CYAN.toVector();
        Vector4 c1 = Color.WHITE.toVector();
        Vector4 mid = c0.lerp(c1,0.8f);
        
        cam.setClearColor(mid);
        
        go.transform().translation.set(0,10,10);
        
        FirstPersonControl fpc = new FirstPersonControl();
        FPCToggle mgt = new FPCToggle(true);
        Light light = new Light();
        
        go.addComponent(light);
        go.addComponent(mgt);
        go.addComponent(fpc);
        go.addComponent(cam);
        
        return go;
    }
    
    SceneObject sphere() {
        SceneObject go = new SceneObject();
        
        Geometry geometry = new SphereGeometry();
        Material material = new StandardMaterialBuilder().build();
        
        Mesh mesh = new Mesh(geometry, material);
        
        go.addComponent(mesh);
        
        return go;
    }
    
    SceneObject plane() {
        SceneObject go = new SceneObject();
        
        Geometry geometry = new BoxGeometry(gdim,1,gdim);
        
        StandardMaterialBuilder sl = new StandardMaterialBuilder();
        sl.diffuse = sl.vec3(1f,1f,1f);
        sl.specular = sl.vec3(0.1f,0.1f,0.1f);
        Material material = sl.build();
        
        //GeometryOperation rot = GeometryOperation.transform(Matrix4.rotation(-Mathf.PI_OVER_2,0,0));
        //geometry.modify(rot);
        
        Mesh mesh = new Mesh(geometry, material);
        
        go.transform().translation.set(0,-0.5f,0);
        
        
        go.addComponent(mesh);
        
        return go;
    }
    
    SceneObject perlinDisplay() {
        CanvasObject canvas = new CanvasObject();
        
        SceneObject so = new SceneObject();
        Geometry box = new PlaneGeometry();
        Material mat = new TextureMaterial(mgt.texture);
        Mesh mesh = new Mesh(box,mat);
        so.addComponent(mesh);
        so.transform().scale.set(150,150,1);
        so.transform().translation.set(100,450,0);
        mesh.setDepthTest(false);
        
        canvas.addChild(so);
        
        return canvas.get();
    }
    
    SceneObject light() {
        SceneObject go = new SceneObject();
        
        light = new Light();
        light.setType(Light.DIRECTIONAL);
        light.setColor(Color.WHITE.toVector().xyz());
        light.setDirection(new Vector3(-1.3f,-1.2f,-1.9f));
        light.setAmbient(Color.fromInt(13,13,26).toVector().scale(0.1f).xyz());
        
        go.addComponent(light);
        
        go.transform().translation.set(3,3,-3);
        
        return go;
    }
    
    SceneObject grass() {
        
        SceneObject so = new SceneObject();
        
        int N = gn;
        float d = -gdim/2f;
        
        GeometryBuilder gb = new GeometryBuilder();
        for (int i=0; i<N; i++) {
            float x = Mathf.rand(-d,d);
            float z = Mathf.rand(-d,d);
            float y = 0f;
            Vector3 v = new Vector3(x,y,z);
            gb.addVertex(v);
        }
        Geometry geometry = gb.build();
        
        
        Vector3[] colors = new Vector3[N];
        for (int i=0; i<N; i++) {
            Vector3 color = new Vector3(0,1,0);
            float sat = Mathf.rand(0.3f,1f);
            color.scale(sat);
            colors[i] = color;
        }
        float[] fcolors = Vector3.pack(colors);
        geometry.addAttributes(fcolors,3);
        
        Vector4[] v0 = new Vector4[N];
        Vector4[] v1 = new Vector4[N];
        Vector4[] v2 = new Vector4[N];
        Vector4[] v3 = new Vector4[N];
        for (int i=0; i<N; i++) {
            float rad = Mathf.rand(0,Mathf.PI);
            float height = Mathf.rand(0.25f,2f);
            Matrix4 shift = Matrix4.identity();
            shift.rotate(0,rad,0);
            shift.scale(1,height,1);
            
            v0[i] = shift.getCol(0);
            v1[i] = shift.getCol(1);
            v2[i] = shift.getCol(2);
            v3[i] = shift.getCol(3);
        }
        geometry.addAttributes(Vector4.pack(v0),4);
        geometry.addAttributes(Vector4.pack(v1),4);
        geometry.addAttributes(Vector4.pack(v2),4);
        geometry.addAttributes(Vector4.pack(v3),4);
        
        
        SceneObject grassPoints = new SceneObject();
        Material mat = new SolidMaterial(Color.RED.toVector());
        Mesh grassMesh = new Mesh(geometry,mat,Mode.POINTS);
        grassMesh.setThickness(4f);
        grassPoints.addComponent(grassMesh);
        //so.addChild(grassPoints);
        
        
        SceneObject grassBlades = new SceneObject();
        Material shader = grassShader(mgt.texture);
        Mesh bladesMesh = new Mesh(geometry,shader,Mode.POINTS);
        bladesMesh.cullFace = false;
        grassBlades.addComponent(bladesMesh);
        grassBlades.addComponent(new MovingGrass(shader));
        so.addChild(grassBlades);
        
        
        return so;
        
    }
    
    class MovingGrass extends Property {

        Material material;
        
        MovingGrass(Material material) {
            this.material = material;
        }
        
        @Override
        public void create() {
            // TODO Auto-generated method stub
            TextureUniform texuni = new TextureUniform(mgt.texture,0);
            material.setUniform("texture",texuni);
        }

        @Override
        public void update() {
            
        }
        
    }
    
    Geometry bladeGeometry(Vector3 v) {
        GeometryBuilder gb = new GeometryBuilder();
        
        float rad = Mathf.rand(0,Mathf.TWO_PI);
        
        Matrix4 rot = Matrix4.rotation(0,rad,0);
        Vector3 norm = new Vector3(0,0,1);
        
        Vector3[] blade = grassBlade();
        for (Vector3 b : blade) {
            Vector3 b0 = rot.multiply(new Vector4(b,1)).xyz();
            gb.addVertex(v.get().add(b0));
            gb.addNormal(norm);
        }
        
        Geometry geometry = gb.build();
        
        //int[] indices = { 0, 1, 2, 0, 2, 3, 3, 2, 4, 3, 4, 5, 4, 5, 6 };
        //geometry.setIndices(indices);
        
        return geometry;
    }
    
    Vector3[] grassBlade() {
        
        Vector3[] v = new Vector3[7];
        
        v[0] = new Vector3(-0.1f,0,0);
        v[1] = new Vector3(0.1f,0,0);
        
        v[2] = new Vector3(0.15f,1f,0.1f);
        v[3] = new Vector3(0.0f,1f,0.1f);
        
        v[4] = new Vector3(0.30f,2f,0.4f);
        v[5] = new Vector3(0.15f,2f,0.4f);
        
        v[6] = new Vector3(0.45f,3f,1f);
        
        int[] indices = { 0, 1, 2, 0, 2, 3, 3, 2, 4, 3, 4, 5, 4, 5, 6 }; //15 length
        Vector3[] vec = new Vector3[indices.length];
        for (int i=0; i<indices.length; i++) {
            vec[i] = v[indices[i]];
        }
        
        return vec;
    }
    
    Vector3[] grassBladeNext() {
        
        Vector3[] v = new Vector3[7];
        
        v[0] = new Vector3(-0.1f,0,0);
        v[1] = new Vector3(0.1f,0,0);
        
        v[3] = new Vector3(-0.15f,1f,0.05f);
        v[2] = new Vector3(-0.0f,1f,0.05f);
        
        v[5] = new Vector3(-0.30f,2f,0.2f);
        v[4] = new Vector3(-0.15f,2f,0.2f);
        
        v[6] = new Vector3(-0.45f,3f,0.5f);
        
        int[] indices = { 0, 1, 2, 0, 2, 3, 3, 2, 4, 3, 4, 5, 4, 5, 6 }; //15 length
        Vector3[] vec = new Vector3[indices.length];
        for (int i=0; i<indices.length; i++) {
            vec[i] = v[indices[i]];
        }
        
        return vec;
    }
    
    Vector3[] grassBladeBasic() {
        
        Vector3[] v = new Vector3[7];
        
        v[0] = new Vector3(-0.1f,0,0);
        v[1] = new Vector3(0.1f,0,0);
        v[2] = new Vector3(0.1f,1f,0);
        v[3] = new Vector3(-0.1f,1f,0);
        v[4] = new Vector3(0.1f,2f,0);
        v[5] = new Vector3(-0.1f,2f,0);
        v[6] = new Vector3(0,3f,0);
        
        return v;
    }
    
    
    
    Material grassShader(Texture texture) {
        return new GeneratedShader(grassVS(),grassGS(),grassFS());
    }
    
    class GrassStruct extends SLStruct {
        SLVec4 vertex;
        SLVec3 color;
        SLVec4 v0;
        SLVec4 v1;
        SLVec4 v2;
        SLVec4 v3;
        @Override
        public void init(SLStructBuilder sb) {
            vertex = sb.vec4("vertex");
            color = sb.vec3("color");
            v0 = sb.vec4("v0");
            v1 = sb.vec4("v1");
            v2 = sb.vec4("v2");
            v3 = sb.vec4("v3");
        }
    }
    
    ShaderLanguage grassVS() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        sl.layoutIn(1,"vec3 color");
        sl.layoutIn(2,"vec4 v0");
        sl.layoutIn(3,"vec4 v1");
        sl.layoutIn(4,"vec4 v2");
        sl.layoutIn(5,"vec4 v3");
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 M");
        
        GrassStruct gs = sl.interfaceOut(new GrassStruct());
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        sl.set(gs.vertex,"M * vertex");
        sl.set(gs.color,"color");
        sl.set(gs.v0,"v0");
        sl.set(gs.v1,"v1");
        sl.set(gs.v2,"v2");
        sl.set(gs.v3,"v3");
        
        return sl;
    }
    
    ShaderLanguage grassGS() {
        ShaderLanguage sl = new ShaderLanguage();
        
        Vector3[] blade = grassBlade();
        Vector3[] next = grassBladeNext();
        
        sl.version("330");
        sl.layoutIn("points");
        sl.layoutOut("triangle_strip, max_vertices="+blade.length);
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 VP");
        sl.uniform("mat4 M");
        
        SLTexU texture = sl.texture("texture");
        
        SLArray<GrassStruct> ags = sl.interfaceArrayIn(()->new GrassStruct(),1);
        
        SLVec3 color = sl.attributeOut(SLVec3.class,"color");
        
        GrassStruct gs = sl.index(ags,0);
        
        float hdim = gdim / 2f;
        SLVec2 xy = sl.div(sl.add(gs.vertex.xz(),sl.vec2(hdim,hdim)),sl.vec2(gdim,gdim));
        SLVec4 texColor = sl.sample(texture,xy);
        
        SLFloat texAlpha = texColor.x();
        
        SLVec3 c = gs.color;
        
        SLMat4 mat = sl.mat4(gs.v0,gs.v1,gs.v2,gs.v3);
        
        for (int i=0; i<blade.length; i+=3) {

            for (int j=0; j<3; j++) {
                SLVec4 xvec = sl.vec4(blade[i+j],0);
                SLVec4 yvec = sl.vec4(next[i+j],0);
                SLVec4 mix = sl.mix(xvec,yvec,texAlpha);
                SLVec4 bo = sl.vec4("M * "+sl.mul(mat,mix));
                SLVec4 vec = sl.vec4("VP * "+sl.add(gs.vertex,bo));
                //SLVec4 vec = sl.vec4("MVP * "+sl.add(gs.vertex,sl.vec4(blade[i+j],1)));
                
                sl.gl_Position(vec);
                sl.set(color, c);
                sl.emitVertex();
                
            }
            
            sl.endPrimitive();
        }
        
        return sl;
    }

    ShaderLanguage grassFS() {
        ShaderLanguage sl = new ShaderLanguage();
        
        SLVec3 color = sl.attributeIn(SLVec3.class,"color");
        
        sl.gl_FragColor(sl.vec4(color, 1f));
        
        return sl;
    }

}
