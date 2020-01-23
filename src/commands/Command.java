package commands;

import java.util.ListIterator;

public interface Command {
    public void execute() throws Exception;

    public void setParameters(ListIterator<String> it) throws Exception;
}
