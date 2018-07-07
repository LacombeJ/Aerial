package ax.commons.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ax.commons.misc.ArrayUtils;

/**
 * Utility class for printing/reading to/from files
 * 
 * @author Jonathan Lacombe
 *
 */
public class FileIO {
    
    
    public static Reader fread(String file) throws IOException {
        return new IOReader(new BufferedReader(new FileReader(file)));
    }
    
    public static Writer fwrite(String file) throws IOException {
        return new IOWriter(new PrintWriter(new FileWriter(file)));
    }
    
    public static void fclose(Reader r) throws IOException {
        ((IOReader)r).close();
    }
    
    public static void fclose(Writer w) throws IOException {
        ((IOWriter)w).close();
    }
    
    
    private static class IOWriter extends Writer {
        PrintWriter writer;
        IOWriter(PrintWriter pw) {
            writer = pw;
        }
        @Override
        public void print(String... strings) {
            print((Object[])strings);
        }
        @Override
        public void print(Object... objects) {
            for (int i=0; i<objects.length; i++) {
                Object o = objects[i];
                if (o instanceof int[]) {
                    writer.print(ArrayUtils.toString((int[]) o));
                } else if (o instanceof float[]) {
                    writer.print(ArrayUtils.toString((float[]) o));
                } else if (o instanceof byte[]) {
                    writer.print(ArrayUtils.toString((byte[]) o));
                } else if (o instanceof double[]) {
                    writer.print(ArrayUtils.toString((double[]) o));
                } else if (o instanceof char[]) {
                    writer.print(ArrayUtils.toString((char[]) o));
                } else {
                    writer.print(o);
                }
                //print a space only if there is another object following
                if (i==objects.length-1) break;
                writer.print(" ");
            }
        }

        @Override
        public void println(Object... objects) {
            print(objects);
            writer.print(FileUtils.NEW_LINE);
        }
        
        @Override
        public void close() throws IOException {
            writer.close();
        }
    }
    
    
    private static class IOReader extends Reader {
        BufferedReader reader;
        IOReader(BufferedReader br) {
            reader = br;
        }
        @Override
        public String readln() throws IOException {
            return reader.readLine();
        }
        @Override
        public void close() throws IOException {
            reader.close();
        }
    }

    
}