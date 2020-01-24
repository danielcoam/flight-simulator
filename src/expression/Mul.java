package expression;

public class Mul extends BinaryExpression {

    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) * Double.parseDouble(right.calculate()));
    }
}
