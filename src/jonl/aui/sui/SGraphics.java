package jonl.aui.sui;

import java.nio.FloatBuffer;

import jonl.jutils.misc.*;
import jonl.vmath.*;
import jonl.aui.Container;
import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.Widget;
import jonl.jgl.*;
import jonl.jgl.GraphicsLibrary.*;
import jonl.jgl.utils.*;

public class SGraphics implements Graphics {

    GraphicsLibrary gl;
    
    float offsetX;
    float offsetY;
    
    Matrix4 ortho;
    Mesh rect;
    Mesh circle;
    Mesh fontRect;
    Program fontProgram;
    
    Program solid;
    Program basic;
    
    Vector4 buttonColor = new Vector4(0.8f,0.8f,0.8f,1);
    Vector4 buttonColorHover = new Vector4(0.5f,0.5f,0.6f,1);
    
    SGraphics(GraphicsLibrary gl) {
        this.gl = gl;
        
        gl.glEnable(Target.SCISSOR_TEST);
        gl.glEnable(Target.BLEND);
        gl.glBlendFunc(Blend.NORMAL);
        
        rect = gl.glGenMesh(MeshLoader.load("res/models/rect.mesh"));
        circle = gl.glGenMesh(MeshLoader.load("res/models/circle.mesh"));
        
        fontRect = gl.glGenMesh(MeshLoader.load("res/models/rect.mesh"));
        fontProgram = loadProgram("res/shaders/font.vert","res/shaders/font.frag");
        
        solid = loadProgram("res/shaders/solid.vert","res/shaders/solid.frag");
        basic = loadProgram("res/shaders/basic.vert","res/shaders/basic.frag");
        
    }
    
    void setOrtho(Matrix4 ortho) {
        this.ortho = ortho;
    }
    
    int[] currentCut;
    
    private int[] getBox(SWidget w) {
        int[] box = new int[]{w.getWindowX(),w.getWindowY(),w.getWidth(),w.getHeight()};
        SContainer c = (SContainer) w.getParent();
        if (c!=null) {
            box = cutOut(box, ArrayUtils.add( c.getBox(), new int[]{c.getWindowX(),c.getWindowY(),0,0} ));
        }
        return box;
    }
    
    void paint(Widget w) {
        boolean firstCut = false;
        int[] box = getBox((SWidget) w);
        if (currentCut==null) {
            firstCut = true;
            currentCut = box;
        } else {
            currentCut = cutOut(currentCut,box);
        }
        gl.glEnable(Target.SCISSOR_TEST);
        gl.glScissor(currentCut);
        float ox = offsetX;
        float oy = offsetY;
        offsetX = w.getWindowX();
        offsetY = w.getWindowY();
        SWidget sw = (SWidget)w;
        sw.paint(this);
        offsetX = ox;
        offsetY = oy;
        if (w instanceof Container) {
            Container c = (Container) w;
            for (Widget child : c.getChildren()) {
                int[] cc = ArrayUtils.copy(currentCut);
                paint(child);
                currentCut = cc;
            }
        }
        if (firstCut) {
            currentCut = null;
        }
    }
    
    int[] cutOut(int[] paper, int[] scissorBox) {
        int x = Math.max(paper[0],scissorBox[0]);
        int y = Math.max(paper[1],scissorBox[1]);
        
        int paperX1 = paper[0] + paper[2];
        int boxX1 = scissorBox[0] + scissorBox[2];
        int x1 = Math.min(paperX1,boxX1);
        int width = x1 - x;
        
        int paperY1 = paper[1] + paper[3];
        int boxY1 = scissorBox[1] + scissorBox[3];
        int y1 = Math.min(paperY1,boxY1);
        int height = y1 - y;
        
        return new int[]{x,y,width,height};
    }
    
    @Override
    public void renderCircle(float x, float y, float w, float h, Vector4 color) {
        render(circle,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color);
    }
    
    @Override
    public void renderRect(float x, float y, float w, float h, Vector4 color) {
        render(rect,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color);
    }
    
    @Override
    public void renderRect(Matrix4 mat, Vector4 color) {
        Matrix4 matrix = Matrix4.identity().translate(offsetX,offsetY,0).multiply(mat);
        render(rect,matrix,color);
    }
    
