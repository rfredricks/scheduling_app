package controller;

import dbAccess.DBAppointments;
import dbAccess.DBUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Controller class for scheduling application main menu.
    @author Rebecca Fredricks */
public class MainMenuController {

    private static class LoginMessage {
        private static int count = 0;
        public static void loginMessage(){
            if (count <= 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                try {
                    Appointment upcoming = DBAppointments.getUpcomingAppointment(LocalDateTime.now(), DBUsers.getLoggedInUser().getUserId());
                    alert.setContentText(DBUsers.getLoggedInUser().getUserName() + " has an upcoming appointment: " +
                            upcoming.getAppointmentId() + " at " + upcoming.getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                    alert.showAndWait();
                } catch (NullPointerException ignored) {
                    alert.setContentText(DBUsers.getLoggedInUser().getUserName() + " has no upcoming appointments");
                    alert.showAndWait();
                }
                ++count;
            }
        }
    }

    /** Exit the scheduling application.
     @param event the user input event */
    @FXML
    public void onactionexit(ActionEvent event) {
        System.exit(0);
    }

    /** Navigate to appointments screen.
        @param event the user input event
        @exception IOException i/o error */
    @FXML
    public void showAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/appointmentsView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Navigate to customers screen.
        @param event the user input event
        @exception IOException i/o error */
    @FXML
    public void showCustomers(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Navigate to reports screen.
        @param event the user input event
        @exception IOException i/o error */
    @FXML
    public void showreports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/reportsView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Initialize method for main menu controller.
        */
    @FXML
    public void initialize() {
        LoginMessage.loginMessage();
    }
}

