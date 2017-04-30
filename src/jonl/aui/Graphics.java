package jonl.aui;

import jonl.vmath.Matrix4;
import jonl.vmath.Vector4;

public interface Graphics {

    void renderRect(float x, float y, float width, float height, Vector4 color);

    void renderText(String text, float x, float y, HAlign center, VAlign middle, Font font, Vector4 vector4);

    void renderCircle(float f, float g, float dim, float dim2, Vector4 buttonColorHover);

    void renderRect(Matrix4 mat, Vector4 buttonColor);
    
}
