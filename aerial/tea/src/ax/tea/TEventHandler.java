package ax.tea;

import ax.commons.time.Time;

public class TEventHandler {

    boolean inClickState = false;
    Time timeSinceLastClick = new Time();
    boolean mouseFocusSupport = false;
    boolean mouseMotionBounds = false;
    boolean keyFocusSupport = false;
    
}
