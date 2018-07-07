package ax.commons.func;

public interface FunctionXD<X,Y> {

	@SuppressWarnings("unchecked")
	Y f(X... x);
	
}
