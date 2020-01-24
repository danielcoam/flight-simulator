package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyServer implements Server {
    private AtomicBoolean stop;
    private ClientHandler ch;
    private int port;
    private ExecutorService es;

    public MyServer() {
        stop = new AtomicBoolean();
        es = Executors.newFixedThreadPool(5);
    }

    @Override
    public void start(int port, ClientHandler c) {
        this.port = port;
        this.ch = c;

        es.execute(() -> runServer());
    }

    @Override
    public void stop() {
        this.stop.set(true);
    }

    private void runServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(1000);
            Socket aClient = null;
            while (!this.stop.get()) {
                try {
                    aClient = server.accept();// awaits for client.
                    try {
                        ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    } catch (IOException e) {// handles exception thrown from handleClient
                        e.printStackTrace();
                    }
                } catch (SocketTimeoutException e) {
                    if (server != null)
                        server.close();
                    this.runServer();
                } finally {
                    if (aClient != null)
                        aClient.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {// closing the server
            es.shutdown();
            try {
                if (server != null) {
                    server.close();
                    server = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
