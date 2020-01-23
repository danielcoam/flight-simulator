package algorithms;

import expression.Number;
import expression.*;
import interpreter.MyInterpreter;

import java.util.*;

public class ExpressionCalc {

    public static Expression calc(String exp) throws Exception {
        if (exp.startsWith("-"))
            return useArithmetic("0" + exp);
        return useArithmetic(exp);
    }

    public static boolean calcLogic(String exp) throws Exception {
        try {
            return useLogic(exp).calculate().equals("true");
        } catch (Exception e) {
            throw new Exception("Invalid condition");
        }
    }

    private static Expression useLogic(String expression) throws Exception {
        Queue<String> queue = new LinkedList<String>();
        Stack<String> stack = new Stack<String>();
        Stack<Expression> stackExp = new Stack<Expression>();
        String[] split = expression.split(
                "(?<=(&&)|(\\|\\|)|(!=)|(==)|(<=)|(>=)|[-+*/()<>])|(?=(&&)|(\\|\\|)|(!=)|(==)|(<=)|(>=)|[-+*/()<>])");
        StringBuilder builder = new StringBuilder();
        List<String> compareOps1 = Arrays.asList("/", "*");
        List<String> compareOps2 = Arrays.asList("/", "*", "+", "-");
        List<String> compareOps3 = Arrays.asList(">", ">=", "<", "<=", "==", "!=", "/", "*", "+", "-");
        for (int i = 0; i < split.length - 1; i++) {
            if (Arrays.asList(">", "<").contains(split[i]) && split[i + 1].equals("=")) {
                builder.append(split[i] + "=,");
                i++;
                continue;
            }
            builder.append(split[i]);
            builder.append(",");
        }
        builder.append(split[split.length - 1]);
        split = builder.toString().split(",");
        for (String s : split) {
            if (isDouble(s) || isVariable(s)) {
                queue.add(s);
            } else {
                switch (s) {
                    case "/":
                    case "*":
                    case "(":
                        stack.push(s);
                        break;
                    case "+":
                    case "-":
                        while (!stack.empty() && compareOps1.contains(stack.peek()))
                            queue.add(stack.pop());
                        stack.push(s);
                        break;
                    case ">":
                    case ">=":
                    case "<":
                    case "<=":
                    case "==":
                    case "!=":
                        while (!stack.empty() && compareOps2.contains(stack.peek()))
                            queue.add(stack.pop());
                        stack.push(s);
                        break;
                    case "&&":
                        while (!stack.empty() && compareOps3.contains(stack.peek()))
                            queue.add(stack.pop());
                        stack.push(s);
                        break;
                    case "||":
                        while (!stack.empty() && (!stack.peek().equals("("))) {
                            queue.add(stack.pop());
                        }
                        stack.push(s);
                        break;
                    case ")":
                        while (!stack.peek().equals("(")) {
                            queue.add(stack.pop());
                        }
                        stack.pop();
                        break;
                }
            }
        }

        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }

        for (String str : queue) {
            Double p = null;
            if (isDouble(str)) {
                stackExp.push(new Number(Double.parseDouble(str)));
            } else if ((p = getVariable(str)) != null) {
                stackExp.push(new Number(p));
            } else {
                Expression right = stackExp.pop();
                Expression left = stackExp.pop();

                switch (str) {
                    case "||":
                        stackExp.push(new Or(left, right));
                        break;
                    case "&&":
                        stackExp.push(new And(left, right));
                        break;
                    case ">":
                        stackExp.push(new Greater(left, right));
                        break;
                    case ">=":
                        stackExp.push(new GreaterOrEquals(left, right));
                        break;
                    case "<":
                        stackExp.push(new Less(left, right));
                        break;
                    case "<=":
                        stackExp.push(new LessOrEquals(left, right));
                        break;
                    case "==":
                        stackExp.push(new Equals(left, right));
                        break;
                    case "!=":
                        stackExp.push(new NotEquals(left, right));
                        break;
                    case "/":
                        stackExp.push(new Div(left, right));
                        break;
                    case "*":
                        stackExp.push(new Mul(left, right));
                        break;
                    case "+":
                        stackExp.push(new Plus(left, right));
                        break;
                    case "-":
                        stackExp.push(new Minus(left, right));
                        break;
                }
            }
        }

        return stackExp.pop();
    }

    private static Expression useArithmetic(String expression) throws Exception {
        Queue<String> queue = new LinkedList<String>();
        Stack<String> stack = new Stack<String>();
        Stack<Expression> stackExp = new Stack<Expression>();

        String[] split = expression.split("(?<=[-+*/()])|(?=[-+*/()])");
        for (String s : split) {
            if (isDouble(s) || isVariable(s)) {
                queue.add(s);
            } else {
                switch (s) {
                    case "/":
                    case "*":
                    case "(":
                        stack.push(s);
                        break;
                    case "+":
                    case "-":
                        while (!stack.empty() && (!stack.peek().equals("("))) {
                            queue.add(stack.pop());
                        }
                        stack.push(s);
                        break;
                    case ")":
                        while (!stack.peek().equals("(")) {
                            queue.add(stack.pop());
                        }
                        stack.pop();
                        break;
                }
            }
        }
        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }

        for (String str : queue) {
            Double p = null;
            if (isDouble(str)) {
                stackExp.push(new Number(Double.parseDouble(str)));
            } else if ((p = getVariable(str)) != null) {
                stackExp.push(new Number(p));
            } else {
                Expression right = stackExp.pop();
                Expression left = stackExp.pop();

                switch (str) {
                    case "/":
                        stackExp.push(new Div(left, right));
                        break;
                    case "*":
                        stackExp.push(new Mul(left, right));
                        break;
                    case "+":
                        stackExp.push(new Plus(left, right));
                        break;
                    case "-":
                        stackExp.push(new Minus(left, right));
                        break;
                    default:
                        stackExp.push(left);
                        stackExp.push(right);
                        stackExp.push(new StringExpression(str));
                }
            }
        }
        return stackExp.pop();
    }

    private static Double getVariable(String str) {
        String name = null;
        if (MyInterpreter.getBindingTable() != null && MyInterpreter.getSymbolTable() != null) {
            if (MyInterpreter.getBindingTable().isBindToSimulator(str)) {
                name = MyInterpreter.getBindingTable().getNameInSimulator(str);
                try {
                    return MyInterpreter.getSymbolTable().getVar(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (MyInterpreter.getSymbolTable().isExist(str))
                try {
                    return MyInterpreter.getSymbolTable().getVar(str);
                } catch (Exception e) {
                }
        }
        return null;
    }

    private static boolean isVariable(String str) {
        if (MyInterpreter.getSymbolTable() != null && MyInterpreter.getBindingTable() != null)
            return MyInterpreter.getBindingTable().isBindToSimulator(str)
                    || MyInterpreter.getSymbolTable().isExist(str);
        return false;
    }

    private static boolean isDouble(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
