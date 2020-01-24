package expression;

public class Or extends BinaryExpression {

    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String calculate() throws Exception {
        if (left.calculate().equals("true"))
            return "true";
        return "" + right.calculate().equals("true");
    }
}
