package server;

public interface Server {
    void start(int port, ClientHandler c);

    void stop();
}
