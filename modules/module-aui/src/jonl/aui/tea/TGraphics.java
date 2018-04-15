package jonl.aui.tea;

import jonl.jutils.func.Function0D;
import jonl.jutils.func.List;
import jonl.jutils.misc.*;
import jonl.vmath.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.graphics.TFont;
import jonl.aui.tea.graphics.TImage;
import jonl.aui.tea.graphics.TImageManager;
import jonl.aui.tea.graphics.TMesh;
import jonl.aui.tea.graphics.TTextManager;
import jonl.jgl.*;
import jonl.jgl.utils.*;

public class TGraphics implements Graphics {

    GL gl;
    
    Function0D<Integer> windowHeight;
    
    TTextManager textManager;
    TImageManager imageManager;
    
    float offsetX;
    float offsetY;
    
    Matrix4 ortho;
    
    Mesh line;
    
    Mesh box;
    Mesh circle;
    
    Mesh boxOutline;
    Mesh circleOutline;
    
    Mesh fontRect;
    Program fontProgram;
    
    Program solid;
    Program basic;
    
    Texture check;
    
    public TGraphics(GL gl, Function0D<Integer> windowHeight) {
        this.gl = gl;
        this.windowHeight = windowHeight;
        
        int version = gl.glGetGLSLVersioni();
        
        line = gl.glGenMesh(TMesh.LINE.data);
        
        box = gl.glGenMesh(TMesh.BOX.data);
        circle = gl.glGenMesh(TMesh.CIRCLE.data);
        
        boxOutline = gl.glGenMesh(TMesh.BOX_OUTLINE.data);
        circleOutline = gl.glGenMesh(TMesh.CIRCLE_OUTLINE.data);
        
        fontRect = gl.glGenMesh(TMesh.BOX.data);
        fontProgram = loadProgramFromSource(Presets.fontVSSource(version),Presets.fontFSSource(version));
        
        check = createCheck();
        
        solid = loadProgramFromSource(Presets.solidVSSource(version),Presets.solidFSSource(version));
        basic = loadProgramFromSource(Presets.basicVSSource(version),Presets.basicFSSource(version));
        
        textManager = new TTextManager(gl);
        imageManager = new TImageManager(gl);
    }
    
    public void setOrtho(Matrix4 ortho) {
        this.ortho = ortho;
    }
    
    int[] currentCut;
    
    public void beginGL() {
        gl.glDisable(GL.DEPTH_TEST);
        gl.glEnable(GL.SCISSOR_TEST);
        gl.glEnable(GL.BLEND);
        gl.glBlendFunc(GL.NORMAL);
    }
    
    public void endGL() {
        gl.glEnable(GL.DEPTH_TEST);
        gl.glDisable(GL.SCISSOR_TEST);
        gl.glDisable(GL.BLEND);
        gl.glBlendFunc(GL.NORMAL);
    }
    
    public void paint(TWidget w) {
        boolean firstCut = false;
        int[] box = getBox(w);
        if (currentCut==null) {
            firstCut = true;
            currentCut = box;
        } else {
            currentCut = cutOut(currentCut,box);
        }
        int[] cut = new int[] {currentCut[0], windowHeight.f()-currentCut[1]-currentCut[3], currentCut[2], currentCut[3]}; // top left orientation
        gl.glScissor(cut);
        float ox = offsetX;
        float oy = offsetY;
        offsetX = w.windowX();
        offsetY = w.windowY();
        paintWidget(w);
        offsetX = ox;
        offsetY = oy;
        if (w.hasChildren()) {
            // Flip render order so that children that receive mouse events first, will be rendered
            // last (so they are on top)
            ArrayList<TWidget> flip = List.flip(w.getChildren());
            for (TWidget child : flip) {
                int[] cc = ArrayUtils.copy(currentCut);
                paint(child);
                currentCut = cc;
            }
        }
        if (firstCut) {
            currentCut = null;
        }
    }
    
