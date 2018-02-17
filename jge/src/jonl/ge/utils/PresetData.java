package jonl.ge.utils;

import jonl.jgl.utils.Presets;

public class PresetData {
    
	//TOOD remove this
	
    private static MeshData md(jonl.jgl.utils.MeshData md) {
        MeshData ret = new MeshData(md.vertexData,md.normalData,md.texCoordData,md.indices);
        return ret;
    }
    
    public static String basicVSSource(int version) {
        return Presets.basicVSSource(version);
    }
    
    public static String basicFSSource(int version) {
        return Presets.basicFSSource(version);
    }
    
    public static String fontVSSource(int version) {
        return Presets.fontVSSource(version);
    }
    
    public static String fontFSSource(int version) {
        return Presets.fontFSSource(version);
    }
    
    public static String solidVSSource(int version) {
        return Presets.solidVSSource(version);
    }
    
    public static String solidFSSource(int version) {
        return Presets.solidFSSource(version);
    }

    
    public static MeshData rectMesh() {
        return md(Presets.rectMesh());
    }
    
    public static MeshData planeMesh() {
        return md(Presets.planeMesh());
    }
    
    public static MeshData circleMesh() {
        return md(Presets.circleMesh());
    }
    
    public static MeshData cubeMesh() {
        return md(Presets.cubeMesh());
    }
    
    public static MeshData coneMesh() {
        return md(Presets.coneMesh());
    }
    
    public static MeshData sphereMesh() {
        return md(Presets.sphereMesh());
    }
    
}
