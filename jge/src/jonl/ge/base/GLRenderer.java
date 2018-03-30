package jonl.ge.base;

import java.util.HashMap;

import jonl.ge.core.FrameBuffer;
import jonl.ge.core.Texture;
import jonl.ge.core.TextureUniform;
import jonl.ge.core.geometry.Geometry;
import jonl.ge.core.text.Font;
import jonl.ge.utils.GLUtils;
import jonl.ge.utils.Loader;
import jonl.ge.utils.PresetData;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Program;
import jonl.jutils.func.Tuple2;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix2;
import jonl.vmath.Matrix3;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

class GLRenderer {

	
	private GraphicsLibrary gl;
	
	private int version = 430;

    private HashMap<BaseGeometry,jonl.jgl.Mesh>    		meshMap     	= new HashMap<>();
    private HashMap<Font,jonl.jgl.Font>                 fontMap     	= new HashMap<>();
    private HashMap<BaseTexture,jonl.jgl.Texture>     	textureMap  	= new HashMap<>();
    private HashMap<FrameBuffer,jonl.jgl.FrameBuffer>   bufferMap   	= new HashMap<>();
    
    private HashMap<String,Program> 					shaderKeyMap 	= new HashMap<>();
    
    // TODO find a better way to do this ? Maybe use PlaneGeometry ?
    private Geometry rectGeometry = Loader.loadMesh(PresetData.rectMesh());
    
    GLRenderer(GraphicsLibrary gl) {
        this.gl = gl;
    }
    
    void setGLSLVersion(int version) {
    	this.version = version;
    }
    
    Geometry getRectGeometry() {
    	return rectGeometry;
    }
    
    jonl.jgl.Mesh getOrCreateMesh(BaseGeometry mesh) {
        jonl.jgl.Mesh glmesh = meshMap.get(mesh);
        if (glmesh==null) {
            glmesh = gl.glGenMesh(mesh.vertices, mesh.normals, mesh.texCoords, mesh.indices);
            checkUpdateTangents(mesh, glmesh);
            meshMap.put(mesh,glmesh);
            
        } else if (mesh.update) {
        	if (mesh.vertices!=null) glmesh.setVertexAttrib(mesh.vertices, 3);
        	if (mesh.normals!=null) glmesh.setNormalAttrib(mesh.normals, 3);
        	if (mesh.texCoords!=null) glmesh.setTexCoordAttrib(mesh.texCoords, 2);
        	if (mesh.indices!=null) glmesh.setIndices(mesh.indices);
        	checkUpdateTangents(mesh, glmesh);
        }
        mesh.update = false;
        return glmesh;
    }
    
    jonl.jgl.Font getOrCreateFont(Font font) {
        jonl.jgl.Font glfont = fontMap.get(font);
        if (glfont==null) {
            glfont = gl.glGenFont(font.font(),font.type(),font.size(),font.antialias());
            fontMap.put(font,glfont);
        }
        return glfont;
    }
    
    jonl.jgl.Texture getOrCreateTexture(Texture texture) {
        jonl.jgl.Texture gltexture = textureMap.get(texture);
        if (gltexture==null) {
            if (texture.data==null) {
                gltexture = gl.glGenTexture(
                        texture.width, texture.height,
                        GLUtils.map(texture.format),
                        GLUtils.map(texture.wrap),
                        GLUtils.map(texture.filter));
            } else {
                gltexture = gl.glGenTexture(
                        texture.data,
                        texture.width,texture.height,
                        GLUtils.map(texture.format),
                        GLUtils.map(texture.wrap),
                        GLUtils.map(texture.filter));
            }
            textureMap.put(texture,gltexture);
        }
        return gltexture;
    }
    
    jonl.jgl.FrameBuffer getOrCreateFrameBuffer(FrameBuffer buffer) {
        jonl.jgl.FrameBuffer glbuffer = bufferMap.get(buffer);
        if (glbuffer==null) {
            glbuffer = gl.glGenFramebuffer(buffer.width(),buffer.height());
            for (Texture texture : buffer.textures()) {
                jonl.jgl.Texture gltexture = getOrCreateTexture(texture);
                glbuffer.attach(gltexture);
            }
            glbuffer.link();
            bufferMap.put(buffer,glbuffer);
        }
        return glbuffer;
    }
    
    // -----------------------------------------------------------------------------------
    
