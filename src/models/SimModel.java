package models;

import interpreter.Interpreter;
import interpreter.MyInterpreter;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimModel extends Observable {
    private class ActiveScriptSender {
        private BlockingQueue<Runnable> queue;
        private AtomicBoolean stop;
        private Thread scriptThread;
        private Interpreter inter;

        public ActiveScriptSender() {
            inter = new MyInterpreter();
            queue = new LinkedBlockingQueue<>();
            stop = new AtomicBoolean();
            scriptThread = new Thread(() -> {
                while (!stop.get()) {
                    try {
                        queue.take().run();
                    } catch (InterruptedException e) {
                    }
                }
            }, "script_thread");
        }

        public void sendScript(String script) {
            queue.add(() -> {
                inter.interpret(script);
            });
        }

        public void start() {
            scriptThread.start();
        }
    }

    private ActiveScriptSender activeSender;

    public SimModel() {
        activeSender = new ActiveScriptSender();
        activeSender.start();
    }

    public void sendScript(String script) {
        activeSender.sendScript(script);
    }

    public void openDataServer(int port, int frequency) {
        activeSender.sendScript("openDataServer " + port + " " + frequency);
    }

    public void connectToSimulator(String ip, int port) {
        activeSender.sendScript("connect " + ip + " " + port);
    }

    public void setThrottle(double value) {
        activeSender.sendScript("VM_Throttle = " + value);
    }

    public void setRudder(double value) {
        activeSender.sendScript("VM_Rudder = " + value);
    }

    public void setAileron(double value) {
        activeSender.sendScript("VM_Aileron = " + value);
    }

    public void setElevator(double value) {
        activeSender.sendScript("VM_Elevator = " + value);
    }
}
