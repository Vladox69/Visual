
package uta.interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class conexion {
    Connection connect=null;
    public Connection conectar(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect =DriverManager.getConnection("jdbc:mysql://localhost/UTA2","root","");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
       return connect; 
    }
}
