package edu.ncsu.csc.dbproject.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.ncsu.csc.dbproject.util.CustomLogger;

public class dbconnection {
	
	private static CustomLogger s_Logger = CustomLogger.getInstance(dbconnection.class);
	private static final String jdbcURL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

	// Put your oracle ID and password here
	private static final String user = "rtripat";
	private static final String password = "welcome";

	public static Connection connection = null;

	public static Connection get(){
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(jdbcURL, user, password);

		} catch (ClassNotFoundException e) {
			s_Logger.error(e.getMessage());
		} catch (SQLException e) {
			s_Logger.error(e.getMessage());
		}
		return connection;
	}
	
	public static void release(Connection conn){
		
		try {
			conn.close();
		} catch (SQLException e) {
			s_Logger.error(e.getMessage());
		}
	}
}
