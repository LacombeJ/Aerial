package jonl.ge;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import jonl.jutils.func.List;
import jonl.jutils.func.ListUtils;
import jonl.jutils.func.Tuple2;
import jonl.jutils.io.Console;
import jonl.jutils.misc.BufferPool;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.GraphicsLibrary.Blend;
import jonl.jgl.GraphicsLibrary.Mask;
import jonl.jgl.GraphicsLibrary.Target;
import jonl.jgl.utils.MeshLoader;
import jonl.jgl.utils.Presets;
import jonl.jgl.Program;
import jonl.vmath.Matrix2;
import jonl.vmath.Matrix3;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

class AppRenderer implements Renderer {
    
    private App app;
    private Updater updater;
    private GraphicsLibrary gl;
    private GLMap glm;
    private ShaderGenerator sg;
    
    private jonl.jgl.Mesh fontRect;
    private Program fontProgram;
    
    private Matrix4 canvas;
    
    private final Material defaultMaterial;
    
    AppRenderer(App app, GraphicsLibrary gl) {
        this.app = app;
        updater = app.getUpdater();
        this.gl = gl;
        glm = new GLMap(gl);
        sg = new ShaderGenerator(gl);
        defaultMaterial = AppUtil.defaultMaterial();
    }
    
    @Override
    public void load() {
        int version = gl.glGetGLSLVersioni();
        sg.setGLSLVersion(version);
        
        gl.glEnable(Target.DEPTH_TEST);
        gl.glEnable(Target.CULL_FACE);
        gl.glBlendFunc(Blend.NORMAL);
        
        fontRect = gl.glGenMesh(MeshLoader.load("res/models/rect.mesh"));
        fontProgram = AppUtil.createProgramFromSource(gl, Presets.fontVSSource(version), Presets.fontFSSource(version));
    }
    
    @Override
    public void render(Scene scene) {
        
        canvas = Matrix4.orthographic(0,app.getWidth(),0,app.getHeight(),-1,1);
        
        ArrayList<Camera> cameras = scene.findComponents(Camera.class);
        ArrayList<Light> lights = scene.findComponents(Light.class);
        
        for (Camera camera : cameras) {
        
            GameObject g = camera.gameObject;
            Transform t = updater.getWorldTransform(g);
            
            Matrix4 view = Matrix4.identity()
                    .rotate(t.rotation)
                    .translate(t.translation.get().neg());
            
            int left = (int) (camera.viewLeft * app.getWidth());
            int bottom = (int) (camera.viewBottom * app.getHeight());
            int right = (int) (camera.viewRight * app.getWidth());
            int top = (int) (camera.viewTop * app.getHeight());
            int width = right - left;
            int height = top - bottom;
            
            gl.glViewport(left,bottom,width,height);
            gl.glClearColor(camera.clearColor);
            if (camera.scissor) {
                gl.glScissor(left,bottom,width,height);
                gl.glEnable(Target.SCISSOR_TEST);
                gl.glClear(Mask.COLOR_BUFFER_BIT,Mask.DEPTH_BUFFER_BIT);
                gl.glDisable(Target.SCISSOR_TEST);
            }
            
            Matrix4 VP = camera.projection.get().multiply(view);
            
            
            //TODO find a better way to do all of this below
            
            
            ArrayList<GameObject> gameObjects = scene.getAllGameObjects();
            
            Tuple2<List<GameObject>,List<GameObject>> T = ListUtils
                    .list(gameObjects)
                    .split(o -> o.getComponent(CanvasRenderer.class)==null);

            List<GameObject> sceneObjects = T.x;
            List<GameObject> canvasObjects = T.y;
            
            List<List<GameObject>> sceneMeshes = sceneObjects
                    .filter((x -> x.getComponent(MeshRenderer.class)!=null))
                    .bin((x,y) -> x.getComponent(MeshRenderer.class)==y.getComponent(MeshRenderer.class));
            List<GameObject> sceneText = sceneObjects
                    .filter((x -> x.getComponent(Text.class)!=null));
            
            //TODO render different renderers separately (MeshRenderer, CanvasRenderer, TextRenderer, ?InstanceRenderer?
            
            for (List<GameObject> listMeshes : sceneMeshes) {
                if (listMeshes.size()==1) {
                    renderGameObject(listMeshes.first(),VP,view,camera.projection,lights,camera);
                } else {
                    renderInstances(listMeshes,VP,view,camera.projection,lights,camera);
                }
            }
            
            for (GameObject text : sceneText) {
                renderText(text,VP,view,camera.projection,lights,camera);
            }
            
            //Render Canvas after (over 3D scene)
            for (GameObject gameObject : canvasObjects) {
                gl.glDisable(Target.DEPTH_TEST);
                renderGameObject(gameObject,canvas,null,null,lights,camera);
                gl.glEnable(Target.DEPTH_TEST);
            }
            
        }
    }
        
    
    
