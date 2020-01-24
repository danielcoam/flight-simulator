package interpreter;

import java.io.PrintWriter;

public class MyUpdater implements SimulatorUpdater {

    @Override
    public void update(PrintWriter out, String nameInSimulator, double value) {
        String command = "set " + nameInSimulator + " " + value;
        out.println(command);
        out.flush();
    }
}
