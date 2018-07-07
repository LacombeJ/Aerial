package ax.commons.func;

import ax.commons.misc.ArrayUtils;

public class Tuple2<X,Y> {

	public final X x;
	public final Y y;
	public Tuple2(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

    public static void rip(Tuple2<String, String>[] list, String[] xdest, String[] ydest) {
        String[] xd = new String[list.length];
        String[] yd = new String[list.length];
        for (int i=0; i<list.length; i++) {
            xd[i] = list[i].x;
            yd[i] = list[i].y;
        }
        ArrayUtils.copy(xd, xdest);
        ArrayUtils.copy(yd, ydest);
    }
	
}
