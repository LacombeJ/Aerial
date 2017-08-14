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
            glmesh = gl.glGenMesh(mesh.getVertices(),mesh.getNormals(),mesh.getTexCoords(),mesh.getIndices());
            mesh.calculateTangents();
            glmesh.setCustomAttrib(3,mesh.getTangents(),3);
            glmesh.setCustomAttrib(4,mesh.getBiTangents(),3);
            mesh.setTangentsNull();
            mesh.setBiTangentsNull();
            if (mesh.isStatic()) { //TODO ?
                mesh.setVerticesNull();
                mesh.setNormalsNull();
                mesh.setTexCoordsNull();
                mesh.setIndicesNull();
            }
            mesh.overrideChanged();
            meshMap.put(mesh,glmesh);
        } else if (mesh.isChanged()) {
            if (!mesh.isVerticesNull())    glmesh.setVertexAttrib(mesh.getVertices(),3);
            if (!mesh.isNormalsNull())     glmesh.setNormalAttrib(mesh.getNormals(),3);
            if (!mesh.isTexCoordsNull())   glmesh.setTexCoordAttrib(mesh.getTexCoords(),2);
            if (!mesh.isIndicesNull())     glmesh.setIndices(mesh.getIndices());
            if (mesh.isStatic()) { //TODO
                mesh.setVerticesNull();
                mesh.setNormalsNull();
                mesh.setTexCoordsNull();
                mesh.setIndicesNull();
            }
            mesh.overrideChanged();
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
    
    
    
    /* ******************************************************************************** */
    
    // We want to create a texture for each text object (to save rendering time in matrix
    // multiplication and rendering each character as a separate texture
    // TODO remove font textures that haven't been used in a long time
    /*
    jonl.jgl.Texture getOrCreateTextTexture(Text text) {
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
    */
    
    
    /* ******************************************************************************** */
    
    
}


