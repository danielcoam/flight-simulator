package commands;

import algorithms.ExpressionCalc;
import interpreter.ActiveUpdater;
import interpreter.MyUpdater;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ListIterator;

public class ConnectCommand implements Command {
    private static Socket simulator = null;
    private static PrintWriter out = null;
    private static ActiveUpdater activeUpdater = null;
    private static boolean simulatorReady = false;
    private String ip;
    private int port;

    @Override
    public void execute() throws Exception {
        try {
            simulator = new Socket(ip, port);
            out = new PrintWriter(simulator.getOutputStream());
            activeUpdater = new ActiveUpdater(new MyUpdater());
            System.out.println("connected to the simulator");
            startCommunicationWithSimulator();
        } catch (Exception e) {
            System.out.println("waiting for simulator");
            Thread.sleep(3000);
            execute();
        }
    }

    @Override
    public void setParameters(ListIterator<String> it) throws Exception {
        ip = it.next();
        port = (int) Double.parseDouble(ExpressionCalc.calc(it.next()).calculate());
    }

    public static void updateSimulator(String name, double value) throws Exception {
        activeUpdater.update(out, name, value);
    }

    public static void startCommunicationWithSimulator() {
        try {
            if (simulatorReady)
                activeUpdater.start();
            else {
                System.out.println("waiting for simulator to be ready");
                Thread.sleep(35 * 1000);
                startCommunicationWithSimulator();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setSimulatorCondition(boolean status) {
        simulatorReady = status;
    }

    public static boolean isSimulatorReady() {
        return simulatorReady;
    }

    public static void disconnectFromSimulator() {
        if (activeUpdater != null) {
            activeUpdater.stop();
            activeUpdater = null;
        }
        if (simulator != null)
            try {
                out.close();
                simulator.close();
                simulator = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
