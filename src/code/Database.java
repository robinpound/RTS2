package code;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection = null;
    private Statement statement;
    Database(String url) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("DROP TABLE users");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (id Integer PRIMARY_KEY, username STRING, password STRING)");// IF NOT EXIST users
            //statement.executeUpdate("CREATE TABLE users (id PRIMARY_KEY, username STRING, password STRING) IF NOT EXIST users");
            //statement.executeUpdate("CREATE TABLE users (id PRIMARY_KEY, username STRING, password STRING) IF NOT EXIST users");


            //statement.executeUpdate("INSERT INTO users VALUES(1, 'robin', 'password123')");

        }catch(SQLException e) {System.err.println(e.getMessage());}

    }
    public Boolean LoginUserCheck(String username, String password){
        try {
            ResultSet result = statement.executeQuery("SELECT COUNT (*) FROM users WHERE username = '" + username + "' AND password = '" + password + "'");
            System.out.println(result.getInt(1));
            if (result.getInt(1) > 0) {
                return true;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void closeDatabase(){

        try{if(connection != null) connection.close();}
        catch(SQLException e){System.err.println(e.getMessage());}
    }

    public Boolean RegisterUser(String username, String password1, String password2){
        try{
            //Trimming
            String trailing_space = "\\s+$";
            String leading_space = "^\\s+";
            String trimmed_username = username.replaceAll(trailing_space,"").replaceAll(leading_space,"");

            ResultSet result = statement.executeQuery("SELECT COUNT (*) FROM users WHERE username = '" + trimmed_username + "'");//check for existence
            System.out.println(trimmed_username.length());
            if (result.getInt(1) > 0 || !password1.equals(password2) || trimmed_username.length() == 0 || password1.length() == 0) {
                return false;
            }else{
                ResultSet howlongsql = statement.executeQuery("SELECT COUNT (*) FROM users");
                int key = howlongsql.getInt(1)+1;
                statement.executeUpdate(String.format("INSERT INTO users VALUES(%d, '%s', '%s')",null,trimmed_username,password1));
                return true;
            }
        }catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }
}
