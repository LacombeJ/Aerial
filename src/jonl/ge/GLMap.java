package jonl.ge;

import java.util.HashMap;

import jonl.jgl.GraphicsLibrary;

class GLMap {
    
    private GraphicsLibrary gl;

    private HashMap<Mesh,jonl.jgl.Mesh>                 meshMap     = new HashMap<>();
    private HashMap<Font,jonl.jgl.Font>                 fontMap     = new HashMap<>();
    private HashMap<Texture,jonl.jgl.Texture>           textureMap  = new HashMap<>();
    private HashMap<FrameBuffer,jonl.jgl.FrameBuffer>   bufferMap   = new HashMap<>();
    
    GLMap(GraphicsLibrary gl) {
        this.gl = gl;
    }
    
    jonl.jgl.Mesh getOrCreateMesh(Mesh mesh) {
        jonl.jgl.Mesh glmesh = meshMap.get(mesh);
        if (glmesh==null) {
            glmesh = gl.glGenMesh(mesh.vertices,mesh.normals,mesh.texCoords,mesh.indices);
            mesh.calculateTangents();
            glmesh.setCustomAttrib(3,mesh.tangents,3);
            glmesh.setCustomAttrib(4,mesh.bitangents,3);
            mesh.tangents = null;
            mesh.bitangents = null;
            if (mesh.staticData) {
                mesh.vertices = null;
                mesh.normals = null;
                mesh.texCoords = null;
                mesh.indices = null;
            }
            meshMap.put(mesh,glmesh);
        } else if (mesh.changed) {
            if (mesh.vertices!=null)    glmesh.setVertexAttrib(mesh.vertices,3);
            if (mesh.normals!=null)     glmesh.setNormalAttrib(mesh.normals,3);
            if (mesh.normals!=null)     glmesh.setTexCoordAttrib(mesh.normals,2);
            if (mesh.indices!=null)     glmesh.setIndices(mesh.indices);
            if (!mesh.staticData) {
                mesh.vertices = null;
                mesh.normals = null;
                mesh.texCoords = null;
                mesh.indices = null;
            }
        }
        return glmesh;
    }
    
    jonl.jgl.Font getOrCreateFont(Font font) {
        jonl.jgl.Font glfont = fontMap.get(font);
        if (glfont==null) {
            glfont = gl.glGenFont(font.font,font.type,font.size,font.antialias);
            fontMap.put(font,glfont);
        }
        return glfont;
    }
    
    jonl.jgl.Texture getOrCreateTexture(Texture texture) {
        jonl.jgl.Texture gltexture = textureMap.get(texture);
        if (gltexture==null) {
            if (texture.data==null) {
                gltexture = gl.glGenTexture(texture.width,texture.height,
                        texture.format.format,texture.wrap.wrap,texture.filter.filter);
            } else {
                gltexture = gl.glGenTexture(texture.data,texture.width,texture.height,
                        texture.format.format,texture.wrap.wrap,texture.filter.filter);
            }
            textureMap.put(texture,gltexture);
        }
        return gltexture;
    }
    
    jonl.jgl.FrameBuffer getOrCreateFrameBuffer(FrameBuffer buffer) {
        jonl.jgl.FrameBuffer glbuffer = bufferMap.get(buffer);
        if (glbuffer==null) {
            glbuffer = gl.glGenFramebuffer(buffer.width,buffer.height);
            for (Texture texture : buffer.textures) {
                jonl.jgl.Texture gltexture = getOrCreateTexture(texture);
                glbuffer.attach(gltexture);
            }
            glbuffer.link();
            bufferMap.put(buffer,glbuffer);
        }
        return glbuffer;
    }
    
}


