package ax.commons.func;

public class Wrapper<X> {

	public X x;
	
	public Wrapper(X x) {
		this.x = x;
	}
	
	@Override
	public String toString() {
		return x.toString();
	}
	
}
