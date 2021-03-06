package ax.engine.core;

import java.util.HashMap;

import ax.commons.func.Tuple2;
import ax.engine.core.Geometry.Attribute;
import ax.engine.utils.GLUtils;
import ax.engine.utils.Loader;
import ax.engine.utils.PresetData;
import ax.graphics.GL;
import ax.graphics.Program;
import ax.math.vector.Mathf;
import ax.math.vector.Matrix2;
import ax.math.vector.Matrix3;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

class GLRenderer {

	
	private GL gl;
	
	private int version = 430;

    private HashMap<Geometry,ax.graphics.Mesh>             meshMap         = new HashMap<>();
    private HashMap<Texture,ax.graphics.Texture>           textureMap      = new HashMap<>();
    private HashMap<FrameBuffer,ax.graphics.FrameBuffer>   bufferMap       = new HashMap<>();
    
    private HashMap<String,Program> 					shaderKeyMap 	= new HashMap<>();
    
    // TODO find a better way to do this ? Maybe use PlaneGeometry ?
    private Geometry rectGeometry = Loader.loadMesh(PresetData.rectMesh());
    
    GLRenderer(Service service, GL gl) {
        this.gl = gl;
        
        service.implementGetOrCreateMesh(mesh -> getOrCreateMesh(mesh));
        service.implementGetOrCreateTexture(texture -> getOrCreateTexture(texture));
        service.implementGetOrCreateFrameBuffer(fb -> getOrCreateFrameBuffer(fb));
        service.implementGetOrCreateProgram(material -> getOrCreateProgram(material));
    }
    
    void setGLSLVersion(int version) {
    	this.version = version;
    }
    
    Geometry getRectGeometry() {
    	return rectGeometry;
    }
    
    ax.graphics.Mesh getOrCreateMesh(Geometry mesh) {
        ax.graphics.Mesh glmesh = meshMap.get(mesh);
        // TODO find a better way to set attribute start index
        int sindex = 1; // Attribute start index, 1 after vertices
        if (glmesh==null) {
            glmesh = gl.glGenMesh(
                Vector3.pack(mesh.vertices), 
                Vector3.pack(mesh.normals),
                Vector2.pack(mesh.texCoords),
                mesh.indices);
            if (checkUpdateTangents(mesh, glmesh)) {
                sindex = 5; // after tangent and bitangent
            }
            for (int i=0; i<mesh.getNumAttributes(); i++) {
                Attribute a = mesh.attributes.get(i);
                glmesh.setCustomAttrib(sindex+i,a.data,a.size);
            }
            meshMap.put(mesh,glmesh);
        } else if (mesh.update) {
        	if (mesh.vertices!=null && mesh.vertices.length>0) glmesh.setVertexAttrib(Vector3.pack(mesh.vertices), 3);
        	if (mesh.normals!=null && mesh.normals.length>0) glmesh.setNormalAttrib(Vector3.pack(mesh.normals), 3);
        	if (mesh.texCoords!=null && mesh.texCoords.length>0) glmesh.setTexCoordAttrib(Vector2.pack(mesh.texCoords), 2);
        	if (mesh.indices!=null && mesh.indices.length>0) glmesh.setIndices(mesh.indices);
        	if (checkUpdateTangents(mesh, glmesh)) {
                sindex = 5; // after tangent and bitangent
            }
        	for (int i=0; i<mesh.getNumAttributes(); i++) {
                Attribute a = mesh.attributes.get(i);
                glmesh.setCustomAttrib(sindex+i,a.data,a.size);
            }
        }
        mesh.update = false;
        return glmesh;
    }
    
    ax.graphics.Texture getOrCreateTexture(Texture texture) {
        ax.graphics.Texture gltexture = textureMap.get(texture);
        if (gltexture==null) {
            if (texture.data==null) {
                gltexture = gl.glGenTexture(
                        texture.width, texture.height,
                        texture.format,
                        texture.wrap,
                        texture.filter);
            } else {
                gltexture = gl.glGenTexture(
                        texture.data,
                        texture.width,texture.height,
                        texture.format,
                        texture.wrap,
                        texture.filter);
            }
            textureMap.put(texture,gltexture);
        }
        return gltexture;
    }
    
