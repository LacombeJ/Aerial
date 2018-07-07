package ax.engine.utils;

import ax.math.vector.Mathf;
import ax.math.vector.Matrix2;
import ax.math.vector.Matrix3;
import ax.math.vector.Matrix4;
import ax.math.vector.SquareMatrix;
import ax.math.vector.Vector;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class PseudoShader {

    public vec4 gl_FragColor = vec4(0,0,0,1);
    public vec4 gl_FragCoord = vec4(0,0,0,1);
    
    public void main() {
        
    }
    
    
    
    
    
    
    private abstract class vecX<V extends Vector<V>>  {
        abstract V t();
        abstract vecX<V> t(V v);
    }
    public class vec2 extends vecX<Vector2> {
        public float x, y;
        @Override Vector2 t() { return new Vector2(x,y); }
        @Override vec2 t(Vector2 v) { return vec2(v.x,v.y); }
        
        public void set(vec2 v) {
            x = v.x;
            y = v.y;
        }
        public vec2 xy() { return vec2(x,y); }
    }
    public class vec3 extends vecX<Vector3> {
        public float x, y, z;
        @Override Vector3 t() { return new Vector3(x,y,z); }
        @Override vec3 t(Vector3 v) { return vec3(v.x,v.y,v.z); }
        
        public void set(vec3 v) {
            x = v.x;
            y = v.y;
            z = v.z;
        }
        
        public vec2 xy() { return vec2(x,y); }
        public vec2 yz() { return vec2(y,z); }
        public vec2 xz() { return vec2(x,z); }
        
        public vec3 xyz() { return vec3(x,y,z); }
    }
    public class vec4 extends vecX<Vector4> {
        float x, y, z, w;
        @Override Vector4 t() { return new Vector4(x,y,z,w); }
        @Override vec4 t(Vector4 v) { return vec4(v.x,v.y,v.z,v.w); }
        
        public void set(vec4 v) {
            x = v.x;
            y = v.y;
            z = v.z;
            w = v.w;
        }
        
        public vec2 xy() { return vec2(x,y); }
        public vec2 yz() { return vec2(y,z); }
        public vec2 xz() { return vec2(x,z); }
        
        public vec3 xyz() { return vec3(x,y,z); }
        
        public vec4 xyzw() { return vec4(x,y,z,w); }
    }
    
    
    private abstract class matX<M extends SquareMatrix<?, ?>>  {
        //abstract M t();
        //abstract matX<M> t(M m);
    }
    public class mat2 extends matX<Matrix2> {
        public vec2 x = new vec2();
        public vec2 y = new vec2();
    }
    public class mat3 extends matX<Matrix3> {
        public vec3 x = new vec3();
        public vec3 y = new vec3();
        public vec3 z = new vec3();
    }
    public class mat4 extends matX<Matrix4> {
        public vec4 x = new vec4();
        public vec4 y = new vec4();
        public vec4 z = new vec4();
        public vec4 w = new vec4();
    }
    
    public vec2 vec2(float x, float y) {
        vec2 v = new vec2();
        v.x = x;
        v.y = y;
        return v;
    }
    public vec2 vec2(float v) {
        return vec2(v,v);
    }
    
    public vec3 vec3(float x, float y, float z) {
        vec3 v = new vec3();
        v.x = x;
        v.y = y;
        v.z = z;
        return v;
    }
    public vec3 vec3(float v) {
        return vec3(v,v,v);
    }
    public vec3 vec3(vec2 u, float z) {
        return vec3(u.x,u.y,z);
    }
    
    public vec4 vec4(float x, float y, float z, float w) {
        vec4 v = new vec4();
        v.x = x;
        v.y = y;
        v.z = z;
        v.w = w;
        return v;
    }
    public vec4 vec4(float v) {
        return vec4(v,v,v,v);
    }
    public vec4 vec4(vec2 u, float z, float w) {
        return vec4(u.x,u.y,z,w);
    }
    public vec4 vec4(vec3 u, float w) {
        return vec4(u.x,u.y,u.z,w);
    }
    
    
    public mat2 mat2(
            float m00, float m01,
            float m10, float m11) {
        mat2 m = new mat2();
        m.x.x = m00; m.x.y = m01;
        m.y.x = m10; m.y.y = m11;
        return m;
    }
    public mat2 mat2(vec2 x, vec2 y) {
        mat2 v = new mat2();
        v.x = x;
        v.y = y;
        return v;
    }
    public mat3 mat3(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        mat3 m = new mat3();
        m.x.x = m00; m.x.y = m01; m.x.z = m02;
        m.y.x = m10; m.y.y = m11; m.y.z = m12;
        m.z.x = m10; m.z.y = m11; m.z.z = m12;
        return m;
    }
    public mat3 mat3(vec3 x, vec3 y, vec3 z) {
        mat3 v = new mat3();
        v.x = x;
        v.y = y;
        v.z = z;
        return v;
    }
    public mat4 mat4(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        mat4 m = new mat4();
        m.x.x = m00; m.x.y = m01; m.x.z = m02; m.x.w = m03;
        m.y.x = m10; m.y.y = m11; m.y.z = m12; m.y.w = m13;
        m.z.x = m10; m.z.y = m11; m.z.z = m12; m.z.w = m23;
        m.w.x = m10; m.w.y = m11; m.w.z = m12; m.w.w = m23;
        return m;
    }
    public mat4 mat4(vec4 x, vec4 y, vec4 z, vec4 w) {
        mat4 v = new mat4();
        v.x = x;
        v.y = y;
        v.z = z;
        v.w = w;
        return v;
    }
    
    
    
    private <T extends Vector<T>> T t(vecX<T> v) {
        return v.t();
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Vector<T>> vecX<T> t(T v) {
        if (v instanceof Vector2) {
            return (vecX<T>) new vec2().t((Vector2) v);
        } else if (v instanceof Vector3) {
            return (vecX<T>) new vec3().t((Vector3) v);
        } else if (v instanceof Vector4) {
            return (vecX<T>) new vec4().t((Vector4) v);
        }
        return null;
    }
    
    
    public float min(float... v) {
        return Mathf.min(v);
    }
    public float max(float... v) {
        return Mathf.max(v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V min(V v, float a) {
        return (V) t(Mathf.min(t(v),a));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T> > V max(V v, float a) {
        return (V) t(Mathf.max(t(v),a));
    }
    
    
    public <T extends Vector<T>, V extends vecX<T>> float length(V v) {
        return Mathf.mag(t(v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V abs(V v) {
        return (V) t(Mathf.abs(t(v)));
    }
    
    public float floor(float a) {
        return Mathf.floor(a);
    }
    public float ceil(float a) {
        return Mathf.ceil(a);
    }
    public float round(float a) {
        return Mathf.round(a);
    }
    public float fract(float a) {
        return Mathf.fract(a);
    }
    public float mod(float x, float y) {
        return Mathf.mod(x, y);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V floor(V v) {
        return (V) t(Mathf.floor(t(v)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V ceil(V v) {
        return (V) t(Mathf.ceil(t(v)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V round(V v) {
        return (V) t(Mathf.round(t(v)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V fract(V v) {
        return (V) t(Mathf.fract(t(v)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V mod(V v, float a) {
        return (V) t(Mathf.mod(t(v),a));
    }
    
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V add(V a, V b) {
        return (V) t(Mathf.add(t(a), t(b)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V add(V a, float b) {
        return (V) t(Mathf.add(t(a), b));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V sub(V a, V b) {
        return (V) t(Mathf.sub(t(a), t(b)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V sub(V a, float  b) {
        return (V) t(Mathf.sub(t(a), b));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V mul(V a, V b) {
        return (V) t(Mathf.mul(t(a), t(b)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V mul(V a, float b) {
        return (V) t(Mathf.mul(t(a), b));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V div(V a, V b) {
        return (V) t(Mathf.div(t(a), t(b)));
    }
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T>> V div(V a, float b) {
        return (V) t(Mathf.div(t(a), b));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Vector<T>, V extends vecX<T> > V normalize(V v) {
        return (V) t(Mathf.norm(t(v)));
    }
    
    
    public vec3 cross(vec3 u, vec3 v) {
        return (vec3) t(Mathf.cross(t(u), t(v)));
    }
    
    public float sin(float x) {
        return Mathf.sin(x);
    }
    public float cos(float x) {
        return Mathf.cos(x);
    }
    
    
}
