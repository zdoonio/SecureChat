package com.db;
//STEP 1. Import required packages
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbAddTable {
	 // JDBC driver name and database URL

   public void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
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
      System.out.println("Creating table in given database...");
      stmt = conn.createStatement();
      
      String sql = " CREATE TABLE Users (" +
                   " Name 		nvarchar(32) NOT NULL, " + 
                   " Salt 		nvarchar(32) NOT NULL ,  " + 
                   " Representation	nvarchar(32) NOT NULL,"+
                   " CONSTRAINT PK_Name PRIMARY KEY (Name),"+
                   " CONSTRAINT U_Name UNIQUE (Name) ); "; 
   
      
      
      stmt.executeUpdate(sql);
      System.out.println("Created table in given database...");
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