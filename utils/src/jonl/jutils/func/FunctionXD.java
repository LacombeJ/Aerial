package jonl.jutils.func;

public interface FunctionXD<X,Y> {

	@SuppressWarnings("unchecked")
	Y f(X... x);
	
}
