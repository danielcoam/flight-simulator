package interpreter;

import commands.ExpressionCommandFactory;
import expression.ExpressionCommand;

import java.util.List;
import java.util.ListIterator;

public  class MyParser implements Parser {
    private ExpressionCommandFactory cf;

    public MyParser() {
        this.cf = new ExpressionCommandFactory();
    }

    @Override
    public void parse(List<String> expressions) {
        ListIterator<String> iterator = expressions.listIterator();
        while (iterator.hasNext()) {
            ExpressionCommand cmd = cf.createCommand(iterator.next());
            if (cmd != null) {
                cmd.setParameters(iterator);
                try {
                    cmd.calculate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