    Program getOrCreateProgram(BaseMaterial material) {
        String string = material.shaderKey();
        Program glprogram = shaderKeyMap.get(string);
        if (glprogram==null) {
        	
        	String vertex = material.vertexShader(version);
        	String geometry = material.geometryShader(version);
        	String fragment = material.fragmentShader(version);
        	
        	if (geometry != null) {
        		glprogram = GLUtils.createProgramFromSource(gl, vertex, geometry, fragment);
        	} else {
        		glprogram = GLUtils.createProgramFromSource(gl, vertex, fragment);
        	}
        	
            shaderKeyMap.put(string,glprogram);
        }
        
        return glprogram;
    }
    
    // -----------------------------------------------------------------------------------
    
    void setUniform(Program p, String name, Object data) {
    	if (data == null) {
    		return;
    	}
        if (data instanceof TextureUniform) {
            TextureUniform t = (TextureUniform) data;
            if (t.texture!=null) {
                p.setTexture(name, getOrCreateTexture(t.texture),t.id);
            }
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
            p.setUniformMat4(name,m.toArray());
        } else if (data instanceof Matrix3) {
            Matrix3 m = (Matrix3) data;
            p.setUniformMat3(name,m.toArray());
        } else if (data instanceof Matrix2) {
            Matrix2 m = (Matrix2) data;
            p.setUniformMat2(name,m.toArray());
        } else {
            Engine.log.error("Uniform type not supported - "+name+" : "+data);
        }
    }
    
    // -----------------------------------------------------------------------------------
    
    static void checkUpdateTangents(BaseGeometry mesh, jonl.jgl.Mesh glmesh) {
    	if (mesh.calculateTangents) {
        	Tuple2<float[],float[]> calculated = calculateTangents(mesh.vertices, mesh.normals, mesh.texCoords, mesh.indices);
        	if (calculated != null) {
        	    float[] tangents = calculated.x;
                float[] bitangents = calculated.y;
                glmesh.setCustomAttrib(3,tangents,3);
                glmesh.setCustomAttrib(4,bitangents,3);
        	}
        }
    }
    
    // https://learnopengl.com/#!Advanced-Lighting/Normal-Mapping
    //TODO calculate smooth tangents instead of flat tangents?
    static Tuple2<float[],float[]> calculateTangents(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
        
    	if (vertices!=null && normals!=null && texCoords!=null && indices!=null) {
    		
    	    float[] tangents = new float[vertices.length];
    	    float[] bitangents = new float[vertices.length];
            
            for (int i=0; i<indices.length; i+=3) {
            	
                int a = indices[i]*3;
                int b = indices[i+1]*3;
                int c = indices[i+2]*3;
                Vector3 vertexA = new Vector3(vertices[a],vertices[a+1],vertices[a+2]);
                Vector3 vertexB = new Vector3(vertices[b],vertices[b+1],vertices[b+2]);
                Vector3 vertexC = new Vector3(vertices[c],vertices[c+1],vertices[c+2]);
                
                int ta = indices[i]*2;
                int tb = indices[i+1]*2;
                int tc = indices[i+2]*2;
                Vector2 texCoordA = new Vector2(texCoords[ta],texCoords[ta+1]);
                Vector2 texCoordB = new Vector2(texCoords[tb],texCoords[tb+1]);
                Vector2 texCoordC = new Vector2(texCoords[tc],texCoords[tc+1]);
                
                Vector3 edge1 = Mathf.sub(vertexB,vertexA);
                Vector3 edge2 = Mathf.sub(vertexC,vertexA);
                Vector2 deltaUV1 = Mathf.sub(texCoordB,texCoordA);
                Vector2 deltaUV2 = Mathf.sub(texCoordC,texCoordA);
                
                float f =  1f / Vector2.cross(deltaUV1,deltaUV2);
                
                Vector3 tangent = new Vector3();
                tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
                tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
                tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
                tangent.normalize();
                
                Vector3 bitangent = new Vector3();
                bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
                bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
                bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
                bitangent.normalize();
                
                tangents[a  ] = tangent.x;
                tangents[a+1] = tangent.y;
                tangents[a+2] = tangent.z;
                tangents[b  ] = tangent.x;
                tangents[b+1] = tangent.y;
                tangents[b+2] = tangent.z;
                tangents[c  ] = tangent.x;
                tangents[c+1] = tangent.y;
                tangents[c+2] = tangent.z;
                
                bitangents[a  ] = bitangent.x;
                bitangents[a+1] = bitangent.y;
                bitangents[a+2] = bitangent.z;
                bitangents[b  ] = bitangent.x;
                bitangents[b+1] = bitangent.y;
                bitangents[b+2] = bitangent.z;
                bitangents[c  ] = bitangent.x;
                bitangents[c+1] = bitangent.y;
                bitangents[c+2] = bitangent.z;
                
            }
            
            return new Tuple2<>(tangents,bitangents);
        }
    	
    	return null;
    	
    }


}