    private void renderInstances(List<GameObject> list, Matrix4 VP, Matrix4 V, Matrix4 P, ArrayList<Light> lights, Camera cam) {
        
        MeshRenderer renderer = list.first().getComponent(MeshRenderer.class);
        Mesh mesh = renderer.mesh;
        Material material = renderer.material==null ? defaultMaterial : renderer.material;
        jonl.jgl.Mesh glMesh = glm.getOrCreateMesh(mesh);
        
        if (!mesh.instancedSet) {
            List<Matrix4> matrices = list.map(g -> {
                Transform t = updater.getWorldTransform(g);
                Matrix4 model = Matrix4.identity()
                        .translate(t.translation)
                        .rotate(t.rotation)
                        .scale(t.scale);
                return model.transpose();
            });
            List<Float> mat = new List<>(matrices, x -> ListUtils.listFloat(x.toArray()));
            glMesh.setCustomAttribInstanced(5, mat.toFloatArray(), 4, 4);
            mesh.instancedSet = true;
        }
        FloatBuffer fb = BufferPool.borrowFloatBuffer(16,true);
        
        Program program = sg.getOrCreateProgram(material, true);
        
        gl.glUseProgram(program);
        
        program.setUniformMat4("VP",VP.toFloatBuffer(fb));
        program.setUniformMat4("V",V.toFloatBuffer(fb));
        
        for (MaterialBuilder.MBUniform u : material.mbUniformList) {
            setUniform(program,u.name,u.data,u.textureID);
        }
        
        Vector3 eye = updater.getWorldTransform(cam.gameObject).translation;
        AppUtil.setUniform(program,"eye",eye);
        
        int numLights = 0;
        for (int i=0; i<lights.size(); i++) {
            Light light = lights.get(i);
            Vector3 p = updater.getWorldTransform(light.gameObject).translation;
            program.setUniformi("light["+i+"].type",light.type);
            AppUtil.setUniform(program,"light["+i+"].position",p);
            program.setUniform("light["+i+"].range",light.range);
            AppUtil.setUniform(program,"light["+i+"].color",light.color);
            program.setUniform("light["+i+"].intensity",light.intensity);
            program.setUniform("light["+i+"].angle",light.angle);
            numLights++;
        }
        program.setUniformi("numLights",numLights);
        
        //gl.glRender(glMesh,renderer.mode.mode);
        gl.glRenderInstance(glMesh,renderer.mode.mode, list.size());
        
        BufferPool.returnFloatBuffer(fb);
        
        gl.glUseProgram(null);
        
    }
    
    private void renderGameObject(GameObject g, Matrix4 VP, Matrix4 V, Matrix4 P, ArrayList<Light> lights, Camera cam) {
        Transform t = updater.getWorldTransform(g);
        
        Matrix4 model = Matrix4.identity()
                .translate(t.translation)
                .rotate(t.rotation)
                .scale(t.scale);
        
        MeshRenderer renderer = g.getComponent(MeshRenderer.class);
        
        if (renderer!=null) {
            Mesh mesh = renderer.mesh;
            Material material = renderer.material==null ? defaultMaterial : renderer.material;
            if (mesh!=null) {
                Matrix4 MVP = VP.get().multiply(model);
                
                FloatBuffer fb = BufferPool.borrowFloatBuffer(16,true);
                
                Program program = sg.getOrCreateProgram(material, false);
                
                gl.glUseProgram(program);
                
                program.setUniformMat4("MVP",MVP.toFloatBuffer(fb));
                program.setUniformMat4("M",model.toFloatBuffer(fb));
                program.setUniformMat4("MV",V.get().multiply(model).toFloatBuffer(fb));
                
                
                
                for (MaterialBuilder.MBUniform u : material.mbUniformList) {
                    setUniform(program,u.name,u.data,u.textureID);
                }
                
                Vector3 eye = updater.getWorldTransform(cam.gameObject).translation;
                AppUtil.setUniform(program,"eye",eye);
                
                int numLights = 0;
                for (int i=0; i<lights.size(); i++) {
                    Light light = lights.get(i);
                    Vector3 p = updater.getWorldTransform(light.gameObject).translation;
                    program.setUniformi("light["+i+"].type",light.type);
                    AppUtil.setUniform(program,"light["+i+"].position",p);
                    program.setUniform("light["+i+"].range",light.range);
                    AppUtil.setUniform(program,"light["+i+"].color",light.color);
                    program.setUniform("light["+i+"].intensity",light.intensity);
                    program.setUniform("light["+i+"].angle",light.angle);
                    numLights++;
                }
                program.setUniformi("numLights",numLights);
                
                gl.glRender(glm.getOrCreateMesh(mesh),renderer.mode.mode);
                
                BufferPool.returnFloatBuffer(fb);
                
                gl.glUseProgram(null);
            }
        }
        
    }
    
