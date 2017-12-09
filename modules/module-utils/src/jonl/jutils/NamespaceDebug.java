package jonl.jutils;

import jonl.jutils.io.Console;
import jonl.jutils.time.Clock;

public class NamespaceDebug {
    
    private static Clock clock = new Clock();
    
    public static void      print(Object... objects)    { Console.print(objects);       }
    public static void      println(Object... objects)  { Console.println(objects);     }

    public static void      clock()                     { clock = new Clock();          }
    public static long      lap()                       { return clock.lap();           }
    public static double    rate()                      { return clock.rate();          }
    
}
