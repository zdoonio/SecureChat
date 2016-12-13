package com.db;

//STEP 1. Import required packages
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class DbAddUser {

 
 private static final String ADD_USER = "INSERT INTO Users "
         + "(Name, Salt, Representation) VALUES (?, ?, ?)";
 
 
 public static void main(String[] args) throws MySQLIntegrityConstraintViolationException {
     insert(args[0], args[1], args[2]);
 }


private static void insert(String name, String salt, String representation) throws 
        com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
           //STEP 2: Register JDBC driver
            Properties prop=new Properties();
            FileInputStream in = new FileInputStream(System.getProperty("database.properties"));
            prop.load(in);
            in.close();

            String drivers = prop.getProperty("jdbc.drivers");
            String connectionURL = prop.getProperty("jdbc.url");
            String username = prop.getProperty("jdbc.username");
            String password = prop.getProperty("jdbc.password");
            Class.forName(drivers);
            
            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(connectionURL, username, password);
            System.out.println("Connected database successfully...");

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

