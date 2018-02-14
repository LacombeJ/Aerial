package jonl.ge.core;

public class MeshData {

    public final float[] vertexData;
    public final float[] normalData;
    public final float[] texCoordData;
    public final int[] indices;
    
    public MeshData(float[] vertexData, float[] normalData,
            float[] texCoordData, int[] indices) {
        this.vertexData = vertexData;
        this.normalData = normalData;
        this.texCoordData = texCoordData;
        this.indices = indices;
    }
    
    public static MeshData interpolate(MeshData m0, MeshData m1, float f) {
        jonl.jgl.utils.MeshData jm0 = new jonl.jgl.utils.MeshData(m0.vertexData,m0.normalData,m0.texCoordData,m0.indices);
        jonl.jgl.utils.MeshData jm1 = new jonl.jgl.utils.MeshData(m1.vertexData,m1.normalData,m1.texCoordData,m1.indices);
        jonl.jgl.utils.MeshData jmd = jonl.jgl.utils.MeshData.interpolate(jm0,jm1,f);
        return new MeshData(jmd.vertexData,jmd.normalData,jmd.texCoordData,jmd.indices);
    }
    
}
