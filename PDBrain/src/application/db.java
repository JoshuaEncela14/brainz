package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class db {

	 private static db instance;
	    private Connection connection;
	    private String url = "jdbc:mysql://localhost:3306/brainzmcq_mysql";
	    private String user = "root";
	    private String password = "";

	    private db() {
	        try {
	            connection = DriverManager.getConnection(url, user, password);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to connect to the database.");
	        }
	    }

	    public static synchronized db getInstance() {
	        if (instance == null) {
	            instance = new db();
	        }
	        return instance;
	    }

	    public Connection getConnection() {
	        return connection;
	    }
	}
