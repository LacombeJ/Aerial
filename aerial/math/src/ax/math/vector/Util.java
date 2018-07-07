package ax.math.vector;

import java.util.ArrayList;
import java.util.List;

class Util {
    
    static <V extends Vector<V>> void lerp(float alpha, V[] v0, V[] v1, V[] dst) {
        if (v0.length!=v1.length || v0.length!=v1.length) return;
        
        for (int i=0; i<v0.length; i++) {
            dst[i] = v0[i].get().lerp(v1[i],alpha);
        }
    }
    
    static <V extends Vector<V>> void slerp(float alpha, V[] v0, V[] v1, V[] dst) {
        if (v0.length!=v1.length || v0.length!=v1.length) return;
        
        for (int i=0; i<v0.length; i++) {
            dst[i] = v0[i].get().slerp(v1[i],alpha);
        }
    }
    
    static <V extends Vector<V>> float[] pack(List<V> vectors) {
        if (vectors.size()==0) return new float[0];
        
        int dim = vectors.get(0).size();
        float[] values = new float[vectors.size()*dim];
        for (int i=0; i<vectors.size(); i++) {
            for (int j=0; j<dim; j++) {
                values[i*dim+j] = vectors.get(i).get(j);
            }
        }
        return values;
    }
    
    @SuppressWarnings("unchecked")
    static <V extends Vector<V>> float[] pack(V... vectors) {
        if (vectors.length==0) return new float[0];
        
        int dim = vectors[0].size();
        float[] values = new float[vectors.length*dim];
        for (int i=0; i<vectors.length; i++) {
            for (int j=0; j<dim; j++) {
                values[i*dim+j] = vectors[i].get(j);
            }
        }
        return values;
    }
    
    static <V extends Vector<V>> ArrayList<V> unpack(float[] values, V v) {
        ArrayList<V> vectors = new ArrayList<>();
        
        int dim = v.size();
        for (int i=0; i<values.length/dim; i++) {
            V nv = v.getEmptyVector();
            for (int j=0; j<dim; j++) {
                nv.set(j,values[i*dim+j]);
            }
            vectors.add(nv);
        }
        return vectors;
    }
    
    static <V extends Vector<V>> V[] unpackArray(float[] values, V v, V[] av) {
        return unpack(values,v).toArray(av);
    }
    
    
    static <M extends Matrix<M,R,C>,R extends Vector<R>,C extends Vector<C>> float[] mpack(List<M> matrices) {
        if (matrices.size()==0) return new float[0];
        
        int dim = matrices.get(0).size();
        float[] values = new float[matrices.size()*dim];
        for (int i=0; i<matrices.size(); i++) {
            for (int j=0; j<dim; j++) {
                float[] v = matrices.get(i).toArray();
                values[i*dim+j] = v[j];
            }
        }
        return values;
    }
    
    @SuppressWarnings("unchecked")
    static <M extends Matrix<M,R,C>,R extends Vector<R>,C extends Vector<C>> float[] mpack(M... matrices) {
        if (matrices.length==0) return new float[0];
        
        int dim = matrices[0].size();
        float[] values = new float[matrices.length*dim];
        for (int i=0; i<matrices.length; i++) {
            for (int j=0; j<dim; j++) {
                float[] v = matrices[i].toArray();
                values[i*dim+j] = v[j];
            }
        }
        return values;
    }
    
    static <M extends Matrix<M,R,C>,R extends Vector<R>,C extends Vector<C>> ArrayList<M> munpack(float[] values, M m) {
        ArrayList<M> matrices = new ArrayList<>();
        
        int dim = m.size();
        int r = m.getRows();
        int c = m.getColumns();
        for (int i=0; i<values.length/dim; i++) {
            M nm = m.getEmptyMatrix();
            int index = 0;
            for (int j=0; j<r; j++) {
                for (int k=0; k<c; k++) {
                    nm.set(j,k,values[i*dim+index]);
                    index++;
                }
            }
            matrices.add(nm);
        }
        return matrices;
    }
    
    static <M extends Matrix<M,R,C>,R extends Vector<R>,C extends Vector<C>> M[] munpackArray(float[] values, M m, M[] am) {
        return munpack(values,m).toArray(am);
    }

    
}
