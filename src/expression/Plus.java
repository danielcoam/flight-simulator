package expression;

public class Plus extends BinaryExpression {

    public Plus(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) + Double.parseDouble(right.calculate()));
    }
}
