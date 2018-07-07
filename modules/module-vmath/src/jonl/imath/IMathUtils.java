package jonl.imath;

import java.math.BigInteger;

class IMathUtils {

    final static BigInteger ZERO = new BigInteger("0");
    final static BigInteger ONE = new BigInteger("1");
    final static BigInteger TWO = new BigInteger("2");
    final static BigInteger FIVE = new BigInteger("5");
    final static BigInteger TEN = new BigInteger("10");

    final static BigInteger MIN_INT = new BigInteger(Integer.MIN_VALUE + "");
    final static BigInteger MIN_INT_D2 = MIN_INT.divide(TWO);
    final static BigInteger MAX_INT = new BigInteger(Integer.MAX_VALUE + "");
    final static BigInteger MAX_INT_D2 = MAX_INT.divide(TWO);

    static boolean isFractionNegative(BigInteger a, BigInteger b) {
        boolean p = a.signum() == -1;
        boolean q = b.signum() == -1;
        return p ^ q;
    }

    static BigInteger add(BigInteger... integers) {
        BigInteger build = (integers.length > 0) ? integers[0] : null;
        for (int i = 1; i < integers.length; i++) {
            build = build.add(integers[i]);
        }
        return build;
    }

    static BigInteger subtract(BigInteger... integers) {
        BigInteger build = (integers.length > 0) ? integers[0] : null;
        for (int i = 1; i < integers.length; i++) {
            build = build.subtract(integers[i]);
        }
        return build;
    }

    static BigInteger multiply(BigInteger... integers) {
        BigInteger build = (integers.length > 0) ? integers[0] : null;
        for (int i = 1; i < integers.length; i++) {
            build = build.multiply(integers[i]);
        }
        return build;
    }

    static BigInteger divide(BigInteger... integers) {
        BigInteger build = (integers.length > 0) ? integers[0] : null;
        for (int i = 1; i < integers.length; i++) {
            build = build.divide(integers[i]);
        }
        return build;
    }

    static int minCol(double[][] mat) {
        if (mat.length == 0 || mat[0] == null)
            return 0;
        int min = mat[0].length;
        if (min == 0)
            return 0;
        for (int i = 1; i < mat.length; i++) {
            int length = mat[i].length;
            if (min > length)
                min = length;
            if (min == 0)
                return 0;
        }
        return min;
    }

    static int minCol(Object[][] mat) {
        if (mat.length == 0 || mat[0] == null)
            return 0;
        int min = mat[0].length;
        if (min == 0)
            return 0;
        for (int i = 1; i < mat.length; i++) {
            int length = mat[i].length;
            if (min > length)
                min = length;
            if (min == 0)
                return 0;
        }
        return min;
    }

    static double[][] doubleArray2D(int rows, int columns) {
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            matrix[i] = new double[columns];
        }
        return matrix;
    }

    static IFraction[][] fractionArray2D(double[][] matrix) {
        IFraction[][] mat = new IFraction[matrix.length][matrix[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                mat[i][j] = new IFraction(matrix[i][j]);
            }
        }
        return mat;
    }

    static IFraction[] toFractionArray(double[] array) {
        IFraction[] f = new IFraction[array.length];
        for (int i = 0; i < array.length; i++) {
            f[i] = new IFraction(array[i]);
        }
        return f;
    }

}
