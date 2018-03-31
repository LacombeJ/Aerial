package jonl.ge.mod.text;

import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Texture;

interface TextImplementation {

    public Texture get(Text text);
    
    public void load(GraphicsLibrary gl);
    
}
