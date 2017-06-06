package jonl.ge;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import jonl.jutils.misc.BufferPool;
import jonl.jutils.misc.ImageUtils;
import jonl.jgl.utils.MeshLoader;

public class Loader {

    public static Mesh loadMesh(MeshData md) {
        Mesh m = new Mesh();
        m.setVertices(md.vertexData);
        m.setNormals(md.normalData);
        m.setTexCoords(md.texCoordData);
        m.setIndices(md.indices);
        return m;
    }
    
    public static Mesh loadMesh(String fileLoc) {
        return loadMesh(loadMeshData(fileLoc));
    }
    
    public static MeshData loadMeshData(String fileLoc) {
        jonl.jgl.utils.MeshData md = MeshLoader.load(fileLoc);
        return new MeshData(md.vertexData,md.normalData,md.texCoordData,md.indices);
    }
    
    public static Texture loadTexture(String fileLoc) {
        BufferedImage b = ImageUtils.loadBufferedImage(fileLoc);
        FloatBuffer fb = ImageUtils.borrowBufferData(b);
        float[] f = new float[fb.limit()];
        for (int i=0; i<f.length; i++) {
            f[i] = fb.get(i);
        }
        Texture t = new Texture(f,b.getWidth(),b.getHeight());
        BufferPool.returnFloatBuffer(fb);
        return t;
    }
    
}
