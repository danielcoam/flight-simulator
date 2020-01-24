package commands;

import algorithms.ExpressionCalc;
import interpreter.MyInterpreter;

import java.util.ListIterator;

public class PlacementCommand implements Command {
    private String dstName;
    private String srcName;
    private boolean isBind = false;

    @Override
    public void execute() throws Exception {
        if (dstName != null && !isBind) {
            double newVal = getValue(srcName);
            if (MyInterpreter.getBindingTable().isBindToSimulator(dstName)) {
                String simulatorName = MyInterpreter.getBindingTable().getNameInSimulator(dstName);
                ConnectCommand.updateSimulator(simulatorName, newVal);
            } else {
                MyInterpreter.getSymbolTable().setVar(dstName, newVal);
            }
        }
    }

    private double getValue(String var) throws Exception {
        return Double.parseDouble(ExpressionCalc.calc(var).calculate());
    }

    @Override
    public void setParameters(ListIterator<String> it) throws Exception {
        it.previous();
        it.previous();
        dstName = it.next();
        it.next();
        srcName = it.next();
        if (srcName.equals("bind")) {
            isBind = true;
            it.previous();
        }
    }

}
