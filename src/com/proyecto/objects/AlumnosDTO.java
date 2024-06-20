
package com.proyecto.objects;

/**
 *
 * @author aspxe
 */
public class AlumnosDTO {
    
    private int idAlumnos;
    private String nombre;
    private String matricula;
    private String grado;
    private String grupo;
    private String turno;
    
    
    // COnstructor vacio
    public AlumnosDTO(){
        
    }

    public AlumnosDTO(int idAlumnos, String nombre, String matricula, String grado, String grupo, String turno) {
        this.idAlumnos = idAlumnos;
        this.nombre = nombre;
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
        return idAlumnos;
    }

    public void setIdAlumnos(int idAlumnos) {
        this.idAlumnos = idAlumnos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AlumnosDTO{");
        sb.append("idAlumnos=").append(idAlumnos);
        sb.append(", nombre=").append(nombre);
        sb.append(", matricula=").append(matricula);
        sb.append(", grado=").append(grado);
        sb.append(", grupo=").append(grupo);
        sb.append(", turno=").append(turno);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
