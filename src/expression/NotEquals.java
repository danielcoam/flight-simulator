package expression;

public class NotEquals extends BinaryExpression {

    public NotEquals(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) != Double.parseDouble(right.calculate()));
    }
}
