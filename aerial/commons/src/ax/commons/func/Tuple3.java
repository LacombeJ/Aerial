package ax.commons.func;

public class Tuple3<X,Y,Z> {

	public final X x;
	public final Y y;
	public final Z z;
	public Tuple3(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
}
