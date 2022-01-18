package controller;

import dbAccess.DBUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;

/** Controller class for login screen.
    @author Rebecca Fredricks */
public class LoginViewController {

    @FXML
    private ResourceBundle rb ;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label zoneIDlbl;

    @FXML
    public void login(ActionEvent event) throws IOException {
        FileWriter filewriter = new FileWriter("src/login_activity.txt", true);
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        if (DBUsers.isValidUser(username, password)){
            filewriter.write("> User: " + username + " login at " + getTimeNow() + " Successful\n");
            filewriter.close();
            mainMenuView(event);
        }
        else {
            filewriter.write("> User: " + username + " login at " + getTimeNow() + " Unsuccessful\n");
            filewriter.close();
            loginError();
        }
    }

    public String getTimeNow(){
        ZonedDateTime zdtNow = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        return zdtNow.format(dtf1);
    }

    /** Initialize method for login view controller. */
    @FXML
    public void initialize() {
        zoneIDlbl.setText(TimeZone.getDefault().getID());
        rb = ResourceBundle.getBundle("main/Nat");
    }

    /** Navigate to the main menu.
        @param event the event
        @throws IOException file problem */
    public void mainMenuView(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/mainMenuView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Display a login error message if an invalid login is detected. */
    public void loginError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(rb.getString("INVALID_CREDENTIALS"));
        alert.showAndWait();
    }
}
