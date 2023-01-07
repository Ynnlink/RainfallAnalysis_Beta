package demo.rainfallanalysis_beta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RainfallVisualiser extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RainfallVisualiser.class.getResource("visualiser-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 500);
        stage.setTitle("Rainfall Visualiser");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}