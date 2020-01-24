package expression;

public class Minus extends BinaryExpression {

    public Minus(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        try {
            return "" + (Double.parseDouble(left.calculate()) - Double.parseDouble(right.calculate()));
        } catch (NumberFormatException e) {
            return "" + (0 - Double.parseDouble(right.calculate()));
        }
    }
}
