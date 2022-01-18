package controller;


import dbAccess.DBAppointments;
import dbAccess.DBContacts;
import dbAccess.DBCustomers;
import dbAccess.DBUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;

/** Controller class for appointment edit screen.
    @author Rebecca Fredricks */
public class AppDataEntryController {

    @FXML
    private TextField idTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField descTextField;

    @FXML
    private TextField locTextField;

    @FXML
    private ComboBox<Contact> contactCB;

    @FXML
    private ComboBox<String> typeCB;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<LocalTime> startCB;

    @FXML
    private ComboBox<LocalTime> durationCB;

    @FXML
    private ComboBox<Customer> customerCB;

    @FXML
    private ComboBox<User> userCB;

    private ObservableList<String> types = FXCollections.observableArrayList();
    private final ZoneId eastern = ZoneId.of("America/New_York");

    /** Initialize method for appointment editor screen. */
    @FXML
    public void initialize(){
        types.setAll("Presentation","Briefing","Debriefing","Planning Session",
                "Consultation","Informational Interview");
        typeCB.setItems(types);
        contactCB.setItems(DBContacts.getAllContacts());
        userCB.setItems(DBUsers.getAllUsers());
        customerCB.setItems(DBCustomers.getAllCustomers());
        startDatePicker.setValue(LocalDate.now());
        startCB.setItems(availableTimes());
    }

