package jonl.jgl;

import java.awt.image.BufferedImage;

import jonl.jgl.Input.CursorState;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;

/**
 * @author Jonathan Lacombe
 */
public interface Window {

    // ------------------------------------------------------------------------
    
    // Attributes
    
    /** indicates whether the specified window has input focus */
    public static final int FOCUSED = 0;
    
    /** indicates whether the specified window is iconified */
    public static final int ICONIFIED = 1;
    
    /** indicates whether the specified window is maximized */
    public static final int MAXIMIZED = 2;
    
    /** indicates whether the specified window is visible */
    public static final int VISIBLE = 3;
    
    /** indicates whether the specified window is resizable by the user */
    public static final int RESIZABLE = 4;
    
    /** indicates whether the specified window has decorations such as a border, a close widget, etc */
    public static final int DECORATED = 5;
    
    /** indicates whether the specified window is floating, also called topmost or always-on-top */
    public static final int FLOATING = 6;
    
    /** indicates whether the mouse is hovered over the window */
    public static final int HOVERED = 7;
    
    // ------------------------------------------------------------------------
    
    // Standard Cursors
    
    /** the default arrow cursor */
    public final static int ARROW_CURSOR = 0;
    
    public final static int IBEAM_CURSOR = 1;
    
    public final static int CROSSHAIR_CURSOR = 2;
    
    public final static int HAND_CURSOR = 3;
    
    public final static int HRESIZE_CURSOR = 4;
    
    public final static int VRESIZE_CURSOR = 5;
    
    // ------------------------------------------------------------------------
    
    // Resolution type
    
    /** Using passed in window size for the resolution */
    public final static int WINDOW = 0;
    
    /** Using the size of the monitor for a fullscreen resolution (100%) */
    public final static int MONITOR = 1;
    
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
    
    // ------------------------------------------------------------------------
    
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
    
    /** Closes this window */
    public void close();
    
    /** Calls this callback on the window thread */
    public void call(Callback0D call);
    
    // ------------------------------------------------------------------------
    
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
    
    public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight);
    
    public int getScreenWidth();
    public int getScreenHeight();
    
    public void maximize();
    public void minimize();
    public void restore();
    
    public void setIcon(BufferedImage image);
    
    public boolean getAttribute(int attribute);
    
    public Insets getInsets();
    
    public CursorState getCursorState();
    public void setCursorState(CursorState state);
    
    public void setCursor(int cursorId);
    
    public Input getInput();
    public GL getGraphicsLibrary();
    
    public void addSizeListener(Int2ChangedListener sl);
    public void removeSizeListener(Int2ChangedListener sl);
    
    public void addPositionListener(Int2ChangedListener pl);
    public void removePositionListener(Int2ChangedListener pl);
    
    public void addCursorListener(Callback<Boolean> cl);
    public void removeCursorListener(Callback<Boolean> cl);
    
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
