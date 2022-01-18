package dbAccess;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Data access class for appointments. Read/write appointment data.
    @author Rebecca Fredricks */
public class DBAppointments {

public enum appointment_data { ID , TITLE , DESC , LOC , TYPE , START , END , CUST , CONTACT }

private static String appointment_data(appointment_data a){
    String var;
    switch(a){
        case ID : var = "Appointment_ID"; break;
        case TITLE: var = "Title"; break;
        case DESC: var = "Description"; break;
        case LOC: var = "Location"; break;
        case TYPE: var = "Type"; break;
        case START: var = "Start"; break;
        case END: var = "End"; break;
        case CUST: var = "Customer_ID"; break;
        case CONTACT: var = "Contact_ID"; break;
        default: var = null;
    }
    return var;
}

/** Get all appointment records.
    @return the list of all appointments */
public static ObservableList<Appointment> getAllAppointments(){
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    String getAppointments = "SELECT * FROM appointments";
    try {
        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(getAppointments);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Timestamp start = resultSet.getTimestamp("Start");
            Timestamp end = resultSet.getTimestamp("End");
            int custId = resultSet.getInt("Customer_ID");
            int contact = resultSet.getInt("Contact_ID");
            int user = resultSet.getInt("User_ID");
            Appointment appointment = new Appointment (id, title, description, location,
                    type, start, end, custId, contact, user);
            appointments.add(appointment);
        }
    } catch(SQLException ignored) { }
    return appointments;
}

    /** Look up appointments matching any searchable attribute. Searchable attributes are appointment id,
     title, description, location, type, start, end, customer id, and contact id.
     @param lookupValue the search string to look up
     @param appdata the attribute to compare
     @return the list of matching customers */
    public static ObservableList<Appointment> lookupAppointment(String lookupValue, appointment_data appdata) {
        ObservableList<Appointment> appLookup = FXCollections.observableArrayList();
        String value = appointment_data(appdata);
        String lookupApp = "SELECT * FROM appointments WHERE "+value+" LIKE '%" + lookupValue + "%'";
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(lookupApp);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                int custId = resultSet.getInt("Customer_ID");
                int contact = resultSet.getInt("Contact_ID");
                int user = resultSet.getInt("User_ID");
                Appointment appointment = new Appointment (id, title, description, location,
                        type, start, end, custId, contact, user);
                appLookup.add(appointment);
            }
        }
        catch(SQLException ignored){ }
        return appLookup;
    }

    /** Get any upcoming appointment for logged in user within the next 15 minutes. If there is an
        upcoming appointment within 15 minutes of the current time, it will be retrieved.
        @return the appointment if found */
    public static Appointment getUpcomingAppointment(LocalDateTime systemNow, int userId){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String getApp =
                "select * from appointments where User_ID = (?) and Start between (?) AND (?)";
        try{
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getApp);
            ps.setInt(1, userId);
            ps.setString(2, systemNow.format(dtf));
            ps.setString(3, systemNow.plusMinutes(15).format(dtf));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int cust = rs.getInt("Customer_ID");
                int user = rs.getInt("User_ID");
                int contact = rs.getInt("Contact_ID");
                return new Appointment(id, title, desc, loc, type, start, end, cust, contact, user);
            } else return null;
        }
        catch(SQLException ignored){ return null; }
    }

    /** Update an existing appointment record.
        @param appointment the new appointment data
        @param id the id to update
        @return successful/unsuccessful */
    public static boolean updateAppointment(Appointment appointment, int id){
        String update = "UPDATE appointments SET Title = (?), Description = (?), Location = (?), " +
                " Type = (?), Start = (?), End = (?), Customer_ID = (?), User_ID = (?)," +
                " Contact_ID = (?) WHERE Appointment_ID = " + id;
        try(PreparedStatement ps = DBConnection.getConnection().prepareStatement(update)){
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, appointment.getStart());
            ps.setTimestamp(6, appointment.getEnd());
            ps.setInt(7, appointment.getCustomerId());
            ps.setInt(8, appointment.getUserId());
            ps.setInt(9, appointment.getContactId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ignored) { return false; }
    }

    /** Add a new appointment record.
     @param appointment the appointment to add
     @return successful/unsuccessful */
    public static boolean addAppointment(Appointment appointment){
        String add = "INSERT INTO appointments(Title, Description, Location, Type, Start, End," +
                "Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = DBConnection.getConnection().prepareStatement(add)){
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, appointment.getStart());
            ps.setTimestamp(6, appointment.getEnd());
            ps.setInt(7, appointment.getCustomerId());
            ps.setInt(8, appointment.getUserId());
            ps.setInt(9, appointment.getContactId());
            ps.executeUpdate();
            return true;
        }
        catch (SQLException ignored){ return false; }
    }

    /** Delete an appointment record.
     * @param id the id of the appointment to delete
     * @return successful/unsuccessful
     */
    public static boolean deleteAppointment(int id){
        String delete = "DELETE FROM appointments WHERE Appointment_ID="+id;
        try{
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(delete);
            ps.executeUpdate();
            return true;
        } catch (SQLException ignored){ return false; }
    }
}
