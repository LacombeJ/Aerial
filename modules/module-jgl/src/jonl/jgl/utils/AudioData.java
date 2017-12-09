package jonl.jgl.utils;

import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;

public class AudioData {
    
    final static int MEAN_CHUNK_LENGTH = 441; //samples in each mean chunk
    final static int CHUNK_LENGTH = 10; //seconds in each buffer chunk

    int channels;
    int sampleRate;
    
    ShortBuffer buffer;
    ShortBuffer[] chunks;
    int index; //chunk index
    
    int maxMean;
    int[] mean; //similar calculation to root mean squared but uses abs value
    
    /**
     * @param buffer buffer to be associated with this data
     * @param channels number of channels in this audio (1==mono)
     * @param sampleRate sample rate (samples per second)
     * @param stream if audio data should be a stream if the audio data is not too small
     */
    public AudioData(ShortBuffer buffer, int channels, int sampleRate, boolean mono, boolean stream) {
        this.channels = channels;
        this.sampleRate = sampleRate;
        setBuffer(buffer,mono);
        setMean();
        setChunks(stream);
    }
    
    public AudioData(ShortBuffer buffer, int channels, int sampleRate) {
        this(buffer,channels,sampleRate,false,false);
    }
    
    /**
     * 
     * @param data audio data to copy
     * @param mono
     * @param stream
     */
    public AudioData(RawAudio data, boolean mono, boolean stream) {
        this(data.buffer,data.channels,data.sampleRate,mono,stream);
    }
    
    public AudioData(RawAudio data, boolean mono) {
        this(data,mono,false);
    }
    
    public AudioData(RawAudio data) {
        this(data,false);
    }
    
    private void setBuffer(ShortBuffer sb, boolean mono) {
        //If audio is already a mono - copy buffer
        if (channels==1 || !mono) {
            
            buffer = BufferUtils.createShortBuffer(sb.capacity());
            
            buffer.put(sb);
            buffer.flip();
            sb.flip(); //must flip because put(buffer) increments the buffer
            
            buffer.limit(sb.limit());
            
        //Else if audio is not a mono - get average sample into a buffer (set to mono)
        } else {
            
            int limit = sb.limit();
            int capacity = limit/channels;
            
            buffer = BufferUtils.createShortBuffer(capacity);
            
            for (int i=0; i<capacity; i++) {
                int sum = 0;
                for (int j=0; j<channels; j++) {
                    sum += sb.get();
                }
                sum /= channels;
                buffer.put((short)sum);
            }
            
            buffer.flip();
            buffer.limit(capacity);
            
            sb.flip();
            sb.limit(limit);
            
            channels = 1;
            
        }
    }
    
    private void setMean() {
        int limit = buffer.limit();
        mean = new int[limit/MEAN_CHUNK_LENGTH];
        for (int i=0; i<mean.length; i++) {
            int sum = 0;
            for (int j=0; j<MEAN_CHUNK_LENGTH; j++) {
                int val = buffer.get();
                sum += Math.abs(val);
            }
            mean[i] = (int) sum/MEAN_CHUNK_LENGTH;
            maxMean = Math.max(maxMean,mean[i]);
        }
        buffer.flip();
        buffer.limit(limit);
    }
    
    private void setChunks(boolean stream) {
        if (stream) {
            int limit = buffer.limit();
            int chunkSize = sampleRate*CHUNK_LENGTH;
            chunks = new ShortBuffer[limit/chunkSize];
            for (int i=0; i<chunks.length; i++) {
                chunks[i] = BufferUtils.createShortBuffer(chunkSize);
                for (int j=0; j<chunkSize; j++) {
                    chunks[i].put( buffer.get() );
                }
                chunks[i].flip();
                chunks[i].limit(chunkSize);
            }
            buffer.flip();
            buffer.limit(limit);
        } else {
            chunks = new ShortBuffer[]{buffer};
        }
    }
    
    public ShortBuffer getBuffer() {
        return buffer;
    }
    
    public int getChannels() {
        return channels;
    }
    
    public int getSampleRate() {
        return sampleRate;
    }
    
    public int getChunkIndex() {
        return index;
    }
    
    public ShortBuffer getChunkAt(int i) {
        return chunks[i];
    }
    
    public ShortBuffer getChunk() {
        return chunks[index];
    }
    
    public ShortBuffer nextChunk() {
        return chunks[index++];
    }
    
    public boolean hasChunk() {
        return index<chunks.length;
    }
    
    public void resetChunks() {
        index = 0;
    }
    
    public int getChunks() {
        return chunks.length;
    }
    
    public boolean isMono() {
        return channels==1;
    }
    
    public boolean isAudioStream() {
        return chunks.length<2;
    }
    
    public int getMeanAmplitude(int chunkIndex, int index) {
        int trueIndex = chunks[0].capacity()*chunkIndex + index;
        int rmsIndex = trueIndex/MEAN_CHUNK_LENGTH;
        return mean[rmsIndex];
    }
    
    public int getMaxMean() {
        return maxMean;
    }
    
}
