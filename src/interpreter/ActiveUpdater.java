package interpreter;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ActiveUpdater implements SimulatorUpdater {
    private SimulatorUpdater su;
    private BlockingQueue<Runnable> queue;
    private volatile boolean stop;
    private Thread updateThread;

    public ActiveUpdater(SimulatorUpdater su) {
        this.su = su;
        stop = false;
        queue = new LinkedBlockingQueue<>();
        updateThread = new Thread(() -> {
            while (!stop) {
                try {
                    queue.take().run();
                } catch (InterruptedException e) {
                }
            }
        }, "simulator_client");
    }

    @Override
    public void update(PrintWriter out, String nameInSimulator, double value) {
        queue.add(() -> {
            su.update(out, nameInSimulator, value);
        });
    }

    public void start() {
        updateThread.start();
    }

    public void stop() {
        queue.add(() -> stop = true);
    }
}
