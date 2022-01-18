package dbAccess;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Data access class for contacts. Read-only contact data.
    @author Rebecca Fredricks */
public class DBContacts {

    /** Get all contact records.
        @return the list of all contacts */
    public static ObservableList<Contact> getAllContacts(){
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        try {
            String selectContacts = "SELECT * FROM contacts";
            PreparedStatement p = DBConnection.getConnection().prepareStatement(selectContacts);
            ResultSet r = p.executeQuery();
            while(r.next()){
                int contactId = r.getInt("Contact_ID");
                String contactName = r.getString("Contact_Name");
                String contactEmail = r.getString("Email");
                Contact contact = new Contact(contactId, contactName, contactEmail);
                contacts.add(contact);
            }
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return contacts;
    }

    /** Retrieve a contact record.
        @param id the id of the contact to retrieve */
    public static Contact getContact(int id){
        try {
            String selectContact = "SELECT * FROM contacts where Contact_ID = " + id;
            PreparedStatement p = DBConnection.getConnection().prepareStatement(selectContact);
            ResultSet r = p.executeQuery();
            if (r.next()){
                int contactId = r.getInt("Contact_ID");
                String contactName = r.getString("Contact_Name");
                String contactEmail = r.getString("Email");
                return new Contact(contactId, contactName, contactEmail);
            } else return null;
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }
}
