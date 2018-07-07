package jonl.jgl.lwjgl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;

import jonl.jgl.AudioDevice;
import jonl.jgl.AL;

public class ALDevice implements AudioDevice {
    
    private final long device;
    
    private final LWJAL al;
    
    private long context;
    
    public ALDevice() {
        device = ALC10.alcOpenDevice((ByteBuffer)null);
        
        if ( device == MemoryUtil.NULL )
            throw new IllegalStateException("Failed to open the default audio device.");
        
        al = new LWJAL();
    }
    
    @Override
    public void create() {
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = ALC10.alcCreateContext(device, (IntBuffer)null);
        
        ALC10.alcMakeContextCurrent(context);
        org.lwjgl.openal.AL.createCapabilities(deviceCaps);
    }
    
    @Override
    public void destroy() {
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }

    @Override
    public AL getAudioLibrary() {
        return al;
    }

    
    
}
