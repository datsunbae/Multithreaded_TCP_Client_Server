/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Multithreaded_TCPClientServer;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author datsu
 */
public class SqlServer_Connection {
    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL = "jdbc:sqlserver://localhost:1433;Database=TCPClientServerDB;user=sa;password=123456;encrypt=true;trustServerCertificate=true";
            Connection con = DriverManager.getConnection(URL);
            
            return con;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
