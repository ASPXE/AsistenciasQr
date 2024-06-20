
package com.proyecto.objects;

import java.sql.Timestamp;

/**
 *
 * @author aspxe
 */
public class AsistenciasDTO {
    
    private int idAsistencias;
    private String nombres;
    private String matricula;
    private String grado;
    private String grupo;
    private String turno;
    private Timestamp horaEntrada;
    private Timestamp horaSalida;
    
    public AsistenciasDTO(){
        
    }
    
    public AsistenciasDTO(Timestamp horaEntrada){
        this.horaEntrada = horaSalida;
    }

    public int getIdAsistencias() {
        return idAsistencias;
    }

    public void setIdAsistencias(int idAsistencias) {
        this.idAsistencias = idAsistencias;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Timestamp getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Timestamp horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Timestamp getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Timestamp horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AsistenciasDTO{");
        sb.append("idAsistencias=").append(idAsistencias);
        sb.append(", nombres=").append(nombres);
        sb.append(", matricula=").append(matricula);
        sb.append(", grado=").append(grado);
        sb.append(", grupo=").append(grupo);
        sb.append(", turno=").append(turno);
        sb.append(", horaEntrada=").append(horaEntrada);
        sb.append(", horaSalida=").append(horaSalida);
        sb.append('}');
        return sb.toString();
    }

    
    
    
    
}
