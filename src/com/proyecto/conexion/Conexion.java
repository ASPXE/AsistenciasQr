
package com.proyecto.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aspxe
 */
public class Conexion {
    
    // Cambiar esta linea ya que va a ser local
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/Derecho?useSSL=false&useTimeZone=true&serverTimeZone=UTC"
            + "&allowPublicKeyRetrieval=true";

    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWD = "root0000";
    
    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWD);
    }

    public static void cerrar(ResultSet rs) throws SQLException {
        rs.close();
    }

    public static void cerrar(PreparedStatement stmt) throws SQLException {
        stmt.close();
    }

    public static void cerrar(Connection conn) throws SQLException {
        conn.close();
    }

    public static void cerrar(Statement stmt) throws SQLException {
        stmt.close();
    }
    
}
