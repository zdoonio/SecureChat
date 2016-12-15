package com.db;

//STEP 1. Import required packages
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbDrop {
 // JDBC driver name and database URL
    public static void main(String[] args) {
        new DbDrop().drop();
    }
 
    public void drop() {
    Connection conn = null;
    Statement stmt = null;
    try{
    //STEP 2: Register JDBC driver
        
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
    System.out.println("Deleting database...");
    stmt = conn.createStatement();
    
    String sql = "DROP DATABASE USERS";
    stmt.executeUpdate(sql);
    System.out.println("Database deleted successfully...");
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
}//end JDBCExample
