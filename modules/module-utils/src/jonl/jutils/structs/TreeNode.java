package jonl.jutils.structs;

import java.util.ArrayList;
import java.util.Iterator;

import jonl.jutils.func.Function;
import jonl.jutils.func.List;
import jonl.jutils.func.Wrapper;

/**
 * Data structure representing a node of a tree
 * 
 * @author Jonathan
 *
 */
public class TreeNode<T extends TreeNode<T>> implements Iterable<T> {

	T parent = null;
	private ArrayList<T> children = new ArrayList<>();
	
	
	
	public TreeNode() {
		
	}
	
	// ------------------------------------------------------------------------
	
	/* Parent */
	
	public TreeNode<T> getParent() {
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
	
	public T getChildAt(int index) {
		return children.get(index);
	}
	
	public boolean addChild(T child) {
		return children.add(child);
	}
	
	/** Adds child and then returns this object */
	@SuppressWarnings("unchecked")
	public T append(T child) {
		addChild(child);
		return (T) this;
	}
	
	public T removeChildAt(int index) {
		T child = children.remove(index);
		if (child != null) {
			child.parent = null;
		}
		return child;
	}
	
	public void removeChild(T child) {
        if (children.remove(child)) {
            child.parent = null;
        }
    }
	
	public int getChildrenCount() {
		return children.size();
	}
	
	public boolean hasChildren() {
		return getChildrenCount() != 0;
	}
	
	public boolean isLeaf() {
		return !hasChildren();
	}
	
	public boolean isEmpty() {
		return isLeaf();
	}
	
	public T findChild(Function<T,Boolean> found) {
		for (T child : children) {
			if (found.f(child)) {
				return child;
			}
		}
		return null;
	}
	
	public ArrayList<T> findChildren(Function<T,Boolean> found) {
		ArrayList<T> list = new ArrayList<>();
		for (T child : children) {
			if (found.f(child)) {
				list.add(child);
			}
		}
		return list;
	}
	
	
	/* Descendant */
	
	@SuppressWarnings("unchecked")
	public T findDescendant(Function<T,Boolean> found) {
		Wrapper<T> ret = new Wrapper<>(null);
		Function<T,Boolean> breakBFS = (t) -> {
			if (found.f(t)) {
				ret.x = t;
				return true;
			}
			return false;
		};
		recursiveBFS(breakBFS, List.wrap((T)this));
		return ret.x;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<T> findDescendants(Function<T,Boolean> found) {
		ArrayList<T> list = new ArrayList<>();
		Function<T,Boolean> breakBFS = (t) -> {
			if (found.f(t)) {
				list.add(t);
			}
			return true;
		};
		recursiveBFS(breakBFS, List.wrap((T)this));
		return list;
	}
	
	private void recursiveBFS(Function<T,Boolean> shouldBreak, ArrayList<T> collect) {
		if (collect.isEmpty()) { //search is complete
			return;
		}
		ArrayList<T> next = new ArrayList<>();
		for (T child : collect) {
			if (shouldBreak.f(child)) {
				return;
			}
			next.add(child);
		}
		recursiveBFS(shouldBreak, next);
	}
	
	
	
	// ------------------------------------------------------------------------
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < getChildrenCount();
            }
            @Override
            public T next() {
                return getChildAt(i++);
            }
        };
	}
	
	@SuppressWarnings("unchecked")
	public String treeString() {
		StringBuilder sb = new StringBuilder();
		recursiveStringBuild(sb, (T) this, 0);
		return sb.toString();
	}
	
	private static <S extends TreeNode<S>> void recursiveStringBuild(StringBuilder b, TreeNode<S> node, int level) {
		for (int i=0; i<level; i++) {
			b.append("  ");
		}
		b.append(node.toString());
		b.append('\n');
		for (TreeNode<S> child : node) {
			recursiveStringBuild(b,child,level+1);
		}
	}


	
}
