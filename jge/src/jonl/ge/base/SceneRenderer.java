package jonl.ge.base;

import java.util.ArrayList;
import java.util.List;

import jonl.ge.base.BaseMaterial.Uniform;
import jonl.ge.core.Camera;
import jonl.ge.core.GameObject;
import jonl.ge.core.Material;
import jonl.ge.core.Mesh;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.ge.core.Transform;
import jonl.ge.core.Window;
import jonl.ge.core.geometry.Geometry;
import jonl.ge.core.light.Light;
import jonl.ge.core.material.PointsMaterial;
import jonl.ge.core.render.CameraCull;
import jonl.ge.core.render.CameraTarget;
import jonl.ge.core.render.RenderTarget;
import jonl.ge.core.render.RenderTexture;
import jonl.ge.utils.GLUtils;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Program;
import jonl.jgl.GraphicsLibrary.Blend;
import jonl.jgl.GraphicsLibrary.Face;
import jonl.jgl.GraphicsLibrary.Mask;
import jonl.jgl.GraphicsLibrary.PMode;
import jonl.jgl.GraphicsLibrary.Target;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector4;

class SceneRenderer {

	private SceneManager manager;
	private GraphicsLibrary gl;
	private GLRenderer glr;
	
	public SceneRenderer(SceneManager manager, Service service, GraphicsLibrary gl) {
		this.manager = manager;
		this.gl = gl;
		this.glr = new GLRenderer(gl);
		
		service.implementGetGL(() -> this.gl);
		
		service.implementRenderCameraSeparately((camera,scene)->{
			renderCameraSeparately(camera, scene);
		});
		
		service.implementRenderTexture((renderTexture)->{
			renderRenderTexture(renderTexture);
		});
		
	}
	
	void load() {
		int version = gl.glGetGLSLVersioni();
        glr.setGLSLVersion(version);
        
        gl.glEnable(Target.DEPTH_TEST);
        gl.glEnable(Target.CULL_FACE);
        gl.glBlendFunc(Blend.NORMAL);
	}
	
	void render(Scene scene) {
		
		ArrayList<Camera> cameras = scene.findComponentsOfType(Camera.class);
        ArrayList<Light> lights = scene.findComponents(Light.class);
        ArrayList<GameObject> gameObjects = scene.getAllGameObjects();
        
        jonl.jutils.func.List.iterate(manager.delegate().onFindLights(), (cb) -> cb.f(lights) );
        
        // Sort cameras from lowest to highest rendering order
        cameras.sort((Camera c0, Camera c1)->{
        	return Integer.compare(c0.getOrder(), c1.getOrder());
        });
        
        for (Camera camera : cameras) {
            
            renderCamera(camera,lights,gameObjects);
            
        }
		
	}
	
	//TODO refactor this and code that uses this
    void renderCameraSeparately(Camera camera, Scene scene) {
        ArrayList<Light> lights = scene.findComponents(Light.class);
        ArrayList<GameObject> gameObjects = scene.getAllGameObjects();
        
        renderCamera(camera,lights,gameObjects);
    }
    
    ArrayList<Light> copy(List<Light> lights) {
        return jonl.jutils.func.List.copy(lights);
    }
    
