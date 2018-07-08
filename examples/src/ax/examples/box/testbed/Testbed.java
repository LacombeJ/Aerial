package ax.examples.box.testbed;

import ax.examples.box.testbed.tests.DominoTest;

import java.util.ArrayList;

class Testbed {

    private ArrayList<TestbedTest> tests = new ArrayList<>();

    Testbed() {

        tests.add(new DominoTest());

    }

    int count() {
        return tests.size();
    }

    TestbedTest test(int index) {
        return tests.get(index);
    }

}
