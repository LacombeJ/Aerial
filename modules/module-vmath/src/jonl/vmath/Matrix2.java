package jonl.vmath;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class Matrix2 extends SquareMatrix<Matrix2,Vector2> {

    public float m00, m01;
    public float m10, m11;
    
    public Matrix2(
            float m00, float m01,
            float m10, float m11) {
        
        this.m00 = m00; this.m01 = m01;
        this.m10 = m10; this.m11 = m11;
        
    }
    
    public Matrix2(Vector2 v0, Vector2 v1, boolean rows) {
        if (rows) {
            m00 = v0.x; m01 = v0.y;
            m10 = v1.x; m11 = v1.y;
        } else {
            m00 = v0.x; m01 = v1.x;
            m10 = v0.y; m11 = v1.y;
        }
    }
    
    public Matrix2(
            float m00,  /*  0   */
            /*  0   */  float m11) {
        this.m00 = m00;
        this.m11 = m11;
    }
    
    public Matrix2(Matrix2 mat) {
        this.m00 = mat.m00; this.m01 = mat.m01;
        this.m10 = mat.m10; this.m11 = mat.m11;
    }
    
    /**
     * Creates matrix from columns
     * @see #Matrix2f(Vector2, Vector2, boolean)
     */
    public Matrix2(Vector2 v0, Vector2 v1) {
        this(v0,v1,false);
    }
    
    public Matrix2() { }

    @Override
    public int getRows() {
        return 2;
    }
    
    @Override
    public int size() {
        return 4;
    }

    @Override
    public float get(int i, int j) {
        switch (i) {
        case 0: switch (j) {
            case 0: return m00;
            case 1: return m01;
            default: break; } break;
        case 1: switch (j) {
            case 0: return m10;
            case 1: return m11;
            default: break; } break;
        default: break;
        }
        throw new IndexOutOfBoundsException(getExceptionString(i,j));
    }

    @Override
    public void set(int i, int j, float v) {
        switch (i) {
        case 0: switch (j) {
            case 0: m00 = v; break;
            case 1: m01 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        case 1: switch (j) {
            case 0: m10 = v; break;
            case 1: m11 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
        }
    }
    
    @Override
    protected Vector2 getEmptyRow() {
        return new Vector2();
    }
    
    @Override
    protected Matrix2 getEmptyMatrix() {
        return new Matrix2();
    }
    
    @Override
    public <S extends SquareMatrix<S, ?>> S subMatrix() {
        throw new UnsupportedOperationException("Matrix2 cannot not have a sub matrix");
    }
    
    @Override
    public float determinant() {
        return m00 * m11 - m01 * m10;
    }
    
    @Override
    public Matrix2 inverse() {
        float det = determinant();
        if (Mathf.eq(det,0,Mathf.EPSILON)) return null;
        float temp11 = m11;
        m11 = m00;
        m00 = temp11;
        m01 *= -1;
        m10 *= -1;
        return multiply(1/det);
    }
    
    /** @return rotation matrix about "z" axis */
    public static Matrix2 rotation(float theta) {
        float cos = Mathf.cos(theta);
        float sin = Mathf.sin(theta);
        return new Matrix2(
    		cos, -sin,
    		sin,  cos
		);
    }
    
    /** @return the identity matrix */
    public static Matrix2 identity() {
        return new Matrix2(1,1);
    }
    
    /** @return a matrix filled with ones */
    public static Matrix2 ones() {
        return new Matrix2(1,1,1,1);
    }
    
    public static float[] pack(List<Matrix2> matrices) {
        return Util.mpack(matrices);
    }
    
    public static float[] pack(Matrix2... matrices) {
        return Util.mpack(matrices);
    }
    
    public static ArrayList<Matrix2> unpack(float[] values) {
        return Util.munpack(values,new Matrix2());
    }
    
    public static Matrix2[] unpackArray(float[] values) {
        return Util.munpackArray(values,new Matrix2(),new Matrix2[values.length/4]);
    }
    
}
