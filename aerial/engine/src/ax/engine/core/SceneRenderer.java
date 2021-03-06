package ax.engine.core;

import java.util.ArrayList;
import java.util.List;

import ax.engine.core.Material.Uniform;
import ax.engine.core.render.CameraCull;
import ax.engine.core.render.FBCamera;
import ax.engine.core.render.RenderTexture;
import ax.engine.utils.GLUtils;
import ax.graphics.GL;
import ax.graphics.Program;
import ax.math.vector.Mathf;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector4;

class SceneRenderer {

	private SceneManager manager;
	private GL gl;
	private GLRenderer glr;
	
	private boolean subsection = false;
	private int globalX = 0;
	private int globalY = 0;
	private int globalWidth = Integer.MAX_VALUE;
	private int globalHeight = Integer.MAX_VALUE;
	
	public SceneRenderer(SceneManager manager, Service service, GL gl) {
		this.manager = manager;
		this.gl = gl;
		this.glr = new GLRenderer(service, gl);
		
		service.implementGetGL( () -> this.gl );
		
		service.implementRenderCameraSeparately( (c,s) -> renderCameraSeparately(c, s) );
		
		service.implementRenderTexture( (t) ->  renderRenderTexture(t) );
		
		service.implementTargetInvalid( (c,g) -> targetInvalid(c,g) );
		
		service.implementRenderDirect( (c,m,b) -> renderDirect(c,m,b) );
	}
	
	void load() {
		int version = gl.glGetGLSLVersioni();
        glr.setGLSLVersion(version);
        
        gl.glEnable(GL.DEPTH_TEST);
        gl.glEnable(GL.CULL_FACE);
        gl.glEnable(GL.BLEND);
        gl.glBlendFunc(GL.NORMAL);
	}
	
	void render(Scene scene) {
		
		ArrayList<Camera> cameras = scene.findComponentsOfType(Camera.class);
        ArrayList<SceneObject> sceneObjects = scene.getAllSceneObjects();
        
        ax.commons.func.List.iterate(manager.delegate().onFindSceneObjects(), (cb) -> cb.f(sceneObjects) );
        
        // Sort cameras from lowest to highest rendering order
        cameras.sort((Camera c0, Camera c1)->{
        	return Integer.compare(c0.getOrder(), c1.getOrder());
        });
        
        for (Camera camera : cameras) {
            
            renderCamera(camera,sceneObjects);
            
        }
		
	}
	
	//TODO refactor this and code that uses this
    private void renderCameraSeparately(Camera camera, Scene scene) {
        ArrayList<SceneObject> sceneObjects = scene.getAllSceneObjects();
        
        renderCamera(camera,sceneObjects);
    }

