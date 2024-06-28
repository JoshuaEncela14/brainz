//package application;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class db {
//    public static void main(String[] args) {
//        String url = "jdbc:mysql://localhost:3306/login"; // Replace with the name of your database
//        String username = "root";
//        String password = ""; // Replace with your MySQL password if you have set one
//
//        System.out.println("Connecting to the database...");
//
//        try {
//            Connection connection = DriverManager.getConnection(url, username, password);
//            System.out.println("Connected to the database!");
//            // Perform database operations here
//            connection.close();
//        } catch (SQLException e) {
//            System.out.println("Connection failed!");
//            e.printStackTrace();
//        }
//    }
//}
//
//
//if (!passwordValue.equals(confirmValue)) {
//    System.out.println("Passwords do not match!");
//} else {
//    try {
//        Connection connection = DriverManager.getConnection(url, username, password);
//
//        String sql = "INSERT INTO `create` (name, password) VALUES (?, ?)";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setString(1, name);
//        statement.setString(2, passwordValue);
//        
//        String sql = "INSERT INTO create' (name, password) VALUES (?, ?)";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setString(1, name);
//        statement.setString(2, passwordValue);
//
//        int rowsInserted = statement.executeUpdate();
//        if (rowsInserted > 0) {
//            System.out.println("A new user has been registered successfully!");
//        }
//        
//        int rowsInserted = statement.executeUpdate();
//        if (rowsInserted > 0) {
//            System.out.println("A new user has been registered successfully!");
//        }
//
//        statement.close();
//        connection.close();
//    } catch (SQLException ex) {
//        System.out.println("Connection failed or error in SQL statement!");
//        ex.printStackTrace();
//    }
//}
//});
//
//
//if(!passwordValue.equals(confirmValue)) {
//	 System.out.println("Password do not match!");
//} else {
//	 try {
//        Connection connection = DriverManager.getConnection(url, username, password);
////        System.out.println("Connected to the database!");
//
//       
//        String sql = "INSERT INTO create' (name, password) VALUES (?, ?)";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setString(1, name);
//        statement.setString(2, passwordValue);
//
//        int rowsInserted = statement.executeUpdate();
//        if (rowsInserted > 0) {
//            System.out.println("A new user has been registered successfully!");
//        }
//
//        statement.close();
//        connection.close();
//    } catch (SQLException ex) {
//        System.out.println("Connection failed or error in SQL statement!");
//        ex.printStackTrace();
//    }
//}
//});
