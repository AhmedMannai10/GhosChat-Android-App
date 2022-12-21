import entity.User;

import java.sql.*;
import java.util.ArrayList;

public class DataBaseHelper {

    public static String url = "jdbc:sqlite:/home/ghost/Documents/sqlite/";


    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:/home/ghost/Documents/sqlite/" + fileName + ".db";

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static Connection connect(String dbFileName) {
        Connection conn = null;

        try {
            // db parameters
            String urll = url+ dbFileName + ".db";
            // create a connection to the database
            conn = DriverManager.getConnection(urll);

            System.out.println("Connection to SQLite db ["+ dbFileName +"] has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }


    public static void createTableIfNotExist(Connection conn){

        String sql = "CREATE TABLE IF NOT EXISTS "+
                UserTable.NAME+" ("+
                UserTable.Cols.NAME + " varchar(20)," +
                UserTable.Cols.EMAIL + " varchar(20) primary key NOT NULL," +
                UserTable.Cols.PASSWORD + " varchar(20) NOT NULL)" ;

        try{
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table (user) Has been created");

        }catch(SQLException e){
            System.out.println("Error");
            System.out.println(e.getMessage());}

    }


    public static Boolean insert(Connection conn, User user){
        try{
            Statement stmt = conn.createStatement();
            String sqlInsertQuery = "INSERT INTO user VALUES( '"
                    + user.getName()
                    + "', '" + user.getEmail()
                    + "', '"+ user.getPassword()
                    +"')";
            System.out.println(sqlInsertQuery);

            stmt.executeUpdate(sqlInsertQuery);

            stmt.close();
            System.out.println("Insert " + user + " Successfully");
            return true;

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            System.out.println(user + " Already Exist");
        };
        return false;
    }

    public static User  getUserFromEmail(Connection conn, String email){

        try{
            Statement stmt = conn.createStatement();
            String sqlQueryWhere = "SELECT * FROM "+ UserTable.NAME
                    +" WHERE " +
                    UserTable.Cols.EMAIL
                    +" = '" +
                    email+"'" ;
            System.out.println(sqlQueryWhere);
            ResultSet rs = stmt.executeQuery(sqlQueryWhere);
            while(rs.next()){
                String userName = rs.getString(UserTable.Cols.NAME);
                String userEmail = rs.getString(UserTable.Cols.EMAIL);
                String passwordName = rs.getString(UserTable.Cols.PASSWORD);
                 User user = new User(userName, userEmail, userEmail);
                 stmt.close();
                return user;
            }

        }catch (Exception ex){
//            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        System.out.println("There is no user with this email ['" +
                email +
                "']");
        return null;
    }

     public static ArrayList<User>  getAllUsers(Connection conn){
         ArrayList<User> users = new ArrayList<User>();

        try{
            Statement stmt = conn.createStatement();
            String sqlQuery = "SELECT * FROM "+ UserTable.NAME;
            System.out.println(sqlQuery);
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                String userName = rs.getString(UserTable.Cols.NAME);
                String userEmail = rs.getString(UserTable.Cols.EMAIL);
                String passwordName = rs.getString(UserTable.Cols.PASSWORD);
                User user = new User(userName, userEmail, userEmail);
                users.add(user);
            }
            rs.close();
            stmt.close();

        }catch (Exception ex){ex.printStackTrace();}
        return users;
    }

    public static void main(String[] args){
//        createNewDatabase("user");

        Connection conn = connect("user");
        createTableIfNotExist(conn);
        User ahmed = new User("Ahmed", "ahmed@gmail.com", "mannai");
        User khaled = new User("Khaled", "khaled@gmail.com", "khaled");
//        insert(conn, ahmed);
//        insert(conn, khaled);
        System.out.println(getUserFromEmail(conn, "khaled@gmail.com"));
        System.out.println(getAllUsers(conn));

    }

}
