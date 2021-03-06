package ax.graphics.utils;

import ax.commons.misc.ArrayUtils;

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
        float[] vd = new float[m0.vertexData.length];
        for (int i=0; i<m0.vertexData.length; i++) {
            vd[i] = m0.vertexData[i]*(1-f) + m1.vertexData[i]*f;
        }
        
        float[] nd = new float[m0.normalData.length];
        for (int i=0; i<m0.normalData.length; i++) {
            nd[i] = m0.normalData[i]*(1-f) + m1.normalData[i]*f;
        }
        
        float[] tcd = new float[m0.texCoordData.length];
        for (int i=0; i<m0.texCoordData.length; i++) {
            tcd[i] = m0.texCoordData[i]*(1-f) + m1.texCoordData[i]*f;
        }
        
        int[] id = ArrayUtils.copy(m0.indices);
        
        return new MeshData(vd,nd,tcd,id);
    }
    
}
