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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (id PRIMARY_KEY, username STRING, password STRING)");// IF NOT EXIST users
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
            String leading_space = "^\\s";
            String trimmed_username = username.replaceAll(trailing_space,"").replaceAll(leading_space,"");

            ResultSet result = statement.executeQuery("SELECT COUNT (*) FROM users WHERE username = '" + trimmed_username + "'");//check for existence
            System.out.println(trimmed_username.length());
            if (result.getInt(1) > 0 || !password1.equals(password2) || trimmed_username.length() == 0 || password1.length() == 0) {
                return false;
            }else{
                ResultSet howlongsql = statement.executeQuery("SELECT COUNT (*) FROM users");
                int key = howlongsql.getInt(1)+1;
                statement.executeUpdate(String.format("INSERT INTO users VALUES(%d, '%s', '%s')",key,trimmed_username,password1));
                return true;
            }
        }catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }
}


/*
public class Database {
    public static void start() {
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:RocketTrajectorySimulatorDatabase.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}
 */
