package jonl.jutils.structs;

import java.util.ArrayList;
import java.util.Iterator;

import jonl.jutils.func.Callback;
import jonl.jutils.func.Function;
import jonl.jutils.func.List;
import jonl.jutils.func.Wrapper;

/**
 * Data structure representing a node of a tree
 * 
 * @author Jonathan
 *
 */
public class TreeBranch<T extends TreeBranch<T>> implements Iterable<T> {

	private ArrayList<T> children = new ArrayList<>();
	
	// ------------------------------------------------------------------------
	
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
		return children.remove(index);
	}
	
	public boolean removeChild(T child) {
		return children.remove(child);
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
	
	public T getChild(Function<T,Boolean> found) {
		for (T child : children) {
			if (found.f(child)) {
				return child;
			}
		}
		return null;
	}
	
	public ArrayList<T> getChildren(Function<T,Boolean> found) {
		ArrayList<T> list = new ArrayList<>();
		for (T child : children) {
			if (found.f(child)) {
				list.add(child);
			}
		}
		return list;
	}
	
	public ArrayList<T> getChildren() {
		return List.list(children);
	}
	
	public void iterate(Callback<T> callback) {
		List.iterate(children, callback);
	}
	
	
	/* Descendant */
	
	public T getDescendant(Function<T,Boolean> found) {
		Wrapper<T> ret = new Wrapper<>(null);
		Function<T,Boolean> breakBFS = (t) -> {
			if (found.f(t)) {
				ret.x = t;
				return true;
			}
			return false;
		};
		recursiveBFS(breakBFS);
		return ret.x;
	}
	
	public ArrayList<T> getDescendants(Function<T,Boolean> found) {
		ArrayList<T> list = new ArrayList<>();
		Function<T,Boolean> breakBFS = (t) -> {
			if (found.f(t)) {
				list.add(t);
			}
			return false;
		};
		recursiveBFS(breakBFS);
		return list;
	}
	
	/** Calls depth first search with the given callback and without breaking */
	public void recurse(Callback<T> callback) {
		recursiveDFS(
			(t) -> {
				callback.f(t);
				return false;
			}
		);
	}
	
	@SuppressWarnings("unchecked")
	public void recursiveBFS(Function<T,Boolean> shouldBreak) {
		breadthFirst(shouldBreak, List.wrap((T)this));
	}
	
	public void recursiveDFS(Function<T,Boolean> shouldBreak) {
		depthFirst(shouldBreak);
	}
	
	protected void breadthFirst(Function<T,Boolean> shouldBreak, ArrayList<T> collect) {
		if (collect.isEmpty()) { //search is complete
			return;
		}
		ArrayList<T> next = new ArrayList<>();
		for (T t : collect) {
			if (shouldBreak.f(t)) {
				return;
			}
			for (T child : t) {
				next.add(child);
			}	
		}
		breadthFirst(shouldBreak, next);
	}
	
	/** @return false if recurses through all descendants or true if recursion was broken */
	@SuppressWarnings("unchecked")
	protected boolean depthFirst(Function<T,Boolean> shouldBreak) {
		if (shouldBreak.f((T) this)) return true;
		for (T child : this) {
			if (child.depthFirst(shouldBreak)) {
				return true;
			}
		}
		return false;
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
	
	static <S extends TreeBranch<S>> void recursiveStringBuild(StringBuilder b, TreeBranch<S> node, int level) {
		for (int i=0; i<level; i++) {
			b.append("  ");
		}
		b.append(node.toString());
		b.append('\n');
		for (TreeBranch<S> child : node) {
			recursiveStringBuild(b,child,level+1);
		}
	}


	
}
