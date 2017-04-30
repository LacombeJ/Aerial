package jonl.jgl;

import jonl.jutils.misc.BufferPool;

public abstract class AbstractProgram implements Program {
    
    @Override
    public void setUniformMat4(String name, float[] mat) {
        setUniformMat4(name, BufferPool.getFloatBuffer(mat,true));
    }
    
}