    /** Cancel appointment data entry. Prompt user to confirm cancellation.
        @param event the action event
        @throws IOException file problem */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to cancel? \nChanges you made will be lost.");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            showAppointments(event);
        }
    }

    /** Save appointment data. Notify user if save is successful or prompt user if errors are detected.
        @param event the action event
        @throws IOException file problem */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        int appId;
        if(idTextField.getText().isBlank()) { appId = -1; } else { appId = Integer.parseInt(idTextField.getText()); }
        String title = titleTextField.getText();
        String desc = descTextField.getText();
        String loc = locTextField.getText();
        String type = null;
        int contId = 0;
        int custId = 0;
        int userId = 0;
        LocalDateTime startdate = null;
        LocalDateTime enddate = null;
        try {
            type = typeCB.getSelectionModel().getSelectedItem();
            contId = contactCB.getSelectionModel().getSelectedItem().getContactId();
            custId = customerCB.getSelectionModel().getSelectedItem().getCustomerId();
            userId = userCB.getSelectionModel().getSelectedItem().getUserId();
            LocalTime starttime = startCB.getValue();
            startdate = startDatePicker.getValue().atTime(starttime);
            LocalTime endtime = durationCB.getValue();
            enddate = startDatePicker.getValue().atTime(endtime);
        } catch (NullPointerException ignored) {
            errorMessage.show("Make a valid selection");
        }
        if (title == null || desc == null || loc == null || type == null){
            errorMessage.show("Enter valid values");
        } else {
            Appointment newApp = new Appointment(title, desc, loc, type, Timestamp.valueOf(startdate),
                    Timestamp.valueOf(enddate), custId, contId, userId);
            if (checkForOverlaps(startdate, enddate, appId, custId)) {
                Alert overlap = new Alert(Alert.AlertType.ERROR,
                        "Selected time conflicts with another scheduled appointment.");
                overlap.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                overlap.showAndWait();
            } else if (appId >= 0) {
                saveSuccessful(DBAppointments.updateAppointment(newApp, appId));
                showAppointments(event);
            } else {
                saveSuccessful(DBAppointments.addAppointment(newApp));
                showAppointments(event);
            }
        }
    }

    Messages errorMessage = (String s) -> {
        Alert alert = new Alert(Alert.AlertType.ERROR, s);
        alert.showAndWait();
    };

    /** Inform if save was successful.
        @param save yes/no */
    public void saveSuccessful(boolean save){
        String message;
        if(save){ message = "Save successful"; }
        else { message = "Save failed"; }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    /** Populate data when modifying existing appointment.
        @param appointment the appointment data to modify */
    public void setFields(Appointment appointment){
        idTextField.setText(Integer.toString(appointment.getAppointmentId()));
        titleTextField.setText(appointment.getTitle());
        descTextField.setText(appointment.getDescription());
        locTextField.setText(appointment.getLocation());
        contactCB.getSelectionModel().select(DBContacts.getContact(appointment.getContactId()));
        customerCB.getSelectionModel().select(DBCustomers.getCustomer(appointment.getCustomerId()));
        userCB.getSelectionModel().select(DBUsers.getUser(appointment.getUserId()));
        typeCB.getSelectionModel().select(appointment.getType());
        startDatePicker.setValue(appointment.getStart().toLocalDateTime().toLocalDate());
        startCB.setValue(appointment.getStart().toLocalDateTime().toLocalTime());
        onActionGetEndTimes();
        durationCB.setValue(appointment.getEnd().toLocalDateTime().toLocalTime());
    }

    /** Return to the appointment menu.
        @param event the action event
        @throws IOException file problem */
    public void showAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/appointmentsView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Return a list of available times based on the business start and end times of 8:00 AM
     and 10:00 PM EST. Times are available in 15 minute intervals.
     */
    public ObservableList<LocalTime> availableTimes(){
        ObservableList<LocalTime> times = FXCollections.observableArrayList();
        ZoneId local = ZoneId.systemDefault();
        ZonedDateTime zdt_open_eastern = ZonedDateTime.of(startDatePicker.getValue().atTime(8,0,0,0), eastern);
        ZonedDateTime zdt_local_open = zdt_open_eastern.withZoneSameInstant(local);
        ZonedDateTime zdt_close_eastern = ZonedDateTime.of(startDatePicker.getValue().atTime(22,0,0,0), eastern);
        ZonedDateTime zdt_local_close = zdt_close_eastern.withZoneSameInstant(local);

        for(LocalTime lt = zdt_local_open.toLocalTime(); lt.isBefore(zdt_local_close.toLocalTime()); lt = lt.plusMinutes(15)){
            times.add(lt);
        }
        return times;
    }

    public ObservableList<LocalTime> availableTimes(LocalTime startTime){
        ObservableList<LocalTime> times = FXCollections.observableArrayList();
        ZoneId local = ZoneId.systemDefault();
        ZonedDateTime zdt_close_eastern = ZonedDateTime.of(startDatePicker.getValue().atTime(22,0,0,0), eastern);
        ZonedDateTime zdt_local_close = zdt_close_eastern.withZoneSameInstant(local);

        for (LocalTime lt = startTime.plusMinutes(15);
             lt.isBefore(zdt_local_close.toLocalTime()) || lt.equals(zdt_local_close.toLocalTime());
             lt = lt.plusMinutes(15)){
            times.add(lt);
        }
        return times;
    }

    /** When a start time is selected, show valid end times. Appointment minimum duration is 15 minutes
        and appointments are available in 15 minute increments within business hours.
        @param event the action event */
    @FXML
    public void onActionGetEndTimes(ActionEvent event) {
        durationCB.setItems(availableTimes(startCB.getValue()));
    }

    public void onActionGetEndTimes() {
        durationCB.setItems(availableTimes(startCB.getValue()));
    }

    /** Check for overlapping times when saving an appointment.
        @param start new appointment start time
        @param end new appointment end time
        @param appointmentId appointment being edited (if applicable)
        @param customerId customer the appointment is for
        @return true if conflicts exist */
    public boolean checkForOverlaps(LocalDateTime start, LocalDateTime end, int appointmentId, int customerId) {
        ObservableList<Appointment> appointments =
                DBAppointments.lookupAppointment(Integer.toString(customerId),
                        DBAppointments.appointment_data.CUST);
        appointments.removeIf(a -> a.getAppointmentId() == appointmentId); //don't count appointment being edited
        int value = 1;
        try {
            for (Appointment a : appointments) {
                if ((start.isAfter(a.getStart().toLocalDateTime())) && (start.isBefore(a.getEnd().toLocalDateTime()))
                    || ((end.isAfter(a.getStart().toLocalDateTime())) && (end.isBefore(a.getEnd().toLocalDateTime())))
                    || ((start.isEqual(a.getStart().toLocalDateTime())) || (end.isEqual(a.getEnd().toLocalDateTime())))
                    || ((start.isBefore(a.getStart().toLocalDateTime())) && (end.isAfter(a.getEnd().toLocalDateTime()))))
                { --value; }
            }
        } catch (NullPointerException ignored){ }
        return (value < 1);
    }
}