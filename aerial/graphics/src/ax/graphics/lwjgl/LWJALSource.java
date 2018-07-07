package ax.graphics.lwjgl;

import java.util.HashMap;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import ax.graphics.AudioBuffer;
import ax.graphics.AudioSource;

class LWJALSource implements AudioSource {

    final int id;
    
    final HashMap<Integer,AudioBuffer> queued;
    
    LWJALSource() {
        id = AL10.alGenSources();
        queued = new HashMap<>();
    }
    
    @Override
    public void source(AudioBuffer buffer) {
        AL10.alSourcei(id, AL10.AL_BUFFER, ((LWJALBuffer)buffer).id);
    }
    
    @Override
    public void sourceQueue(AudioBuffer buffer) {
        AL10.alSourceQueueBuffers(id, ((LWJALBuffer)buffer).id);
        queued.put(((LWJALBuffer)buffer).id,buffer);
    }
    
    @Override
    public AudioBuffer sourceUnqueue() {
        int unqueued = AL10.alSourceUnqueueBuffers(id);
        return queued.remove(unqueued);
    }
    
    @Override
    public int getProcessed() {
        return AL10.alGetSourcei(id,AL10.AL_BUFFERS_PROCESSED);
    }
    
    @Override
    public float getGain() {
        return AL10.alGetSourcef(id,AL10.AL_GAIN);
    }

    @Override
    public void setGain(float gain) {
        AL10.alSourcef(id,AL10.AL_GAIN,gain);
    }
    
    @Override
    public float getPitch() {
        return AL10.alGetSourcef(id,AL10.AL_PITCH);
    }

    @Override
    public void setPitch(float pitch) {
        AL10.alSourcef(id,AL10.AL_PITCH,pitch);
    }

    @Override
    public void setLooping(boolean looping) {
        AL10.alSourcei(id, AL10.AL_LOOPING, (looping) ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    @Override
    public boolean isLooping() {
        return AL10.alGetSourcei(id,AL10.AL_LOOPING) == AL10.AL_TRUE;
    }
    
    @Override
    public float getSecOffset() {
        return AL10.alGetSourcef(id,AL11.AL_SEC_OFFSET);
    }

    @Override
    public float getByteOffset() {
        return AL10.alGetSourcef(id,AL11.AL_BYTE_OFFSET);
    }
    
    @Override
    public float[] getPosition() {
        float[] p = new float[3];
        AL10.alGetSourcefv(id,AL10.AL_POSITION,p);
        return p;
    }

    @Override
    public void setPosition(float x, float y, float z) {
        AL10.alSource3f(id,AL10.AL_POSITION,x,y,z);
    }

    @Override
    public float[] getVelocity() {
        float[] v = new float[3];
        AL10.alGetSourcefv(id,AL10.AL_VELOCITY,v);
        return v;
    }
    
    @Override
    public void setVelocity(float x, float y, float z) {
        AL10.alSource3f(id,AL10.AL_VELOCITY,x,y,z);
    }

    @Override
    public void play() {
        AL10.alSourcePlay(id);
    }

    @Override
    public void pause() {
        AL10.alSourcePause(id);
    }

    @Override
    public void resume() {
        if (!isPlaying()) {
            play();
        }
    }

    @Override
    public void stop() {
        AL10.alSourceStop(id);
    }
    
    @Override
    public boolean isPlaying() {
        return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }
    
    @Override
    public boolean isPaused() {
        return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_PAUSED;
    }

    @Override
    public void delete() {
        AL10.alDeleteSources(id);
    }
    
    
    
}
