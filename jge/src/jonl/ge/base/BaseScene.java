package jonl.ge.base;

import jonl.ge.core.GameObject;
import jonl.jutils.structs.TreeForest;

public abstract class BaseScene {

	protected TreeForest<GameObject> root = null;
	
	protected abstract void create();
	
	protected abstract void update();
	
}