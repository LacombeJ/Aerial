package jonl.jutils.misc;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Set;

import jonl.jutils.io.Console;
import jonl.jutils.structs.ObjectSet;

/**
 * Keeps track of all buffers created using this utility class
 * <p>
 * The following options are available using this class:
 * <pre>
 * - Borrow a buffer and remove it from the pool
 * - Return or add a buffer to this pool so that other resources
 *   can use
 * - Allocate a buffer (not tracked by BufferPool - use if you do
 *   not intend to return the buffer)
 * </pre>
 * @author Jonathan Lacombe
 *
 */
public class BufferPool {
    
    /**
     * Limit of size difference when getting a buffer of a close size.
     * A non exact buffer will not have a capacity greater than
     * ROUGH_RATIO*(requested size)
     */
    public static final int ROUGH_RATIO = 2;

    /** Minimum size of buffers */
    public static final int MINIMUM = 1;
    
    /*
    private static final OrderedList<Buffer> FLOAT_BUFFERS  = new OrderedList<>(COMPARATOR);
    private static final OrderedList<Buffer> INT_BUFFERS    = new OrderedList<>(COMPARATOR);
    private static final OrderedList<Buffer> BYTE_BUFFERS   = new OrderedList<>(COMPARATOR);
    private static final OrderedList<Buffer> DOUBLE_BUFFERS = new OrderedList<>(COMPARATOR);
    */
    private static final Set<FloatBuffer>   FLOAT_BUFFERS     = new ObjectSet<>();
    private static final Set<IntBuffer>     INT_BUFFERS       = new ObjectSet<>();
    private static final Set<ByteBuffer>    BYTE_BUFFERS      = new ObjectSet<>();
    private static final Set<DoubleBuffer>  DOUBLE_BUFFERS    = new ObjectSet<>();
    
    
    private static final Set<Buffer> BORROWED_BUFFERS = new ObjectSet<>();
    
    
    
    
    
    @SuppressWarnings("unchecked")
    private static synchronized <E extends Buffer> E borrowBuffer(Class<E> c, Set<E> list, int size, boolean exact) {
        if (size<MINIMUM) size = MINIMUM;
        
        //Get smallest buffer with atleast the required size
        E buffer = getSmallestBuffer(c,list,size);
        
        //If it is a returnable buffer based on the parameters return it
        if (buffer!=null) {
            if ((exact && buffer.capacity()==size) ||
                    (!exact && buffer.capacity()/size<=ROUGH_RATIO)) {
                buffer.clear();
                list.remove(buffer);
                BORROWED_BUFFERS.add(buffer);
                return (E) buffer;
            }
        }
        
        buffer = null;
        //Else allocate a new buffer and return that buffer
        if (c.equals(FloatBuffer.class)) {
            buffer = (E) createFloatBuffer(size);
        } else if (c.equals(IntBuffer.class)) {
            buffer = (E) createIntBuffer(size);
        } else if (c.equals(ByteBuffer.class)) {
            buffer = (E) createByteBuffer(size);
        } else if (c.equals(DoubleBuffer.class)) {
            buffer = (E) createDoubleBuffer(size);
        }
        BORROWED_BUFFERS.add(buffer);
        return buffer;
    }
    
    private static synchronized <E extends Buffer> E getSmallestBuffer(Class<E> c, Set<E> set, int size)
    {
        E buffer = null;
        int min = Integer.MAX_VALUE;
        for (E b : set) {
            if (b.capacity()<min && b.capacity()>=size) {
                buffer = b;
                min = b.capacity();
            }
        }
        return buffer;
    }
    
    /**
     * Same as getByteBuffer but removes it from the buffer pool
     * @see #getByteBuffer(int, boolean)
     */
    public static synchronized ByteBuffer borrowByteBuffer(int size, boolean exact) {
        return borrowBuffer(ByteBuffer.class, BYTE_BUFFERS, size, exact);
    }

    /** @see #borrowByteBuffer(int, boolean) */
    public static synchronized FloatBuffer borrowFloatBuffer(int size, boolean exact) {
        return borrowBuffer(FloatBuffer.class, FLOAT_BUFFERS, size, exact);
    }

    /** @see #borrowByteBuffer(int, boolean) */
    public static synchronized IntBuffer borrowIntBuffer(int size, boolean exact) {
        return borrowBuffer(IntBuffer.class, INT_BUFFERS, size, exact);
    }


    public static synchronized DoubleBuffer borrowDoubleBuffer(int size, boolean exact) {
        return borrowBuffer(DoubleBuffer.class,DOUBLE_BUFFERS,size,exact);
    }

    /** 
     * Same as getByteBuffer but removes it from the buffer pool
     * @see #getByteBuffer(byte[], boolean)
     */
    public static synchronized ByteBuffer borrowByteBuffer(byte[] data, boolean exact) {
        ByteBuffer b = borrowByteBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }

    /** @see #borrowByteBuffer(byte[], boolean) */
    public static synchronized FloatBuffer borrowFloatBuffer(float[] data, boolean exact) {
        FloatBuffer b = borrowFloatBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }

    /** @see #borrowByteBuffer(byte[], boolean) */
    public static synchronized IntBuffer borrowIntBuffer(int[] data, boolean exact) {
        IntBuffer b = borrowIntBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }

    /** @see #borrowByteBuffer(byte[], boolean) */
    public static synchronized DoubleBuffer borrowDoubleBuffer(double[] data, boolean exact) {
        DoubleBuffer b = borrowDoubleBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }
    
    
    
    
    
    
    
    
    private static synchronized <E extends Buffer> E returnBuffer(E buffer, Set<E> list) {
        BORROWED_BUFFERS.remove(buffer);
        list.add(buffer);
        return null;
    }

    /** Returns or adds a buffer to the buffer pool */
    public static synchronized ByteBuffer returnByteBuffer(ByteBuffer buffer) {
        return returnBuffer(buffer,BYTE_BUFFERS);
    }

    /** @see #returnByteBuffer(ByteBuffer) */
    public static synchronized FloatBuffer returnFloatBuffer(FloatBuffer buffer) {
        return returnBuffer(buffer,FLOAT_BUFFERS);
    }

    /** @see #returnByteBuffer(ByteBuffer) */
    public static synchronized IntBuffer returnIntBuffer(IntBuffer buffer) {
        return returnBuffer(buffer,INT_BUFFERS);
    }

    /** @see #returnByteBuffer(ByteBuffer) */
    public static synchronized DoubleBuffer returnDoubleBuffer(DoubleBuffer buffer) {
        return returnBuffer(buffer,DOUBLE_BUFFERS);
    }


    
    
    
    
    /**
     * Allocates a direct byte buffer.
     * Use this method if you do not intend to recycle this buffer
     */
    public static ByteBuffer createByteBuffer(int capacity) {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    }

    /** @see #createByteBuffer(int) */
    public static FloatBuffer createFloatBuffer(int capacity) {
        return ByteBuffer.allocateDirect(capacity*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /** @see #createByteBuffer(int) */
    public static IntBuffer createIntBuffer(int capacity) {
        return ByteBuffer.allocateDirect(capacity*4).order(ByteOrder.nativeOrder()).asIntBuffer();
    }
    
    /** @see #createByteBuffer(int) */
    public static DoubleBuffer createDoubleBuffer(int capacity) {
        return ByteBuffer.allocateDirect(capacity*8).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }
    
}
