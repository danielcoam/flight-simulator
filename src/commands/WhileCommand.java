package commands;

public class WhileCommand extends ConditionCommand {

    @Override
    public void execute() throws Exception {
        while (checkCondition()) {
            executeCommands();
        }
    }
}