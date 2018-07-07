package ax.engine.core.geometry;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/ConeGeometry.js

public class ConeGeometry extends CylinderGeometry {

	//TODO fix cone normal calculations
	
    public ConeGeometry() {
        this(0.5f,1f);
    }
    
    public ConeGeometry(float radius, float height) {
        super(0,radius,height);
    }
    
    public ConeGeometry(float radius, float height, int radialSegments, int heightSegments) {
        super(0,radius,height,radialSegments,heightSegments);
    }
    
    public ConeGeometry(float radius, float height, int radialSegments, int heightSegments, boolean openEnded, float thetaStart, float thetalength) {
        super(0,radius,height,radialSegments,heightSegments,openEnded,thetaStart,thetalength);
    }
    
}
