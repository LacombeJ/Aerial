package jonl.ge.core.geometry;

import jonl.ge.core.Geometry;
import jonl.ge.core.Transform;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public interface GeometryOperation {
    
	void modify(Geometry geometry);
	
	// TODO isolate different types of operations (Vertex op, VertexArray op, TexCoord op, etc) into own module (call into modules for geometry operations)
	
	public static GeometryOperation scaleTexCoords(float x, float y, float ox, float oy) {
    	return (g) -> {
    		Vector2[] uvs = g.getTexCoordArray();
        	Vector2 scalar = new Vector2(x,y);
        	Vector2 origin = new Vector2(ox,oy);
        	for (Vector2 uv : uvs) {
        		uv.scale(scalar, origin);
        	}
        	g.setTexCoordArray(uvs);
    	};
    }
    
    public static GeometryOperation scaleTexCoords(float x, float y) {
    	return scaleTexCoords(x,y,0.5f,0.5f);
    }
    
    public static GeometryOperation translateTexCoords(float x, float y) {
    	return (g) -> {
	    	Vector2[] uvs = g.getTexCoordArray();
	    	Vector2 translate = new Vector2(x,y);
	    	for (Vector2 uv : uvs) {
	    		uv.add(translate);
	    	}
	    	g.setTexCoordArray(uvs);
    	};
    }
    
    public static GeometryOperation transform(Matrix4 transformation) {
    	return (g) -> {
	    	Vector3[] vertices = g.getVertexArray();
	    	for (Vector3 v : vertices) {
	    		Vector4 mult = transformation.multiply(new Vector4(v,1));
	    		v.set(mult.x,mult.y,mult.z);
	    	}
	    	g.setVectorArray(vertices);
    	};
    }
    
    public static GeometryOperation transform(Transform t) {
    	return transform(t.computeMatrix());
    }

}