    ax.graphics.FrameBuffer getOrCreateFrameBuffer(FrameBuffer buffer) {
        ax.graphics.FrameBuffer glbuffer = bufferMap.get(buffer);
        if (glbuffer==null) {
            glbuffer = gl.glGenFramebuffer(buffer.width(),buffer.height());
            for (Texture texture : buffer.textures()) {
                ax.graphics.Texture gltexture = getOrCreateTexture(texture);
                glbuffer.attach(gltexture);
            }
            glbuffer.link();
            bufferMap.put(buffer,glbuffer);
        }
        return glbuffer;
    }
    
    // -----------------------------------------------------------------------------------
    
    Program getOrCreateProgram(Material material) {
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
    
    static boolean checkUpdateTangents(Geometry mesh, ax.graphics.Mesh glmesh) {
    	if (mesh.calculateTangents) {
        	Tuple2<Vector3[],Vector3[]> calculated = calculateTangents(mesh.vertices, mesh.normals, mesh.texCoords, mesh.indices);
        	if (calculated != null) {
        	    float[] tangents = Vector3.pack(calculated.x);
                float[] bitangents = Vector3.pack(calculated.y);
                glmesh.setCustomAttrib(3,tangents,3);
                glmesh.setCustomAttrib(4,bitangents,3);
                return true;
        	}
        }
    	return false;
    }
    
    // https://learnopengl.com/#!Advanced-Lighting/Normal-Mapping
    //TODO calculate smooth tangents instead of flat tangents?
    private static Tuple2<Vector3[],Vector3[]> calculateTangents(Vector3[] vertices, Vector3[] normals, Vector2[] texCoords, int[] indices) {
        
        if (vertices!=null && normals!=null && texCoords!=null && indices!=null) {
            if (texCoords.length!=0 && indices.length!=0) {
                //Switched back to using floats because calculations resulted in missing tangent vectors for SphereGeometry
                //TODO find out why this happens, indices don't capture all of the vertices ?
                float[] tangents = new float[vertices.length*3];
                float[] bitangents = new float[vertices.length*3];
                
                for (int i=0; i<indices.length; i+=3) {
                    
                    int a = indices[i];
                    int b = indices[i+1];
                    int c = indices[i+2];
                    Vector3 vertexA = vertices[a];
                    Vector3 vertexB = vertices[b];
                    Vector3 vertexC = vertices[c];
                    
                    int ta = indices[i];
                    int tb = indices[i+1];
                    int tc = indices[i+2];
                    Vector2 texCoordA = texCoords[ta];
                    Vector2 texCoordB = texCoords[tb];
                    Vector2 texCoordC = texCoords[tc];
                    
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
                    
                    int a3 = a*3;
                    int b3 = b*3;
                    int c3 = c*3;
                    
                    tangents[a3  ] = tangent.x;
                    tangents[a3+1] = tangent.y;
                    tangents[a3+2] = tangent.z;
                    tangents[b3  ] = tangent.x;
                    tangents[b3+1] = tangent.y;
                    tangents[b3+2] = tangent.z;
                    tangents[c3  ] = tangent.x;
                    tangents[c3+1] = tangent.y;
                    tangents[c3+2] = tangent.z;
                    
                    bitangents[a3  ] = bitangent.x;
                    bitangents[a3+1] = bitangent.y;
                    bitangents[a3+2] = bitangent.z;
                    bitangents[b3  ] = bitangent.x;
                    bitangents[b3+1] = bitangent.y;
                    bitangents[b3+2] = bitangent.z;
                    bitangents[c3  ] = bitangent.x;
                    bitangents[c3+1] = bitangent.y;
                    bitangents[c3+2] = bitangent.z;
                }
                
                return new Tuple2<>(Vector3.unpackArray(tangents),Vector3.unpackArray(bitangents));
            
            }
        }
        
        return null;
        
    }
    
}
