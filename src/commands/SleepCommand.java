package commands;

import java.util.ListIterator;

public class SleepCommand implements Command {
    private int sleepDuration;

    @Override
    public void execute() throws Exception {
        Thread.sleep(sleepDuration);
    }

    @Override
    public void setParameters(ListIterator<String> it) throws Exception {
        sleepDuration = Integer.parseInt(it.next());
    }

}
