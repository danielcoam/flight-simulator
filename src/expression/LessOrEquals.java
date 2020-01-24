package expression;

public class LessOrEquals extends BinaryExpression {

    public LessOrEquals(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) <= Double.parseDouble(right.calculate()));
    }
}
