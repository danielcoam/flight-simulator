package interpreter;

import java.util.List;

public class MyInterpreter implements Interpreter {
    private Lexer lexer;
    private Parser parser;

    private static BindingTable bindingTable = null;
    private static SymbolTable symbolTable = null;

    public static BindingTable getBindingTable() {
        return bindingTable;
    }

    public static SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public MyInterpreter() {
        bindingTable = new BindingTable();
        symbolTable = new SymbolTable();
        lexer = new MyLexer();
        parser = new MyParser();
    }

    @Override
    public int interpret(String script) {
        List<String> tokens = lexer.tokenize(script);
        parser.parse(tokens);
        return 0;
    }
}
