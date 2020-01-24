package expression;

public class Number implements Expression {

    private double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public String calculate() throws Exception {
        return "" + this.value;
    }
}
