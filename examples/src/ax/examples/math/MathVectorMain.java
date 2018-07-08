package ax.examples.math;

import ax.commons.io.Console;
import ax.math.vector.Vector3;

public class MathVectorMain {

    public static void main(String[]args) {
        new MathVectorMain().run();
    }

    void run() {

        Vector3 v = new Vector3(1f, 1f, 0f);

        v.norm();

        Console.log(v);

    }

}
