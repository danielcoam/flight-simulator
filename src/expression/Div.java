package expression;

public class Div extends BinaryExpression {

    public Div(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) / Double.parseDouble(right.calculate()));
    }
}
