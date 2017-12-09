package jonl.jutils.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jonl.jutils.structs.FloatList;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class ArrayUtils {
    
    public static float[] toFloatArray(List<Float> list) {
        float[] array = new float[list.size()];
        int i=0;
        for (float f : list) {
            array[i++] = f;
        }
        return array;
    }
    
    public static int[] toIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        int i=0;
        for (int n : list) {
            array[i++] = n;
        }
        return array;
    }
    
    /**
     * list.get(i) == return[i]
     * @param list
     * @return a float array containing the float arrays in the given list
     */
    public static float[][] toFloatArray(ArrayList<float[]> list) {
        float[][] array = new float[list.size()][];
        for (int i=0; i<array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    public static float[] getFloatArray(float[][] v) {
        FloatList list = new FloatList(v[0].length*v.length);
        for (int i=0; i<v.length; i++) {
            for (int j=0; j<v[i].length; j++) {
                list.add(v[i][j]);
            }
        }
        return list.toArray();
    }
    
    public static float[][] getVectorArray(float[] f, int size) {
        ArrayList<float[]> list = new ArrayList<>(f.length/size);
        int i=0;
        while (i<f.length) {
            float[] v = new float[size];
            int j=0;
            while (j<size && i<f.length) {
                v[j] = f[i];
                i++;
                j++;
            }
            list.add(v);
        }
        return toFloatArray(list);
        
    }
    
    public static float[] copy(float[] f) {
        return Arrays.copyOf(f,f.length);
    }
    public static float[][] copy(float[][] array) {
        float[][] copy = new float[array.length][];
        for (int i=0; i<array.length; i++) {
            copy[i] = copy(array[i]);
        }
        return copy;
    }
    
    public static int[] intArray(int length, int start, int increment) {
        int[] array = new int[length];
        int val = start;
        for (int i=0; i<array.length; i++) {
            array[i] = val;
            val += increment;
        }
        return array;
    }

    public static int[] copy(int[] i) {
        return Arrays.copyOf(i,i.length);
    }
    
    public static int[][] copy(int[][] array) {
        int[][] copy = new int[array.length][];
        for (int i=0; i<array.length; i++) {
            copy[i] = copy(array[i]);
        }
        return copy;
    }
    
    public static int[] add(int[] i, int... j) {
        int[] sum = new int[i.length];
        for (int k=0; k<i.length; k++) {
            sum[k] = i[k] + j[k];
        }
        return sum;
    }
    
    
    
    public static boolean[] copy(boolean[] array) {
        return Arrays.copyOf(array,array.length);
    }
    
    
    public static double[] copy(double[] array) {
        return Arrays.copyOf(array,array.length);
    }
    
    
    public static String toString(int[] list) {
        StringBuilder build = new StringBuilder();
        build.append("[ ");
        for (int i : list) {
            build.append(i+" ");
        }
        build.append("]");
        return build.toString();
    }
    
    public static String toString(float[] list) {
        StringBuilder build = new StringBuilder();
        build.append("[ ");
        for (float f : list) {
            build.append(f+" ");
        }
        build.append("]");
        return build.toString();
    }
    
    public static String toString(byte[] list) {
        StringBuilder build = new StringBuilder();
        build.append("[ ");
        for (byte b : list) {
            build.append(b+" ");
        }
        build.append("]");
        return build.toString();
    }
    
    public static String toString(double[] list) {
        StringBuilder build = new StringBuilder();
        build.append("[ ");
        for (double d : list) {
            build.append(d+" ");
        }
        build.append("]");
        return build.toString();
    }
    
    public static String toString(char[] list) {
        StringBuilder build = new StringBuilder();
        build.append("[ ");
        for (char c : list) {
            build.append(c+" ");
        }
        build.append("]");
        return build.toString();
    }

    public static Object[] copy(Object[] array) {
        return Arrays.copyOf(array, array.length);
    }
    
    public static void copy(Object[] src, Object[] dst) {
        System.arraycopy(src, 0, dst, 0, src.length);
    }
    
    public static int[] subArray(int[] array, int i, int j) {
        int[] sub = new int[j-i];
        System.arraycopy(array, i, sub, 0, sub.length);
        return sub;
    }
    
    public static float[] subArray(float[] array, int i, int j) {
        float[] sub = new float[j-i];
        System.arraycopy(array, i, sub, 0, sub.length);
        return sub;
    }


    public static boolean equals(float[] a, float[] b) {
        if (a.length != b.length) return false;
        for (int i=0; i<a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean equals(int[] a, int[] b) {
        if (a.length != b.length) return false;
        for (int i=0; i<a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    
    public static Integer[] wrap(int[] array) {
        Integer[] wrap = new Integer[array.length];
        for (int i=0; i<array.length; i++) {
            wrap[i] = array[i];
        }
        return wrap;
    }
    
    public static int[] unwrap(Integer[] array) {
        int[] wrap = new int[array.length];
        for (int i=0; i<array.length; i++) {
            wrap[i] = array[i];
        }
        return wrap;
    }
    
    public static Float[] wrap(float[] array) {
        Float[] wrap = new Float[array.length];
        for (int i=0; i<array.length; i++) {
            wrap[i] = array[i];
        }
        return wrap;
    }
    
    public static float[] unwrap(Float[] array) {
        float[] wrap = new float[array.length];
        for (int i=0; i<array.length; i++) {
            wrap[i] = array[i];
        }
        return wrap;
    }
    
    
}
