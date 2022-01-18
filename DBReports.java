package dbAccess;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/** Data access class for reports.
    @author Rebecca Fredricks */
public class DBReports {

    /** Get report of appointments listed by month and type.
        @return the report data */
    public static String getAppsByTypeMonth(){
        StringBuilder str = new StringBuilder();
        String getReport = "SELECT Type, MONTH(Start), COUNT(*) FROM appointments " +
                "GROUP BY Type, MONTH(Start) ORDER BY MONTH(Start)";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getReport);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                str.append(Month.of(rs.getInt("MONTH(Start)")).name()); str.append("\t");
                str.append(rs.getString("Type")); str.append("\t");
                str.append(rs.getInt("COUNT(*)")); str.append("\n");
            }
        }
        catch(SQLException ignored){
        }
        return str.toString();
    }

    /** Get report of schedule for each contact.
        @return the report data */
    public static String getContactSchedule(){
        StringBuilder str = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        String getReport = "select c.Contact_Name, a.Appointment_ID, a.Title, a.Type, " +
                "a.Description, a.Start, a.End, a.Customer_ID " +
                "from appointments as a " +
                "left outer join contacts as c on c.Contact_ID = a.Contact_ID " +
                "order by c.Contact_Name";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getReport);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                str.append(rs.getString("Contact_Name")).append("\t");
                str.append(rs.getInt("Appointment_ID")).append("\t");
                str.append(rs.getString("Title")).append("\t");
                str.append(rs.getString("Type")).append("\t");
                str.append(rs.getString("Description")).append("\t");
                str.append((rs.getTimestamp("Start")).toLocalDateTime().format(dtf)).append("\t");
                str.append((rs.getTimestamp("End")).toLocalDateTime().format(dtf)).append("\t");
                str.append(rs.getInt("Customer_ID")).append("\n");
            }
        }
        catch(SQLException ignored){
        }
        return str.toString();
    }

    /** Get a report listing customer appointment summary for the past month.
        @return the report data */
    public static String getCountLastMonth(){
        StringBuilder str = new StringBuilder();
        String getReport = "select customer_ID, COUNT(Customer_ID) from appointments " +
                "where Start > NOW() - interval 30 day group by customer_ID";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getReport);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("Customer_ID");
                str.append(id).append("\t    ").append(DBCustomers.getCustomer(id)).append("\t\t");
                str.append(rs.getInt("COUNT(Customer_ID)")).append("\n");
            }
        }
        catch(SQLException ignored){ }
        return str.toString();
    }


}
