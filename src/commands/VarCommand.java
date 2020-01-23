package commands;

import interpreter.MyInterpreter;

import java.util.ListIterator;

public class VarCommand implements Command {
    private String varName;

    @Override
    public void execute() throws Exception {
        MyInterpreter.getSymbolTable().addNewVar(varName);
    }

    @Override
    public void setParameters(ListIterator<String> it) throws Exception {
        varName = it.next();
    }

}
