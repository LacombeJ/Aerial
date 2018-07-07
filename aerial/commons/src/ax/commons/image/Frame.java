package ax.commons.image;

import java.util.HashMap;

public class Frame {

    public static HashMap<String,Window> windowMap = new HashMap<>();
    
    public static void imshow(String window, Image image) {
        Window w = windowMap.get(window);
        if (w == null) {
            w = new Window(window,image.width(), image.height());
            windowMap.put(window, w);
        }
        w.setImage(image);
    }
    
}
