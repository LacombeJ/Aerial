package jonl.imath;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathan
 *
 */
public class IMatrix {

    private IFraction[] matrix;

    private final int rows;
    private final int columns;

    public IMatrix(IFraction[][] mat) {
        columns = IMathUtils.minCol(mat);
        rows = (columns > 0) ? mat.length : 0;
        matrix = new IFraction[rows * columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                set(i, j, mat[i][j]);
            }
        }
    }

    public IMatrix(double[][] mat) {
        this(IMathUtils.fractionArray2D(mat));
    }

    public IMatrix(int rows, int columns) {
        this(IMathUtils.doubleArray2D(rows, columns));
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public IFraction get(int row, int col) {
        return matrix[col * rows + row];
    }

    public void set(int row, int col, IFraction f) {
        matrix[col * rows + row] = f;
    }

    public IFraction[] getRow(int row) {
        IFraction[] f = new IFraction[columns];
        for (int i = 0; i < columns; i++) {
            f[i] = get(row, i);
        }
        return f;
    }

    public IFraction[] getColumn(int column) {
        IFraction[] f = new IFraction[rows];
        for (int i = 0; i < rows; i++) {
            f[i] = get(i, column);
        }
        return f;
    }

    /*
     * Row Operations
     */

    public IOperation interchange(int row0, int row1) {
        IOperation op = new IOperation(IOperation.INTERCHANGE, row0, row1, IFraction.ZERO, IFraction.ZERO);
        IFraction[] hold = new IFraction[columns];
        for (int i = 0; i < columns; i++) {
            hold[i] = get(row0, i);
            set(row0, i, get(row1, i));
        }
        for (int i = 0; i < columns; i++) {
            set(row1, i, hold[i]);
        }
        return op;
    }

    public IOperation scale(int row, IFraction scalar) {
        IOperation op = new IOperation(IOperation.SCALE, 0, row, scalar, IFraction.ZERO);
        for (int i = 0; i < columns; i++) {
            set(row, i, get(row, i).multiply(scalar));
        }
        return op;
    }

    public IOperation scale(int row, double scalar) {
        return scale(row, new IFraction(scalar));
    }

    public IOperation add(int row0, int row1, IFraction scalar0, IFraction scalar1) {
        IOperation op = new IOperation(IOperation.ADD, row0, row1, scalar0, scalar1);
        for (int i = 0; i < columns; i++) {
            IFraction f0 = get(row0, i).multiply(scalar0);
            IFraction f1 = get(row1, i).multiply(scalar1);
            set(row0, i, f0.add(f1));
        }
        return op;
    }

    public IOperation add(int row0, int row1, double scalar0, double scalar1) {
        return add(row0, row1, new IFraction(scalar0), new IFraction(scalar1));
    }

    public List<IOperation> toEchelon() {
        List<IOperation> list = new ArrayList<>();
        echelonRecursion(0, 0, list);
        return list;
    }

    public IOperation[] echelonOp() {
        return getCopy().toEchelon().toArray(new IOperation[0]);
    }

    private void echelonRecursion(int pRow, int pCol, List<IOperation> list) {
        if (pRow >= rows || pCol >= columns)
            return;
        if (get(pRow, pCol).equals(IFraction.ZERO)) {
            boolean found = false;
            for (int i = pRow + 1; i < rows; i++) {
                if (!get(i, pCol).equals(IFraction.ZERO)) {
                    list.add(interchange(pRow, i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                echelonRecursion(pRow, pCol + 1, list);
                return;
            }
        }
        for (int i = pRow + 1; i < rows; i++) {
            if (!get(i, pCol).equals(IFraction.ZERO)) {
                list.add(reduceToZero(i, pCol, pRow));
            }
        }
        echelonRecursion(pRow + 1, pCol + 1, list);
    }

    public List<IOperation> toReduced() {
        List<IOperation> list = new ArrayList<>();
        echelonRecursion(0, 0, list);
        reduce(list);
        return list;
    }

    public IOperation[] reducedOp() {
        return getCopy().toReduced().toArray(new IOperation[0]);
    }

    private void reduce(List<IOperation> list) {
        for (int pRow = rows - 1; pRow >= 0; pRow--) {
            // its fine to loop through all columns without keeping track of last
            // pivot column when searching left to right
            // pivot is always in left most position
            for (int pCol = 0; pCol < columns; pCol++) {
                if (!get(pRow, pCol).equals(IFraction.ZERO)) {
                    list.add(reduceToOne(pRow, pCol));
                    for (int i = pRow - 1; i >= 0; i--) {
                        list.add(reduceToZero(i, pCol, pRow));
                    }
                    break;
                }
            }
        }
    }

    public IOperation reduceToZero(int row, int col, int rowTarget) {
        IFraction a = get(row, col);
        IFraction b = a.divide(get(rowTarget, col));
        return add(row, rowTarget, IFraction.ONE, b.negate());
    }

    public IOperation reduceToOne(int row, int col) {
        return scale(row, IFraction.ONE.divide(get(row, col)));
    }

    public void applyOperations(IOperation... operations) {
        for (IOperation o : operations) {
            int op = o.getOperation();
            switch (op) {
            case IOperation.INTERCHANGE:
                interchange(o.getRow0(), o.getRow1());
                break;
            case IOperation.SCALE:
                scale(o.getRow0(), o.getScalar0());
                break;
            case IOperation.ADD:
                add(o.getRow0(), o.getRow1(), o.getScalar0(), o.getScalar1());
                break;
            }
        }
    }

    public void applyOperations(List<IOperation> operations) {
        applyOperations(operations.toArray(new IOperation[0]));
    }

    /*
     * End of Row Operations
     */

    /*
     * Matrix Operations
     */

    public void negate() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                set(i, j, get(i, j).negate());
            }
        }
    }

    public void add(IMatrix... mat) {
        for (IMatrix m : mat) {
            if (m.rows == rows && m.columns == columns) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        set(i, j, get(i, j).add(m.get(i, j)));
                    }
                }
            }
        }
    }

    public void subtract(IMatrix... mat) {
        for (IMatrix m : mat) {
            if (m.rows == rows && m.columns == columns) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        set(i, j, get(i, j).subtract(m.get(i, j)));
                    }
                }
            }
        }
    }

    /*
     * End of Matrix Operations
     */

    public IMatrix getTranspose() {
        IFraction[][] mat = new IFraction[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mat[j][i] = get(i, j);
            }
        }
        return new IMatrix(mat);
    }

    public IMatrix getCopy() {
        IFraction[][] mat = new IFraction[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mat[i][j] = get(i, j);
            }
        }
        return new IMatrix(mat);
    }

    public void print() {
        String build = "Matrix[" + rows + "][" + columns + "]\n";
        for (int i = 0; i < rows; i++) {
            build += "[ ";
            for (int j = 0; j < columns; j++) {
                build += get(i, j) + " ";
            }
            build += "]\n";
        }
        if (rows == 0)
            build += "[ ]";
        System.out.print(build);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IMatrix) {
            IMatrix m = (IMatrix) obj;
            if (rows != m.rows || columns != m.columns)
                return false;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (!get(i, j).equals(m.get(i, j)))
                        return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        String build = "[ ";
        int i = 0;
        for (i = 0; i < rows - 1; i++) {
            for (int j = 0; j < columns; j++) {
                build += get(i, j) + " ";
            }
            build += "| ";
        }
        if (rows > 0 && columns > 0) {
            for (int j = 0; j < columns; j++) {
                build += get(i, j) + " ";
            }
        }
        build += "]";
        return build;
    }

    public static IMatrix identity(int i) {
        IMatrix m = new IMatrix(i, i);
        for (int j = 0; j < i; j++) {
            m.set(j, j, IFraction.ONE);
        }
        return m;
    }

    public static IMatrix transpose(IMatrix mat) {
        return mat.getTranspose();
    }

    public static IMatrix negate(IMatrix mat) {
        IMatrix m = mat.getCopy();
        m.negate();
        return m;
    }

}
