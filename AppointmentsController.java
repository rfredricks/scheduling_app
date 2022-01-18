package controller;

import dbAccess.DBAppointments;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/** Controller class for appointments menu.
    @author Rebecca Fredricks */
public class AppointmentsController {

    @FXML
    public Button reportsbtn;

    @FXML
    private TableView<Appointment> apptTableView;

    @FXML
    private TableColumn<Appointment, Integer> idTC;

    @FXML
    private TableColumn<Appointment, String> titleTC;

    @FXML
    private TableColumn<Appointment, String> descTC;

    @FXML
    private TableColumn<Appointment, String> locTC;

    @FXML
    private TableColumn<Appointment, Integer> contactTC;

    @FXML
    private TableColumn<Appointment, String> typeTC;

    @FXML
    private TableColumn<Appointment, Timestamp> startTC;

    @FXML
    private TableColumn<Appointment, Timestamp> endTC;

    @FXML
    private TableColumn<Appointment, Integer> customerTC;

    @FXML
    private TableColumn<Appointment, Integer> userTC;


    private Stage stage;
    private Parent scene;

    /** Prepare the form for user input to add a new appointment record.
        @param event the button press event
        @throws IOException file problem */
    @FXML
    public void addappt(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appDataEntryView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Navigate to the customers menu.
        @param event the button press event
        @throws IOException invalid filename */
    @FXML
    public void customerview(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Delete an existing appointment.
        @param event the button press event */
    @FXML
    public void deleteappt(ActionEvent event) {
        try{
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to cancel " +
                    apptTableView.getSelectionModel().getSelectedItem().toString() + "?");
            confirm.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String message;
                if (DBAppointments.deleteAppointment(apptTableView.getSelectionModel().getSelectedItem().getAppointmentId())) {
                    message = apptTableView.getSelectionModel().getSelectedItem().toString() + " canceled";
                } else {
                    message = "There was a problem canceling the appointment.";
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
                setApptTableView(DBAppointments.getAllAppointments());
                apptTableView.refresh();
            }
        } catch (NullPointerException ignored){
            Alert notify = new Alert(Alert.AlertType.ERROR, "Please make a selection");
            notify.showAndWait();
        }
    }

    /** Navigate to the main menu.
        @param event the button press event
        @throws IOException invalid filename */
    @FXML
    public void mainmenuview(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/mainMenuView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Modify an existing appointment.
        @param event the button press event
        @throws IOException file problem */
    @FXML
    public void modifyappt(ActionEvent event) throws IOException {
        try {
            sendAppointmentData(apptTableView.getSelectionModel().getSelectedItem(), event);
        } catch (NullPointerException ignored) {
            Alert notify = new Alert(Alert.AlertType.ERROR, "Please make a selection");
            notify.showAndWait();
        }
    }

    /** Initialize method for appointments menu controller. */
    @FXML
    public void initialize() {
        setApptTableView(DBAppointments.getAllAppointments());
    }

    /** Add appointments to main TableView.
        @param appointments the appointments to add */
    public void setApptTableView(ObservableList<Appointment> appointments){
        apptTableView.setItems(appointments);
        idTC.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        descTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        locTC.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTC.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactTC.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        customerTC.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userTC.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    /** Navigate to the reports screen.
        @param event user input
        @throws IOException invalid filename */
    @FXML
    public void viewreports(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/reportsView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Display appointments for a selected customer. Used to view all appointments for a particular
        customer.
        @param customer the selected customer */
    public void sendData(Customer customer){
        apptTableView.setItems(DBAppointments.lookupAppointment(String.valueOf(customer.getCustomerId()), DBAppointments.appointment_data.CUST));
    }

    /** Send existing appointment data to appointment edit screen.
        @param appointment the existing appointment
        @param event the action event
        @throws IOException file problem */
    public void sendAppointmentData(Appointment appointment, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/appDataEntryView.fxml"));
        loader.load();

        AppDataEntryController appdatacontroller = loader.getController();

        appdatacontroller.setFields(appointment);

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }


    /** Display all appointments.
        @param event user menu button choice */
    @FXML
    public void viewall(ActionEvent event) {
        setApptTableView(DBAppointments.getAllAppointments());
        apptTableView.refresh();
    }

    /** Display calendar of appointments for the next 30 days.
        @param event user menu button choice */
    @FXML
    public void viewmonth(ActionEvent event) {
        FilteredList<Appointment> flist = new FilteredList<>(DBAppointments.getAllAppointments(), p -> true);
        flist.setPredicate( appointment -> {
            LocalDateTime ldt = LocalDateTime.now().minusDays(1);
            return (appointment.getStart().after(Timestamp.valueOf(ldt)) && appointment.getStart().before(Timestamp.valueOf(ldt.plusDays(31))));
        });
        SortedList<Appointment> slist = new SortedList<>(flist);
        slist.comparatorProperty().bind(apptTableView.comparatorProperty());
        setApptTableView(slist);
        apptTableView.refresh();
    }

    /** Display calendar of appointments for the next 7 days.
        This method uses lambda to filter and sort a list. Lambda use is justified by greatly simplifying
        the code and enhancing readability. Fewer lines of code are needed, thus making the code easier to
        read and maintain.
        @param event user menu button choice */
    @FXML
    public void viewweek(ActionEvent event) {
        FilteredList<Appointment> flist = new FilteredList<>(DBAppointments.getAllAppointments(), p -> true);
        flist.setPredicate( appointment -> {
            LocalDateTime ldt = LocalDateTime.now().minusDays(1);
            return (appointment.getStart().after(Timestamp.valueOf(ldt)) && appointment.getStart().before(Timestamp.valueOf(ldt.plusDays(8))));
        });
        SortedList<Appointment> slist = new SortedList<>(flist);
        slist.comparatorProperty().bind(apptTableView.comparatorProperty());
        setApptTableView(slist);
        apptTableView.refresh();
    }
}

