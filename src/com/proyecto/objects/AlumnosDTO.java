
package com.proyecto.objects;

/**
 *
 * @author aspxe
 */
public class AlumnosDTO {
    
    private int idAlumnos;
    private String nombreCompleto;
    private String matricula;
    private String grado;
    private String grupo;
    private String turno;
    
    
    // COnstructor vacio
    public AlumnosDTO(){
        
    }
    
    // Constructor con todos las variables
    public AlumnosDTO(int idAlumnos, String nombreCompleto, String matricula, String grado, String grupo,
            String turno){
        this.idAlumnos = idAlumnos;
        this.nombreCompleto = nombreCompleto;
        this.matricula = matricula;
        this.grado = grado;
        this.grupo = grupo;
        this.turno = turno;
    }
    
    // Constructor solo con idAlumnos
    public AlumnosDTO(int idAlumnos){
        this.idAlumnos = idAlumnos;
    }
    
    // Constructor con solo matricula
    public AlumnosDTO(String matricula){
        this.matricula = matricula;
    }
    
    // Metodos get y set

    public int getIdAlumnos() {
        return this.idAlumnos;
    }

    public void setIdAlumnos(int idAlumnos) {
        this.idAlumnos = idAlumnos;
    }

    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getGrado() {
        return this.grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getGrupo() {
        return this.grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTurno() {
        return this.turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
    
    // Metodo toString

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AlumnosDTO{");
        sb.append("idAlumnos=").append(idAlumnos);
        sb.append(", nombreCompleto=").append(nombreCompleto);
        sb.append(", matricula=").append(matricula);
        sb.append(", grado=").append(grado);
        sb.append(", grupo=").append(grupo);
        sb.append(", turno=").append(turno);
        sb.append('}');
        return sb.toString();
    }
    
}
