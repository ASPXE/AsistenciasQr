
package com.proyecto.data;

import com.proyecto.conexion.Conexion;
import com.proyecto.objects.AlumnosDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aspxe
 */
public class AlumnosDAOJDBC {
    
    private Connection conexionTransaccional;
    
    // constructor vacio
    public AlumnosDAOJDBC(){
        
    }
    
    // COnstructor con conexionTransaccional
    public AlumnosDAOJDBC(Connection conexionTransaccional){
        this.conexionTransaccional = conexionTransaccional;
    }
    
    private static final String SQL_SELECT_ALL = "SELECT * FROM alumnos";
    private static final String SQL_SELECT_ONE = "SELECT * FROM alumnos WHERE matricula = ?";
    private static final String SQL_INSERT_ALL = "INSERT INTO alumnos(nombre, matricula, grado, grupo, turno) VALUES(?,"
            + "?, ?, ?, ?)";
    private static final String SQL_DELETE_ONE = "DELETE FROM alumnos WHERE matricula = ?";
    private static final String SQL_TRUNCATE_ALUMNOS = "TRUNCATE TABLE alumnos";
    
    public List<AlumnosDTO> selectAll() throws SQLException{
        
        int idAlumnos = 0;
        String nombre = "", matricula = "", grado = "", grupo = "", turno = "";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AlumnosDTO alumno = null;
        List<AlumnosDTO> alumnos = new ArrayList<>();
        
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.conectar();
            stmt = conn.prepareStatement(AlumnosDAOJDBC.SQL_SELECT_ALL);
            rs = stmt.executeQuery();
            while(rs.next()){
                idAlumnos = rs.getInt("idAlumnos");
                nombre = rs.getString("nombre");
                matricula = rs.getString("matricula");
                grado = rs.getString("grado");
                grupo = rs.getString("grupo");
                turno = rs.getString("turno");
                
                alumno = new AlumnosDTO(idAlumnos, nombre, matricula, grado, grupo, turno);
                alumnos.add(alumno);
            }
        }catch(SQLException e){
            System.out.println("Ha ocurrido un error al tratar de recuperar los registros de Alumnos: "+e);
        }finally{
            Conexion.cerrar(rs);
            Conexion.cerrar(stmt);
            if(this.conexionTransaccional == null){
                Conexion.cerrar(conn);
            }
        }
        
        return alumnos;
    }
    
    public AlumnosDTO selectOne(String matricula) throws SQLException{
        
        int idAlumnos = 0;
        String nombre = "", matriculaRecuperada = "", grado = "", grupo = "", turno = "";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AlumnosDTO alumno = null;
        
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.conectar();
            stmt = conn.prepareStatement(AlumnosDAOJDBC.SQL_SELECT_ONE);
            stmt.setString(1, matricula);
            rs = stmt.executeQuery();
            while(rs.next()){
                idAlumnos = rs.getInt("idAlumnos");
                nombre = rs.getString("nombre");
                matriculaRecuperada = rs.getString("matricula");
                grado = rs.getString("grado");
                grupo = rs.getString("grupo");
                turno = rs.getString("turno");
                
                alumno = new AlumnosDTO(idAlumnos, nombre, matriculaRecuperada, grado, grupo, turno);
            }
        }catch(SQLException e){
            System.out.println("Ha ocurrido un error al tratar de recuperar los registros de Alumnos: "+e);
        }finally{
            Conexion.cerrar(rs);
            Conexion.cerrar(stmt);
            if(this.conexionTransaccional == null){
                Conexion.cerrar(conn);
            }
        }
        
        return alumno;
        
    }
    
    public int insertAll(AlumnosDTO alumno) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.conectar();
            stmt = conn.prepareStatement(AlumnosDAOJDBC.SQL_INSERT_ALL);
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getMatricula());
            stmt.setString(3, alumno.getGrado());
            stmt.setString(4, alumno.getGrupo());
            stmt.setString(5, alumno.getTurno());
            registros = stmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("HA ocurrido un error al tratar de insertar en Alumnos: "+e);
        }finally{
            Conexion.cerrar(stmt);
            if(this.conexionTransaccional == null){
                Conexion.cerrar(conn);
            }
        }
        
        return registros;
        
    }
    
    public int deleteOne(AlumnosDTO alumno) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.conectar();
            stmt = conn.prepareStatement(AlumnosDAOJDBC.SQL_DELETE_ONE);
            stmt.setString(1, alumno.getMatricula());
            registros = stmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("HA ocurrido un error al tratar de eliminar el registro en Alumnos: "+e);
        }finally{
            Conexion.cerrar(stmt);
            if(this.conexionTransaccional == null){
                Conexion.cerrar(conn);
            }
        }
        
        return registros;
        
    }
    
    public int truncateAlumnos() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.conectar();
            stmt = conn.prepareStatement(SQL_TRUNCATE_ALUMNOS);
            registros = stmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("Ha ocurrido un error al tratar de truncar la tabla de Alumnos: "+e);
        }finally{
            Conexion.cerrar(stmt);
            if(this.conexionTransaccional == null){
                Conexion.cerrar(conn);
            }
        }
        return registros;
    }
    
}
