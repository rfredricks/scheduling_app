package model;

/** The User class represents users.
    @author Rebecca Fredricks */
public class User {

    private String userName;
    private String password;
    private int userId;

    public User(String name, String pw, int id){
        this.userName = name;
        this.password = pw;
        this.userId = id;
    }

    /** Get username.
        @return the username */
    public String getUserName() {
        return userName;
    }

    /** Set username.
        @param userName the username */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Get password.
        @return the password */
    public String getPassword() {
        return password;
    }

    /** Set password.
        @param password the password */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Get user id.
        @return the user id */
    public int getUserId() { return userId; }

    /** Set user id.
        @param id the user id */
    public void setUserId(int id) { this.userId = id; }

    /** To String method for User.
        @return User as String */
    @Override
    public String toString(){
        return userName;
    }
}
