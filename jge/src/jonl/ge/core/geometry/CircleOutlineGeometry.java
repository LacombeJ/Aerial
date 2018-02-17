package jonl.ge.core.geometry;

import jonl.ge.core.Geometry;
import jonl.jutils.structs.FloatArray;
import jonl.jutils.structs.IntArray;
import jonl.vmath.Mathf;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

/**
 * 
 * @author Jonathan
 *
 */
public class CircleOutlineGeometry extends Geometry {
    
    private FloatArray vertices;
    private FloatArray normals;
    private FloatArray texCoords;
    private IntArray indices;
    
    public CircleOutlineGeometry() {
        this(0.5f);
    }
    
    public CircleOutlineGeometry(float radius) {
    	this(radius,32);
    }
    
    public CircleOutlineGeometry(float radius, int segments) {
        this(radius,segments,0,Mathf.TWO_PI);
    }
    
    public CircleOutlineGeometry(float radius, int segments, float thetaStart, float thetaLength) {
        
        int numVerts = segments + 1;
        
        vertices = new FloatArray(3 * numVerts);
        normals = new FloatArray(3 * numVerts);
        texCoords = new FloatArray(2 * numVerts);
        indices = new IntArray(2 * segments);

        int i, s;
        Vector3 vertex = new Vector3();
    	Vector2 uv = new Vector2();

    	for ( s = 0, i = 0; s <= segments; s ++, i += 3 ) {

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

    	for ( i = 0; i < segments; i ++ ) {
    		int n = (i+1) % segments;
    		indices.put( i, n );

    	}
        
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.getArray());
        
        
        // This is called because geometry isn't triangle based
        // TODO Find a better way to do this
        setCalculateTangents(false);
        
    }
    
    
}
