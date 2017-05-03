package jonl.ge;

import jonl.vmath.Matrix4;
import jonl.vmath.Quaternion;
import jonl.vmath.Vector3;

public class Transform {

    public Vector3      scale; //TODO hide variables, getters setters?
    public Quaternion   rotation;
    public Vector3      translation;
    
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
    
    public Transform get() {
        return new Transform(this);
    }

    public Matrix4 computeMatrix() {
        return Matrix4.identity()
                .translate(translation)
                .rotate(rotation)
                .scale(scale);
    }
    
    /**
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
    
    @Override
    public String toString() {
        return "[Scale"+scale+" Rot"+rotation+" Trans"+translation+"]";
    }
    
}
