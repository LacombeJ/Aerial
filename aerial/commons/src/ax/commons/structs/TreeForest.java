package ax.commons.structs;

import ax.commons.func.Function;

/**
 * Data structure representing a forest of trees.
 * 
 * Methods are overriden because of the structure of a tree forest. Unlike the tree node,
 * a tree forest has no parent and thus it's underlying structure is not the same
 * as a node where the branch itself can be casted to the generic type.
 * 
 * @author Jonathan
 *
 */
public class TreeForest<T extends TreeNode<T>> extends TreeBranch<T> implements Iterable<T> {
	
	@Override
	public void recursiveBFS(Function<T,Boolean> shouldBreak) {
		breadthFirst(shouldBreak, getChildren());
	}
	
	@Override
	protected boolean depthFirst(Function<T,Boolean> shouldBreak) {
		for (T child : this) {
			if (shouldBreak.f(child)) return true;
			if (child.depthFirst(shouldBreak)) {
				return true;
			}
		}
		return false;
	}
	
}