    private void renderCamera(Camera camera, ArrayList<SceneObject> sceneObjects) {
        ax.commons.func.List.iterate(manager.delegate().onPreRenderCamera(), (cb) -> cb.f(camera) );
        
        setupCamera(camera);
        
        Transform cameraTransform = manager.updater().getWorldTransform(camera.sceneObject());
        Matrix4 V = Camera.computeViewMatrix(cameraTransform);
        Matrix4 P = camera.getProjection();
        Matrix4 VP = P.get().multiply(V);
        
        for (SceneObject so : sceneObjects) {
        	
            if (targetInvalid(camera,so)) {
                continue;
            }
            
            ax.commons.func.List.iterate(manager.delegate().onSceneObjectRender(), (cb) -> cb.f(so,camera) );
            
            Mesh mesh = so.getComponent(Mesh.class);
            if (mesh != null && mesh.isVisible()) {
                
                Material mat = mesh.getMaterial();
                Geometry geometry = mesh.getGeometry();
                
                Matrix4 M = sceneObjectTransform(so);
                Matrix4 MVP = VP.get().multiply(M);
                
                Program program = glr.getOrCreateProgram(mat);
                
                gl.glUseProgram(program);
                
                float windowWidth = so.window().width();
                float windowHeight = so.window().height();
                
                //TODO meshes should be able to choose which uniforms it needs?
                program.setUniformMat4("MVP",MVP.toArray());
                program.setUniformMat4("MV",V.get().multiply(M).toArray());
                program.setUniformMat4("VP",VP.toArray());
                program.setUniformMat4("M",M.toArray());
                program.setUniformMat4("V",V.toArray());
                program.setUniformMat4("P",P.toArray());
                program.setUniform("_width", windowWidth);
                program.setUniform("_height", windowHeight);
                program.setUniform("_near", camera.near());
                program.setUniform("_far", camera.far());
                float logDepthBufFC = 2.0f / (Mathf.log(camera.far() + 1.0f) / Mathf.LN2);
                program.setUniform("_logDepthBufFC", logDepthBufFC);
                
                ax.commons.func.List.iterate(manager.delegate().onMaterialUpdate(), (cb) -> cb.f(program,mat,camera) );
                
                List<Uniform> uniforms = mat.uniforms();
                for (Uniform u : uniforms) {
                    glr.setUniform(program,u.name,u.data);
                }
                
                ax.commons.func.List.iterate(manager.delegate().onProgramUpdate(), (cb) -> cb.f(program,camera) );
                
                if (mesh.cullFace) {
                    gl.glEnable(GL.CULL_FACE);
                } else {
                    gl.glDisable(GL.CULL_FACE);
                }
                
                if (mesh.isWireframe()) {
                    gl.glPolygonMode(GL.FRONT_AND_BACK, GL.LINE); 
                }
                
                if (!mesh.depthTest) {
                    gl.glDisable(GL.DEPTH_TEST);
                }
                
                gl.glLineWidth(mesh.thickness);
                gl.glPointSize(mesh.thickness);
                
                ax.commons.func.List.iterate(manager.delegate().onGLPreRender(), (cb) -> cb.f(so,mesh,gl) );
                
                gl.glRender(glr.getOrCreateMesh(geometry),mesh.getMode());
                
                ax.commons.func.List.iterate(manager.delegate().onGLPostRender(), (cb) -> cb.f(so,mesh,gl) );
                
                gl.glLineWidth(1);
                gl.glPointSize(1);
                
                if (!mesh.depthTest) {
                    gl.glEnable(GL.DEPTH_TEST);
                }
                
                if (mesh.isWireframe()) {
                    gl.glPolygonMode(GL.FRONT_AND_BACK, GL.FILL);
                }

                gl.glEnable(GL.CULL_FACE);
                
                gl.glUseProgram(null);
            }
            
        }
        
        detachCamera(camera);
        
        ax.commons.func.List.iterate(manager.delegate().onPostRenderCamera(), (cb) -> cb.f(camera) );
    }
    
    public void subsection(boolean enable) {
        subsection = enable;
    }
    
    public void offset(int x, int y) {
        globalX = x;
        globalY = y;
    }
    
    public void dimension(int width, int height) {
        globalWidth = width;
        globalHeight = height;
    }
    
    /**
     * Sets up the gl viewport, clear color, scissor, and view matrix for the camera
     * @return the view matrix of the camera
     */
    private void setupCamera(Camera camera) {
        if (camera instanceof FBCamera) {
            FBCamera fbc = (FBCamera)camera;
            setupCamera(camera,fbc.buffer());
        } else {
            setupCamera(camera,null);
        }
    }
    
