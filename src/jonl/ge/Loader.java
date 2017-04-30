package jonl.ge;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import jonl.jutils.misc.ImageUtils;
import jonl.jgl.utils.MeshData;
import jonl.jgl.utils.MeshLoader;

public class Loader {

    public static Mesh loadMesh(String fileLoc) {
        Mesh m = new Mesh();
        MeshData md = MeshLoader.load(fileLoc);
        m.vertices = md.vertexData;
        m.normals = md.normalData;
        m.texCoords = md.texCoordData;
        m.indices = md.indices;
        return m;
    }
    
    public static Texture loadTexture(String fileLoc) {
        BufferedImage b = ImageUtils.loadBufferedImage(fileLoc);
        FloatBuffer fb = ImageUtils.getBufferData(b);
        float[] f = new float[fb.limit()];
        for (int i=0; i<f.length; i++) {
            f[i] = fb.get(i);
        }
        Texture t = new Texture(f,b.getWidth(),b.getHeight());
        return t;
    }
    
}