    private void renderText(GameObject g, Matrix4 VP, Matrix4 V, Matrix4 P, ArrayList<Light> lights, Camera cam) {
        Transform t = updater.getWorldTransform(g);
        
        Matrix4 model = Matrix4.identity()
                .translate(t.translation)
                .rotate(t.rotation)
                .scale(t.scale);
        
        Text text = g.getComponent(Text.class);
        if (text!=null || false) {
            gl.glUseProgram(fontProgram);
            
            jonl.jgl.Font font = glm.getOrCreateFont(text.font);
            
            fontProgram.setTexture("texture",font.getTexture(),0);
            fontProgram.setUniform("fontColor",text.color[0],text.color[1],text.color[2],text.color[3]);
            
            renderString(text.text,text.halign,text.valign,font,model,VP);
            
            gl.glUseProgram(null);
        }
    }
    
    
    
    private void renderChar(char c, float x, float y, jonl.jgl.Font font,
            Matrix4 model, Matrix4 VP) { 
        fontRect.setTexCoordAttrib(font.getIndices(c),2);
        
        float width = font.getWidth(c);
        float height = font.getHeight();
        x = x+width/2f;
        y = y+height/2f;
        
        Matrix4 M = model.get().translate(x,y,0).scale(width,height,1);
        
        Matrix4 MVP = VP.get().multiply(M);
        
        FloatBuffer fb = BufferPool.borrowFloatBuffer(16,true);
        fontProgram.setUniformMat4("MVP",MVP.toFloatBuffer(fb));
        
        gl.glRender(fontRect);
        
        BufferPool.returnFloatBuffer(fb);
    }
    
    //TODO static text transform to reduce matrix multiplications?
    public void renderString(String string, int halign, int valign, jonl.jgl.Font font,
            Matrix4 model, Matrix4 VP) {
        
        String[] str = string.split("\n");
        float totalHeight = font.getHeight()*str.length;
        
        float sdy = 0;
        
        if (valign==Text.VA_BOTTOM) {
            sdy = totalHeight - font.getHeight();
        } else if (valign==Text.VA_MIDDLE) {
            sdy = (totalHeight)/2 - font.getHeight();
        } else {
            sdy -= font.getHeight();
        }
        
        for (int i=0; i<str.length; i++) {
            String line = str[i];
            float sdx = 0;
            if (halign==Text.HA_CENTER) {
                sdx = -font.getWidth(line)/2;
            } else if (halign==Text.HA_RIGHT) {
                sdx = -font.getWidth(line);
            }
            for (int j=0; j<line.length(); j++) {
                char c = line.charAt(j);
                //Parameters x and y must be given as ints or else
                //characters would show unwanted artifacts;
                //unknown reason
                renderChar(c,(int)sdx,(int)sdy,font,model,VP);
                sdx += font.getWidth(c);
            }
            sdy -= font.getHeight();
        }
    }
    
    
    private void setUniform(Program p, String name, Object data, int textureID) {
        if (data instanceof Texture) {
            Texture t = (Texture) data;
            p.setTexture(name,glm.getOrCreateTexture(t),textureID);
        } else if (data instanceof Boolean) {
            boolean b = (boolean) data;
            p.setUniformi(name,b ? 1 : 0);
        } else if (data instanceof Integer) {
            int i = (int) data;
            p.setUniformi(name,i);
        } else if (data instanceof Float) {
            float f = (float) data;
            p.setUniform(name,f);
        } else if (data instanceof Vector4) {
            Vector4 v = (Vector4) data;
            p.setUniform(name,v.x,v.y,v.z,v.w);
        } else if (data instanceof Vector3) {
            Vector3 v = (Vector3) data;
            p.setUniform(name,v.x,v.y,v.z);
        } else if (data instanceof Vector2) {
            Vector2 v = (Vector2) data;
            p.setUniform(name,v.x,v.y);
        } else if (data instanceof Matrix4) {
            Matrix4 m = (Matrix4) data;
            FloatBuffer fb = BufferPool.borrowFloatBuffer(16,true);
            p.setUniformMat4(name,m.toFloatBuffer(fb));
            BufferPool.returnFloatBuffer(fb);
        } else if (data instanceof Matrix3) {
            Matrix3 m = (Matrix3) data;
            FloatBuffer fb = BufferPool.borrowFloatBuffer(9,true);
            p.setUniformMat3(name,m.toFloatBuffer(fb));
            BufferPool.returnFloatBuffer(fb);
        } else if (data instanceof Matrix2) {
            Matrix2 m = (Matrix2) data;
            FloatBuffer fb = BufferPool.borrowFloatBuffer(4,true);
            p.setUniformMat2(name,m.toFloatBuffer(fb));
            BufferPool.returnFloatBuffer(fb);
        }
    }
    

    
}
