package jonl.vmath;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class Matrix3 extends SquareMatrix<Matrix3,Vector3> {

    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;
    
    public Matrix3(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
        
    }
    
    public Matrix3(Vector3 v0, Vector3 v1, Vector3 v2, boolean rows) {
        if (rows) {
            m00 = v0.x; m01 = v0.y; m02 = v0.z;
            m10 = v1.x; m11 = v1.y; m12 = v1.z;
            m20 = v2.x; m12 = v2.y; m22 = v2.z;
        } else {
            m00 = v0.x; m01 = v1.x; m02 = v2.x;
            m10 = v0.y; m11 = v1.y; m12 = v2.y;
            m20 = v0.z; m12 = v1.z; m22 = v2.z;
        }
    }
    
    /**
     * Creates matrix from columns
     * @see #Matrix3f(Vector3, Vector3, Vector3, boolean)
     */
    public Matrix3(Vector3 v0, Vector3 v1, Vector3 v2) {
        this(v0,v1,v2,false);
    }
    
    public Matrix3(
            float m00, /*  0   */ /*  0   */
            /*  0   */ float m11, /*  0   */
            /*  0   */ /*  0   */ float m22) {
        this.m00 = m00;
        this.m11 = m11;
        this.m22 = m22;
    }
    
    public Matrix3(Matrix3 mat) {
        this.m00 = mat.m00; this.m01 = mat.m01; this.m02 = mat.m02;
        this.m10 = mat.m10; this.m11 = mat.m11; this.m12 = mat.m12;
        this.m20 = mat.m20; this.m21 = mat.m21; this.m22 = mat.m22;
    }
    
    public Matrix3() { }

    @Override
    public int getRows() {
        return 3;
    }
    
    @Override
    public int size() {
        return 9;
    }

    @Override
    public float get(int i, int j) {
        switch (i) {
        case 0: switch (j) {
            case 0: return m00;
            case 1: return m01;
            case 2: return m02;
            default: break; } break;
        case 1: switch (j) {
            case 0: return m10;
            case 1: return m11;
            case 2: return m12;
            default: break; } break;
        case 2: switch (j) {
            case 0: return m20;
            case 1: return m21;
            case 2: return m22;
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
            case 2: m02 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        case 1: switch (j) {
            case 0: m10 = v; break;
            case 1: m11 = v; break;
            case 2: m12 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        case 2: switch (j) {
            case 0: m20 = v; break;
            case 1: m21 = v; break;
            case 2: m22 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
        }
    }
    
    @Override
    protected Vector3 getEmptyRow() {
        return new Vector3();
    }

    @Override
    protected Matrix3 getEmptyMatrix() {
        return new Matrix3();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <S extends SquareMatrix<S, ?>> S subMatrix() {
        return (S) new Matrix2();
    }
    
    /** @return rotation matrix about x axis */
    public static Matrix3 rotationX(float theta) {
        float cos = Mathf.cos(theta);
        float sin = Mathf.sin(theta);
        return new Matrix3(
    		  1,    0,    0,
    		  0,  cos, -sin,
    		  0,  sin,  cos
		);
    }
    
    /** @return rotation matrix about y axis */
    public static Matrix3 rotationY(float theta) {
        float cos = Mathf.cos(theta);
        float sin = Mathf.sin(theta);
        return new Matrix3(
    		 cos,    0,  sin,
    		   0,    1,    0,
    		-sin,    0,  cos
		);
    }
    
    /** @return rotation matrix about z axis */
    public static Matrix3 rotationZ(float theta) {
        float cos = Mathf.cos(theta);
        float sin = Mathf.sin(theta);
        return new Matrix3(
    		cos, -sin,   0,
    		sin,  cos,   0,
    		  0,    0,   1
		);
    }
    
    /** @return the identity matrix */
    public static Matrix3 identity() {
        return new Matrix3(1,1,1);
    }
    
    /** @return a matrix filled with ones */
    public static Matrix3 ones() {
        return new Matrix3(1,1,1,1,1,1,1,1,1);
    }
    
    public static float[] pack(List<Matrix3> matrices) {
        return Util.mpack(matrices);
    }
    
    public static float[] pack(Matrix3... matrices) {
        return Util.mpack(matrices);
    }
    
    public static ArrayList<Matrix3> unpack(float[] values) {
        return Util.munpack(values,new Matrix3());
    }
    
    public static Matrix3[] unpackArray(float[] values) {
        return Util.munpackArray(values,new Matrix3(),new Matrix3[values.length/9]);
    }
    
}
