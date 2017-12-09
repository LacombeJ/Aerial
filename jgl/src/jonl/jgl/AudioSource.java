package jonl.jgl;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public interface AudioSource {
    
    public void source(AudioBuffer buffer);
    
    public void sourceQueue(AudioBuffer buffer);
    
    /** @return unqueued buffered */
    public AudioBuffer sourceUnqueue();
    
    /**
     * This number goes up when a buffer is queued and down when unqueued
     * @return number of processed buffers
     */
    public int getProcessed();
    
    public float getGain();
    
    public void setGain(float gain);
    
    public float getPitch();
    
    public void setPitch(float pitch);
    
    public void setLooping(boolean looping);
    
    public boolean isLooping();

    public float getSecOffset();
    
    public float getByteOffset();
    
    public float[] getPosition();
    
    public void setPosition(float x, float y, float z);
    
    public float[] getVelocity();
    
    public void setVelocity(float x, float y, float z);
    
    /** Plays audio or restart if it is already playing */
    public void play();
    
    /** Pauses audio if it is playing */
    public void pause();
    
    /** Plays audio if it isn't playing already */
    public void resume();
    
    /** Stops audio if it is playing */
    public void stop();
    
    public boolean isPlaying();
    
    public boolean isPaused();
    
    public void delete();
    
}
