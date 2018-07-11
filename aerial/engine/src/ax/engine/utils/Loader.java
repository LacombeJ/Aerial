package ax.engine.utils;

import java.awt.image.BufferedImage;

import ax.commons.misc.ImageUtils;
import ax.engine.core.Geometry;
import ax.engine.core.Texture;
import ax.graphics.GL;
import ax.graphics.utils.MeshLoader;

public class Loader {

    public static Geometry loadMesh(MeshData md) {
        Geometry m = new Geometry();
        m.set(md.vertexData,md.normalData,md.texCoordData,md.indices);
        return m;
    }
    
    public static Geometry loadMesh(String fileLoc) {
        return loadMesh(loadMeshData(fileLoc));
    }
    
    public static MeshData loadMeshData(String fileLoc) {
        ax.graphics.utils.MeshData md = MeshLoader.load(fileLoc);
        return new MeshData(md.vertexData,md.normalData,md.texCoordData,md.indices);
    }
    
    public static Texture loadTexture(String fileLoc, GL.Internal internal, GL.Wrap wrap, GL.Filter filter) {
        BufferedImage b = ImageUtils.load(fileLoc);
        Texture t = new Texture(ImageUtils.data(b),b.getWidth(),b.getHeight(),internal,wrap,filter);
        return t;
    }
    
    public static Texture loadTexture(String fileLoc, GL.Wrap wrap) {
        return loadTexture(fileLoc,GL.RGBA16,wrap,GL.MIPMAP);
    }
    
    public static Texture loadTexture(String fileLoc) {
        return loadTexture(fileLoc,GL.RGBA16,GL.CLAMP,GL.MIPMAP);
    }
    
}
