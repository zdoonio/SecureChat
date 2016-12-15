package com.db;

//STEP 1. Import required packages
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbCreate {
 // JDBC driver name and database URL
 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
 static final String DB_URL = "jdbc:mysql://localhost:3306/";

 //  Database credentials
 static final String USER = "root";
 static final String PASS = "123";
 
    public static void main(String[] args) {
        new DbCreate().create();
    }
 
    public void create() {
    Connection conn = null;
    Statement stmt = null;
    try{
        
        String filename = "database.properties";
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
    System.out.println("Creating database...");
    stmt = conn.createStatement();
    
    String sql = "CREATE DATABASE USERS";
    stmt.executeUpdate(sql);
    System.out.println("Database created successfully...");
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
          stmt.close();
    }catch(SQLException se2){
    }// nothing we can do
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
