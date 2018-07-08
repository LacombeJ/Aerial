package ax.std.text;

import ax.graphics.GL;
import ax.graphics.Texture;

interface TextImplementation {

    public Texture get(Text text);
    
    public void load(GL gl);
    
}
