package fr.pierronus.gcaplugin;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
   private Connection connection;
   
   public static void createNewDatabase(String fileName) {  
	   String url;
	   if(System.getProperty("os.name").toLowerCase().contains("windows")) {
		   url = "jdbc:sqlite:C:/Users/alpha/Desktop/gca/" + fileName; 
	   }else {
		   url = "jdbc:sqlite:/home/ubuntu/gca/" + fileName; 
	   }
  
       try {  
           Connection conn = DriverManager.getConnection(url);  
           if (conn != null) {  
               DatabaseMetaData meta = conn.getMetaData();  
               System.out.println("The driver name is " + meta.getDriverName());  
               System.out.println("A new database has been created.");  
           }  
  
       } catch (SQLException e) {  
           System.out.println(e.getMessage());  
       }  
   }  
   
   
   public boolean isConnected() { 
      return this.connection != null;
   }
   

   public void connect() throws ClassNotFoundException, SQLException {
      if (!this.isConnected()) {
    	 createNewDatabase("gca.db"); 
         if(System.getProperty("os.name").toLowerCase().contains("windows")) {
        	 this.connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/alpha/Desktop/gca/gca.db");
         }else {
        	 this.connection = DriverManager.getConnection("jdbc:sqlite:/home/ubuntu/gca/gca.db");
         }
         
      }

   } 
   

   public void disconnect() {
      if (this.isConnected()) {
         try {
            this.connection.close();
         } catch (SQLException var2) {
            var2.printStackTrace();
         }
      }

   }

   public Connection getConnection() {
      return this.connection;
   }
}