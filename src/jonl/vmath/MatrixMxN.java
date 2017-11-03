package jonl.vmath;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class MatrixMxN extends Matrix<MatrixMxN,VectorN,VectorN> {

    public final float[] matrix;
    
    private final int rows;
    
    public MatrixMxN(int width, int height) {
        
        matrix = new float[width*height];
        rows = height;
        
    }
    
    public MatrixMxN(float[][] array) {
        rows = array.length;
        int cols = array[0].length;
        matrix = new float[rows*cols];
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                set(i,j,array[i][j]);
            }
        }
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return matrix.length/rows;
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
    protected VectorN getEmptyCol() {
        return new VectorN(new float[getRows()]);
    }

    @Override
    protected MatrixMxN getEmptyMatrix() {
        return new MatrixMxN(getRows(),getColumns());
    }
    
}
