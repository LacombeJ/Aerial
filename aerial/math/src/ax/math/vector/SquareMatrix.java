package ax.math.vector;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public abstract class SquareMatrix<M extends SquareMatrix<M,V>,
    V extends Vector<V>> extends Matrix<M,V,V> {
    
    /**
     * SubMatrix is a square matrix with one less dimension than the given sub matrix
     * @return an empty submatrix or null if this matrix is a 2x2 matrix
     */
    public abstract <S extends SquareMatrix<S,?>> S subMatrix();
    
    @Override
    public int getColumns() {
        return getRows();
    }
    
    @Override
    protected V getEmptyCol() {
        return getEmptyRow();
    }
    
    /** @return this matrix after it has been multiplied by the given matrix */
    @SuppressWarnings("unchecked")
    public M multiply(M matrix) {
        if (getRows() != matrix.getRows())
            return null;
        M m = getEmptyMatrix();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                m.set(i,j,getRow(i).dot((V)matrix.getCol(j)));
            }
        }
        set(m);
        return (M)this;
    }
    
    /**
     * @return this matrix after it has been transposed or
     * null if not transposable
     */
    @SuppressWarnings("unchecked")
    public M transpose() {
        for (int i = 0; i < getRows()-1; i++) {
            for (int j = i+1; j < getColumns(); j++) {
                float tmp = get(i,j);
                set(i,j,get(j,i));
                set(j,i,tmp);
            }
        }
        return (M)this;
    }
    
    /** @return the determinant of this square matrix */
    public float determinant() {
        if (getRows()==2) {
            return get(0,0)*get(1,1) - get(0,1)*get(1,0);
        }
        float sum = 0;
        int sign = 1;
        for (int i=0; i<getColumns(); i++) {
            sum += sign * get(0,i) * minor(0,i).determinant();
            sign *= -1;
        }
        return sum;
    }
    
    /**
     * Returns the minor defined by the row and col to skip
     * @param row row to skip
     * @param col column to skip
     * @return a sub matrix of minor of the given matrix
     */
    public SquareMatrix<?,?> minor(int row, int col) {
        SquareMatrix<?,?> minor = subMatrix();
        int II = 0;
        for (int i=0; i<getRows(); i++) {
            if (i==row) continue;
            int JJ = 0;
            for (int j=0; j<getColumns(); j++) {
                if (j==col) continue;
                minor.set(II, JJ, get(i,j));
                JJ++;
            }
            II++;
        }
        return minor;
    }
    
    /**
     * A matrix of minors of a matrix M is where each element of M is replaced
     * by the determinant of a sub-matrix without the row or col of the element.
     * @return the matrix of minors of this matrix
     */
    public M minors() {
        M mat = getEmptyMatrix();
        for (int i=0; i<getRows(); i++) {
            for (int j=0; j<getColumns(); j++) {
                SquareMatrix<?,?> minor = minor(i,j);
                mat.set(i,j,minor.determinant());
            }
        }
        return mat;
    }
    
    /**
     * @return this matrix as an alternating signed matrix
     */
    @SuppressWarnings("unchecked")
    public M signed() {
        int signRow = 1;
        for (int i=0; i<getRows(); i++) {
            int sign = signRow;
            for (int j=0; j<getColumns(); j++) {
                set(i,j,sign*get(i,j));
                sign *= -1;
            }
            signRow *= -1;
        }
        return (M) this;
    }
    
    /** @return the cofactor matrix which is the signed matrix of minors */
    public M cofactor() {
        return minors().signed();
    }
    
    /** @return the adjugate matrix which is the transpose of the cofactor matrix */
    public M adjugate() {
       return cofactor().transpose();
        
    }
    
    /** @return the inverse matrix of this matrix */
    public M inverse() {
        M cofactor = cofactor();
        float det = 0;
        for (int i=0; i<getColumns(); i++) {
            det += get(0,i) * cofactor.get(0,i);
        }
        M adjugate = cofactor.transpose();
        return adjugate.multiply(1/det);
    }
    
    
    
    
}
