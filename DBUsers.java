package dbAccess;


import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Data access class for users. Read-only user data.
    @author Rebecca Fredricks */
public class DBUsers {

    private static User loggedInUser;
    public static void setLoggedInUser(User user){
        loggedInUser = user;
    }
    public static User getLoggedInUser(){
        return loggedInUser;
    }

    /** Get all users.
        @return the list of users */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            String selectUsers = "SELECT * FROM users";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(selectUsers);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");
                int id = resultSet.getInt("User_ID");
                User user = new User(username, password, id);
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    /** Determine if a user is valid. Attempt to locate a matching record with a given
        username and password.
        @param name the username
        @param pw the password
        @return valid or invalid */
    public static boolean isValidUser(String name, String pw){
        String selectUsers = "SELECT * FROM users WHERE User_Name = (?) AND Password = (?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectUsers)){
            ps.setString(1, name);
            ps.setString(2, pw);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                int id = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");
                setLoggedInUser(new User(username, password, id));
                return true;
            } else return false;
        } catch (SQLException ignored){
        }
        return false;
    }

    public static User getUser(int id){
        try {
            String selectUser = "SELECT * FROM users where User_ID = " + id;
            PreparedStatement p = DBConnection.getConnection().prepareStatement(selectUser);
            ResultSet r = p.executeQuery();
            if(r.next()){
                int uid = r.getInt("User_ID");
                String name = r.getString("User_Name");
                String pw = r.getString("Password");
                return (new User(name, pw, uid));
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }
}
