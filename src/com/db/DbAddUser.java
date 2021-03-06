package com.db;

//STEP 1. Import required packages
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbAddUser {

	 // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/USERS";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "123";
 
 private static final String ADD_USER = "INSERT INTO Users "
         + "(Name, Salt, Representation) VALUES (?, ?, ?)";
 
 
 public static void main(String[] args) throws MySQLIntegrityConstraintViolationException {
     new DbAddUser().insert(args[0], args[1], args[2]);
 }


private void insert(String name, String salt, String representation) throws 
        com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
        
        String filename = "users.properties";
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream(filename);
        prop.load(in);
        in.close();
        
        String drivers = prop.getProperty("jdbc.drivers");
        String connectionURL = prop.getProperty("jdbc.url");
        String username = prop.getProperty("jdbc.username");
        String password = prop.getProperty("jdbc.password");
        Class.forName(drivers);
        System.out.println("Connecting ...");
        conn = DriverManager.getConnection(connectionURL,username,password);
        System.out.println("Connection Successful");

           //STEP 4: Execute a query
           System.out.println("Inserting records into the table...");
           stmt = conn.prepareStatement(ADD_USER);

           
           stmt.setString(1, name);
           stmt.setString(2, salt);
           stmt.setString(3, representation);
           
           stmt.executeUpdate();
           System.out.println("Inserted records into the table...");   
        } catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException msce) {
            throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
           
        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }finally{
           //finally block used to close resources
           try{
              if(stmt!=null)
                 conn.close();
           }catch(SQLException se){
           }// do nothing
           try{
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        System.out.println("Goodbye!");
       }//end main

}

