package expression;

public class Equals extends BinaryExpression {

    public Equals(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) == Double.parseDouble(right.calculate()));
    }
}
