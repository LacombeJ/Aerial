package jonl.ge.core;

import java.util.ArrayList;

import jonl.jutils.func.List;

public class FrameBuffer {

    final ArrayList<Texture> textures = new ArrayList<>();
    final int width;
    final int height;
    
    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public FrameBuffer(Texture texture) {
        this(texture.width,texture.height);
        textures.add(texture);
    }
    
    public int width() {
    	return width;
    }
    
    public int height() {
    	return height;
    }
    
    public ArrayList<Texture> textures() {
    	return List.copy(textures);
    }
    
    public void attach(Texture texture) {
        textures.add(texture);
    }

    public Texture getTexture(int i) {
        return textures.get(i);
    }
    
    public int getTextureCount() {
        return textures.size();
    }
    
}
