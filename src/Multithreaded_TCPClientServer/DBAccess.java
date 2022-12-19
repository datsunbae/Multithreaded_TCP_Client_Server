/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Multithreaded_TCPClientServer;
import java.sql.*;
/**
 *
 * @author datsu
 */
public class DBAccess {
    private Connection con;
    private Statement stmt;

    public DBAccess() {
        try {
            // connect with MySQL
            SqlServer_Connection myCon = new SqlServer_Connection();
            //SQLServer_Connection myCon = new SQLServer_Connection();
            // connect with SQL Server
          //  SQLServer_Connection myCon = new SQLServer_Connection();
            con = myCon.getConnection();
            stmt = con.createStatement();
        } catch (Exception e) {
        }
    }

    public int Update(String str) {
        try {
            System.out.println(str);
            int i = stmt.executeUpdate(str);
            return i;
        } catch (Exception e) {
            return -1;
        }
    }

    public ResultSet Query(String str) {
        try {
            ResultSet rs = stmt.executeQuery(str);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }
    
    
}
