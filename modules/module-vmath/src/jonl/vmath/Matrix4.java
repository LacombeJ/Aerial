package jonl.vmath;

import java.nio.FloatBuffer;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class Matrix4 extends SquareMatrix<Matrix4,Vector4> {
    
    private static final Matrix4 tmp1 = new Matrix4();
    
    /*
    public static final int M00 = 0,  M01 = 4,  M02 = 8,  M03 = 12;
    public static final int M10 = 1,  M11 = 5,  M12 = 9,  M13 = 13;
    public static final int M20 = 2,  M21 = 6,  M22 = 10, M23 = 14;
    public static final int M30 = 3,  M31 = 7,  M32 = 11, M33 = 15;
    */
    
    //*
    public static final int M00 = 0,  M01 = 1,  M02 = 2,  M03 = 3;
    public static final int M10 = 4,  M11 = 5,  M12 = 6,  M13 = 7;
    public static final int M20 = 8,  M21 = 9,  M22 = 10, M23 = 11;
    public static final int M30 = 12, M31 = 13, M32 = 14, M33 = 15;
    //*
    
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;
    
    public Matrix4(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
        
    }
    
    public Matrix4(Vector4 v0, Vector4 v1, Vector4 v2, Vector4 v3, boolean rows) {
        if (rows) {
            m00 = v0.x; m01 = v0.y; m02 = v0.z; m03 = v0.w;
            m10 = v1.x; m11 = v1.y; m12 = v1.z; m13 = v0.w;
            m20 = v2.x; m12 = v2.y; m22 = v2.z; m23 = v0.w;
            m30 = v3.x; m13 = v3.y; m32 = v3.z; m33 = v0.w;
        } else {
            m00 = v0.x; m01 = v1.x; m02 = v2.x; m03 = v3.x;
            m10 = v0.y; m11 = v1.y; m12 = v2.y; m13 = v3.y;
            m20 = v0.z; m12 = v1.z; m22 = v2.z; m23 = v3.z;
            m30 = v0.w; m13 = v1.w; m32 = v2.w; m33 = v3.w;
        }
    }
    
    /**
     * Creates matrix from columns
     * @see #Matrix4f(Vector4, Vector4, Vector4, Vector4, boolean)
     */
    public Matrix4(Vector4 v0, Vector4 v1, Vector4 v2, Vector4 v3) {
        this(v0,v1,v2,v3,false);
    }
    
    public Matrix4(
            float m00, /*  0   */ /*  0   */ /*  0   */
            /*  0   */ float m11, /*  0   */ /*  0   */
            /*  0   */ /*  0   */ float m22, /*  0   */
            /*  0   */ /*  0   */ /*  0   */ float m33) {
        this.m00 = m00;
        this.m11 = m11;
        this.m22 = m22;
        this.m33 = m33;
    }
    
    public Matrix4(Matrix4 mat) {
        this.m00 = mat.m00; this.m01 = mat.m01; this.m02 = mat.m02; this.m03 = mat.m03;
        this.m10 = mat.m10; this.m11 = mat.m11; this.m12 = mat.m12; this.m13 = mat.m13;
        this.m20 = mat.m20; this.m21 = mat.m21; this.m22 = mat.m22; this.m23 = mat.m23;
        this.m30 = mat.m30; this.m31 = mat.m31; this.m32 = mat.m32; this.m33 = mat.m33;
    }
    
    public Matrix4(FloatBuffer fb) {
        for (int i=0; i<getRows(); i++) {
            for (int j=0; j<getColumns(); j++) {
                set(i,j,fb.get(j*getRows() + i));
            }
        }
    }
    
    public Matrix4() { }

    @Override
    public int getRows() {
        return 4;
    }
    
    @Override
    public int size() {
        return 16;
    }

    @Override
    public float get(int i, int j) {
        switch (i) {
        case 0: switch (j) {
            case 0: return m00;
            case 1: return m01;
            case 2: return m02;
            case 3: return m03; 
            default: break; } break;
        case 1: switch (j) {
            case 0: return m10;
            case 1: return m11;
            case 2: return m12;
            case 3: return m13;
            default: break; } break;
        case 2: switch (j) {
            case 0: return m20;
            case 1: return m21;
            case 2: return m22;
            case 3: return m23;
            default: break; } break;
        case 3: switch (j) {
            case 0: return m30;
            case 1: return m31;
            case 2: return m32;
            case 3: return m33;
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
            case 3: m03 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        case 1: switch (j) {
            case 0: m10 = v; break;
            case 1: m11 = v; break;
            case 2: m12 = v; break;
            case 3: m13 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        case 2: switch (j) {
            case 0: m20 = v; break;
            case 1: m21 = v; break;
            case 2: m22 = v; break;
            case 3: m23 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        case 3: switch (j) {
            case 0: m30 = v; break;
            case 1: m31 = v; break;
            case 2: m32 = v; break;
            case 3: m33 = v; break;
            default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
            } break;
        default: throw new IndexOutOfBoundsException(getExceptionString(i,j));
        }
    }

    @Override
    protected Vector4 getEmptyRow() {
        return new Vector4();
    }
    
    @Override
    protected Matrix4 getEmptyMatrix() {
        return new Matrix4();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <S extends SquareMatrix<S, ?>> S subMatrix() {
        return (S) new Matrix3();
    }
    
    /** 
     * As a transformation, this translates the matrix
     * @return this matrix
     */
    public Matrix4 translate(float x, float y, float z) {
        m03 += m00*x + m01*y + m02*z;
        m13 += m10*x + m11*y + m12*z;
        m23 += m20*x + m21*y + m22*z;
        m33 += m30*x + m31*y + m32*z;
        return this;
    }
    
    /** @see #translate(float, float, float) */
    public Matrix4 translate(Vector3 translation) {
        return translate(translation.x,translation.y,translation.z);
    }
    
    /**
     * As a transformation, this rotates this matrix 3 times
     * around the different axis
     * @see #rotateX(float)
     * @see #rotateY(float)
     * @see #rotateZ(float)
     */
    public Matrix4 rotate(float xrad, float yrad, float zrad) {
        return rotateX(xrad).rotateY(yrad).rotateZ(zrad);
    }
    
    /** @see #rotate(float, float, float) */
    public Matrix4 rotate(Vector3 rotation) {
        return rotate(rotation.x,rotation.y,rotation.z);
    }
    
    /**
     * This rotates the matrix around the x axis by
     * the given angle in radians
     * @return this matrix
     */
    public Matrix4 rotateX(float rad) {
        float cos = Mathf.cos(rad);
        float sin = Mathf.sin(rad);
        float mm01 = m01*cos + m02*sin;
        float mm02 = m02*cos - m01*sin;
        float mm11 = m11*cos + m12*sin;
        float mm12 = m12*cos - m11*sin;
        float mm21 = m21*cos + m22*sin;
        float mm22 = m22*cos - m21*sin;
        float mm31 = m31*cos + m32*sin;
        m32 = m32*cos - m31*sin;
        m01 = mm01; m02 = mm02;
        m11 = mm11; m12 = mm12;
        m21 = mm21; m22 = mm22;
        m31 = mm31;
        return this;
    }
    
    /**
     * This rotates the matrix around the y axis by
     * the given angle in radians
     * @return this matrix
     */
    public Matrix4 rotateY(float rad) {
        float cos = Mathf.cos(rad);
        float sin = Mathf.sin(rad);
        float mm00 = m00*cos - m02*sin;
        float mm02 = m02*cos + m00*sin;
        float mm10 = m10*cos - m12*sin;
        float mm12 = m12*cos + m10*sin;
        float mm20 = m20*cos - m22*sin;
        float mm22 = m22*cos + m20*sin;
        float mm30 = m30*cos - m32*sin;
        m32 = m32*cos + m30*sin;
        m00 = mm00; m02 = mm02;
        m10 = mm10; m12 = mm12;
        m20 = mm20; m22 = mm22;
        m30 = mm30;
        return this;
    }
    
    /**
     * This rotates the matrix around the z axis by
     * the given angle in radians
     * @return this matrix
     */
    public Matrix4 rotateZ(float rad) {
        float cos = Mathf.cos(rad);
        float sin = Mathf.sin(rad);
        float mm00 = m00*cos + m01*sin;
        float mm01 = m01*cos - m00*sin;
        float mm10 = m10*cos + m11*sin;
        float mm11 = m11*cos - m10*sin;
        float mm20 = m20*cos + m21*sin;
        float mm21 = m21*cos - m20*sin;
        float mm30 = m30*cos + m31*sin;
        m31 = m31*cos - m30*sin;
        m00 = mm00; m01 = mm01;
        m10 = mm10; m11 = mm11;
        m20 = mm20; m21 = mm21;
        m30 = mm30;
        return this;
    }
    
    public Matrix4 rotate(Quaternion quat) {
        return multiply(quat.toMatrix(tmp1));
    }
    
    /** 
     * As a transformation, this method scales the matrix
     * by the given values
     * @return this matrix
     */
    public Matrix4 scale(float x, float y, float z) {
        m00*=x; m01*=y; m02*=z;
        m10*=x; m11*=y; m12*=z;
        m20*=x; m21*=y; m22*=z;
        m30*=x; m31*=y; m32*=z;
        return this;
    }
    
    public Matrix4 scale(Vector3 scale) {
        return scale(scale.x,scale.y,scale.z);
    }
    
    public void transform(Vector3[] vertices) {
        for (Vector3 v : vertices) {
            v.transform(this);
        }
    }
    
    
    
    // =============================================================================
    // Extracting components
    // https://forum.unity.com/threads/how-to-assign-matrix4x4-to-transform.121966/
    
    
    public Vector3 getTranslation() {
    	Vector3 translation = new Vector3();
    	translation.x = m03;
    	translation.y = m13;
    	translation.z = m23;
    	return translation;
    }
    
    public Quaternion getRotation() {
    	Quaternion rotation = new Quaternion();
    	rotation.setFromMatrix(true, this);
    	return rotation;
    }
    
    public Vector3 getScale() {
    	Vector3 scale = new Vector3();
    	scale.x = new Vector4(m00,m10,m20,m30).mag();
    	scale.y = new Vector4(m01,m11,m21,m31).mag();
    	scale.z = new Vector4(m02,m12,m22,m32).mag();
    	return scale;
    }
    
    // =============================================================================
    
    
    
    /** @return the Matrix representation of the given axis rotation */
    public static Matrix4 axisRotation(Vector3 axis, float rad) {
        float c = Mathf.cos(rad);
        float s = Mathf.sin(rad);
        float t = 1-c;
        float x = axis.x;
        float y = axis.y;
        float z = axis.z;
        return new Matrix4(
            t*x*x + x,   t*x*y - z*s,   t*x*z + y*s,    0,
            t*x*y + z*s, t*y*y + c,     t*y*z - x*s,    0,
            t*x*z - y*s, t*y*z + x*s,   t*z*z + c,      0,
            0,           0,             0,              1
        );
    }
    
    /**
     * @param l left
     * @param r right
     * @param b bottom
     * @param t top
     * @param n near
     * @param f far
     * @return the orthographic matrix represented by
     * the given parameters
     */
    public static Matrix4 orthographic(float l, float r, float b, float t, float n, float f) {
        float rml = r-l;
        float tmb = t-b;
        float fmn = f-n;
        Matrix4 proj = new Matrix4(
            2/(rml),0,      0,          -(r+l)/(rml),
            0,      2/(tmb),0,          -(t+b)/(tmb),
            0,      0,      -2/(fmn),   -(f+n)/(fmn),
            0,      0,      0,          1
        );
        return proj;
    }
    
    /**
     * 
     * @param height
     * @param aspect width/height
     * @param near
     * @param far
     * @return
     */
    public static Matrix4 orthographic(float height, float aspect, float near, float far) {
        float width = aspect*height;
        float r = (width/2);
        float l = -r;
        float t = (height/2);
        float b = -t;
        return orthographic(l,r,b,t,near,far);
    }
    
    /**
     * This method returns a perspective Matrix with a width
     * of <code>(2*near*tan(fov/2))</code> and a height of <code>(width/aspect)</code>
     * @return the perspective view matrix represented by the given
     * parameters
     */
    public static Matrix4 perspective(float fov, float aspect, float near, float far) {
        float nmf = near-far;
        float rad = Mathf.rad(fov);
        float width = 2*near*Mathf.tan(rad * 0.5f);
        float ttndw = 2*near/width;
        Matrix4 proj = new Matrix4(
            ttndw,  0,              0,                  0,
            0,      ttndw*aspect,   0,                  0,
            0,      0,              (far+near)/(nmf),   2*far*near/(nmf),
            0,      0,              -1,                 0
        );
        return proj;
    }
    
    public static Vector4 toScreenSpace(Matrix4 MVP, Vector3 vertex) {
        Vector4 v = MVP.multiply(new Vector4(vertex,1));
        v = ( v.divide(v.w).add(new Vector4(1,1,1,1)) ).scale(0.5f);
        return v;
    }
    
    public static Vector3 fromScreenSpace(Matrix4 MVP, Vector4 fragCoord) {
        Vector4 v = fragCoord.get();
        v.scale(2);
        v.sub(new Vector4(1,1,1,1));
        v.scale(v.w);
        v = MVP.get().inverse().multiply(v);
        v.divide(v.w);
        return v.xyz();
    }
    
    public static Matrix4 translation(float x, float y, float z) {
        return Matrix4.identity().translate(x, y, z);
    }
    
    public static Matrix4 translation(Vector3 translation) {
        return Matrix4.identity().translate(translation);
    }
    
    /** @return 3d rotation matrix about x axis */
    public static Matrix4 rotationX(float theta) {
        float cos = Mathf.cos(theta);
        float sin = Mathf.sin(theta);
        return new Matrix4(
    		  1,    0,    0,   0,
    		  0,  cos, -sin,   0,
    		  0,  sin,  cos,   0,
    		  0,    0,    0,   1
		);
    }
    
    /** @return 3d rotation matrix about y axis */
    public static Matrix4 rotationY(float theta) {
        float cos = Mathf.cos(theta);
        float sin = Mathf.sin(theta);
        return new Matrix4(
    		 cos,    0,  sin,   0,
    		   0,    1,    0,   0,
    		-sin,    0,  cos,   0,
    		   0,    0,    0,   1
		);
    }
    
    /** @return 3d rotation matrix about z axis */
    public static Matrix4 rotationZ(float theta) {
        float cos = Mathf.cos(theta);
        float sin = Mathf.sin(theta);
        return new Matrix4(
    		cos, -sin,   0,   0,
    		sin,  cos,   0,   0,
    		  0,    0,   1,   0,
    		  0,    0,   0,   1
		);
    }
    
    public static Matrix4 rotation(float xrad, float yrad, float zrad) {
        return Matrix4.identity().rotate(xrad, yrad, zrad);
    }
    
    public static Matrix4 rotation(Vector3 rotation) {
        return Matrix4.identity().rotate(rotation);
    }
    
    public static Matrix4 scaled(float x, float y, float z) {
        return Matrix4.identity().scale(x, y, z);
    }
    
    public static Matrix4 scaled(Vector3 scale) {
        return Matrix4.identity().scale(scale);
    }
    
    /** @return the identity matrix */
    public static Matrix4 identity() {
        return new Matrix4(1,1,1,1);
    }
    
    /** @return a matrix filled with ones */
    public static Matrix4 ones() {
        return new Matrix4(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
    }
    
}
