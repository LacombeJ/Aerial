package jonl.ge.utils;

import java.awt.image.BufferedImage;
import jonl.jutils.misc.ImageUtils;
import jonl.ge.core.Texture;
import jonl.ge.core.Texture.Filter;
import jonl.ge.core.Texture.Internal;
import jonl.ge.core.Texture.Wrap;
import jonl.ge.core.geometry.Geometry;
import jonl.jgl.utils.MeshLoader;

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
        jonl.jgl.utils.MeshData md = MeshLoader.load(fileLoc);
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
