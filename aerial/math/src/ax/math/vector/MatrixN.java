package ax.math.vector;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class MatrixN extends SquareMatrix<MatrixN,VectorN> {

    public final float[] matrix;
    
    private final int rows;
    
    public MatrixN(int dimension) {
        
        matrix = new float[dimension*dimension];
        rows = dimension;
        
    }
    
    public MatrixN(float[][] array) {
        rows = array.length;
        matrix = new float[rows*rows];
        for (int i=0; i<rows; i++) {
            for (int j=0; j<rows; j++) {
                set(i,j,array[i][j]);
            }
        }
    }

    @Override
    public int getRows() {
        return rows;
    }
    
    @Override
    public int size() {
        return matrix.length;
    }

    @Override
    public float get(int i, int j) {
        return matrix[i*rows + j];
    }

    @Override
    public void set(int i, int j, float v) {
        matrix[i*rows + j] = v;
    }
    
    @Override
    protected VectorN getEmptyRow() {
        return new VectorN(new float[getColumns()]);
    }
    
    @Override
    protected MatrixN getEmptyMatrix() {
        return new MatrixN(getRows());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <S extends SquareMatrix<S, ?>> S subMatrix() {
        return (S) new MatrixN(rows-1);
    }
    
}
