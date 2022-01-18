package dbAccess;


import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Data access class for first level divisions. Read-only access
    for first level division data.
    @author Rebecca Fredricks */
public class DBFirstLevelDivisions {

    /** Get all first level division records.
     @return the list of all first level divisions */
    public static ObservableList<FirstLevelDivision> getAllfld(){
        ObservableList<FirstLevelDivision> fld = FXCollections.observableArrayList();

        try {
            String selectFLD = "SELECT f.Division_ID, f.Division, c.Country_ID, c.Country FROM" +
                    " first_level_divisions as f LEFT OUTER JOIN countries AS c ON f.COUNTRY_ID = c.Country_ID";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(selectFLD);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                FirstLevelDivision firstLevelDivision = new
                        FirstLevelDivision(divisionId, division, new Country(countryName, countryId));
                fld.add(firstLevelDivision);
            }
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return fld;
    }

    /** Get all first level division records for a specified country id.
        @param country the country to filter by
        @return the list of matching first level division records */
    public static ObservableList<FirstLevelDivision> getLocalfld(Country country){
        ObservableList<FirstLevelDivision> fld = FXCollections.observableArrayList();

        try {
            String selectContacts = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID=" + country.getCountry_ID();
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(selectContacts);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(divisionId, division, country);
                fld.add(firstLevelDivision);
            }
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return fld;
    }

}
