package jonl.jutils.func;

public class Tuple2<X,Y> {

	public final X x;
	public final Y y;
	public Tuple2(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
