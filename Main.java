package main;

import database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** Driver class for scheduling application.
    @author Rebecca Fredricks */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        ResourceBundle resources = null;
        try {
            resources = ResourceBundle.getBundle("main/Nat", Locale.getDefault());
        } catch (Exception ignored) {
        }
        Parent root = FXMLLoader.load(getClass().getResource("../view/loginView.fxml"), resources);
        primaryStage.setTitle(resources.getString("SCHEDULING_APPLICATION"));
        primaryStage.setScene(new Scene(root, 250, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {

        DBConnection.startConnection();

        launch(args);

        DBConnection.closeConnection();
    }
}
