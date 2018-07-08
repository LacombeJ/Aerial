package ax.engine.utils;

import java.awt.image.BufferedImage;

import ax.commons.misc.ImageUtils;
import ax.engine.core.Geometry;
import ax.engine.core.Texture;
import ax.engine.core.Texture.Filter;
import ax.engine.core.Texture.Internal;
import ax.engine.core.Texture.Wrap;
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
    
    public static Texture loadTexture(String fileLoc, Internal internal, Wrap wrap, Filter filter) {
        BufferedImage b = ImageUtils.load(fileLoc);
        Texture t = new Texture(ImageUtils.data(b),b.getWidth(),b.getHeight(),internal,wrap,filter);
        return t;
    }
    
    public static Texture loadTexture(String fileLoc, Wrap wrap) {
        return loadTexture(fileLoc,Internal.RGBA16,wrap,Filter.MIPMAP);
    }
    
    public static Texture loadTexture(String fileLoc) {
        return loadTexture(fileLoc,Internal.RGBA16,Wrap.CLAMP,Filter.MIPMAP);
    }
    
}
