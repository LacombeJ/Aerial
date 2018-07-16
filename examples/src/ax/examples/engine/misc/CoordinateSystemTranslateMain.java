package ax.examples.engine.misc;

import ax.commons.io.Console;
import ax.engine.core.*;
import ax.engine.core.geometry.ConeGeometry;
import ax.engine.core.geometry.GeometryOperation;
import ax.engine.core.material.SolidMaterial;
import ax.math.vector.*;
import ax.std.StandardApplication;
import ax.std.Std;
import ax.std.misc.FirstPersonControl;
import ax.std.misc.MouseGrabToggle;
import ax.std.misc.ScreenLog;

public class CoordinateSystemTranslateMain {

    public static void main(String[]args) {
        new CoordinateSystemTranslateMain().run();
    }

    void run() {

        Console.log("Up:",Vector3.up());
        Console.log("Right:",Vector3.right());
        Console.log("Forward:",Vector3.forward());

        Matrix4 transformation = Matrix4.rotation(-Mathf.PI_OVER_2,0,0);

        check(transformation, Vector3.up(), Vector3.up());
        check(transformation, Vector3.right(), Vector3.right());
        check(transformation, Vector3.forward(), Vector3.forward());

        Console.log("===========");

        Std.CoordinateSystem blender = new Std.CoordinateSystem(
                Std.CoordinateAxis.POSITIVE_Z,
                Std.CoordinateAxis.POSITIVE_X,
                Std.CoordinateAxis.POSITIVE_Y);

        Std.CoordinateSystem standard = new Std.CoordinateSystem(
                Std.CoordinateAxis.POSITIVE_Y,
                Std.CoordinateAxis.POSITIVE_X,
                Std.CoordinateAxis.NEGATIVE_Z);

        check(blender, standard, Vector3.up(), Vector3.up());
        check(blender, standard, Vector3.right(), Vector3.right());
        check(blender, standard, Vector3.forward(), Vector3.forward());

    }

    public void check(Std.CoordinateSystem src, Std.CoordinateSystem dst, Vector3 vertex, Vector3 normal) {

        Vector3 vt = vertex.get();
        Vector3 nt = normal.get();

        Std.coordinateTranslate(src, dst, vt);
        Std.coordinateTranslate(src, dst, nt);

        Console.log("Check Std transformation");
        Console.log("Vertex:", vertex, "Normal:", normal);
        Console.log("Vertex:", vt, "Normal:", nt);

    }

    public void check(Matrix4 transformation, Vector3 vertex, Vector3 normal) {

        Vector3 vt = new Vector3();
        Vector3 nt = new Vector3();

        transform(transformation, vertex, normal, vt, nt);

        Console.log("Check");
        Console.log("Vertex:", vertex, "Normal:", normal);
        Console.log("Vertex:", vt, "Normal:", nt);

    }

    public void transform(Matrix4 transformation, Vector3 vertex, Vector3 normal, Vector3 vertexTransformed, Vector3 normalTransformed) {

        vertexTransformed.set(transformation.multiply(new Vector4(vertex,1)).xyz());

        Matrix4 rot = transformation.getRotation().toMatrix();

        normalTransformed.set(rot.multiply(new Vector4(normal,1)).xyz());

    }


}
