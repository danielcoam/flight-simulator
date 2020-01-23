package commands;

import algorithms.ExpressionCalc;

import java.util.ListIterator;

public abstract class ConditionCommand extends ScopeCommand {
    protected String conditionsLine;

    protected void setConditionLine(ListIterator<String> it) {
        String token = null;
        StringBuilder builder = new StringBuilder();
        while (!(token = it.next()).equals("{"))
            builder.append(token);
        conditionsLine = builder.toString();
        it.next();
    }

    protected boolean checkCondition() throws Exception {
        return ExpressionCalc.calcLogic(conditionsLine);
    }

    @Override
    public void setParameters(ListIterator<String> it) throws Exception {
        this.setConditionLine(it);
        this.loadScope(it);
    }
}
