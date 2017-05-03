package jonl.ge.utils;

import jonl.ge.Loader;
import jonl.ge.MeshData;
import jonl.jutils.io.Console;
import jonl.jutils.misc.ArrayUtils;
import jonl.jutils.structs.Array3Df;
import jonl.jutils.structs.IntArray;

public class GEUtilities {

    public static MeshData createTerrainGrid(float squareDim, int rows, int columns) {
        
        int R = rows+1;
        int C = columns+1;
        
        Array3Df vertexData = new Array3Df(3,C,R);
        Array3Df normalData = new Array3Df(3,C,R);
        Array3Df texCoordData = new Array3Df(2,C,R);
        IntArray indices = new IntArray(rows*columns*2*3);
        
        for (int i=0; i<R; i++) {
            for (int j=0; j<C; j++) {
                
                vertexData.set(0,j,i,j*squareDim);
                vertexData.set(1,j,i,0);
                vertexData.set(2,j,i,i*squareDim);
                
                normalData.set(0,j,i,0);
                normalData.set(1,j,i,1);
                normalData.set(2,j,i,0);
                
                texCoordData.set(0,j,i,0);
                texCoordData.set(1,j,i,0);
                
                
                if (i!=rows && j!=columns) {
                    int index0 = getIndex(i,  j,  C);
                    int index1 = getIndex(i,  j+1,C);
                    int index2 = getIndex(i+1,j,  C);
                    int index3 = getIndex(i+1,j+1,C);
                    indices.put(index2);
                    indices.put(index3);
                    indices.put(index0);
                    indices.put(index3);
                    indices.put(index1);
                    indices.put(index0);
                }
                
            }
        }
        
        Console.println(vertexData.getArray());
        
        for (int i=0; i<R; i++) {
            for (int j=0; j<C; j++) {
                
                float[] vertex = new float[]{
                        vertexData.get(0,j,i),
                        vertexData.get(1,j,i),
                        vertexData.get(2,j,i) };
                
                Console.println("Vertex:",vertex);
                
                }
        }
        Console.println("Space");
        printVertices(vertexData.getArray());
        
        Console.println(indices.getArray());
        
        MeshData plane = Loader.loadMeshData("res/models/plane.mesh");
        
        Console.println(plane.vertexData);
        printVertices(plane.vertexData);
        Console.println(plane.indices);
        
        MeshData md = new MeshData(vertexData.getArray(), normalData.getArray(),
                texCoordData.getArray(), indices.getArray());
        //return Loader.loadMeshData("res/models/cube2.mesh");
        return md;
    }
    
    private static void printVertices(float[] v) {
        int c = v.length/3;
        for (int i=0; i<c; i++) {
            float[] vertex = ArrayUtils.subArray(v,i*3,i*3+3);
            Console.println("Vertex:",vertex);
        }
    }
    
    private static int getIndex(int r, int c, int columns) {
        return r*columns + c;
    }
    
}
