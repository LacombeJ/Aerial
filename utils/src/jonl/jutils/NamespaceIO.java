package jonl.jutils;

import java.io.IOException;

import jonl.jutils.io.Console;
import jonl.jutils.io.FileIO;
import jonl.jutils.io.Reader;
import jonl.jutils.io.Writer;

public class NamespaceIO {
    
    public static void      print(Object... objects)    { Console.print(objects);       }
    public static void      println(Object... objects)  { Console.println(objects);     }
    
    public static boolean   open()                      { return Console.open();        }
    public static boolean   close()                     { return Console.close();       }
    public static boolean   isClosed()                  { return Console.isClosed();    }
    public static String    read()                      { return Console.read();        }
    public static String    readln()                    { return Console.readln();      }
    public static int       readInt()                   { return Console.readInt();     }
    public static double    readDouble()                { return Console.readDouble();  }
    
    
    public static Reader    fread(String file) throws IOException   { return FileIO.fread(file);   }
    public static Writer    fwrite(String file) throws IOException  { return FileIO.fwrite(file);   }
    public static void      fclose(Reader r) throws IOException     { FileIO.fclose(r); }  
    public static void      fclose(Writer w) throws IOException     { FileIO.fclose(w); } 

    
}
