package jonl.ge.base;

import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public abstract class BaseGeometry {

	//TODO Find better way for handling static/dynamic data and keeping or removing vertices on gl_mesh creation
	
	protected boolean update = false;
	protected Vector3[] vertices;
    protected Vector3[] normals;
    protected Vector2[] texCoords;
    protected int[] indices;
    
    protected boolean calculateTangents = true;
    protected Vector3[] tangents;
    protected Vector3[] bitangents;
    
}
