package jonl.jutils.parallel;

import jonl.jutils.time.StopWatch;
import jonl.jutils.time.TimeMetric;
import jonl.jutils.time.TimeUtils;

/**
 * Use to check if another thread has timed out
 * @author Jonathan
 *
 */
public class TimeOut {

	/** When set to true, thread has failed to have timed out and will continue */
	private boolean safe = false;
	
	StopWatch watch;
	private double seconds = 0;
	
	public TimeOut(double seconds) {
		watch = new StopWatch();
		this.seconds = seconds;
	}
	
	/**
	 * Released hold to return true / succeed
	 */
	public void pass() {
		safe = true;
	}
	
	/**
	 * Holds thread until given time has elapsed or until pass is called
	 * @param seconds time in seconds
	 * @return false if there was a time out or true if pass was called by another thread
	 */
	public boolean hold() {
		while (!safe) {
			double elapsed = TimeUtils.timeConvert(watch.lap(), TimeMetric.NANO, TimeMetric.SECOND);
			if (elapsed > seconds) {
				return false; // Time out detected
			}
		}
		return true; // Time out failed, pass was called
	}
	
	/**
	 * Similar to hold but will not stop thread
	 * @return false if there was a time out or true if time not reached yet
	 */
	public boolean check() {
		if (!safe) {
			double elapsed = TimeUtils.timeConvert(watch.lap(), TimeMetric.NANO, TimeMetric.SECOND);
			if (elapsed > seconds) {
				return false; // Time out detected
			}
		}
		return true;
	}
	
}
