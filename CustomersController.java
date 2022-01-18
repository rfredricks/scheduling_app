package controller;

import dbAccess.DBAppointments;
import dbAccess.DBCustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;


import java.io.IOException;
import java.util.Optional;

/** Controller class for customers screen.
    @author Rebecca Fredricks */
public class CustomersController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Customer> customerTV;

    @FXML
    private TableColumn<Customer, Integer> idTC;

    @FXML
    private TableColumn<Customer, String> nameTC;

    @FXML
    private TableColumn<Customer, String> addressTC;

    @FXML
    private TableColumn<Customer, String> postalTC;

    @FXML
    private TableColumn<Customer, String> countryTC;

    @FXML
    private TableColumn<Customer, Integer> fldTC;

    @FXML
    private TableColumn<Customer, String> phoneTC;


    private Stage stage;
    private Parent scene;

    /** Add a new customer. Navigate to the customer edit screen without sending existing data.
        @param event button click
        @throws IOException file problem */
    @FXML
    public void addcustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/custDataEntryView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Show the appointments menu. May display appointments for selected customer only.
        @param event button click
        @exception IOException i/o error */
    @FXML
    public void appointmentsview(ActionEvent event) throws IOException {
        try{
            Alert prompt = new Alert(Alert.AlertType.CONFIRMATION,
                    "Would you like to view appointments for " +
                            customerTV.getSelectionModel().getSelectedItem().toString() + "?");
            Optional<ButtonType> result = prompt.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/appointmentsView.fxml"));
                loader.load();

                AppointmentsController appcontroller = loader.getController();

                appcontroller.sendData(customerTV.getSelectionModel().getSelectedItem());

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
            } else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/appointmentsView.fxml"));
            }
            stage.setScene(new Scene(scene));
            stage.setTitle("Scheduling application");
            stage.show();
        } catch (NullPointerException ignored){
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/appointmentsView.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Scheduling application");
            stage.show();
        }
    }

    /** Search appointment records for this customer. If any appointments exist,
        they must be deleted first due to foreign key constraints.
        @param customer the customer to search
        @return true if customer has appointment records */
    public boolean hasAppointments(Customer customer){
        ObservableList<Appointment> apps = FXCollections.observableArrayList();
        apps.clear();
        apps = DBAppointments.lookupAppointment(String.valueOf(customer.getCustomerId()),
                DBAppointments.appointment_data.ID);
        return (apps.size()>0);     //customer must have 0 appointments for safe delete
    }

    /** Delete a customer record. Delete is only allowed if all associated appointment
        records for the customer have been deleted.
        @param event button click */
    @FXML
    public void deletecustomer(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String message;
            try {
                if (hasAppointments(customerTV.getSelectionModel().getSelectedItem())) {
                    Alert hasAppointments = new Alert(Alert.AlertType.CONFIRMATION,
                            "Selected customer has appointments. Would you like \n"
                                    + "to delete all appointments and continue?");
                    Optional<ButtonType> okbtn = hasAppointments.showAndWait();
                    if (okbtn.isPresent() && okbtn.get() == ButtonType.OK){
                        if(DBCustomers.deleteCustomerWithAppointments
                                (customerTV.getSelectionModel().getSelectedItem().getCustomerId())){
                            message = customerTV.getSelectionModel().getSelectedItem().toString() + " deleted";
                        } else message = "There was a problem deleting.";
                    } else message = "Delete canceled.";
                } else if (DBCustomers.deleteCustomer(customerTV.getSelectionModel().
                        getSelectedItem().getCustomerId())) {
                    message = customerTV.getSelectionModel().getSelectedItem().toString() + " deleted.";
                    getAllCustomers();
                    customerTV.refresh();
                } else {
                    message = "There was a problem deleting.";
                }
                Alert deletemessage = new Alert(Alert.AlertType.INFORMATION, message);
                deletemessage.showAndWait();
            } catch (NullPointerException ignored) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please make a selection to continue.");
                alert.showAndWait();
            }
        }
    }

    /** Show the main menu.
        @param event button click
        @exception IOException i/o error */
    @FXML
    public void mainmenuview(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to return to the main menu? Unsaved changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainMenuView.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Scheduling application");
            stage.show();
        }
    }



    /** Prepare form for user input to modify an existing customer record.
        @param event button click
        @throws IOException file problem */
    @FXML
    public void modifycustomer(ActionEvent event) throws IOException {
        try {
            sendCustomerData(customerTV.getSelectionModel().getSelectedItem(), event);
        } catch (NullPointerException ignored) {
            Alert notify = new Alert(Alert.AlertType.ERROR, "Please make a selection");
            notify.showAndWait();
        }
    }

    /** Send existing customer data to customer edit screen.
     @param customer the existing customer
     @param event the action event
     @throws IOException file problem */
    public void sendCustomerData(Customer customer, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/custDataEntryView.fxml"));
        loader.load();

        CustDataEntryController custdatacontroller = loader.getController();

        custdatacontroller.setFields(customer);

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Search for a customer by name or phone number. Display the results of the search.
        @param event user input */
    @FXML
    public void searchcustomer(ActionEvent event) {
        String search = searchTextField.getText();
        ObservableList<Customer> allcustomers = DBCustomers.getAllCustomers();
        ObservableList<Customer> filteredcustomers = FXCollections.observableArrayList();
        for (Customer c : allcustomers){
            if(c.getCustomerName().toLowerCase().contains(search.toLowerCase())){
                filteredcustomers.add(c);
            } else if (c.getPhone().contains(search)){
                filteredcustomers.add(c);
            }
        }
        setTable(filteredcustomers);
    }

    /** Display all customers. Query the database for all customers and set the
        customers TableView to display all customers. */
    public void getAllCustomers(){
        ObservableList<Customer> allcustomers = DBCustomers.getAllCustomers();
        setTable(allcustomers);
    }

    /** Display customers. Set the customers TableView with a list of customers.
        @param customers the list of customers */
    public void setTable(ObservableList<Customer> customers){
        customerTV.setItems(customers);
        idTC.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameTC.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressTC.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalTC.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryTC.setCellValueFactory(new PropertyValueFactory<>("country"));
        fldTC.setCellValueFactory(new PropertyValueFactory<>("firstLevelDivision"));
        phoneTC.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }


    /** Initialize method for CustomersController. */
    @FXML
    public void initialize() {
        getAllCustomers();
    }
}