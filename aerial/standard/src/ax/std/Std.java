package ax.std;

import ax.math.vector.Mathf;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;

public class Std {

    public static Quaternion rotationFromVector(Vector3 v) {

        float theta = v.theta();
        float phi = v.phi();

        Quaternion quat = new Quaternion();
        quat.mul( Quaternion.eulerRad(0, 0, theta).conjugate().nor() );
        quat.mul( Quaternion.eulerRad(0, phi+Mathf.PI_OVER_2, 0) );

        return quat;

    }

    public static void coordinateTranslate(CoordinateSystem src, CoordinateSystem dst, Vector3 vector) {

        float up = vector.get(src.up.unit.index);
        float right = vector.get(src.right.unit.index);
        float forward = vector.get(src.forward.unit.index);

        if (src.up.positive^dst.up.positive) up = -up;
        if (src.right.positive^dst.right.positive) right = -right;
        if (src.forward.positive^dst.forward.positive) forward = -forward;

        vector.set(dst.up.unit.index, up);
        vector.set(dst.right.unit.index, right);
        vector.set(dst.forward.unit.index, forward);

    }

    public static void coordinateTranslate(CoordinateSystem src, CoordinateSystem dst, Vector3[] vectors) {
        for (Vector3 v : vectors) {
            coordinateTranslate(src,dst,v);
        }
    }


    public static final CoordinateSystem BLENDER_COORDINATES = new Std.CoordinateSystem(
            Std.CoordinateAxis.POSITIVE_Z,
            Std.CoordinateAxis.POSITIVE_X,
            Std.CoordinateAxis.POSITIVE_Y);

    public static final CoordinateSystem STANDARD_COORDINATES = new Std.CoordinateSystem(
            Std.CoordinateAxis.POSITIVE_Y,
            Std.CoordinateAxis.POSITIVE_X,
            Std.CoordinateAxis.NEGATIVE_Z);

    public enum UnitAxis {
        X(0),
        Y(1),
        Z(2);
        public final int index;
        UnitAxis(int index) {
            this.index = index;
        }
    }

    public enum CoordinateAxis {
        POSITIVE_X(UnitAxis.X,true),
        POSITIVE_Y(UnitAxis.Y,true),
        POSITIVE_Z(UnitAxis.Z,true),
        NEGATIVE_X(UnitAxis.X,false),
        NEGATIVE_Y(UnitAxis.Y,false),
        NEGATIVE_Z(UnitAxis.Z,false);
        public final UnitAxis unit;
        public final boolean positive;
        CoordinateAxis(UnitAxis unit, boolean positive) {
            this.unit = unit;
            this.positive = positive;
        }
    }

    public static class CoordinateSystem {
        public final CoordinateAxis up;
        public final CoordinateAxis right;
        public final CoordinateAxis forward;
        public CoordinateSystem(CoordinateAxis up, CoordinateAxis right, CoordinateAxis forward) {
            this.up = up;
            this.right = right;
            this.forward = forward;
        }
    }

}
