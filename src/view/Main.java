package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.SimModel;
import view_model.ViewModel;

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage _primaryStage) {
        primaryStage = _primaryStage;
        SimModel simModel = new SimModel();
        ViewModel viewModel = new ViewModel(simModel);

        try {
            FXMLLoader fxml = new FXMLLoader();
            BorderPane root = fxml.load(getClass().getResource("MainWindow.fxml").openStream());
            root.setStyle("-fx-background-image: url(\"/resources/B.jpg\");");
            MainWindowController mainWindowC = fxml.getController();// view
            mainWindowC.setViewModel(viewModel);
            primaryStage.setTitle("Flight Gear - Ron Arviv & Daniel Cohen");
            Scene scene = new Scene(root, 400, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
            mainWindowC.setSliderOnDragEvent();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);

    }
}
