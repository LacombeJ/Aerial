package jonl.ge;

public class MeshRenderer extends Component {

    public Mesh mesh;
    public boolean castShadows = true;
    public boolean recieveShadows = false;
    Material material = EngineAssets.DEFAULT_MATERIAL;
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material mat) {
        material = mat;
    }

}
