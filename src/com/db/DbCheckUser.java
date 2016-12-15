package com.db;

//STEP 1. Import required packages
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbCheckUser {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/USERS";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "123";
   
   public String[] check(String name) {
   String salt = null, representation = null;
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
      System.out.println("Creating statement...");
      stmt = conn.createStatement();

      String sql = "SELECT Salt, Representation FROM Users WHERE Name='"+name+"';";
      ResultSet rs = stmt.executeQuery(sql);
      //STEP 5: Extract data from result set
      while(rs.next()){
         //Retrieve by column name
         salt = rs.getString("salt");
         representation = rs.getString("representation");

         //Display values
         System.out.print("Salt: " + salt);
         System.out.print(", Representation: " + representation);
        // return new String[]{salt, representation};
      }
      rs.close();
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
   return new String[]{salt, representation};
}//end main
}//end JDBCExample

