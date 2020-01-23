package commands;

import expression.ExpressionCommand;
import interpreter.MyInterpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public abstract class ScopeCommand implements Command {
    protected List<ExpressionCommand> commands;

    public ScopeCommand() {
        this.commands = Collections.synchronizedList(new ArrayList<ExpressionCommand>());
    }

    protected void loadScope(ListIterator<String> it) {
        ExpressionCommandFactory cf = new ExpressionCommandFactory();
        ExpressionCommand c = null;
        String str;
        while (it.hasNext() && !(str = it.next()).equals("}")) {
            c = cf.createCommand(str);
            if (c != null) {
                c.setParameters(it);
                commands.add(c);
            }
        }
    }

    protected void executeCommands() {
        MyInterpreter.getSymbolTable().addScope();
        try {
            ListIterator<ExpressionCommand> it = commands.listIterator();
            while (it.hasNext())
                it.next().calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyInterpreter.getSymbolTable().removeScope();
    }
}
