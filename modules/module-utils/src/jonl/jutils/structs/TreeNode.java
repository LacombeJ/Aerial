package jonl.jutils.structs;

/**
 * Data structure representing a node of a tree
 * 
 * @author Jonathan
 *
 */
public class TreeNode<T extends TreeNode<T>> extends TreeBranch<T> implements Iterable<T> {

	T parent = null;
	
	// ------------------------------------------------------------------------
	
	/* Parent */
	
	public T getParent() {
		return parent;
	}
	
	/* Ancestor */
	
	public boolean isRoot() {
		return getParent() == null;
	}
	
	@SuppressWarnings("unchecked")
	public T getRoot() {
		if (isRoot()) {
			return (T) this;
		}
		return parent.getRoot();
	}
	
	
	/* Child */
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean addChild(T child) {
		boolean add = super.addChild(child);
		if (add) child.parent = (T) this;
		return add;
	}
	
	@Override
	public T removeChildAt(int index) {
		T child = super.removeChildAt(index);
		if (child != null) {
			child.parent = null;
		}
		return child;
	}
	
	@Override
	public boolean removeChild(T child) {
        boolean remove = super.removeChild(child);
        if (remove) child.parent = null;
        return remove;
    }
	
}
