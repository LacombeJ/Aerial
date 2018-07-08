package ax.examples.box.testbed;

import ax.box.Collider2d;
import ax.box.RigidBody2d;
import ax.engine.core.*;
import ax.engine.core.geometry.BoxGeometry;
import ax.engine.core.material.SolidMaterial;
import ax.math.vector.Color;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;
import ax.std.render.StandardMaterial;
import ax.std.render.StandardMaterialBuilder;

public abstract class TestbedTest {

    public abstract String name();

    public abstract void init(Scene scene);

    protected void add(SceneObject so, Collider2d collider, RigidBody2d body) {
        so.addComponent(collider);
        so.addComponent(body);
    }

    public final static Color BACK = Color.LIGHT_GRAY;
    public final static Color STATIC = Color.SLATE;
    public final static Color DYNAMIC = Color.CERULEAN;

    protected void boxMesh(SceneObject so, float width, float height, Color color) {
        Geometry geometry = new BoxGeometry(width, height, 1);
        //Material material = new SolidMaterial(color.toVector());
        Material material = standard(color.toVector());
        Mesh mesh = new Mesh(geometry, material);
        so.addComponent(mesh);
    }

    static StandardMaterial standard(Vector4 color) {
        StandardMaterialBuilder smb = new StandardMaterialBuilder();

        smb.diffuse = smb.vec3(color.xyz());
        smb.specular = smb.vec3(0.05f);

        return smb.build();
    }


}