    private void renderCamera(Camera camera, List<Light> lights, List<GameObject> gameObjects) {
        Matrix4 V = setupCamera(camera);
        Matrix4 P = camera.getProjection();
        Matrix4 VP = P.get().multiply(V);
        
        for (GameObject g : gameObjects) {
        	
            if (targetInvalid(camera,g)) {
                continue;
            }
            
            jonl.jutils.func.List.iterate(manager.delegate().onGameObjectRender(), (cb) -> cb.f(g,camera) );
            
            Mesh mesh = g.getComponent(Mesh.class);
            if (mesh != null) {
                Material mat = mesh.getMaterial();
                Geometry geometry = mesh.getGeometry();
                
                Matrix4 M = gameObjectTransform(g);
                Matrix4 MVP = VP.get().multiply(M);
                
                Program program = glr.getOrCreateProgram(mat);
                
                gl.glUseProgram(program);
                
                float windowWidth = g.window().width();
                float windowHeight = g.window().height();
                
                //TODO meshes should be able to choose which uniforms it needs?
                program.setUniformMat4("MVP",MVP.toArray());
                program.setUniformMat4("MV",V.get().multiply(M).toArray());
                program.setUniformMat4("M",M.toArray());
                program.setUniformMat4("V",V.toArray());
                program.setUniformMat4("P",P.toArray());
                program.setUniform("_width", windowWidth);
                program.setUniform("_height", windowHeight);
                program.setUniform("_near", camera.near());
                program.setUniform("_far", camera.far());
                
                List<Uniform> uniforms = mat.uniforms();
                for (Uniform u : uniforms) {
                    glr.setUniform(program,u.name,u.data);
                }
                
                jonl.jutils.func.List.iterate(manager.delegate().onProgramUpdate(), (cb) -> cb.f(program,mat,camera) );
                
                if (mesh.cullFace) {
                    gl.glEnable(Target.CULL_FACE);
                } else {
                    gl.glDisable(Target.CULL_FACE);
                }
                
                if (mesh.isWireframe()) {
                    gl.glPolygonMode(Face.FRONT_AND_BACK, PMode.LINE); 
                }
                
                if (mat instanceof PointsMaterial) {
                    gl.glEnable(Target.PROGRAM_POINT_SIZE);
                }
                
                gl.glRender(glr.getOrCreateMesh(geometry),GLUtils.map(mesh.getMode()));
                
                if (mesh.isWireframe()) {
                    gl.glPolygonMode(Face.FRONT_AND_BACK, PMode.FILL);
                }
                
                if (mat instanceof PointsMaterial) {
                    gl.glDisable(Target.PROGRAM_POINT_SIZE);
                }
                
                gl.glEnable(Target.CULL_FACE);
                
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
        GameObject g = camera.gameObject();
        Transform t = manager.updater().getWorldTransform(g);
        
        Matrix4 view = Camera.computeViewMatrix(t);
        
        int left=0, bottom=0, right=0, top=0, width=0, height=0;
        if (camera instanceof CameraTarget) {
        	CameraTarget ct = (CameraTarget) camera;
            right = ct.buffer().width();
            top = ct.buffer().height();
            width = right;
            height = top;
            
            gl.glBindFramebuffer(glr.getOrCreateFrameBuffer(ct.buffer()));
        } else if (camera instanceof RenderTarget) {
            RenderTarget rt = (RenderTarget) camera;
            right = rt.buffer().width();
            top = rt.buffer().height();
            width = right;
            height = top;
            
            gl.glBindFramebuffer(glr.getOrCreateFrameBuffer(rt.buffer()));
        } else {
        	Vector4 viewport = camera.getViewport();
        	Window window = camera.window();
            left = (int) (viewport.x * window.width());
            bottom = (int) (viewport.y * window.height());
            right = (int) (viewport.z * window.width());
            top = (int) (viewport.w * window.height());
            width = right - left;
            height = top - bottom;
        }
        
        gl.glViewport(left,bottom,width,height);
        gl.glClearColor(camera.getClearColor());
        if (camera.scissorEnabled()) {
            gl.glScissor(left,bottom,width,height);
            gl.glEnable(Target.SCISSOR_TEST);
            gl.glClear(Mask.COLOR_BUFFER_BIT,Mask.DEPTH_BUFFER_BIT);
            gl.glDisable(Target.SCISSOR_TEST);
        }
        
        return view;
    }
    
    private void detachCamera(Camera camera) {
    	if (camera instanceof CameraTarget || camera instanceof RenderTarget) {
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
        Transform t = manager.updater().getWorldTransform(g);
        return t.computeMatrix();
    }
    
    
    void renderRenderTexture(RenderTexture rt) {
        jonl.jgl.FrameBuffer buffer = glr.getOrCreateFrameBuffer(rt.buffers()[0]);
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        
        gl.glBindFramebuffer(glr.getOrCreateFrameBuffer(rt.buffers()[0]));
        
        gl.glViewport(0,0,width,height);
        gl.glClearColor(0,0,0,1);
        
        gl.glScissor(0,0,width,height);
        gl.glEnable(Target.SCISSOR_TEST);
        gl.glClear(Mask.COLOR_BUFFER_BIT,Mask.DEPTH_BUFFER_BIT);
        gl.glDisable(Target.SCISSOR_TEST);
        
        Material mat = rt.getMaterial();
        Matrix4 canvas = Matrix4.orthographic(-0.5f, 0.5f, -0.5f, 0.5f, -1, 1);
        
        Program program = glr.getOrCreateProgram(mat);
        
        gl.glUseProgram(program);
        
        program.setUniformMat4("MVP",canvas.toArray());
        
        List<Uniform> uniforms = mat.uniforms();
        for (Uniform u : uniforms) {
            glr.setUniform(program,u.name,u.data);
        }
        
        gl.glRender(glr.getOrCreateMesh(glr.getRectGeometry()),GraphicsLibrary.Mode.TRIANGLES);
        
        gl.glUseProgram(null);
        
        gl.glBindFramebuffer(null);
        
    }
    
	
}
