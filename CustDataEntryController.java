package controller;

import dbAccess.DBCountries;
import dbAccess.DBCustomers;
import dbAccess.DBFirstLevelDivisions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import model.Country;
import model.FirstLevelDivision;

import java.io.IOException;
import java.util.Optional;

/** Controller class for customer edit screen.
    @author Rebecca Fredricks */
public class CustDataEntryController {

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField postalTextField;

    @FXML
    private ComboBox<Country> countryCB;

    @FXML
    private ComboBox<FirstLevelDivision> fldCB;

    @FXML
    private TextField phoneTextField;

    /** Cancel customer data entry. Prompt user to confirm cancellation.
        @param event the action event
        @throws IOException file problem */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to cancel? Changes you made will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            showCustomers(event);
        }
    }

    /** Return to the customers menu.
     @param event the action event
     @throws IOException file problem */
    public void showCustomers(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Scheduling application");
        stage.show();
    }

    /** Save customer data. Notify user if save is successful or prompt user if errors are detected.
        This method uses a lambda to prompt the user if there are errors. Lambda use is justified
        by streamlining the code, reducing redundancy and making it easier to read.
        @param event the action event
        @throws IOException file problem */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postal = postalTextField.getText();
        FirstLevelDivision fld = fldCB.getSelectionModel().getSelectedItem();
        String phone = phoneTextField.getText();
        if (name == null || address == null || postal == null || phone == null || fld == null){
            errorMessage.show("Enter valid values");
        } else {
            Customer newCust = new Customer(name, address, postal, phone, fld.getDivisionId());
            //test if id field is blank
            if (idTextField.getText().isBlank()) {
                saveSuccessful(DBCustomers.addCustomer(newCust));
                showCustomers(event);
            } else {
                try {
                    saveSuccessful(DBCustomers.updateCustomer(newCust,
                            Integer.parseInt(idTextField.getText())));
                    showCustomers(event);
                } catch (NumberFormatException ignored) {
                }
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

    /** Populate data when modifying an existing customer.
        @param customer the existing customer */
    public void setFields(Customer customer){
        idTextField.setText(Integer.toString(customer.getCustomerId()));
        nameTextField.setText(customer.getCustomerName());
        addressTextField.setText(customer.getAddress());
        postalTextField.setText(customer.getPostalCode());
        countryCB.getSelectionModel().select(customer.getCountry());
        fldCB.setItems(DBFirstLevelDivisions.getLocalfld(countryCB.getValue()));
        fldCB.getSelectionModel().select(customer.getFirstLevelDivision());
        phoneTextField.setText(customer.getPhone());
    }

    /** Filter first level divisions when a country is selected. Only display first level divisions
        associated with the selected country.
        @param event the action event */
    @FXML
    public void onActionFilter(ActionEvent event) {
        fldCB.getSelectionModel().clearSelection();
        fldCB.setItems(DBFirstLevelDivisions.getLocalfld(countryCB.getSelectionModel().getSelectedItem()));
    }

    /** Initialize method for customer data entry controller. */
    @FXML
    public void initialize(){
        countryCB.setItems(DBCountries.getAllCountries());
    }

}
