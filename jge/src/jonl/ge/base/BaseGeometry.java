package jonl.ge.base;

public abstract class BaseGeometry {

	//TODO Find better way for handling static/dynamic data and keeping or removing vertices on gl_mesh creation
	
	protected boolean update = false;
	protected float[] vertices;
    protected float[] normals;
    protected float[] texCoords;
    protected int[] indices;
    
    protected boolean calculateTangents = true;
    protected float[] tangents;
    protected float[] bitangents;
    
}
