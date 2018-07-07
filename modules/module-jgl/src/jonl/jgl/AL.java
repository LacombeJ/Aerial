package jonl.jgl;

import java.nio.ShortBuffer;

import jonl.jgl.utils.AudioData;

/**
 * Audio Library
 * 
 * @author Jonathan
 *
 */
public interface AL {

    /** @return string associated with an error or null if no error was found */
    public String alGetError();
    
    public AudioBuffer alGenBuffer(ShortBuffer buffer, int channels, int sampleRate);
    
    public AudioBuffer alGenBuffer(String file);
    
    public AudioBuffer alGenBuffer(AudioData audio);
    
    public AudioSource alGenSource();
    
    public float[] alGetPosition();
    
    public void alPosition(float x, float y, float z);
    
    public float[] alGetVelocity();
    
    public void alVelocity(float x, float y, float z);
    
    public float[] alGetOrientation();
    
    public void alOrientation(float lookX, float lookY, float lookZ, float upX, float upY, float upZ);
    
    public float alGetGain();
    
    public void alGain(float gain);
    
}
