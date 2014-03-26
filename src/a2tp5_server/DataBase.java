/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2tp5_server;

import java.sql.Connection;
import java.sql.DriverManager;


public class DataBase {
    
//Test.. fonctionne pas..  

  public static void main(String[] args) {      
    try {
      Class.forName("org.postgresql.Driver");
      System.out.println("Driver O.K.");

      String url = "jdbc:postgresql://localhost:5432/java_tp5";
      String user = "root";
      String passwd = "";

      Connection conn = DriverManager.getConnection(url, user, passwd);
      System.out.println("Connexion effective !");         
         
    } catch (Exception e) {
      e.printStackTrace();
    }      
  }
}