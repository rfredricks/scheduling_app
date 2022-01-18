package controller;

import dbAccess.DBReports;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

/** Controller class for scheduling application reports menu.
    @author Rebecca Fredricks */
public class ReportsViewController {

    @FXML
    private TextArea reportTextArea;

    private Stage stage;
    private Parent scene;

    /** Navigate to the reports screen.
        @param event user input event
        @exception IOException i/o error */
    @FXML
    public void appointmentsview(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointmentsView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Generate a report showing monthly schedule of appointments grouped by appointment type.
        @param event user input event */
    @FXML
    public void appsbytypemonth(ActionEvent event) {
        reportTextArea.setText("Count\tMonth\tType\t\n" + DBReports.getAppsByTypeMonth());
    }

    /** Navigate to the main menu.
        @param event user input event
        @exception IOException i/o error */
    @FXML
    public void mainmenuview(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/mainMenuView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Generate a report showing schedule of appointments grouped by contact.
        @param actionEvent user input event */
    @FXML
    public void schedulebycontact(ActionEvent actionEvent) {
        reportTextArea.setText("Contact Name\tAppointment ID\tTitle\tType\tDescription\t" +
                "Start\tEnd\tCustomer ID\n" + DBReports.getContactSchedule());
    }

    /** Generate a report showing weekly schedule of appointments grouped by location.
        @param actionEvent user input event */
    @FXML
    public void appsLastMonth(ActionEvent actionEvent) {
        reportTextArea.setText("Customer ID\tCustomer name\tAppointments in last 30 days\n" +
                DBReports.getCountLastMonth());
    }

    /** Initialize method for reports menu controller.
        */
    @FXML
    public void initialize() {
        reportTextArea.setMinHeight(Region.USE_PREF_SIZE);
        reportTextArea.clear();
    }
}