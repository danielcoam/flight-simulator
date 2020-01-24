package commands;

import algorithms.ExpressionCalc;
import server.MyServer;
import server.Server;
import server.MyClientHandler;

import java.util.ListIterator;

public class OpenServerCommand implements Command {
    private static Server server = null;
    private int port;
    private int Hz;

    public static void closeServer() {
        if (server != null)
            server.stop();
    }

    @Override
    public void execute() throws Exception {
        server = new MyServer();
        server.start(port, new MyClientHandler(Hz));
    }

    @Override
    public void setParameters(ListIterator<String> it) throws Exception {
        port = (int) Double.parseDouble(ExpressionCalc.calc(it.next()).calculate());
        Hz = (int) Double.parseDouble(ExpressionCalc.calc(it.next()).calculate());
    }
}
