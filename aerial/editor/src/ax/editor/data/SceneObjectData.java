package ax.editor.data;

import java.util.ArrayList;

import ax.engine.core.Transform;

public class SceneObjectData {

    String name;
    Transform transform;
    ArrayList<String> componentIds;
    ArrayList<String> childrenIds;

}
