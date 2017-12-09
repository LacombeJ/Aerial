/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package jonl.vmath;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class MathUtil {
    
    public static int floatToIntBits (float value) {
        return Float.floatToIntBits(value);
    }

    public static int floatToRawIntBits (float value) {
        return Float.floatToRawIntBits(value);
    }

    public static int floatToIntColor (float value) {
        return Float.floatToRawIntBits(value);
    }

    /** Encodes the ABGR int color as a float. The high bits are masked to avoid using floats in the NaN range, which unfortunately
     * means the full range of alpha cannot be used. See {@link Float#intBitsToFloat(int)} javadocs. */
    public static float intToFloatColor (int value) {
        return Float.intBitsToFloat(value & 0xfeffffff);
    }

    public static float intBitsToFloat (int value) {
        return Float.intBitsToFloat(value);
    }

    public static long doubleToLongBits (double value) {
        return Double.doubleToLongBits(value);
    }

    public static double longBitsToDouble (long value) {
        return Double.longBitsToDouble(value);
    }

    /** @return the given FloatBuffer with values of this matrix stored in it */
    public static FloatBuffer storeInBuffer(Matrix<?,?,?> mat, FloatBuffer fb) {
        for (int i = 0; i < mat.getRows(); i++) {
            for (int j = 0; j < mat.getColumns(); j++) {
                fb.put(mat.get(i,j));
            }
        }
        fb.flip();
        return fb;
    }
    
    /** @return the given FloatBuffer with values of this matrix stored in it as rgb values */
    public static FloatBuffer storeInBufferRGB(Matrix<?,?,?> mat, FloatBuffer fb) {
        for (int i = 0; i < mat.getRows(); i++) {
            for (int j = 0; j < mat.getColumns(); j++) {
                fb.put(mat.get(i,j));
                fb.put(mat.get(i,j));
                fb.put(mat.get(i,j));
                fb.put(1);
            }
        }
        fb.flip();
        return fb;
    }
    
    public static float[] getFloatArray(Vector<?>[] v) {
        float[] list = new float[v[0].size()*v.length];
        int index = 0;
        for (int i=0; i<v.length; i++) {
            for (int j=0; j<v[i].size(); j++) {
                list[index++] = v[i].get(j);
            }
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Vector<T>> T[] getVectorArray(float[] f, Vector<T> a, Vector<T>[] array) {
        ArrayList<T> list = new ArrayList<>();
        int i=0;
        while (i<f.length) {
            T v = a.getEmptyVector();
            int j=0;
            while (j<v.size() && i<f.length) {
                v.set(j,f[i]);
                i++;
                j++;
            }
            list.add(v);
        }
        return (T[]) list.toArray(array);
        
    }
    
}
