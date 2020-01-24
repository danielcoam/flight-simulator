package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import view_model.ViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainWindowController {
    private ViewModel viewModel;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    // MVVM Variables
    private StringProperty aileronV, elevatorV;
    // Autopilot mode
    @FXML
    private TextArea simScript;


    // Manual mode objects (slider + joystick)
    @FXML
    private Slider rudderSlider;
    @FXML
    private Slider throttleSlider;
    @FXML
    private Circle joystick;
    @FXML
    private Circle frameCircle;

    // Objects for manual mode data panel
    @FXML
    private Label aileronValue;
    @FXML
    private Label elevatorValue;
    @FXML
    private Label throttleValue;
    @FXML
    private Label rudderValue;

    public MainWindowController() {
        simScript = new TextArea();
        rudderSlider = new Slider();
        throttleSlider = new Slider();
        joystick = new Circle();
        frameCircle = new Circle();
        aileronValue = new Label();
        elevatorValue = new Label();
        aileronV = new SimpleStringProperty();
        elevatorV = new SimpleStringProperty();
        throttleValue = new Label();
        rudderValue = new Label();


    }

    public void setViewModel(ViewModel vm) {
        viewModel = vm;

        viewModel.script.bind(simScript.textProperty());
        viewModel.throttle.bind(throttleSlider.valueProperty());
        viewModel.rudder.bind(rudderSlider.valueProperty());
        viewModel.aileron.bind(aileronV);
        viewModel.elevator.bind(elevatorV);
    }

    @FXML
    private void runScript() {
        viewModel.connectToSimulator("127.0.0.1", 5402);
        String script = "";
        String line;
        File file = new File("commands.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                script = script + line + "\n";
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        simScript.setText(script);

        if (simScript.getText().length() != 0)
            viewModel.sendScriptToSimulator();
        simScript.clear();
        rudderValue.setText("" + (Math.round((rudderSlider.getValue() * 10.00))) / 10.00); // round to the closest
        throttleValue.setText("" + (Math.round((throttleSlider.getValue() * 10.00))) / 10.00); // round to the
        aileronValue.setText("" + 0);
        elevatorValue.setText("" + 0);
    }

    @FXML
    private void joystickPressed(MouseEvent me) {
        orgSceneX = me.getSceneX();
        orgSceneY = me.getSceneY();
        orgTranslateX = ((Circle) (me.getSource())).getTranslateX();
        orgTranslateY = ((Circle) (me.getSource())).getTranslateY();
    }

    @FXML
    private void joystickDragged(MouseEvent me) {
        double offsetX = me.getSceneX() - orgSceneX;
        double offsetY = me.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;
        double joystickCenterX = frameCircle.getTranslateX() + frameCircle.getRadius() - joystick.getRadius();
        double joystickCenterY = frameCircle.getTranslateY() - frameCircle.getRadius() - joystick.getRadius();
        double frameRadius = frameCircle.getRadius();
        double maxX = joystickCenterX + frameRadius;
        double contractionsCenterX = joystickCenterX - frameRadius;
        double maxY = joystickCenterY - frameRadius;
        double contractionsCenterY = joystickCenterY + frameRadius;

        double slant = Math
                .sqrt(Math.pow(newTranslateX - joystickCenterX, 2) + Math.pow(newTranslateY - joystickCenterY, 2));

        if (slant > frameRadius) {
            double alpha = Math.atan((newTranslateY - joystickCenterY) / (newTranslateX - joystickCenterX));
            if ((newTranslateX - joystickCenterX) < 0) {
                alpha = alpha + Math.PI;
            }
            newTranslateX = Math.cos(alpha) * frameRadius + orgTranslateX;
            newTranslateY = Math.sin(alpha) * frameRadius + orgTranslateY;
        }
        ((Circle) (me.getSource())).setTranslateX(newTranslateX);
        ((Circle) (me.getSource())).setTranslateY(newTranslateY);
        // normalize to range of [-1,1]
        double normalX = Math
                .round(((((newTranslateX - contractionsCenterX) / (maxX - contractionsCenterX)) * 2) - 1) * 100.00)
                / 100.00;
        // normalize to range of [-1,1]
        double normalY = Math
                .round(((((newTranslateY - contractionsCenterY) / (maxY - contractionsCenterY)) * 2) - 1) * 100.00)
                / 100.00;
        // send command only if manual mode is selected
        aileronValue.setText("" + normalX);
        elevatorValue.setText("" + normalY);
        aileronV.set("" + normalX);
        elevatorV.set("" + normalY);

        viewModel.setJoystickChanges();

    }

    @FXML
    private void joystickReleased(MouseEvent me) {
        ((Circle) (me.getSource()))
                .setTranslateX(frameCircle.getTranslateX() + frameCircle.getRadius() - joystick.getRadius());
        ((Circle) (me.getSource()))
                .setTranslateY(frameCircle.getTranslateY() - frameCircle.getRadius() - joystick.getRadius());

        aileronValue.setText("" + 0);
        elevatorValue.setText("" + 0);
        aileronV.set("0.0");
        elevatorV.set("0.0");

        // update that the value changed
        viewModel.setJoystickChanges();

    }


    public void setSliderOnDragEvent() {
        rudderSlider.valueProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
                rudderValue.textProperty().setValue("" + (Math.round((rudderSlider.getValue() * 10.00))) / 10.00);
                viewModel.setRudder();
            }
        });

        throttleSlider.valueProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
                throttleValue.textProperty()
                        .setValue("" + (Math.round((throttleSlider.getValue() * 10.00))) / 10.00);
                viewModel.setThrottle();
            }
        });
    }

}
