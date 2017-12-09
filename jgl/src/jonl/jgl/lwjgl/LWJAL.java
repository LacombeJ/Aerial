package jonl.jgl.lwjgl;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL10;

import jonl.jgl.AudioBuffer;
import jonl.jgl.AudioLibrary;
import jonl.jgl.AudioSource;
import jonl.jgl.utils.AudioData;
import jonl.jgl.utils.RawAudio;

class LWJAL implements AudioLibrary {

    @Override
    public String alGetError() {
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR)
            return AL10.alGetString(error);
        return null;
    }
    
    @Override
    public AudioBuffer alGenBuffer(ShortBuffer buffer, int channels, int sampleRate) {
        return new LWJALBuffer(buffer,channels,sampleRate);
    }
    
    @Override
    public AudioBuffer alGenBuffer(AudioData audio) {
        return new LWJALBuffer(audio.getBuffer(),audio.getChannels(),audio.getSampleRate());
    }
    
    @Override
    public AudioBuffer alGenBuffer(String file) {
        RawAudio audio = AudioUtils.readVorbis(file);
        return alGenBuffer(new AudioData(audio));
    }
    
    @Override
    public AudioSource alGenSource() {
        return new LWJALSource();
    }
    
    
    
    @Override
    public float[] alGetPosition() {
        float[] p = new float[3];
        AL10.alGetListenerfv(AL10.AL_POSITION,p);
        return p;
    }

    @Override
    public void alPosition(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION,x,y,z);
    }

    @Override
    public float[] alGetVelocity() {
        float[] p = new float[3];
        AL10.alGetListenerfv(AL10.AL_VELOCITY,p);
        return p;
    }

    @Override
    public void alVelocity(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_VELOCITY,x,y,z);
    }

    @Override
    public float alGetGain() {
        return AL10.alGetListenerf(AL10.AL_GAIN);
    }

    @Override
    public void alGain(float gain) {
        AL10.alListenerf(AL10.AL_GAIN,gain);
    }

    @Override
    public float[] alGetOrientation() {
        float[] p = new float[6];
        AL10.alGetListenerfv(AL10.AL_ORIENTATION,p);
        return p;
    }

    @Override
    public void alOrientation(float lookX, float lookY, float lookZ, float upX, float upY, float upZ) {
        float[] orientation = new float[]{lookX,lookY,lookZ,upX,upY,upZ};
        AL10.alListenerfv(AL10.AL_ORIENTATION,orientation);
    }
    


    

    
    
}
