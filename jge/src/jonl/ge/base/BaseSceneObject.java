package jonl.ge.base;

import jonl.ge.core.Transform;
import jonl.jutils.structs.TreeNode;

public abstract class BaseSceneObject<T extends TreeNode<T>> extends TreeNode<T> {

	protected Transform transform;
	
}
