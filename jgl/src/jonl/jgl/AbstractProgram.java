package jonl.jgl;

import java.nio.FloatBuffer;

import jonl.jutils.misc.BufferPool;

public abstract class AbstractProgram implements Program {
    
    @Override
    public void setUniformMat4(String name, float[] mat) {
        FloatBuffer fb = BufferPool.borrowFloatBuffer(mat,true);
        setUniformMat4(name, fb);
        BufferPool.returnFloatBuffer(fb);
    }
    
}