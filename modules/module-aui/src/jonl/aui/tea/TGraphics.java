package jonl.aui.tea;

import jonl.jutils.func.Function0D;
import jonl.jutils.func.List;
import jonl.jutils.jss.Style;
import jonl.jutils.misc.ArrayUtils;
import jonl.vmath.Color;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

import java.util.ArrayList;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.graphics.TFont;
import jonl.aui.tea.graphics.TImage;
import jonl.aui.tea.graphics.TImageManager;
import jonl.aui.tea.graphics.TMesh;
import jonl.aui.tea.graphics.TMeshBox;
import jonl.aui.tea.graphics.TShader;
import jonl.aui.tea.graphics.TTextManager;
import jonl.jgl.GL;
import jonl.jgl.Mesh;
import jonl.jgl.Program;
import jonl.jgl.Shader;
import jonl.jgl.Texture;

public class TGraphics implements Graphics {

    TUIManager ui;
    GL gl;
    
    Function0D<Integer> windowHeight;
    
    TTextManager textManager;
    TImageManager imageManager;
    
    float offsetX;
    float offsetY;
    
    Matrix4 ortho;
    
    Mesh line;
    
    Mesh newBox;
    
    Mesh box;
    Mesh circle;
    
    Mesh boxOutline;
    Mesh circleOutline;
    
    Mesh fontRect;
    Program fontProgram;
    
    Program solid;
    Program textureProgram;
    Program gradient;
    Program boxProgram;
    
    Style cascade;
    
    public TGraphics(GL gl, Function0D<Integer> windowHeight) {
        this.gl = gl;
        this.windowHeight = windowHeight;
        ui = TUIManager.instance();
        
        int version = gl.glGetGLSLVersioni();
        
        line = gl.glGenMesh(TMesh.LINE.data);
        
        newBox = TMeshBox.createBoxMesh(gl,1,1,0.5f);
        
        box = gl.glGenMesh(TMesh.BOX.data);
        circle = gl.glGenMesh(TMesh.CIRCLE.data);
        
        boxOutline = gl.glGenMesh(TMesh.BOX_OUTLINE.data);
        circleOutline = gl.glGenMesh(TMesh.CIRCLE_OUTLINE.data);
        
        fontRect = gl.glGenMesh(TMesh.BOX.data);
        fontProgram = loadProgramFromSource(TShader.fontVSSource(version),TShader.fontFSSource(version));
        
        solid = loadProgramFromSource(TShader.solidVSSource(version),TShader.solidFSSource(version));
        textureProgram = loadProgramFromSource(TShader.textureVSSource(version),TShader.textureFSSource(version));
        gradient = loadProgramFromSource(TShader.gradientVSSource(version),TShader.gradientFSSource(version));
        boxProgram = loadProgramFromSource(TShader.boxVertex(version),TShader.boxFragment(version));
        
        textManager = new TTextManager(gl);
        imageManager = new TImageManager(gl);
    }
    
    public void setOrtho(Matrix4 ortho) {
        this.ortho = ortho;
    }
    
    public Style style() {
        return cascade;
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
            cascade = TUIManager.instance().styleSheet().copy();
        } else {
            currentCut = cutOut(currentCut,box);
        }
        int[] cut = new int[] {currentCut[0], windowHeight.f()-currentCut[1]-currentCut[3], currentCut[2], currentCut[3]}; // top left orientation
        gl.glScissor(cut);
        float ox = offsetX;
        float oy = offsetY;
        offsetX = w.windowX();
        offsetY = w.windowY();
        
        Style css = cascade.copy();
        cascade = w.cascade(cascade);
        paintWidget(w);
        cascade = css;
        
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
            cascade = null;
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
    
    public void renderBox(float x, float y, float w, float h, float border, float radius,
            Color color00, Color color01, Color color10, Color color11,
            Color borderColor00, Color borderColor01, Color borderColor10, Color borderColor11) {
        renderBox(x+w/2+offsetX,y+h/2+offsetY,w,h,border,radius,
                color00.toVector(),color01.toVector(),color10.toVector(),color11.toVector(),
                borderColor00.toVector(),borderColor01.toVector(),borderColor10.toVector(),borderColor11.toVector());
    }
    
