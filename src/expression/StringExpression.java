package expression;

public class StringExpression implements Expression {
    private String str;

    public StringExpression(String str) {
        super();
        this.str = str;
    }

    @Override
    public String calculate() throws Exception {
        return str;
    }
}
