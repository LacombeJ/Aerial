package ax.engine.core;

/**
 * 
 * Class used to add-on to engine and simplify handling structures using delegates and services
 * 
 * @author Jonathan L
 *
 */
public abstract class Attachment {

	private final String name;
	
	public Attachment(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract void add(Delegate delegate, Service service);
	
	public abstract void remove(Delegate delegate, Service service);
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name.toString()+": "+super.toString();
	}
	
}
