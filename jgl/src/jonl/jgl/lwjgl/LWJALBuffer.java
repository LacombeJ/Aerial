package jonl.jgl.lwjgl;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL10;

import jonl.jgl.AudioBuffer;

class LWJALBuffer implements AudioBuffer {

    final int id;
    final boolean mono;
    
    LWJALBuffer(ShortBuffer sb, int channels, int sampleRate) {
        id = AL10.alGenBuffers();
        
        mono = channels == 1;
        
        AL10.alBufferData(id, mono ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16, sb, sampleRate);
    }

    @Override
    public boolean isMono() {
        return mono;
    }

    @Override
    public void delete() {
        AL10.alDeleteBuffers(id);
    }
    
}
