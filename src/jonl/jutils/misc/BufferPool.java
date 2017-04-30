package jonl.jutils.misc;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import jonl.jutils.structs.OrderedList;

/**
 * Keeps track of all buffers created using this utility class
 * <p>
 * The following options are available using this class:
 * <pre>
 * - Use buffers while still keeping them in the pool for other
 *   resources to use (for temporary use)
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
    
    /** Comparator used for priority queues */
    private static final Comparator<Buffer> COMPARATOR = new Comparator<Buffer>() {
        public int compare(Buffer lhs, Buffer rhs) {
            int x = lhs.capacity();
            int y = rhs.capacity();
            return (x < y) ? -1 : ((x == y) ? 0 : 1); //From Integer.compare(l,r);
        }
    };

    private static final OrderedList<Buffer> FLOAT_BUFFERS  = new OrderedList<>(COMPARATOR);
    private static final OrderedList<Buffer> INT_BUFFERS    = new OrderedList<>(COMPARATOR);
    private static final OrderedList<Buffer> BYTE_BUFFERS   = new OrderedList<>(COMPARATOR);
    private static final OrderedList<Buffer> DOUBLE_BUFFERS = new OrderedList<>(COMPARATOR);

    private static final Set<Buffer> BORROWED_BUFFERS = new HashSet<>();

    
    
    /**
     * Returns the smallest ByteBuffer in this pool
     * with at least the given size. This returned buffer
     * still remains in the pool.
     * <p>
     * If there is not a ByteBuffer with this capacity,
     * this method will create and return a new ByteBuffer. </p>
     * @param size capacity of ByteBuffer
     * @param exact specifies whether this buffer should have exactly
     * the given size
     * @return a ByteBuffer with at least the given capacity
     */
    public static synchronized ByteBuffer getByteBuffer(int size, boolean exact) {
        return getBuffer(ByteBuffer.class,BYTE_BUFFERS,size,exact);
    }

    /** @see #byteBuffer(int, boolean) */
    public static synchronized FloatBuffer getFloatBuffer(int size, boolean exact) {
        return getBuffer(FloatBuffer.class,FLOAT_BUFFERS,size,exact);
    }

    /** @see #byteBuffer(int, boolean) */
    public static synchronized IntBuffer getIntBuffer(int size, boolean exact) {
        return getBuffer(IntBuffer.class,INT_BUFFERS,size,exact);
    }

    /** @see #byteBuffer(int, boolean) */
    public static synchronized DoubleBuffer getDoubleBuffer(int size, boolean exact) {
        return getBuffer(DoubleBuffer.class,DOUBLE_BUFFERS,size,exact);
    }
    
    
   
    
    /**
     * Returns a ByteBuffer containing the given data
     * <p>
     * This method finds a ByteBuffer with an appropriate size
     * and writes to this buffer with buffer.put(data) and
     * buffer.flip()
     * @param data byte data
     * @param exact specifies whether this buffer should have exactly
     * the given size
     * @return a ByteBuffer with the given data
     * @see #getByteBuffer(int, boolean)
     */
    public static synchronized ByteBuffer getByteBuffer(byte[] data, boolean exact) {
        ByteBuffer buffer = getByteBuffer(data.length, exact);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    /** @see #toByteBuffer(byte[], boolean) */
    public static synchronized FloatBuffer getFloatBuffer(float[] data, boolean exact) {
        FloatBuffer buffer = getFloatBuffer(data.length, exact);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    /** @see #toByteBuffer(byte[], boolean) */
    public static synchronized IntBuffer getIntBuffer(int[] data, boolean exact) {
        IntBuffer buffer = getIntBuffer(data.length, exact);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /** @see #toByteBuffer(byte[], boolean) */
    public static synchronized DoubleBuffer toDoubleBuffer(double[] data, boolean exact) {
        DoubleBuffer buffer = getDoubleBuffer(data.length, exact);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    
    /**
     * Same as getByteBuffer but removes it from the buffer pool
     * @see #getByteBuffer(int, boolean)
     */
    public static ByteBuffer borrowByteBuffer(int size, boolean exact) {
        return borrowBuffer(ByteBuffer.class, BYTE_BUFFERS, size, exact);
    }

    /** @see #borrowByteBuffer(int, boolean) */
    public static FloatBuffer borrowFloatBuffer(int size, boolean exact) {
        return borrowBuffer(FloatBuffer.class, FLOAT_BUFFERS, size, exact);
    }

    /** @see #borrowByteBuffer(int, boolean) */
    public static IntBuffer borrowIntBuffer(int size, boolean exact) {
        return borrowBuffer(IntBuffer.class, INT_BUFFERS, size, exact);
    }

    /** @see #borrowByteBuffer(int, boolean) */
    public static DoubleBuffer borrowDoubleBuffer(int size, boolean exact) {
        return borrowBuffer(DoubleBuffer.class,DOUBLE_BUFFERS,size,exact);
    }

    /** 
     * Same as getByteBuffer but removes it from the buffer pool
     * @see #getByteBuffer(byte[], boolean)
     */
    public static ByteBuffer borrowByteBuffer(byte[] data, boolean exact) {
        ByteBuffer b = borrowByteBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }

    /** @see #borrowByteBuffer(byte[], boolean) */
    public static FloatBuffer borrowFloatBuffer(float[] data, boolean exact) {
        FloatBuffer b = borrowFloatBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }

    /** @see #borrowByteBuffer(byte[], boolean) */
    public static IntBuffer borrowIntBuffer(int[] data, boolean exact) {
        IntBuffer b = borrowIntBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }

    /** @see #borrowByteBuffer(byte[], boolean) */
    public static DoubleBuffer borrowDoubleBuffer(double[] data, boolean exact) {
        DoubleBuffer b = borrowDoubleBuffer(data.length, exact);
        b.put(data);
        b.flip();
        return b;
    }

    /** Returns or adds a buffer to the buffer pool */
    public static ByteBuffer returnByteBuffer(ByteBuffer buffer) {
        BORROWED_BUFFERS.remove(buffer);
        BYTE_BUFFERS.add(buffer);
        return null;
    }

    /** @see #returnByteBuffer(ByteBuffer) */
    public static FloatBuffer returnFloatBuffer(FloatBuffer buffer) {
        BORROWED_BUFFERS.remove(buffer);
        FLOAT_BUFFERS.add(buffer);
        return null;
    }

    /** @see #returnByteBuffer(ByteBuffer) */
    public static IntBuffer returnIntBuffer(IntBuffer buffer) {
        BORROWED_BUFFERS.remove(buffer);
        INT_BUFFERS.add(buffer);
        return null;
    }

    /** @see #returnByteBuffer(ByteBuffer) */
    public static DoubleBuffer returnDoubleBuffer(DoubleBuffer buffer) {
        BORROWED_BUFFERS.remove(buffer);
        DOUBLE_BUFFERS.add(buffer);
        return null;
    }
    
    
    
    @SuppressWarnings("unchecked")
    private static synchronized <E extends Buffer> E getBuffer(Class<E> c, OrderedList<Buffer> list, int size, boolean exact) {
        if (size<MINIMUM) size = MINIMUM;
        //Get Buffer
        for (Buffer buffer : list) {
            //If a returnable buffer is found
            if ((exact && buffer.capacity()==size) ||
                    (!exact && buffer.capacity()>=size
                            && buffer.capacity()/size<=ROUGH_RATIO)) {
                buffer.clear();
                return (E) buffer;
            }
        }
        //Create Buffer
        if (c.equals(FloatBuffer.class)) {
            return (E) add(createFloatBuffer(size));
        } else if (c.equals(IntBuffer.class)) {
            return (E) add(createIntBuffer(size));
        } else if (c.equals(ByteBuffer.class)) {
            return (E) add(createByteBuffer(size));
        } else if (c.equals(DoubleBuffer.class)) {
            return (E) add(createDoubleBuffer(size));
        }
        return null;
    }
    
    
    
    /**
     * Adds a ByteBuffer to this pool
     * @param buffer ByteBuffer to add
     * @return the same ByteBuffer
     */
    private static synchronized ByteBuffer add(ByteBuffer buffer) {
        return add(ByteBuffer.class, BYTE_BUFFERS, buffer);
    }

    /** @see #add(ByteBuffer) */
    private static synchronized FloatBuffer add(FloatBuffer buffer) {
        return add(FloatBuffer.class, FLOAT_BUFFERS, buffer);
    }

    /** @see #add(ByteBuffer) */
    private static synchronized IntBuffer add(IntBuffer buffer) {
        return add(IntBuffer.class, INT_BUFFERS, buffer);
    }

    /** @see #add(ByteBuffer) */
    private static synchronized DoubleBuffer add(DoubleBuffer buffer) {
        return add(DoubleBuffer.class,DOUBLE_BUFFERS,buffer);
    }
    
    /** @see #add(ByteBuffer) */
    private static synchronized <E extends Buffer> E add(Class<E> c, OrderedList<Buffer> list, E buffer) {
        list.add(buffer);
        return buffer;
    }
    
    
    
    private static <B extends Buffer> B borrowBuffer(Class<B> c, OrderedList<Buffer> list, int size, boolean exact) {
        B b = getBuffer(c, list, size, exact);
        list.remove(b);
        BORROWED_BUFFERS.add(b);
        return b;
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
