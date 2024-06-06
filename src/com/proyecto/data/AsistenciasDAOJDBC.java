
package com.proyecto.data;

import com.proyecto.conexion.Conexion;
import com.proyecto.objects.AlumnosDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author aspxe
 */
public class AsistenciasDAOJDBC {
    
    private Connection conexionTransaccional;
    
    public AsistenciasDAOJDBC(){
        
    }
    
    public AsistenciasDAOJDBC(Connection conexionTransaccional){
        this.conexionTransaccional = conexionTransaccional;
    }
    
    private static final String SQL_INSERT_ENTRADA = "INSERT INTO Asistencias(nombreCompleto, matricula, grado, grupo, turno, horaEntrada) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_SALIDA = 
        "UPDATE Asistencias " +
        "SET horaSalida = ? " +
        "WHERE matricula = ? " +
        "AND horaSalida IS NULL " +
        "AND DATE(horaEntrada) = CURDATE()";
    private static final String SQL_TRUNCATE_ASISTENCIAS = "TRUNCATE TABLE Asistencias";
    
    public int registrarEntrada(AlumnosDTO alumno) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        
        // Obtener la fecha y hora actual
        Date date = new Date();
        // Crear un objeto Timestamp con la fecha y hora actual
        Timestamp timestamp = new Timestamp(date.getTime());
        
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.conectar();
            stmt = conn.prepareStatement(AsistenciasDAOJDBC.SQL_INSERT_ENTRADA);
            stmt.setString(1, alumno.getNombreCompleto());
            stmt.setString(2, alumno.getMatricula());
            stmt.setString(3, alumno.getGrado());
            stmt.setString(4, alumno.getGrupo());
            stmt.setString(5, alumno.getTurno());
            stmt.setTimestamp(6, timestamp);
            registros = stmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("HA ocurrido un error al tratar de registrar la entrada: "+e);
        }finally{
            Conexion.cerrar(stmt);
            if(this.conexionTransaccional == null){
                Conexion.cerrar(conn);
            }
        }
        return registros;
        
    }
    
    public int registrarSalida(AlumnosDTO alumno) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        
        // Obtener la fecha y hora actual
        Date date = new Date();
        // Crear un objeto Timestamp con la fecha y hora actual
        Timestamp timestamp = new Timestamp(date.getTime());
        
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.conectar();
            stmt = conn.prepareStatement(AsistenciasDAOJDBC.SQL_INSERT_SALIDA);
            stmt.setTimestamp(1, timestamp);
            stmt.setString(2, alumno.getMatricula());
            registros = stmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("HA ocurrido un error al tratar de registrar la entrada: "+e);
        }finally{
            Conexion.cerrar(stmt);
            if(this.conexionTransaccional == null){
                Conexion.cerrar(conn);
            }
        }
        return registros;
        
    }
    
    
}
