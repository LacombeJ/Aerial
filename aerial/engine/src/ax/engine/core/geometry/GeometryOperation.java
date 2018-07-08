package ax.engine.core.geometry;

import ax.engine.core.Geometry;
import ax.engine.core.Transform;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

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
	    	
	    	//TODO verify
	    	Matrix4 rot = transformation.getRotation().toMatrix();
	    	Vector3[] normals = g.getNormalArray();
            for (Vector3 v : normals) {
                Vector4 mult = rot.multiply(new Vector4(v,1));
                v.set(mult.x,mult.y,mult.z);
            }
            g.setNormalArray(normals);
	    	
    	};
    }
    
    public static GeometryOperation transform(Transform t) {
    	return transform(t.computeMatrix());
    }

}
