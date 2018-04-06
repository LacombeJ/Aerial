package jonl.imath;

/**
 * Matrix Operation
 * 
 * @author Jonathan Lacombe
 *
 */
public class IOperation {

    public static final int INTERCHANGE = 0;
    public static final int SCALE = 1;
    public static final int ADD = 2;

    private final int row0;
    private final int row1;
    private final IFraction scalar0;
    private final IFraction scalar1;

    private final int operation;

    public IOperation(int operation, int row0, int row1, IFraction scalar0, IFraction scalar1) {
        this.operation = operation;
        this.row0 = row0;
        this.row1 = row1;
        this.scalar0 = scalar0;
        this.scalar1 = scalar1;
    }

    public int getRow0() {
        return row0;
    }

    public int getRow1() {
        return row1;
    }

    public IFraction getScalar0() {
        return scalar0;
    }

    public IFraction getScalar1() {
        return scalar1;
    }

    public int getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        switch (operation) {
        case INTERCHANGE:
            return "Interchange: " + row0 + " " + row1;
        case SCALE:
            return "Scale: " + row0 + " " + scalar0;
        case ADD:
            return "Add: " + row0 + " " + row1 + " " + scalar0 + " " + scalar1;
        }
        return "Undefined Operation";
    }

}
