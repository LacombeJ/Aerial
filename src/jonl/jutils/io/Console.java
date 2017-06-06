package jonl.jutils.io;

import java.util.Scanner;

import jonl.jutils.misc.ArrayUtils;

/**
 * Utility class for printing/reading to/from console
 * 
 * @author Jonathan Lacombe
 *
 */
public class Console {
    
    
    
    /* ************************************************************************* */
    /* *************************         OUTPUT         ************************ */
    /* ************************************************************************* */
    
    public static synchronized void print(String... strings) {
        print((Object[])strings);
    }
    
    /**
     * Prints the following objects on one line
     * @param objects
     */
    public static synchronized void print(Object... objects) {
        for (int i=0; i<objects.length; i++) {
            Object o = objects[i];
            if (o instanceof int[]) {
                System.out.print(ArrayUtils.toString((int[]) o));
            } else if (o instanceof float[]) {
                System.out.print(ArrayUtils.toString((float[]) o));
            } else if (o instanceof byte[]) {
                System.out.print(ArrayUtils.toString((byte[]) o));
            } else if (o instanceof double[]) {
                System.out.print(ArrayUtils.toString((double[]) o));
            } else if (o instanceof char[]) {
                System.out.print(ArrayUtils.toString((char[]) o));
            } else {
                System.out.print(o);
            }
            //print a space only if there is another object following
            if (i==objects.length-1) break;
            System.out.print(" ");
        }
    }
    
    /**
     * Prints the following objects on one line then prints a new line
     * @param objects
     */
    public static synchronized void println(Object... objects) {
        print(objects);
        System.out.println();
    }
    
    /* ************************************************************************* */
    /* *************************        INPUT           ************************ */
    /* ************************************************************************* */
    
    private static Scanner scanner;
    
    public static boolean isClosed() {
        return scanner == null;
    }
    
    public static boolean open() {
        boolean isClosed = scanner==null;
        if (!isClosed) {
            scanner = new Scanner(System.in);
            return true;
        }
        return false;
    }
    
    public static boolean close() {
        boolean isClosed = scanner==null;
        if (!isClosed) {
            scanner.close();
            scanner = null;
            return true;
        }
        return false;
    }
    
    public static String read() {
        return scanner.next();
    }
    
    public static String readln() {
        return scanner.nextLine();
    }
    
    public static int readInt() {
        return scanner.nextInt();
    }
    
    public static double readDouble() {
        return scanner.nextDouble();
    }
    
    
}