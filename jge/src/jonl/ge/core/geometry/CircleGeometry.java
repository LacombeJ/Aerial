package jonl.ge.core.geometry;

import jonl.ge.core.Geometry;
import jonl.jutils.structs.FloatArray;
import jonl.jutils.structs.IntArray;
import jonl.vmath.Mathf;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/CircleGeometry.js

public class CircleGeometry extends Geometry {
    
    private FloatArray vertices;
    private FloatArray normals;
    private FloatArray texCoords;
    private IntArray indices;
    
    public CircleGeometry() {
        this(0.5f);
    }
    
    public CircleGeometry(float radius) {
    	this(radius,32);
    }
    
    public CircleGeometry(float radius, int segments) {
        this(radius,segments,0,Mathf.TWO_PI);
    }
    
    public CircleGeometry(float radius, int segments, float thetaStart, float thetaLength) {
        
        int numVerts = segments + 2;
        
        vertices = new FloatArray(3 * numVerts);
        normals = new FloatArray(3 * numVerts);
        texCoords = new FloatArray(2 * numVerts);
        indices = new IntArray(3 * segments);

        int i, s;
        Vector3 vertex = new Vector3();
    	Vector2 uv = new Vector2();

    	// center point

    	vertices.put( 0, 0, 0 );
    	normals.put( 0, 0, 1 );
    	texCoords.put( 0.5f, 0.5f );

    	for ( s = 0, i = 3; s <= segments; s ++, i += 3 ) {

    		float segment = thetaStart + s / (float)segments * thetaLength;

    		// vertex

    		vertex.x = radius * Mathf.cos( segment );
    		vertex.y = radius * Mathf.sin( segment );

    		vertices.put( vertex.x, vertex.y, vertex.z );

    		// normal

    		normals.put( 0, 0, 1 );

    		// uvs

    		uv.x = ( vertices.get(i) / radius + 1 ) / 2f;
    		uv.y = ( vertices.get(i+1) / radius + 1 ) / 2f;

    		texCoords.put( uv.x, uv.y );

    	}

    	// indices

    	for ( i = 1; i <= segments; i ++ ) {

    		indices.put( i, i + 1, 0 );

    	}
        
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.getArray());
        
    }
    
    
}