    @Override
    public void renderText(String string, float x, float y, HAlign halign, VAlign valign,
            jonl.aui.Font font, Vector4 color) {
        renderString(string,x+offsetX,y+offsetY,halign,valign,(SFont)font,color);
    }
    
    
    public void renderTexture(Texture texture, float x, float y, float w, float h) {
        render(rect,new Vector3(x+offsetX,y+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),texture);
    }
    
    
    
    
    private void render(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Vector4 color) {
        Matrix4 mat = Matrix4.identity();
        mat.translate(trans);
        mat.rotate(rot);
        mat.scale(scale);
        
        gl.glUseProgram(solid);
        solid.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        solid.setUniform("color",color.x,color.y,color.z,color.w);
        gl.glRender(mesh);
        gl.glUseProgram(null);
    }
    
    
    
    void render(Mesh mesh, Matrix4 mat, Vector4 color) {        
        gl.glUseProgram(solid);
        solid.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        solid.setUniform("color",color.x,color.y,color.z,color.w);
        gl.glRender(mesh);
        gl.glUseProgram(null);
    }
    
    private void render(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Texture texture) {
        Matrix4 mat = Matrix4.identity();
        mat.translate(trans);
        mat.rotate(rot);
        mat.scale(scale);
        
        gl.glUseProgram(basic);
        basic.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        basic.setTexture("texture",texture,0);
        gl.glRender(mesh);
        gl.glUseProgram(null);
    }
    
    Matrix4 pos(float x, float y) {
        return Matrix4.identity().translate(x,y,0);
    }
    
    
    private void renderChar(char c, float x, float y, SFont font,
            Matrix4 model) { 
        fontRect.setTexCoordAttrib(font.font.getIndices(c),2);
        
        float width = font.getWidth(c);
        float height = font.getHeight();
        x = x+width/2f;
        y = y+height/2f;
        
        Matrix4 M = model.get().translate(x,y,0).scale(width,height,1);
        
        Matrix4 MVP = ortho.get().multiply(M);
        
        FloatBuffer fb = BufferPool.borrowFloatBuffer(16,true);
        fontProgram.setUniformMat4("MVP",MVP.toFloatBuffer(fb));
        BufferPool.returnFloatBuffer(fb);
        
        gl.glRender(fontRect);
    }
    
    //TODO static text transform to reduce matrix multiplications?
    private void renderString(String string, float x, float y, HAlign halign, VAlign valign,
            SFont font, Vector4 color) {
        
        Matrix4 model = Matrix4.identity().translate(x,y,0);
        gl.glUseProgram(fontProgram);
        
        fontProgram.setTexture("texture",font.font.getTexture(),0);
        fontProgram.setUniform("fontColor",color.x,color.y,color.z,color.w);
        
        String[] str = string.split("\n");
        float totalHeight = font.getHeight()*str.length;
        
        float sdy = 0;
        
        if (valign==VAlign.BOTTOM) {
            sdy = totalHeight - font.getHeight();
        } else if (valign==VAlign.MIDDLE) {
            sdy = (totalHeight)/2 - font.getHeight();
        } else {
            sdy -= font.getHeight();
        }
        
        for (int i=0; i<str.length; i++) {
            String line = str[i];
            float sdx = 0;
            if (halign==HAlign.CENTER) {
                sdx = -font.getWidth(line)/2;
            } else if (halign==HAlign.RIGHT) {
                sdx = -font.getWidth(line);
            }
            for (int j=0; j<line.length(); j++) {
                char c = line.charAt(j);
                //Parameters x and y must be given as ints or else
                //characters would show unwanted artifacts;
                //unknown reason
                renderChar(c,(int)sdx,(int)sdy,font,model);
                sdx += font.getWidth(c);
            }
            sdy -= font.getHeight();
        }
        gl.glUseProgram(null);
    }
    
    private Program loadProgram(String vertFile, String fragFile) {
        Program p = gl.glCreateProgram();
        
        Shader vert = gl.glCreateShader(ShaderType.VERTEX_SHADER);
        Shader frag = gl.glCreateShader(ShaderType.FRAGMENT_SHADER);
        
        vert.compileSourceFromFile(vertFile);
        frag.compileSourceFromFile(fragFile);
        
        p.attach(vert);
        p.attach(frag);
        p.link();
        
        return p;
    }
    
    public GraphicsLibrary getGL() {
        return gl;
    }
    
}
