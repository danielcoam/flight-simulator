package commands;

import algorithms.ExpressionCalc;

import java.util.ListIterator;

public class PrintCommand implements Command {
    private String text;

    @Override
    public void execute() throws Exception {
        try {
            System.out.println(ExpressionCalc.calc(text).calculate());
        } catch (Exception e) {
            System.out.println(text);
        }
    }

    @Override
    public void setParameters(ListIterator<String> it) throws Exception {
        text = it.next();
    }

}
