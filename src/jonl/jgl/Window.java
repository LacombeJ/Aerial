package jonl.jgl;

import jonl.jgl.Input.CursorState;

/**
 * @author Jonathan Lacombe
 */
public interface Window {

    /**
     * Sets the loader for this window whose method is called
     * when start is called and before the window is shown
     * @see #start()
     */
    public void setLoader(Loader loader);
    
    /**
     * Sets the runner for this window whose method is called
     * when start is called and after the window is shown
     * @see #start()
     */
    public void setRunner(Runner runner);
    
    /**
     * Sets the closer for this window whose method is called
     * after the window closes
     * @see #start()
     */
    public void setCloser(Closer closer);
    
    /**
     * Does the following in order:
     * <ol>
     * <li>Make the GL context</li>
     * <li>Call the loader</li>
     * <li>Show the window</li>
     * <li>Call the runner</li>
     * <li>Close the window</li>
     * <li>Call the closer</li>
     * <li>Terminate</li>
     * </ol>
     * @see #setLoader(Loader)
     * @see #setRunner(Runner)
     * @see #setCloser(Closer)
     */
    public void start();
    
    public void close();
    
    /**
     * Returns whether window is still running and calls
     * an update method to poll inputs and swap buffers.
     * <p>
     * @return true while window is still open
     */
    public boolean isRunning();
    
    public boolean isFullscreen();
    public boolean isResizable();
    public boolean isDecorated();
    public boolean isVisible();
    public void setVisible(boolean visible);
    
    public String getTitle();
    public void setTitle(String title); 
    
    public int getX();
    public int getY();
    public void setPosition(int x, int y);
    
    public int getWidth();
    public int getHeight();
    public void setSize(int width, int height);
    
    public int getScreenWidth();
    public int getScreenHeight();
    
    public Insets getInsets();
    
    public CursorState getCursorState();
    public void setCursorState(CursorState state);
    
    public Input getInput();
    public GraphicsLibrary getGraphicsLibrary();
    
    public void addSizeListener(Int2ChangedListener sl);
    public void removeSizeListener(Int2ChangedListener sl);
    
    public void addPositionListener(Int2ChangedListener pl);
    public void removePositionListener(Int2ChangedListener pl);
    
    public interface Int2ChangedListener {
        public void valueChanged(int x, int y, int x2, int y2);
    }
    
    public interface Float2ChangedListener {
        public void valueChanged(float x, float y, float x2, float y2);
    }
    
    public class Insets {
        public final int top;
        public final int bottom;
        public final int left;
        public final int right;
        public Insets(int top, int bottom, int left, int right) {
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }
        @Override
        public String toString() {
            return "Insets["+top+","+bottom+","+left+","+right+"]";
        }
        @Override
        public boolean equals(Object o) {
            if (o instanceof Insets) {
                Insets i = (Insets) o;
                return top==i.top && bottom==i.bottom &&
                        left==i.left && right==i.right;
            }
            return false;
        }
    }
    
}
