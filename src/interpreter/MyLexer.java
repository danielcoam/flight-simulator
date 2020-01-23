package interpreter;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MyLexer implements Lexer {
    @Override
    public List<String> tokenize(String script) {// separetes without bind
        String[] tokens = separateTokens(script);
        Scanner sc;
        StringBuilder builder = new StringBuilder();
        for (String token : tokens) {
            sc = new Scanner(token);
            String str1 = sc.next();
            String str2;
            builder.append(str1);

            while (sc.hasNext()) {// get the next expression
                str2 = sc.next();
                if (isEndOfExpression(str1, str2))// end of expression
                    builder.append(",");
                builder.append(str2);
                str1 = str2;
            }
            builder.append(",");
        }
        return Arrays.asList(builder.toString().split(","));

    }

    private boolean isEndOfExpression(String str1, String str2) {
        Pattern end = Pattern.compile(".*[\\w)\"]");
        Pattern start = Pattern.compile("[\\w(\"].*");
        return (end.matcher(str1).matches() && start.matcher(str2).matches());
    }

    private String[] separateTokens(String script) {
        return script.split("(?<=([={}])|(bind))|(?=([={}]))");
    }

}
