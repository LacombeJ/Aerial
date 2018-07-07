package ax.commons.func;

public class Tuple4<X,Y,Z,W> {

	public final X x;
	public final Y y;
	public final Z z;
	public final W w;
	public Tuple4(X x, Y y, Z z, W w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
	
}
