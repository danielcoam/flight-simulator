package commands;

import expression.ExpressionCommand;

import java.util.concurrent.ConcurrentHashMap;

public class ExpressionCommandFactory {
    private interface CommandCreator {
        public ExpressionCommand create();
    }

    private ConcurrentHashMap<String, CommandCreator> creators;

    public ExpressionCommandFactory() {
        creators = new ConcurrentHashMap<>();
        creators.put("openDataServer", () -> new ExpressionCommand(new OpenServerCommand()));
        creators.put("connect", () -> new ExpressionCommand(new ConnectCommand()));
        creators.put("var", () -> new ExpressionCommand(new VarCommand()));
        creators.put("=", () -> new ExpressionCommand(new PlacementCommand()));
        creators.put("bind", () -> new ExpressionCommand(new BindCommand()));
        creators.put("while", () -> new ExpressionCommand(new WhileCommand()));
        creators.put("sleep", () -> new ExpressionCommand(new SleepCommand()));
        creators.put("print", () -> new ExpressionCommand(new PrintCommand()));
    }

    public ExpressionCommand createCommand(String name) {
        CommandCreator creator = creators.get(name);
        if (creator != null)
            return creator.create();
        return null;
    }
}
