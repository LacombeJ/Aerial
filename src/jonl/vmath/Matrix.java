package jonl.vmath;

import java.nio.FloatBuffer;

/**
 * Class representing an abstract matrix
 * <p>
 * For a matrix of size MxN the row vector should be of size N(number of
 * columns) and the column vector should be of size M(number of rows). For
 * instance, a matrix of size 8x4, the row vector should be of size 4 and the
 * column vector should be of size 8.
 * 
 * @author Jonathan Lacombe
 *
 * @param <M> matrix type
 * @param <R> row vector
 * @param <C> column vector
 */
public abstract class Matrix<M extends Matrix<M,R,C>,
    R extends Vector<R>,C extends Vector<C>> {

    /** @return the number of rows in this matrix */
    public abstract int getRows();

    /** @return the number of columns in this matrix */
    public abstract int getColumns();

    /** @return the number of elements in this matrix */
    public int size() {
        return getRows() * getColumns();
    }

    /** @return the value at the given row and column */
    public abstract float get(int row, int col);

    /** Sets the value at the given row and column of this matrix */
    public abstract void set(int row, int col, float value);

    /** @return a vector filled with 0s */
    protected abstract R getEmptyRow();

    /** @return a vector filled with 0s */
    protected abstract C getEmptyCol();

    /** @return a matrix filled with 0s */
    protected abstract M getEmptyMatrix();

    /** @return a copy of this matrix */
    public M get() {
        M m = getEmptyMatrix();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                m.set(i,j,get(i,j));
            }
        }
        return m;
    }

    /** Sets the value of this matrix from the given matrix */
    public void set(M matrix) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                set(i,j,matrix.get(i,j));
            }
        }
    }

    /** @return the row vector at the given index */
    public R getRow(int row) {
        R v = getEmptyRow();
        for (int i = 0; i < getColumns(); i++) {
            v.set(i,get(row,i));
        }
        return v;
    }

    /** @return the column vector at the given index */
    public C getCol(int col) {
        C v = getEmptyCol();
        for (int i = 0; i < getColumns(); i++) {
            v.set(i,get(i,col));
        }
        return v;
    }
    
    /** @return this matrix after it has been added to the given matrix */
    @SuppressWarnings("unchecked")
    public M add(M matrix) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                set(i,j,get(i,j)+matrix.get(i,j));
            }
        }
        return (M)this;
    }
    
    /** @return this matrix after it has been added to the given matrix */
    @SuppressWarnings("unchecked")
    public M sub(M matrix) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                set(i,j,get(i,j)-matrix.get(i,j));
            }
        }
        return (M)this;
    }

    /** @return the product of this matrix and a vector */
    public C multiply(R vector) {
        C v = getEmptyCol();
        for (int i = 0; i < v.size(); i++) {
            float sum = 0;
            for (int j = 0; j < vector.size(); j++) {
                sum += get(i,j) * vector.get(j);
            }
            v.set(i,sum);
        }
        return v;
    }

    /** @return this matrix after it has been multiplied by the given matrix */
    @SuppressWarnings("unchecked")
    public M multiply(M matrix) {
        if (!isTransposable() || !matrix.isTransposable())
            return null;
        if (getColumns() != matrix.getColumns())
            return null;
        M m = getEmptyMatrix();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                m.set(i,j,getRow(i).dot((R)matrix.getCol(j)));
            }
        }
        set(m);
        return (M)this;
    }

    /** @return this matrix after it has been scaled by the given constant */
    @SuppressWarnings("unchecked")
    public M multiply(float scalar) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                set(i,j,scalar * get(i,j));
            }
        }
        return (M)this;
    }

    /**
     * @return this matrix after it has been transposed or
     * null if not transposable
     */
    @SuppressWarnings("unchecked")
    public M transpose() {
        if (!isTransposable())
            return null;
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns() - i; j++) {
                float tmp = get(i,j);
                set(i,j,get(j,i));
                set(j,i,tmp);
            }
        }
        return (M)this;
    }

    /** @return true if rows==columns */
    public final boolean isTransposable() {
        return getRows() == getColumns();
    }
    
    /** @return a float array with the same elements */
    public float[] toArray() {
        float[] array = new float[size()];
        int index = 0;
        for (int i=0; i<getRows(); i++) {
            for (int j=0; j<getColumns(); j++) {
                array[index++] = get(i,j);
            }
        }
        return array;
    }
    
    public FloatBuffer toFloatBuffer(FloatBuffer fb) {
        for (int i=0; i<getRows(); i++) {
            for (int j=0; j<getColumns(); j++) {
                fb.put(get(i,j));
            }
        }
        fb.flip();
        return fb;
    }

    /** @return exception string for index out of bounds */
    protected String getExceptionString(int row, int col) {
        return "Index out of bounds.. Size: " + getRows() + "x"
                + getColumns() + " Target: " + row + "x" + col;
    }

    /** Prints this matrix on n+1 lines where n==number of rows */
    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix[" + getRows() + "][" + getColumns() + "]\n");
        for (int i = 0; i < getRows(); i++) {
            sb.append("[ ");
            for (int j = 0; j < getColumns(); j++) {
                sb.append(get(i,j) + " ");
            }
            sb.append("]\n");
        }
        if (getRows() == 0)
            sb.append("[ ]");
        System.out.print(sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        int i = 0;
        for (i = 0; i < getRows() - 1; i++) {
            for (int j = 0; j < getColumns(); j++) {
                sb.append(get(i,j) + " ");
            }
            sb.append("| ");
        }
        if (getRows() > 0 && getColumns() > 0) {
            for (int j = 0; j < getColumns(); j++) {
                sb.append(get(i,j) + " ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix<?,?,?>) {
            Matrix<?,?,?> m = (Matrix<?,?,?>)obj;
            if (getRows() != m.getRows() || getColumns() != m.getColumns())
                return false;
            for (int i = 0; i < getRows(); i++) {
                for (int j = 0; j < getColumns(); j++) {
                    if (get(i,j) != m.get(i,j))
                        return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }
    

}