    private void setupCamera(Camera camera, FrameBuffer buffer) {
        int left=0, bottom=0, right=0, top=0, width=0, height=0;
        if (buffer!=null) {
            right = buffer.width();
            top = buffer.height();
            width = right;
            height = top;
            
            gl.glBindFramebuffer(glr.getOrCreateFrameBuffer(buffer));
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
        
        int ox = 0;
        int oy = 0;
        
        // Global cut is set for apps that render scene in a subsection of the window
        int[] globalCut = new int[] {globalX,globalY,globalWidth,globalHeight};
        if (subsection) {
            ox = globalX;
            oy = globalY;
        }
        
        gl.glViewport(
                left+ox,
                bottom+oy,
                width,
                height);
        
        Vector4 c = camera.clearColor;
        gl.glClearColor(c.x, c.y, c.z, c.w); //rgba
        
        //TODO simplify this code
        if (camera.scissorMode != Camera.NONE || subsection) {
            gl.glEnable(GL.SCISSOR_TEST);
            
            if (camera.scissorMode == Camera.NONE) {
                gl.glScissor(globalCut);
            } else if (camera.scissorMode == Camera.VIEWPORT) {
                int[] s = {left+ox,bottom+oy,width,height};
                if (subsection) s = cutOut(globalCut,s);
                gl.glScissor(s);
            } else {
                int[] s = {camera.scissorLeft+ox, camera.scissorBottom+oy, camera.scissorRight, camera.scissorTop};
                if (subsection) s = cutOut(globalCut,s);
                gl.glScissor(s);
            }
            
            if (camera.scissorMode != Camera.NONE && camera.shouldClearColor)
            {
                gl.glClear(GL.COLOR_BUFFER_BIT,GL.DEPTH_BUFFER_BIT);
            }
        } else {
            gl.glDisable(GL.SCISSOR_TEST);
        }
    }
    
    private void detachCamera(Camera camera) {
        gl.glDisable(GL.SCISSOR_TEST);
    	if (camera instanceof FBCamera) {
            gl.glBindFramebuffer(null);
        }
    }
    
    /** @return true if camera should not render the given SceneObject */
    private boolean targetInvalid(Camera camera, SceneObject g) {
        
        //Has precedence over camera targets
        CameraCull cull = g.getComponent(CameraCull.class);
        
        if (cull != null) {
            return !cull.hasTarget(camera) ^ cull.getTargetType()==CameraCull.Target.EXCEPT;
        }
        
        Camera.Target targ = camera.getTargetType();
        if (targ==Camera.EXCEPT) {
            if (camera.hasTarget(g)) {
                return true;
            }
        } else if (targ==Camera.ONLY) {
            if (!camera.hasTarget(g)) {
                return true;
            }
        }
        
        return false;
    }
    
    private Matrix4 sceneObjectTransform(SceneObject g) {
        Transform t = manager.updater().getWorldTransform(g);
        return t.computeMatrix();
    }
    
    private void renderDirect(Camera camera, Material material, FrameBuffer buffer) {
        setupCamera(camera,buffer);
        
        Matrix4 canvas = Matrix4.orthographic(-0.5f, 0.5f, -0.5f, 0.5f, -1, 1);
        
        Program program = glr.getOrCreateProgram(material);
        
        gl.glUseProgram(program);
        
        program.setUniformMat4("MVP",canvas.toArray());
        
        List<Uniform> uniforms = material.uniforms();
        for (Uniform u : uniforms) {
            glr.setUniform(program,u.name,u.data);
        }
        
        gl.glRender(glr.getOrCreateMesh(glr.getRectGeometry()),GL.TRIANGLES);
        
        gl.glUseProgram(null);
        
        gl.glBindFramebuffer(null);
        
        detachCamera(camera);
    }
    
    private void renderRenderTexture(RenderTexture rt) {
        ax.graphics.FrameBuffer buffer = glr.getOrCreateFrameBuffer(rt.buffers()[0]);
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        
        gl.glBindFramebuffer(glr.getOrCreateFrameBuffer(rt.buffers()[0]));
        
        gl.glViewport(0,0,width,height);
        gl.glClearColor(0,0,0,1);
        
        gl.glScissor(0,0,width,height);
        gl.glEnable(GL.SCISSOR_TEST);
        gl.glClear(GL.COLOR_BUFFER_BIT,GL.DEPTH_BUFFER_BIT);
        gl.glDisable(GL.SCISSOR_TEST);
        
        Material mat = rt.getMaterial();
        Matrix4 canvas = Matrix4.orthographic(-0.5f, 0.5f, -0.5f, 0.5f, -1, 1);
        
        Program program = glr.getOrCreateProgram(mat);
        
        gl.glUseProgram(program);
        
        program.setUniformMat4("MVP",canvas.toArray());
        
        List<Uniform> uniforms = mat.uniforms();
        for (Uniform u : uniforms) {
            glr.setUniform(program,u.name,u.data);
        }
        
        gl.glRender(glr.getOrCreateMesh(glr.getRectGeometry()),GL.TRIANGLES);
        
        gl.glUseProgram(null);
        
        gl.glBindFramebuffer(null);
        
    }
    
    /**
     * Returns scissorBox clipped within paper bounds. Integer arrays for arguments
     * and return are [x,y,width,height] values.
     */
    private int[] cutOut(int[] paper, int[] scissorBox) {
        int x = Math.max(paper[0],scissorBox[0]);
        int y = Math.max(paper[1],scissorBox[1]);
        
        int paperX1 = paper[0] + paper[2];
        int boxX1 = scissorBox[0] + scissorBox[2];
        int x1 = Math.min(paperX1,boxX1);
        int width = x1 - x;
        
        int paperY1 = paper[1] + paper[3];
        int boxY1 = scissorBox[1] + scissorBox[3];
        int y1 = Math.min(paperY1,boxY1);
        int height = y1 - y;
        
        return new int[]{x,y,width,height};
    }
    
	
}