    public void renderBox(float x, float y, float w, float h, float border, float radius, Color colorBot, Color colorTop, Color borderColorBot, Color borderColorTop) {
        renderBox(x,y,w,h,border,radius,colorBot,colorBot,colorTop,colorTop,borderColorBot,borderColorBot,borderColorTop,borderColorTop);
    }
    
    public void renderBox(float x, float y, float w, float h, float border, float radius, Color color, Color borderColor) {
        renderBox(x,y,w,h,border,radius,color,color,borderColor,borderColor);
    }
    
    public void renderBox(float x, float y, float w, float h, Color colorBot, Color colorTop) {
        renderBox(x,y,w,h,0,0,colorBot,colorBot,colorTop,colorTop);
    }
    
    public void renderBox(float x, float y, float w, float h, Color color) {
        renderBox(x,y,w,h,color,Color.TRANSPARENT);
    }
    
    public void renderRect(float x, float y, float w, float h, Vector4 color) {
        render(box,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color);
    }
    
    public void renderRect(float x, float y, float w, float h, Color color) {
        render(box,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color.toVector());
    }
    
    public void renderRect(float x, float y, float w, float h, Color color, float radius) {
        TMeshBox.adjust(newBox,w,h,radius);
        render(newBox,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),color.toVector());
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
    
    public void renderGradient(float x, float y, float w, float h, Color botLeft, Color botRight, Color topLeft, Color topRight) {
        renderGradient(box,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(w,h,1),botLeft.toVector(),botRight.toVector(),topLeft.toVector(),topRight.toVector());
    }
    
    public void renderGradient(float x, float y, float w, float h, Color bot, Color top) {
        renderGradient(x,y,w,h,bot,bot,top,top);
    }
    
    public void renderGradient(float x, float y, float w, float h, Color bot, Color top, float radius) {
        renderGradient(x,y,w,h,bot,bot,top,top,radius);
    }
    
    public void renderGradient(float x, float y, float w, float h, Color botLeft, Color botRight, Color topLeft, Color topRight, float radius) {
        TMeshBox.adjust(newBox,w,h,radius);
        renderGradient(newBox,new Vector3(x+w/2+offsetX,y+h/2+offsetY,0),new Vector3(0,0,0),new Vector3(1,1,1),botLeft.toVector(),botRight.toVector(),topLeft.toVector(),topRight.toVector());
    }
    
    public void renderText(String string, float x, float y, HAlign halign, VAlign valign,
            TFont font, Vector4 color) {
        renderText(string,x,y,halign,valign,font,Color.fromVector(color));
    }
    
    public void renderText(String string, float x, float y, HAlign halign, VAlign valign,
            TFont font, Color color) {
        if (!string.equals("")) {
            textManager.render(string, x+offsetX, y+offsetY, halign, valign, font, color, ortho, fontRect, fontProgram);
        }
    }
    
    public void renderImage(TImage image, float x, float y, Color color) {
        Texture texture = imageManager.getOrCreateTexture(image);
        renderTexture(texture,x,y,texture.getWidth(),texture.getHeight(),color.toVector());
    }
    
    public void renderImage(TImage image, float x, float y, float rot, Color color) {
        Texture texture = imageManager.getOrCreateTexture(image);
        renderTexture(texture,x,y,texture.getWidth(),texture.getHeight(),rot,color.toVector());
    }
    
    public void renderImage(TImage image, float x, float y) {
        renderImage(image,x,y,Color.WHITE);
    }
    
    public void renderCheck(float x, float y, Color color) {
        
        renderImage(ui.resource("ui/check").icon(),x,y,color);
    }
    
    public void renderCheck(float x, float y) {
        renderImage(ui.resource("ui/check").icon(),x,y);
    }
    
    public void renderCaret(float x, float y, Color color) {
        renderImage(ui.resource("ui/caret").icon(),x,y);
    }
    
    public void renderCaret(float x, float y) {
        renderImage(ui.resource("ui/caret").icon(),x,y);
    }
    
