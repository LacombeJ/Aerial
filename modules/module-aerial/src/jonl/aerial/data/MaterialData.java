package jonl.aerial.data;

import jonl.vmath.Vector4;

public class MaterialData {

    String materialType;
    
    public static class SolidMaterialData extends MaterialData {
        {
            materialType = "SolidMaterial";
        }
        Vector4 color = new Vector4(1,1,1,1);
    }
    
}
