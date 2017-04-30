package jonl.jgl.utils;

import java.nio.ShortBuffer;

public class RawAudio {
    public ShortBuffer buffer;
    public int channels;
    public int sampleRate;
    public RawAudio(ShortBuffer buffer, int channels, int sampleRate) {
        this.buffer = buffer;
        this.channels = channels;
        this.sampleRate = sampleRate;
    }
}
