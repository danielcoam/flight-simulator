package interpreter;

import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class SymbolTable {
    private volatile LinkedBlockingDeque<ConcurrentHashMap<String, Double>> symbolTableStack;
    private Object lock = new Object();

    public SymbolTable() {
        getTableStack();
        addScope();
        try {
            addNewVar("/instrumentation/airspeed-indicator/indicated-speed-kt");
            addNewVar("/instrumentation/altimeter/indicated-altitude-ft");
            addNewVar("/instrumentation/altimeter/pressure-alt-ft");
            addNewVar("/instrumentation/attitude-indicator/indicated-pitch-deg");
            addNewVar("/instrumentation/attitude-indicator/indicated-roll-deg");
            addNewVar("/instrumentation/attitude-indicator/internal-pitch-deg");
            addNewVar("/instrumentation/attitude-indicator/internal-roll-deg");
            addNewVar("/instrumentation/encoder/indicated-altitude-ft");
            addNewVar("/instrumentation/encoder/pressure-alt-ft");
            addNewVar("/instrumentation/gps/indicated-altitude-ft");
            addNewVar("/instrumentation/gps/indicated-ground-speed-kt");
            addNewVar("/instrumentation/gps/indicated-vertical-speed");
            addNewVar("/instrumentation/heading-indicator/indicated-heading-deg");
            addNewVar("/instrumentation/magnetic-compass/indicated-heading-deg");
            addNewVar("/instrumentation/slip-skid-ball/indicated-slip-skid");
            addNewVar("/instrumentation/turn-indicator/indicated-turn-rate");
            addNewVar("/instrumentation/vertical-speed-indicator/indicated-speed-fpm");
            addNewVar("/controls/flight/aileron");
            addNewVar("/controls/flight/elevator");
            addNewVar("/controls/flight/rudder");
            addNewVar("/controls/flight/flaps");
            addNewVar("/controls/engines/current-engine/throttle");
            addNewVar("/engines/engine/rpm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinkedBlockingDeque<ConcurrentHashMap<String, Double>> getTableStack() {
        LinkedBlockingDeque<ConcurrentHashMap<String, Double>> result = symbolTableStack;
        if (result == null) {
            synchronized (lock) {
                result = symbolTableStack;
                if (result == null) {
                    symbolTableStack = result = new LinkedBlockingDeque<ConcurrentHashMap<String, Double>>();
                }
            }
        }
        return symbolTableStack;
    }

    public void updateFromSimulator(String varName, double newVal) {
        ConcurrentHashMap<String, Double> mainTable = getTableStack().peekFirst();
        if (mainTable.containsKey(varName)) {
            mainTable.put(varName, newVal);
        }
    }

    public void setVar(String varName, double newVal) throws Exception {
        Stack<ConcurrentHashMap<String, Double>> stack = new Stack<>();
        boolean isFound = false;

        while ((!getTableStack().isEmpty()) && !isFound) {
            ConcurrentHashMap<String, Double> table = getTableStack().pollLast();
            stack.push(table);
            if (table.containsKey(varName)) {
                isFound = true;
                if (table.get(varName) != newVal) {
                    table.remove(varName);
                    table.put(varName, newVal);
                }
            }
        }
        while (!stack.empty())
            try {
                getTableStack().putLast(stack.pop());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        if (!isFound)
            throw new Exception("var " + varName + " doesn't exists");
    }

    private boolean isInCurrentScope(String name) throws Exception {
        try {
            return getTableStack().peekLast().containsKey(name);
        } catch (NullPointerException e) {
            throw new Exception("table not initialized");
        }
    }

    public void addNewVar(String name) throws Exception {
        if (!isInCurrentScope(name))
            getTableStack().peekLast().put(name, new Double(0.0));
        else
            throw new Exception("Variable " + name + " exists");
    }

    public boolean isExist(String varName) {
        Stack<ConcurrentHashMap<String, Double>> stack = new Stack<>();
        boolean answer = false;
        while ((!getTableStack().isEmpty()) && !answer) {
            stack.push(getTableStack().pollLast());
            if (stack.peek().containsKey(varName))
                answer = true;
        }
        while (!stack.empty())
            try {
                getTableStack().putLast(stack.pop());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        return answer;
    }

    public Double getVar(String varName) throws Exception {
        Stack<ConcurrentHashMap<String, Double>> stack = new Stack<>();
        Double retVal = null;

        while ((!getTableStack().isEmpty()) && retVal == null) {
            stack.push(getTableStack().pollLast());
            if (stack.peek().containsKey(varName))
                retVal = stack.peek().get(varName);
        }
        while (!stack.empty())
            try {
                getTableStack().putLast(stack.pop());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        if (retVal == null)
            throw new Exception("var " + varName + " doesn't exists");
        return retVal;
    }

    public void addScope() {
        try {
            getTableStack().putLast(new ConcurrentHashMap<>());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeScope() {
        try {
            getTableStack().removeLast();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}
