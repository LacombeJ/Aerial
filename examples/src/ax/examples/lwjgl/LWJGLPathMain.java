package ax.examples.lwjgl;

import ax.commons.io.Console;

public class LWJGLPathMain {

    public static void main(String[]args) {
        new LWJGLPathMain().run();
    }

    void run() {

        String prop = System.getProperty("java.library.path");

        Console.log(prop);

    }

}
