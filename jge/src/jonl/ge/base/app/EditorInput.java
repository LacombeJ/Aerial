package jonl.ge.base.app;

import jonl.aui.Widget;
import jonl.ge.core.Input;
import jonl.ge.core.InputEvent;
import jonl.vmath.Vector2;

public class EditorInput implements Input {
    
    final jonl.jgl.Input input;
    
    final Widget box;
    
    public EditorInput(Widget box, jonl.jgl.Input input) {
        this.box = box;
        this.input = input;
    }
    
    boolean isWithin(float x, float y) {
        return (x>=0 && x<=box.getWidth() && y>=0 && y<=box.getHeight());
    }
    
    boolean isMouseWithin() {
        return isWithin(getX(),getY());
    }
    
    boolean glCheckEvent(InputEvent e) {
        return input.checkEvent(new jonl.jgl.InputEvent(e.type,e.code));
    }

    @Override
    public boolean checkEvent(InputEvent e) {
        return isMouseWithin() && glCheckEvent(e);
    }

    @Override
    public boolean isKeyDown(int key) {
        return input.isKeyDown(key);
    }

    @Override
    public boolean isKeyPressed(int key) {
        return input.isKeyPressed(key);
    }

    @Override
    public boolean isKeyReleased(int key) {
        return input.isKeyReleased(key);
    }

    @Override
    public float getX() {
        return input.getX() - box.getWindowX();
    }

    @Override
    public float getY() {
        return input.getY() - box.getWindowY();
    }
    
    @Override
    public Vector2 getXY() {
        return new Vector2(getX(),getY());
    }

    @Override
    public float getDX() {
        return input.getDX();
    }

    @Override
    public float getDY() {
        return input.getDY();
    }

    @Override
    public Vector2 getDXY() {
        return new Vector2(getDX(),getDY());
    }
    
    @Override
    public float getScrollX() {
        return input.getScrollX();
    }

    @Override
    public float getScrollY() {
        return input.getScrollY();
    }
    
    @Override
    public Vector2 getScrollXY() {
        return new Vector2(getScrollX(),getScrollY());
    }

    @Override
    public boolean isButtonDown(int mbutton) {
        return isMouseWithin() && input.isButtonDown(mbutton);
    }

    @Override
    public boolean isButtonPressed(int mbutton) {
        return isMouseWithin() && input.isButtonPressed(mbutton);
    }

    @Override
    public boolean isButtonReleased(int mbutton) {
        return isMouseWithin() && input.isButtonReleased(mbutton);
    }
    
    
    boolean glIsButtonDown(int mbutton) {
        return input.isButtonDown(mbutton);
    }

    
    boolean glIsButtonPressed(int mbutton) {
        return input.isButtonPressed(mbutton);
    }

    
    boolean glIsButtonReleased(int mbutton) {
        return input.isButtonReleased(mbutton);
    }

}
