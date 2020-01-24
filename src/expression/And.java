package expression;

public class And extends BinaryExpression {

    public And(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        if (left.calculate().equals("false"))
            return "false";
        return "" + right.calculate().equals("true");
    }
}
