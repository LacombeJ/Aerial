package jonl.ge;

import java.util.ArrayList;
import java.util.List;

/**
 * Has precedence over Camera targets
 * 
 * @author Jonathan Lacombe
 *
 */
public class CameraCull extends Component {
    
    public enum Target {
        EXCEPT,
        ONLY,
    }
    
    private Target type = Target.ONLY;
    private List<Camera> targets = new ArrayList<>();
    
    public CameraCull(Target type) {
        this.type = type;
    }
    
    /**
     * Defaults to object being visible only to cameras added to this object
     */
    public CameraCull() {
        
    }
    
    public Target getTargetType() {
        return type;
    }
    
    public void setTargetType(Target type) {
        this.type = type;
    }
    
    public boolean hasTarget(Camera camera) {
        return targets.contains(camera);
    }
    
    public void addTargets(Camera... camera) {
        for (Camera c : camera) {
            targets.add(c);
        }
    }
    
    public void removeTargets(Camera... camera) {
        for (Camera c : camera) {
            targets.remove(c);
        }
    }
    
    public void removeAllTargets() {
        targets = new ArrayList<Camera>();
    }
    
    
}