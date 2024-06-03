
package com.proyecto.data;

import com.proyecto.objects.AlumnosDTO;
import java.sql.SQLException;

/**
 *
 * @author aspxe
 */
public interface AsistenciasDAO {
    
    public int registrarEntrada(AlumnosDTO alumno) throws SQLException;
    public int registrarSalida(AlumnosDTO alumno) throws SQLException;
    
}
