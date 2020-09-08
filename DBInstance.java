/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentdatabase.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import javax.swing.JOptionPane;
import studentdatabase.model.Term;

/**
 *
 * @author elshaday
 */
public class DBInstance {

    private static DBInstance dbInstance;
    private Connection connection;

    private DBInstance() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver" );
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/omnilyf",
                            "root" ,"Alemayehu1990!");			
			System.out.print("Database connected");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static DBInstance getInstance() {
        if (dbInstance == null) {
            dbInstance = new DBInstance();
        }
        return dbInstance;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    
}
