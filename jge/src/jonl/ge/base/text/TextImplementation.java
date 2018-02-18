package jonl.ge.base.text;

import jonl.ge.core.text.Text;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Texture;

interface TextImplementation {

    public Texture get(Text text);
    
    public void load(GraphicsLibrary gl);
    
}