    public void renderTexture(Texture texture, float x, float y, float w, float h, Vector4 color) {
        renderTexture(texture,x,y,w,h,0,color);
    }
    
    public void renderTexture(Texture texture, float x, float y, float w, float h, float rot, Vector4 color) {
        render(box,new Vector3(x+offsetX,y+offsetY,0),new Vector3(0,0,rot),new Vector3(w,h,1),texture,color);
    }
    
    // Top-left orientation
    
    private void renderGradient(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Vector4 botLeft, Vector4 botRight, Vector4 topLeft, Vector4 topRight, GL.Mode mode) {
        Matrix4 mat = Matrix4.identity();
        mat.translate(trans);
        mat.rotate(rot);
        mat.scale(scale.x,-scale.y,scale.z);
        
        gl.glUseProgram(gradient);
        gradient.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        gradient.setUniform("color00",botLeft.x, botLeft.y, botLeft.z, botLeft.w);
        gradient.setUniform("color10",botRight.x, botRight.y, botRight.z, botRight.w);
        gradient.setUniform("color01",topLeft.x, topLeft.y, topLeft.z, topLeft.w);
        gradient.setUniform("color11",topRight.x, topRight.y, topRight.z, topRight.w);
        gl.glRender(mesh, mode);
        gl.glUseProgram(null);
    }
    
    private void renderGradient(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Vector4 botLeft, Vector4 botRight, Vector4 topLeft, Vector4 topRight) {
        renderGradient(mesh,trans,rot,scale,botLeft,botRight,topLeft,topRight,GL.TRIANGLES);
    }
    
    private void renderBox(float x, float y, float width, float height, float border, float radius,
            Vector4 c00,
            Vector4 c01,
            Vector4 c10,
            Vector4 c11,
            Vector4 bc00,
            Vector4 bc01,
            Vector4 bc10,
            Vector4 bc11) {
        Matrix4 mat = Matrix4.identity();
        mat.translate(x,y,0);
        mat.scale(width,height,1);
        
        Matrix4 B = Matrix4.identity();
        B.translate(width/2,height/2,0);
        B.scale(width,height,1);
        
        gl.glUseProgram(boxProgram);
        boxProgram.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        boxProgram.setUniformMat4("B",B.toArray());
        boxProgram.setUniform("width",width);
        boxProgram.setUniform("height",height);
        boxProgram.setUniform("border",border);
        boxProgram.setUniform("radius",radius);
        boxProgram.setUniform("c00",c00.x,c00.y,c00.z,c00.w); //bottom-left
        boxProgram.setUniform("c01",c01.x,c01.y,c01.z,c01.w); //bottom-right
        boxProgram.setUniform("c10",c10.x,c10.y,c10.z,c10.w); //top-left
        boxProgram.setUniform("c11",c11.x,c11.y,c11.z,c11.w); //top-right
        boxProgram.setUniform("bc00",bc00.x,bc00.y,bc00.z,bc00.w); //border bottom-left
        boxProgram.setUniform("bc01",bc01.x,bc01.y,bc01.z,bc01.w); //border bottom-right
        boxProgram.setUniform("bc10",bc10.x,bc10.y,bc10.z,bc10.w); //border top-left
        boxProgram.setUniform("bc11",bc11.x,bc11.y,bc11.z,bc11.w); //border top-right
        gl.glRender(box, GL.TRIANGLES);
        gl.glUseProgram(null);
    }
    
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
    
    private void render(Mesh mesh, Vector3 trans, Vector3 rot, Vector3 scale, Texture texture, Vector4 color) {
        Matrix4 mat = Matrix4.identity();
        mat.translate(trans);
        mat.rotate(rot);
        mat.scale(scale.x,-scale.y,scale.z);
        
        gl.glUseProgram(textureProgram);
        textureProgram.setUniformMat4("MVP",ortho.get().multiply(mat).toArray());
        textureProgram.setUniform("color",color.x,color.y,color.z,color.w);
        textureProgram.setTexture("texture",texture,0);
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
    
}
