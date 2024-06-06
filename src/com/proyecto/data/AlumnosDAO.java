
package com.proyecto.data;

import com.proyecto.objects.AlumnosDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author aspxe
 */
public interface AlumnosDAO {
    
    public List<AlumnosDTO> selectAll() throws SQLException;
    public AlumnosDTO selectOne(String matricula) throws SQLException;
    public int insertAll(AlumnosDTO alumno) throws SQLException;
    public int deleteOne(AlumnosDTO alumno) throws SQLException;
    public int truncateAlumnos() throws SQLException;
}
