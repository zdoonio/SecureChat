package com.db;

import java.sql.*;



public class DbAddUser {
	//DB users
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String dbUrl = "jdbc:mysql://localhost:3306/users";
	static final String user = "root";
	static final String pass = "123";
	
	static Connection myConn = null;
	static Statement myStmt = null;
	static ResultSet myRs = null;
	
	public static void main(String[] args) throws SQLException {
		
		
		     
		     
		
		
		
		try {
			 //STEP : Register JDBC driver
			 Class.forName("com.mysql.jdbc.Driver");
			// 1. Get a connection to database
			myConn = DriverManager.getConnection(dbUrl, user , pass);
			System.out.println("Database connection successful!\n");
			
			// 2. Create a statement
			//myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			//myRs = myStmt.executeQuery("select * from employees");
			
			// 4. Process the result set
			//while (myRs.next()) {
				//System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
			//}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
		/*	if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}*/
			
			if (myConn != null) {
				myConn.close();
			}
		}
	}
}
