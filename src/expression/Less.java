package expression;

public class Less extends BinaryExpression {

    public Less(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        return "" + (Double.parseDouble(left.calculate()) < Double.parseDouble(right.calculate()));
    }
}
