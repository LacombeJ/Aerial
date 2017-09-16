package jonl.ge;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import jonl.jutils.misc.BufferPool;
import jonl.ge.Material.Uniform;
import jonl.ge.utils.PresetData;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.GraphicsLibrary.Blend;
import jonl.jgl.GraphicsLibrary.Mask;
import jonl.jgl.GraphicsLibrary.Target;
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
    private MaterialProgramMapper mpm;
    private Mesh rectMesh;
    
    AppRenderer(App app, GraphicsLibrary gl) {
        this.app = app;
        updater = app.getUpdater();
        this.gl = gl;
        glm = new GLMap(gl);
        mpm = new MaterialProgramMapper(gl);
        rectMesh = Loader.loadMesh(PresetData.rectMesh());
    }
    
    @Override
    public void load() {
        int version = gl.glGetGLSLVersioni();
        mpm.setGLSLVersion(version);
        
        gl.glEnable(Target.DEPTH_TEST);
        gl.glEnable(Target.CULL_FACE);
        gl.glBlendFunc(Blend.NORMAL);
    }
    
    @Override
    public void render(Scene scene) {
        
        ArrayList<Camera> cameras = scene.findComponentsOfType(Camera.class);
        ArrayList<Light> lights = scene.findComponents(Light.class);
        ArrayList<GameObject> gameObjects = scene.getAllGameObjects();
        
        for (Camera camera : cameras) {
            
            renderCamera(camera,lights,gameObjects);
            
        }
    }
    
    void renderCameraSeparately(Camera camera, Scene scene) {
        ArrayList<Light> lights = scene.findComponents(Light.class);
        ArrayList<GameObject> gameObjects = scene.getAllGameObjects();
        
        renderCamera(camera,lights,gameObjects);
    }
    
    private void renderCamera(Camera camera, List<Light> lights, List<GameObject> gameObjects) {
        Matrix4 V = setupCamera(camera);
        Matrix4 P = camera.projection;
        Matrix4 VP = P.get().multiply(V);
        
        for (GameObject g : gameObjects) {
            if (targetInvalid(camera,g)) {
                continue;
            }
            
            Matrix4 M = gameObjectTransform(g);
            
            MeshRenderer mr = g.getComponent(MeshRenderer.class);
            if (mr != null) {
                Material mat = mr.material;
                Mesh mesh = mr.mesh;
                
                Matrix4 MVP = VP.get().multiply(M);
                
                FloatBuffer fb = BufferPool.borrowFloatBuffer(16,true);
                
                Program program = mpm.getOrCreateProgram(mat);
                
                gl.glUseProgram(program);
                
                program.setUniformMat4("MVP",MVP.toFloatBuffer(fb));
                program.setUniformMat4("M",M.toFloatBuffer(fb));
                program.setUniformMat4("MV",V.get().multiply(M).toFloatBuffer(fb));
                
                List<Uniform> uniforms = mat.uniforms();
                for (Uniform u : uniforms) {
                    setUniform(program,u.name,u.data);
                }
                
                if (mr.recieveLight) {
                    setLightUniforms(program,lights,camera);
                }
                
                gl.glRender(glm.getOrCreateMesh(mesh),mr.mode.mode);
                
                BufferPool.returnFloatBuffer(fb);
                
                gl.glUseProgram(null);
            }
            
        }
        
        detachCamera(camera);
    }
        
    /**
     * Sets up the gl viewport, clear color, scissor, and view matrix for the camera
     * @return the view matrix of the camera
     */
    private Matrix4 setupCamera(Camera camera) {
        GameObject g = camera.gameObject;
        Transform t = updater.getWorldTransform(g);
        
        Matrix4 view = Matrix4.identity()
                .rotate(t.rotation)
                .translate(t.translation.get().neg());
        
        int left=0, bottom=0, right=0, top=0, width=0, height=0;
        if (camera instanceof RenderTarget) {
            RenderTarget rt = (RenderTarget) camera;
            right = rt.buffer.width;
            top = rt.buffer.height;
            width = right;
            height = top;

            gl.glBindFramebuffer(glm.getOrCreateFrameBuffer(rt.buffer));
        } else {
            left = (int) (camera.viewLeft * app.getWidth());
            bottom = (int) (camera.viewBottom * app.getHeight());
            right = (int) (camera.viewRight * app.getWidth());
            top = (int) (camera.viewTop * app.getHeight());
            width = right - left;
            height = top - bottom;
        }
        
        gl.glViewport(left,bottom,width,height);
        gl.glClearColor(camera.clearColor);
        if (camera.scissor) {
            gl.glScissor(left,bottom,width,height);
            gl.glEnable(Target.SCISSOR_TEST);
            gl.glClear(Mask.COLOR_BUFFER_BIT,Mask.DEPTH_BUFFER_BIT);
            gl.glDisable(Target.SCISSOR_TEST);
        }
        
        return view;
    }
    
    private void detachCamera(Camera camera) {
        if (camera instanceof RenderTarget) {
            gl.glBindFramebuffer(null);
        }
    }
    
    /** @return true if camera should not render the given GameObject */
    private boolean targetInvalid(Camera camera, GameObject g) {
        
        //Has precedence over camera targets
        CameraCull cull = g.getComponent(CameraCull.class);
        
        if (cull != null) {
            return !cull.hasTarget(camera) ^ cull.getTargetType()==CameraCull.Target.EXCEPT;
        }
        
        Camera.Target targ = camera.getTargetType();
        if (targ==Camera.Target.EXCEPT) {
            if (camera.hasTarget(g)) {
                return true;
            }
        } else if (targ==Camera.Target.ONLY) {
            if (!camera.hasTarget(g)) {
                return true;
            }
        }
        
        return false;
    }
    
    private Matrix4 gameObjectTransform(GameObject g) {
        Transform t = updater.getWorldTransform(g);
        
        Matrix4 model = Matrix4.identity()
                .translate(t.translation)
                .rotate(t.rotation)
                .scale(t.scale);
        return model;
    }
    
    private void setLightUniforms(Program program, List<Light> lights, Camera camera) {
        Vector3 eye = updater.getWorldTransform(camera.gameObject).translation;
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
    }
    
    private void setUniform(Program p, String name, Object data) {
        if (data instanceof TextureUniform) {
            TextureUniform t = (TextureUniform) data;
            p.setTexture(name,glm.getOrCreateTexture(t.texture),t.id);
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
        } else {
            System.err.println("Uniform type not supported - "+name+" : "+data);
        }
    }

    
    
    
    public void renderRenderTexture(RenderTexture rt) {
        jonl.jgl.FrameBuffer buffer = glm.getOrCreateFrameBuffer(rt.buffer);
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        
        gl.glBindFramebuffer(glm.getOrCreateFrameBuffer(rt.buffer));
        
        gl.glViewport(0,0,width,height);
        gl.glClearColor(0,0,0,1);
        
        gl.glScissor(0,0,width,height);
        gl.glEnable(Target.SCISSOR_TEST);
        gl.glClear(Mask.COLOR_BUFFER_BIT,Mask.DEPTH_BUFFER_BIT);
        gl.glDisable(Target.SCISSOR_TEST);
        
        Material mat = rt.material;
        Matrix4 canvas = Matrix4.orthographic(-0.5f, 0.5f, -0.5f, 0.5f, -1, 1);
        
        FloatBuffer fb = BufferPool.borrowFloatBuffer(16,false);
        
        Program program = mpm.getOrCreateProgram(mat);
        
        gl.glUseProgram(program);
        
        program.setUniformMat4("MVP",canvas.toFloatBuffer(fb));
        
        List<Uniform> uniforms = mat.uniforms();
        for (Uniform u : uniforms) {
            setUniform(program,u.name,u.data);
        }
        
        gl.glRender(glm.getOrCreateMesh(rectMesh),GraphicsLibrary.Mode.TRIANGLES);
        
        BufferPool.returnFloatBuffer(fb);
        
        gl.glUseProgram(null);
        
        gl.glBindFramebuffer(null);
        
    }
    
    
    
    
    
}
