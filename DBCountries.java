package dbAccess;


import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Data access class for countries. Read-only country data.
    @author Rebecca Fredricks */
public class DBCountries {

    /** Get all country records.
        @return the list of all countries */
    public static ObservableList<Country> getAllCountries(){
        ObservableList<Country> clist = FXCollections.observableArrayList();

        try {
            String selectcountries = "SELECT * FROM countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectcountries);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country c = new Country(countryName, countryId);
                clist.add(c);
            }
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return clist;
    }
}
