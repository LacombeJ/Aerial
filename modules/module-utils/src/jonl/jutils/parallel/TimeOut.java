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
	
	public TimeOut() {
		
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
	 * @return true if pass was called by another thread or false if time out duration is reached
	 */
	public boolean hold(double seconds) {
		StopWatch watch = new StopWatch();
		while (!safe) {
			long elapsed = (long) TimeUtils.timeConvert(watch.lap(), TimeMetric.NANO, TimeMetric.SECOND);
			if (elapsed > seconds) {
				return false; // Time out detected
			}
		}
		return true; // Time out failed, pass was called
	}
	
}
