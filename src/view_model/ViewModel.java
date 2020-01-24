package view_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import models.SimModel;

public class ViewModel{

    private SimModel sm;
    public StringProperty simulatorIP;
    public StringProperty simulatorPort;
    public DoubleProperty throttle;
    public DoubleProperty rudder;
    public StringProperty aileron;
    public StringProperty elevator;
    public StringProperty script;

    public ViewModel(SimModel sm) {
        this.sm = sm;
        simulatorIP = new SimpleStringProperty();
        simulatorPort = new SimpleStringProperty();
        throttle = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        aileron = new SimpleStringProperty();
        elevator = new SimpleStringProperty();
        script = new SimpleStringProperty();
        openServer();
        System.out.println("opened server");
    }

    private void openServer() {
        sm.openDataServer(5400, 10);
    }
    public void connectToSimulator(String ip, int port) {
        sm.connectToSimulator(ip, port);
    }

    public void sendScriptToSimulator() {
        sm.sendScript(script.get());
    }

    public void setThrottle() {
        sm.setThrottle(throttle.get());
    }

    public void setRudder() {
        sm.setRudder(rudder.get());
    }

    public void setJoystickChanges() {
        sm.setAileron(Double.parseDouble(aileron.get()));
        sm.setElevator(Double.parseDouble(elevator.get()));
    }
}
