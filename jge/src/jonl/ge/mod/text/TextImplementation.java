package jonl.ge.mod.text;

import jonl.jgl.GL;
import jonl.jgl.Texture;

interface TextImplementation {

    public Texture get(Text text);
    
    public void load(GL gl);
    
}
