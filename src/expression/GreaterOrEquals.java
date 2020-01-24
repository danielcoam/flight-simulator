package expression;

public class GreaterOrEquals extends BinaryExpression {

    public GreaterOrEquals(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) >= Double.parseDouble(right.calculate()));
    }
}
