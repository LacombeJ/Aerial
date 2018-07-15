package ax.std.misc;

import ax.commons.misc.ArrayUtils;
import ax.engine.core.Property;
import ax.engine.core.SceneObject;
import ax.std.text.Align;
import ax.std.text.Text;
import ax.std.text.TextMesh;

import java.util.ArrayDeque;

public class ScreenLog {

    CanvasObject canvas;
    SceneObject root;

    int limit;
    ArrayDeque<String> textDeque;


    public ScreenLog(int limit) {
        this.limit = limit;

        textDeque = new ArrayDeque<>();

        canvas = new CanvasObject();
        root = new SceneObject();

        Text text = new Text("");
        TextMesh mesh = new TextMesh(text);
        mesh.setAlign(Align.HA_LEFT, Align.VA_BOTTOM);
        root.addComponent(mesh);

        root.addUpdate(()->{
            String str = "";
            String[] lines = textDeque.toArray(new String[0]);
            for (String line : lines) {
                str += line;
                str += "\n";
            }
            text.setText(str);
        });

        canvas.addChild(root);

    }

    public ScreenLog() {
        this(10);
    }

    public SceneObject get() {
        return canvas.get();
    }

    private void logLine(String line) {
        if (textDeque.size()>=limit && !textDeque.isEmpty()) {
            textDeque.removeFirst();
        }
        textDeque.addLast(line);
    }

    public void logText(String text) {
        for (String line : text.split("\n")) {
            logLine(line);
        }
    }


    public void log(Object... objects) {
        for (int i=0; i<objects.length; i++) {
            Object o = objects[i];
            if (o==null) {
                logText("null");
            } else if (o instanceof int[]) {
                logText(ArrayUtils.toString((int[]) o));
            } else if (o instanceof float[]) {
                logText(ArrayUtils.toString((float[]) o));
            } else if (o instanceof byte[]) {
                logText(ArrayUtils.toString((byte[]) o));
            } else if (o instanceof double[]) {
                logText(ArrayUtils.toString((double[]) o));
            } else if (o instanceof long[]) {
                logText(ArrayUtils.toString((long[]) o));
            } else if (o instanceof char[]) {
                logText(ArrayUtils.toString((char[]) o));
            } else if (o.getClass().isArray()) {
                log(o);
            } else {
                logText(o.toString());
            }
            //print a space only if there is another object following
            if (i==objects.length-1) break;
            logText(" ");
        }
    }


}
