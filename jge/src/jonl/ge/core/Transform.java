package jonl.ge.core;

import jonl.vmath.Matrix4;
import jonl.vmath.Quaternion;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class Transform {

    public Vector3      scale; //TODO hide variables, getters setters?
    public Quaternion   rotation;
    public Vector3      translation;
    
    public static Transform identity() {
        return new Transform();
    }
    
    public Transform() {
        scale = new Vector3(1,1,1);
        rotation = new Quaternion();
        translation = new Vector3(0,0,0);
    }
    
    public Transform(Vector3 scale, Quaternion rotation, Vector3 translation) {
        this.scale = new Vector3(scale);
        this.rotation = new Quaternion(rotation);
        this.translation = new Vector3(translation);
    }
    
    public Transform(Transform t) {
        this.scale = new Vector3(t.scale);
        this.rotation = new Quaternion(t.rotation);
        this.translation = new Vector3(t.translation);
    }
    
    public Transform(Matrix4 m) {
    	this.scale = m.getScale();
    	this.rotation = m.getRotation();
    	this.translation = m.getTranslation();
    }
    
    public Transform get() {
        return new Transform(this);
    }
    
    public void set(Transform t) {
    	this.scale.set(t.scale);
    	this.rotation.set(t.rotation);
    	this.translation.set(t.translation);
    }

    public Matrix4 computeMatrix() {
        return Matrix4.identity()
                .translate(translation)
                .rotate(rotation)
                .scale(scale);
    }
    
    /**
     * Multiplication<br>
     * multiply(inverse(this)) = Transform.identity
     * @return this transform after it has been multiplied by the given transform parent
     */
    public Transform multiply(Transform parent) {
        scale.multiply(parent.scale);
        rotation.mul(parent.rotation);
        translation.multiply(parent.scale);
        translation.set(parent.rotation.transform(translation));
        translation.add(parent.translation);
        return this;
    }
    
    /**
     * Reverse multiplication<br>
     * inverse(multiply(this)) = Transform.identity
     * @return this transform after it has been multiplied by the inverse transform of the parent (parent^(-1) * this)
     */
    public Transform inverse(Transform parent) {
    	
    	translation.sub(parent.translation);
    	translation.set(parent.rotation.get().conjugate().transform(translation));
    	translation.divide(parent.scale);
    	rotation.mul(parent.rotation.get().conjugate());
    	scale.divide(parent.scale);
    	
        return this;
    }
    
    public Vector4 transform(Vector4 v) {
    	return computeMatrix().multiply(v);
    }
    
    public Vector3 transform(Vector3 v) {
    	return computeMatrix().multiply(new Vector4(v,1)).xyz();
    }
    
    // TODO add: public void lookAt(Vector3 v)
    
    public Vector3 up() {
		return rotation.get().conjugate().transform(Vector3.up());
    }
    
    public Vector3 right() {
		return rotation.get().conjugate().transform(Vector3.right());
    }
    
    public Vector3 forward() {
		return rotation.get().conjugate().transform(Vector3.forward());
    }
    
    @Override
    public String toString() {
        return "[Scale"+scale+" Rot"+rotation+" Trans"+translation+"]";
    }
    
}
