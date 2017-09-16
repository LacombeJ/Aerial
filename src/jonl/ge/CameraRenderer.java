package jonl.ge;

/**
 * Adds rendering functionality outside of render loop.
 * 
 * @author Jonathan Lacombe
 *
 */
public class CameraRenderer extends Component {

    public void render() {
        
        Scene scene = getGameObject().getScene();
        AppRenderer renderer = (AppRenderer) scene.application.getRenderer();
        renderer.renderCameraSeparately(getGameObject().getComponentOfType(Camera.class),scene);
        
    }
    
}
