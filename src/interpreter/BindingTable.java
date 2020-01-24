package interpreter;

import java.util.concurrent.ConcurrentHashMap;

public class BindingTable {
    private volatile ConcurrentHashMap<String, String> bindTable;// local name-> simulator name
    private Object lock = new Object();

    public BindingTable() {
        getTable();
    }

    private ConcurrentHashMap<String, String> getTable() {
        ConcurrentHashMap<String, String> result = bindTable;
        if (result == null) {
            synchronized (lock) {
                result = bindTable;
                if (result == null) {
                    bindTable = result = new ConcurrentHashMap<>();
                    // reset the control from ViewModel
                    addBinding("VM_Throttle", "/controls/engines/current-engine/throttle");
                    addBinding("VM_Rudder", "/controls/flight/rudder");
                    addBinding("VM_Aileron", "/controls/flight/aileron");
                    addBinding("VM_Elevator", "/controls/flight/elevator");
                }
            }
        }
        return bindTable;
    }

    public boolean isBindToSimulator(String localVarName) {
        return getTable().containsKey(localVarName);
    }

    public String getNameInSimulator(String localVarName) {
        return getTable().get(localVarName);
    }

    public void addBinding(String localVarName, String simVarName) {
        if (!isBindToSimulator(localVarName))
            getTable().put(localVarName, simVarName);
    }
}
