package interpreter;

import java.util.List;

public interface Lexer {
    public List<String> tokenize(String line);
}
