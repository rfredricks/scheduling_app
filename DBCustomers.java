package dbAccess;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Data access class for Customers. Read/write customer data.
    @author Rebecca Fredricks */
public class DBCustomers {

    /** Get all customer records.
        @return list of Customer objects */
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String createCustomerView = "CREATE OR REPLACE VIEW v_customers AS SELECT c.Customer_ID, " +
                "c.Customer_Name, c.Address, c.Postal_Code, c.Phone, f.Division_ID, f.Division, " +
                "t.Country_ID, t.Country FROM customers AS c " +
                "LEFT OUTER JOIN first_level_divisions as f on c.Division_ID = f.Division_ID " +
                "LEFT OUTER JOIN countries as t on f.COUNTRY_ID = t.Country_Id; ";
        String getCustomers = "SELECT * FROM v_customers";
        try {
            PreparedStatement viewStatement = DBConnection.getConnection().prepareStatement(createCustomerView);
            viewStatement.executeUpdate();
            PreparedStatement getCustomerStatement = DBConnection.getConnection().prepareStatement(getCustomers);
            ResultSet resultSet = getCustomerStatement.executeQuery();
            while(resultSet.next()){
                int custid = resultSet.getInt("Customer_ID");
                String custname = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postal = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                int divid = resultSet.getInt("Division_ID");
                String divname = resultSet.getString("Division");
                int countryid = resultSet.getInt("Country_Id");
                String countryname = resultSet.getString("Country");
                Customer customer = new Customer(custid, custname, address, postal, phone,
                        new FirstLevelDivision(divid, divname,
                                new Country(countryname, countryid)));
                customers.add(customer);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customers;
    }

    /** Add a new customer record.
     @param customer the customer to add
     @return successful */
    public static boolean addCustomer(Customer customer){
        String addCustomer = "INSERT INTO customers(Customer_Name, Address, Postal_Code, "+
                "Phone, Division_ID) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(addCustomer)) {
            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getDivisionId());
            ps.executeUpdate();
            return true;
        }
        catch (SQLException ignored){
            return false;
        }
    }

    /** Update an existing customer record.
     @param customer the new customer data
     @param id the id of the existing customer to overwrite
     @return successful */
    public static boolean updateCustomer(Customer customer, int id){
        String updateCustomer = "UPDATE customers SET Customer_Name = (?),Address = (?),"+
                " Postal_Code = (?), Phone = (?), Division_ID = (?) WHERE Customer_ID = " +id;
        try(PreparedStatement ps = DBConnection.getConnection().prepareStatement(updateCustomer)){
            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getDivisionId());
            ps.executeUpdate();
            return true;
        }
        catch(SQLException ignored){
            return false;
        }
    }

    /** Delete a customer record.
     @param id the id of the customer to delete
     @return successful */
    public static boolean deleteCustomer(int id){
        try {
            String deleteCustomer = "DELETE FROM customers WHERE Customer_ID = " + id;
            PreparedStatement p = DBConnection.getConnection().prepareStatement(deleteCustomer);
            p.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    /** Delete a customer record where the customer has appointments.
        @param id the id of the customer to delete
        @return successful */
    public static boolean deleteCustomerWithAppointments(int id){
        String delete = "DELETE FROM appointments WHERE Customer_ID = (?); \n DELETE FROM " +
                "customers WHERE Customer_ID = (?);";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(delete);
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ignored) { }
        return false;
    }

    /** Retrieve a customer record.
        @param id the id of the customer to retrieve
        @return the Customer object */
    public static Customer getCustomer(int id) {
        try {
            String selectCustomer = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, " +
                    "f.Division_ID, f.Division, t.Country_ID, t.Country FROM customers AS c " +
                    "LEFT OUTER JOIN first_level_divisions as f on c.Division_ID = f.Division_ID " +
                    "LEFT OUTER JOIN countries as t on f.COUNTRY_ID = t.Country_Id WHERE Customer_ID = " + id;
            PreparedStatement p = DBConnection.getConnection().prepareStatement(selectCustomer);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                int customerid = r.getInt("Customer_ID");
                String name = r.getString("Customer_Name");
                String address = r.getString("Address");
                String post = r.getString("Postal_Code");
                String phone = r.getString("Phone");
                int divid = r.getInt("Division_ID");
                String divname = r.getString("Division");
                int countryid = r.getInt("Country_ID");
                String countryname = r.getString("Country");
                return (new Customer(customerid, name, address, post,
                        phone, new FirstLevelDivision(divid, divname,
                        new Country(countryname, countryid))));
            } else return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
