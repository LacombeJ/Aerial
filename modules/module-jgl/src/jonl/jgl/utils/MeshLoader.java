package jonl.jgl.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility for loading .mesh files<p>
 * Approx. 100 times faster than using OBJLoader
 * 
 * @author Jonathan Lacombe
 *
 */
public class MeshLoader {

    /**
     * Loads a .mesh file and returns the MeshData
     */
    public static MeshData load(String file) {
        
        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            
            float[] v = parseFloatArray(fis);
            float[] n = parseFloatArray(fis);
            float[] t = parseFloatArray(fis);
            int[] i = parseIntArray(fis);
            
            fis.close();
            
            return new MeshData(v,n,t,i);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
        
    }
    
    private static float[] parseFloatArray(InputStream is) throws IOException {
        int len = readInt(is);
        float[] v = new float[len];
        for (int i=0; i<len; i++) {
            int r = readInt(is);
            v[i] = Float.intBitsToFloat(r);
        }
        return v;
    }
    
    private static int[] parseIntArray(InputStream is) throws IOException {
        int len = readInt(is);
        int[] v = new int[len];
        for (int i=0; i<len; i++) {
            int r = readInt(is);
            v[i] = r;
        }
        return v;
    }
    
    
    
    
    public static void save(String file, MeshData md) {
        
        try {
            
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            
            writeFloatArray(os,md.vertexData);
            writeFloatArray(os,md.normalData);
            writeFloatArray(os,md.texCoordData);
            writeIntArray(os,md.indices);
            
            os.flush();
            os.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private static void writeFloatArray(OutputStream os, float[] array) throws IOException {
        writeInt(os,array.length);
        for (float f : array) {
            int i = Float.floatToIntBits(f);
            writeInt(os,i);
        }
    }
    
    private static void writeIntArray(OutputStream os, int[] array) throws IOException {
        writeInt(os,array.length);
        for (int i : array) {
            writeInt(os,i);
        }
    }
    
    private static void writeInt(OutputStream os, int i) throws IOException {
        os.write((i & 0xff000000) >> 24);
        os.write((i & 0x00ff0000) >> 16);
        os.write((i & 0x0000ff00) >>  8);
        os.write((i & 0x000000ff)      );
    }
    
    private static int readInt(InputStream is) throws IOException {
        int i = 0x00000000;
        i |= is.read() << 24;
        i |= is.read() << 16;
        i |= is.read() <<  8;
        i |= is.read();
        return i;
    }
    
    
    
    
}
