package jonl.jutils;

import jonl.jutils.io.Console;
import jonl.jutils.time.Clock;

public class NamespaceDebug {
    
    public static void      print(Object... objects)    { Console.print(objects);       }
    public static void      println(Object... objects)  { Console.println(objects);     }

    public static void      clock()                     { Clock.clock();                }
    public static long      lap()                       { return Clock.lap();           }
    public static double    rate()                      { return Clock.rate();          }
    
}
