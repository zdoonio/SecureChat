package com.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karol
 */
public class DbConnection {
    
    public static void main(String args[]) {
        try {
            new DbConnection().connect();
        } catch (IOException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect() throws IOException, ClassNotFoundException, SQLException {
        
        Connection con;
        
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
        con = DriverManager.getConnection(connectionURL,username,password);
        System.out.println("Connection Successful");
        
    }
    
}