    public void renderLine(float x1, float y1, float x2, float y2, Color color, float thickness) {
        float[] verts = new float[] {x1,-y1,0,x2,-y2,0};
        //TODO have widgets create their own line geometries to render (would also have to clean up?)
        //instead of setting vertex attrib each frame?
        line.setVertexAttrib(verts,3);
        gl.glLineWidth(thickness);
        render(line,new Vector3(offsetX,offsetY,0), new Vector3(0,0,0), new Vector3(1,1,1), color.toVector(),GL.LINE_STRIP);
        gl.glLineWidth(1f);
    }
    
    public void renderCircle(float x, float y, float w, float h, Vector4 color) {
        render(circle,new Vector3(x+offsetX,y+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color);
    }
    
    public void renderCircle(Matrix4 mat, Vector4 color) {
        Matrix4 matrix = Matrix4.identity().translate(offsetX,offsetY,0).multiply(mat);
        render(circle,matrix,color);
    }
    
    public void renderCircleOutline(float x, float y, float w, float h, Vector4 color) {
        render(circleOutline,new Vector3(x+offsetX,y+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color,GL.LINE_STRIP);
    }
    
    public void renderRect(float x, float y, float w, float h, Vector4 color) {
        render(box,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color);
    }
    
    public void renderRect(float x, float y, float w, float h, Color color) {
        render(box,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color.toVector());
    }
    
    public void renderRect(Matrix4 mat, Vector4 color) {
        Matrix4 matrix = Matrix4.identity().translate(offsetX,offsetY,0).multiply(mat);
        render(box,matrix,color);
    }
    
    public void renderRectOutline(float x, float y, float w, float h, Vector4 color, float thickness) {
        gl.glLineWidth(thickness);
        render(boxOutline,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color,GL.LINE_STRIP);
        gl.glLineWidth(1f);
    }
    
    public void renderRectOutline(float x, float y, float w, float h, Color color, float thickness) {
        gl.glLineWidth(thickness);
        render(boxOutline,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color.toVector(),GL.LINE_STRIP);
        gl.glLineWidth(1f);
    }
    
    public void renderRectOutline(float x, float y, float w, float h, Vector4 color) {
        renderRectOutline(x,y,w,h,color,1);
    }
    
    public void renderRectOutline(float x, float y, float w, float h, Color color) {
        renderRectOutline(x,y,w,h,color,1);
    }
    
    public void renderText(String string, float x, float y, HAlign halign, VAlign valign,
            TFont font, Vector4 color) {
        renderText(string,x,y,halign,valign,font,Color.fromVector(color));
    }
    
    public void renderText(String string, float x, float y, HAlign halign, VAlign valign,
            TFont font, Color color) {
        if (string!="") {
            textManager.render(string, x+offsetX, y+offsetY, halign, valign, font, color, ortho, fontRect, fontProgram);
        }
    }
    
    public void renderImage(TImage image, float x, float y) {
        Texture texture = imageManager.getOrCreateTexture(image);
        renderTexture(texture,x,y,texture.getWidth(),texture.getHeight());
    }
    
    public void renderCheck(float x, float y) {
        renderTexture(check,x,y,check.getWidth(),check.getHeight());
    }
    
    public void renderTexture(Texture texture, float x, float y, float w, float h) {
        render(box,new Vector3(x+offsetX,y+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),texture);
    }
    
    // Top-left orientation
    
    private void render(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Vector4 color, GL.Mode mode) {
        Matrix4 mat = Matrix4.identity();
        mat.translate(trans);
        mat.rotate(rot);
        mat.scale(scale.x,-scale.y,scale.z);
        
        gl.glUseProgram(solid);
        solid.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        solid.setUniform("color",color.x,color.y,color.z,color.w);
        gl.glRender(mesh, mode);
        gl.glUseProgram(null);
    }
    private void render(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Vector4 color) {
        render(mesh,trans,rot,scale,color,GL.TRIANGLES);
    }
    
    private void render(Mesh mesh, Matrix4 mat, Vector4 color) {        
        gl.glUseProgram(solid);
        mat.scale(1, -1, 1);
        solid.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        solid.setUniform("color",color.x,color.y,color.z,color.w);
        gl.glRender(mesh);
        gl.glUseProgram(null);
    }
    
    private void render(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Texture texture) {
        Matrix4 mat = Matrix4.identity();
        mat.translate(trans);
        mat.rotate(rot);
        mat.scale(scale.x,-scale.y,scale.z);
        
        gl.glUseProgram(basic);
        basic.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        basic.setTexture("texture",texture,0);
        gl.glRender(mesh);
        gl.glUseProgram(null);
    }
    
    private Program loadProgramFromSource(String vertSource, String fragSource) {
        Program p = gl.glCreateProgram();
        
        Shader vert = gl.glCreateShader(GL.VERTEX_SHADER);
        Shader frag = gl.glCreateShader(GL.FRAGMENT_SHADER);
        
        vert.compileSource(vertSource);
        frag.compileSource(fragSource);
        
        p.attach(vert);
        p.attach(frag);
        p.link();
        
        return p;
    }
    
    public GL getGL() {
        return gl;
    }
    
    protected void paintWidget(TWidget w) {
        w.paint(this);
    }
    
    protected int[] widgetBox(TWidget w) {
        // TODO if scroll widget:
        //return new int[]{0,horBarHeight,getWidth()-verBarWidth,getHeight()};
        return new int[]{ 0, 0, w.width, w.height };
    }
    
    protected boolean isWithinBox(TWidget w, int x, int y) {
        int[] box = widgetBox(w);
        return (x>=box[0] && x<=box[2] && y>=box[1] && y<=box[3]);
    }
    
    protected int[] getBox(TWidget w) {
        int[] box = new int[]{w.windowX(),w.windowY(),w.width,w.height};
        TWidget c = w.parent();
        if (c!=null) {
            box = cutOut(box, ArrayUtils.add( widgetBox(c), new int[]{c.windowX(),c.windowY(),0,0} ));
        }
        return box;
    }
    
    protected int[] cutOut(int[] paper, int[] scissorBox) {
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
    
    Texture createCheck() {
        int[][] check = {
            {0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
            {0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,30},{0,0,0,62},{0,0,0,42},
            {0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,30},{0,0,0,126},{0,0,0,255},{0,0,0,84},
            {0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,94},{0,0,0,255},{0,0,0,158},{0,0,0,42},
            {0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,30},{0,0,0,158},{0,0,0,255},{0,0,0,94},{0,0,0,0},
            {0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,30},{0,0,0,126},{0,0,0,255},{0,0,0,126},{0,0,0,30},{0,0,0,0},
            {0,0,0,42},{0,0,0,62},{0,0,0,30},{0,0,0,0},{0,0,0,0},{0,0,0,30},{0,0,0,126},{0,0,0,255},{0,0,0,126},{0,0,0,30},{0,0,0,0},{0,0,0,0},
            {0,0,0,84},{0,0,0,255},{0,0,0,126},{0,0,0,30},{0,0,0,0},{0,0,0,94},{0,0,0,255},{0,0,0,158},{0,0,0,30},{0,0,0,0},{0,0,0,0},{0,0,0,0},
            {0,0,0,42},{0,0,0,126},{0,0,0,255},{0,0,0,126},{0,0,0,62},{0,0,0,158},{0,0,0,255},{0,0,0,94},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
            {0,0,0,0},{0,0,0,30},{0,0,0,126},{0,0,0,255},{0,0,0,190},{0,0,0,255},{0,0,0,126},{0,0,0,30},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
            {0,0,0,0},{0,0,0,0},{0,0,0,30},{0,0,0,126},{0,0,0,255},{0,0,0,126},{0,0,0,30},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
            {0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,42},{0,0,0,84},{0,0,0,42},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
        };
        BufferedImage data = ImageUtils.load(check,12,12);
        return gl.glGenTexture(data);
    }
    
}
