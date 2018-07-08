package ax.std.misc;

import ax.engine.core.Camera;
import ax.engine.core.Property;
import ax.math.vector.Vector2;

public class OrthoBox extends Property {

    Camera camera = null;
    
    boolean dragEnabled = false;
    
    Vector2 prevPosition = new Vector2();
    float prevHeight = 10;
    float prevAspect = 1;
    Vector2 prevScreenToWorld = new Vector2();
    
    @Override
    public void create() {
        camera = getComponent(Camera.class);
    }

    @Override
    public void update() {
        if (dragEnabled) {
            Vector2 newMousePos = window().toNormSpace(input().getXY());
            Vector2 newScreenToWorld = screenToWorld(newMousePos,prevPosition,prevHeight,prevAspect);
            
            Vector2 diff = prevScreenToWorld.get().sub(newScreenToWorld);
            Vector2 newPos = prevPosition.get().add(diff);
            
            transform().translation.x = newPos.x;
            transform().translation.y = newPos.y;
        }
    }
    
    public void drag() {
        dragEnabled = true;
        
        prevPosition = transform().translation.xy();
        prevHeight = camera.height();
        prevAspect = camera.aspect();
        Vector2 prevMousePos = window().toNormSpace(input().getXY());
        prevScreenToWorld = screenToWorld(prevMousePos, prevPosition, prevHeight, prevAspect);
    }
    
    public void release() {
        dragEnabled = false;
    }
    
    public void zoom(float zoom) {
        if (zoom==0) return;
        
        float height = camera.height();
        
        if (zoom>0) {
            height *= 1.1f * zoom;
        } else if (zoom<0) {
            height *= 0.99f * -(1/zoom);
        }
        
        if (camera.isOrthograpic()) {
            camera.setOrthographic(height,camera.aspect(),camera.near(),camera.far());
        }
    }
    
    public Vector2 toWorldSpace(Vector2 screen) {
        return screenToWorld(window().toNormSpace(screen), transform().translation.xy(), camera.height(), camera.aspect());
    }
    
    private Vector2 screenToWorld(Vector2 norm, Vector2 position, float height, float aspect) {
        
        float width = height * aspect;
        
        float halfHeight = height * 0.5f;
        float halfWidth = width * 0.5f;
        
        float x = halfWidth*norm.x + position.x;
        float y = halfHeight*norm.y + position.y;
        
        return new Vector2(x,y);
    }
    
    
    
}
