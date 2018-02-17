package jonl.ge.core.render;

import java.util.ArrayList;

import jonl.ge.core.Component;
import jonl.ge.core.FrameBuffer;
import jonl.ge.core.Texture;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public abstract class CameraExtension extends Component {
    
    ArrayList<ShaderData> shaderData = new ArrayList<>();
    
    FrameBuffer buffer;
    
    public boolean renderToTexture;
    
    public abstract void create();

    public abstract void render();
    
    public abstract String shader();
    
    private void setData(Object data, String name) {
        shaderData.add(new ShaderData(data,name));
    }
    
    public Texture getTexture() {
        return buffer.getTexture(0);
    }
    
    public void setTexture(Texture texture, String name) {
        setData(texture,name);
    }
    
    public void setInt(int i, String name) {
        setData(i,name);
    }
    
    public void setFloat(float f, String name) {
        setData(f,name);
    }
    
    public void setVec3(Vector3 v, String name) {
        setData(v,name);
    }
    
    public void setVec4(Vector4 v, String name) {
        setData(v,name);
    }
    
    public void setVec4(float[] v, String name) {
        setVec4(new Vector4(v[0],v[1],v[2],v[3]),name);
    }
    
    public void setVec3Array(Vector3[] array, String name) {
        setData(array,name);
    }
    
    public void setMat4(Matrix4 mat, String name) {
        setData(mat,name);
    }
    
    class ShaderData {
        Object data;
        String name;
        ShaderData(Object data, String name) {
            this.data = data;
            this.name = name;
        }
    }
    
}
