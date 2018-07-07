package jonl.ge.mod.misc;

import jonl.ge.core.Camera;
import jonl.ge.core.Property;
import jonl.ge.core.Transform;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class ScreenSpaceScale extends Property {

    Camera camera;
    float scale;
    float radius;
    
    public ScreenSpaceScale(Camera camera, float scale, float radius) {
        this.camera = camera;
        this.scale = scale;
        this.radius = radius;
    }
    
    public ScreenSpaceScale(Camera camera, float scale) {
        this(camera,scale,0.5f);
    }
    
    public ScreenSpaceScale(Camera camera) {
        this(camera,0.1f);
    }
    
    @Override
    public void create() {
        
    }

    public void setScale(float scale) {
        this.scale = Mathf.clamp(scale,0,1);
    }
    
    public void setRadius(float radius) {
        this.radius = Mathf.clamp(radius,0,Float.MAX_VALUE);
    }
    
    @Override
    public void update() {
        
        Transform cameraTransform = camera.computeWorldTransform();
        Vector3 up = cameraTransform.up().norm();
        
        Matrix4 VP = camera.computeViewProjectionMatrix();
        Transform srcWorldTransform = computeWorldTransform();
        Transform dstWorldTransform = srcWorldTransform.get();
        dstWorldTransform.translation.add(up.scale(radius));
        
        Vector4 srcScreen = Matrix4.toScreenSpace(VP,srcWorldTransform.translation);
        Vector4 dstScreen = Matrix4.toScreenSpace(VP,dstWorldTransform.translation);
        
        float screenSize = srcScreen.dist(dstScreen);
        
        float scalar = window().aspect() * scale / screenSize;
        
        //TODO handle scale with hierarchy
        transform().scale.set(new Vector3(scalar));
        
    }

    
    
}
