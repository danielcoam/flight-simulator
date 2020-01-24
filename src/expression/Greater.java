package expression;

public class Greater extends BinaryExpression {

    public Greater(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) > Double.parseDouble(right.calculate()));
    }
}
