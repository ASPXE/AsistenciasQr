/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.proyecto.view;

import com.proyecto.data.AlumnosDAOJDBC;
import com.proyecto.data.AsistenciasDAOJDBC;
import com.proyecto.objects.AlumnosDTO;
import com.proyecto.objects.EncriptadorDTO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author aspxe
 */
public class Asistencias extends javax.swing.JPanel {
    
    private Timer timer;
    private AsistenciasDAOJDBC asistencia;
    private AlumnosDAOJDBC alumnosRecuperados;
    private AlumnosDTO alumno;
    private int registro;
    private EncriptadorDTO desencriptar;
    private String matricula;
    
    String regex = "^(?i)(1[7-9]|[2-9][0-9])(AL|BL)([0-9]{7})$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher1 = null;

    /**
     * Creates new form Asistencias
     */
    public Asistencias() throws Exception {
        alumno = null;
        desencriptar = new EncriptadorDTO();
        initComponents();
        btnGrupo1.add(radioIniciar);
        btnGrupo1.add(radioDetener);
        radioIniciar.setEnabled(false);
        radioDetener.setEnabled(false);
        btnDetener.setEnabled(false);
        txtArea.setEnabled(false);
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTimerAction();
            }
        });

        // Agregar un DocumentListener al JTextArea
        txtArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Método llamado cuando se inserta texto en el JTextArea
                // Reiniciar el temporizador en cada cambio para evitar ejecuciones prematuras
                timer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Método llamado cuando se elimina texto en el JTextArea
                // Reiniciar el temporizador en cada cambio para evitar ejecuciones prematuras
                timer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Método llamado cuando hay cambios que no son insertar o eliminar texto
            }
        });
    }
    
    private void handleTimerAction() {
        if (radioIniciar.isSelected() || radioDetener.isSelected()) {
            if (!txtArea.getText().trim().isEmpty()) {
                try {
                    if (desencriptar != null) {
                        matricula = desencriptar.decrypt(txtArea.getText().trim());
                    } else {
                        throw new NullPointerException("Desencriptar object is null");
                    }
                    System.out.println(matricula);
                } catch (Exception ex) {
                    Logger.getLogger(Asistencias.class.getName()).log(Level.SEVERE, null, ex);
                }
                matcher1 = pattern.matcher(matricula);
                if (matcher1.matches()) {
                    System.out.println(matricula + ": " + matcher1.matches());
                    if (radioIniciar.isSelected()) {
                        registrarEntrada(matricula);
                    } else if (radioDetener.isSelected()) {
                        registrarSalida(matricula);
                    }
                } else {
                    System.out.println(matricula + ": " + matcher1.matches());
                    JOptionPane.showMessageDialog(lblNombre, "El dato escaneado no corresponde a una matrícula válida", "Dato inválido", JOptionPane.ERROR_MESSAGE);
                    txtArea.setText(null);
                }
            }
        }
    }
    
    
    private void completarInformacion() {
        
        lblNombreRecuperado.setText(this.alumno.getNombreCompleto());
        lblGradoRecuperado.setText(this.alumno.getGrado());
        lblGrupoRecuperado.setText(this.alumno.getGrupo());
        lblTurnoRecuperado.setText(this.alumno.getTurno());
    }

    private void limpiarInformacion() {
        lblNombreRecuperado.setText("");
        lblGradoRecuperado.setText("");
        lblGrupoRecuperado.setText("");
        lblTurnoRecuperado.setText("");
    }
    
    private void hacerFocus(){
        txtArea.requestFocus();
    }
    
    private void actualizarEstatus(){
        if(radioIniciar.isSelected()){
            lblEstatus.setText("ESTATUS DE ACCIONES: REGISTRAR ENTRADAS"); 
            lblEstatus.setForeground(Color.BLUE);
        }else if(radioDetener.isSelected()){
            lblEstatus.setText("ESTATUS DE ACCIONES: REGISTRAR SALIDAS");
            lblEstatus.setForeground(Color.BLUE);
        }
        
    }
    
    private void mostrarInformacion() {
        lblInformacion.setEnabled(true);
        lblNombre.setEnabled(true);
        lblGrado.setEnabled(true);
        lblGrupo.setEnabled(true);
        lblTurno.setEnabled(true);
        lblNombreRecuperado.setVisible(true);
        lblGradoRecuperado.setVisible(true);
        lblGrupoRecuperado.setVisible(true);
        lblTurnoRecuperado.setVisible(true);
    }
    
    private void ocultarInformacion() {
        lblInformacion.setEnabled(false);
        lblNombre.setEnabled(false);
        lblGrado.setEnabled(false);
        lblGrupo.setEnabled(false);
        lblTurno.setEnabled(false);
        lblNombreRecuperado.setVisible(false);
        lblGradoRecuperado.setVisible(false);
        lblGrupoRecuperado.setVisible(false);
        lblTurnoRecuperado.setVisible(false);
    }
    
    private void registrarEntrada(String matricula){
        try {
            asistencia = new AsistenciasDAOJDBC();
            alumnosRecuperados = new AlumnosDAOJDBC();
            alumno = alumnosRecuperados.selectOne(matricula);
            if (alumno != null) {
                completarInformacion();
                registro = asistencia.registrarEntrada(alumno);
                txtArea.setText(null);
            } else {
                JOptionPane.showMessageDialog(lblNombre, "La matrícula " + txtArea.getText().trim() + " no está registrada en la base de datos", "Matrícula no encontrada", JOptionPane.ERROR_MESSAGE);
                txtArea.setText(null);
            }
        } catch (SQLException ex) {
            System.out.println("Ha ocurrido un error al registrar la entrada: " + ex);
        }
    }
    
    private void registrarSalida(String matricula){
        try {
            asistencia = new AsistenciasDAOJDBC();
            alumnosRecuperados = new AlumnosDAOJDBC();
            alumno = alumnosRecuperados.selectOne(matricula);
            if (alumno != null) {
                registro = asistencia.registrarSalida(alumno);
                if (registro == 0) {
                    JOptionPane.showMessageDialog(lblNombre, "Este asistente no tiene registro de entrada", "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
                }
                txtArea.setText(null);
                completarInformacion();
            } else {
                JOptionPane.showMessageDialog(lblNombre, "La matrícula " + matricula + " no está registrada en la base de datos", "Matrícula no encontrada", JOptionPane.ERROR_MESSAGE);
                txtArea.setText(null);
            }
        } catch (SQLException ex) {
            System.out.println("Ha ocurrido un error al registrar la salida: " + ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrupo1 = new javax.swing.ButtonGroup();
        btnIniciar = new javax.swing.JButton();
        btnDetener = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        radioIniciar = new javax.swing.JRadioButton();
        radioDetener = new javax.swing.JRadioButton();
        lblEstatus = new javax.swing.JLabel();
        lblInformacion = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblGrado = new javax.swing.JLabel();
        lblGrupo = new javax.swing.JLabel();
        lblTurno = new javax.swing.JLabel();
        lblNombreRecuperado = new javax.swing.JLabel();
        lblGradoRecuperado = new javax.swing.JLabel();
        lblGrupoRecuperado = new javax.swing.JLabel();
        lblTurnoRecuperado = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(900, 455));
        setMinimumSize(new java.awt.Dimension(900, 455));
        setLayout(null);

        btnIniciar.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnIniciar.setIcon(new javax.swing.ImageIcon("/home/aspxe/NetBeansProjects/SoftwareAdministrativo/src/main/resources/images/codigo-qr.png")); // NOI18N
        btnIniciar.setText("Iniciar Escaneo");
        btnIniciar.setMaximumSize(new java.awt.Dimension(250, 80));
        btnIniciar.setMinimumSize(new java.awt.Dimension(250, 80));
        btnIniciar.setPreferredSize(new java.awt.Dimension(250, 80));
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        add(btnIniciar);
        btnIniciar.setBounds(37, 151, 250, 80);

        btnDetener.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnDetener.setIcon(new javax.swing.ImageIcon("/home/aspxe/NetBeansProjects/SoftwareAdministrativo/src/main/resources/images/detener(1).png")); // NOI18N
        btnDetener.setText("Detener Escaneo");
        btnDetener.setMaximumSize(new java.awt.Dimension(250, 80));
        btnDetener.setMinimumSize(new java.awt.Dimension(250, 80));
        btnDetener.setPreferredSize(new java.awt.Dimension(250, 80));
        btnDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetenerActionPerformed(evt);
            }
        });
        add(btnDetener);
        btnDetener.setBounds(37, 301, 250, 80);

        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane1.setViewportView(txtArea);

        add(jScrollPane1);
        jScrollPane1.setBounds(487, 151, 274, 96);

        radioIniciar.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        radioIniciar.setText("Registrar Entrada");
        radioIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIniciarActionPerformed(evt);
            }
        });
        add(radioIniciar);
        radioIniciar.setBounds(403, 49, 175, 26);

        radioDetener.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        radioDetener.setText("Registrar Salida");
        radioDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioDetenerActionPerformed(evt);
            }
        });
        add(radioDetener);
        radioDetener.setBounds(696, 49, 161, 26);

        lblEstatus.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblEstatus.setForeground(new java.awt.Color(255, 0, 51));
        lblEstatus.setText("ESTATUS DE ACCIONES: NO SE HA SELECCIONADO UNA ACCIÓN");
        add(lblEstatus);
        lblEstatus.setBounds(302, 93, 583, 22);

        lblInformacion.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblInformacion.setText("Información del asistente");
        add(lblInformacion);
        lblInformacion.setBounds(521, 265, 221, 22);

        lblNombre.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblNombre.setText("Nombre Completo:");
        add(lblNombre);
        lblNombre.setBounds(362, 302, 167, 22);

        lblGrado.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblGrado.setText("Grado:");
        add(lblGrado);
        lblGrado.setBounds(360, 330, 60, 22);

        lblGrupo.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblGrupo.setText("Grupo:");
        add(lblGrupo);
        lblGrupo.setBounds(360, 360, 61, 22);

        lblTurno.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblTurno.setText("Turno:");
        add(lblTurno);
        lblTurno.setBounds(360, 390, 58, 22);
        add(lblNombreRecuperado);
        lblNombreRecuperado.setBounds(535, 301, 350, 27);
        add(lblGradoRecuperado);
        lblGradoRecuperado.setBounds(420, 330, 457, 27);
        add(lblGrupoRecuperado);
        lblGrupoRecuperado.setBounds(430, 360, 456, 30);
        add(lblTurnoRecuperado);
        lblTurnoRecuperado.setBounds(420, 390, 459, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        JOptionPane.showMessageDialog(this, "Seleccione la acción a realizar", "Empezando escaneo...", JOptionPane.INFORMATION_MESSAGE);
        radioIniciar.setEnabled(true);
        radioDetener.setEnabled(true);
        radioIniciar.setVisible(true);
        radioDetener.setVisible(true);
        mostrarInformacion();
        lblEstatus.setText("ESTATUS DE ACCIÓN: ESPERANDO A SELECCIONAR UNA OPCIÓN");
        lblEstatus.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetenerActionPerformed
        JOptionPane.showMessageDialog(this, "Escaneo detenido", "Acción seleccionada", JOptionPane.INFORMATION_MESSAGE);
        radioIniciar.setEnabled(false);
        radioDetener.setEnabled(false);
        txtArea.setEnabled(false);
        btnDetener.setEnabled(false);
        radioIniciar.setSelected(false);
        radioDetener.setSelected(false);
        radioIniciar.setVisible(false);
        radioDetener.setVisible(false);
        txtArea.setText(null);
        ocultarInformacion();
        limpiarInformacion();
        actualizarEstatus();
        lblEstatus.setText("ESTATUS DE ACCIONES: ESCANEO DETENIDO");
        lblEstatus.setForeground(Color.red); 
    }//GEN-LAST:event_btnDetenerActionPerformed

    private void radioIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioIniciarActionPerformed
        if (radioIniciar.isSelected()) {
            JOptionPane.showMessageDialog(this, "Registro de entradas seleccionado", "Accion seleccionada", JOptionPane.INFORMATION_MESSAGE);
            radioDetener.setSelected(false);
            txtArea.setEnabled(true);
            btnDetener.setEnabled(true);
            mostrarInformacion();
            hacerFocus();
            actualizarEstatus();
        }
    }//GEN-LAST:event_radioIniciarActionPerformed

    private void radioDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioDetenerActionPerformed
        if (radioDetener.isSelected()) {
            JOptionPane.showMessageDialog(this, "Registro de salidas seleccionado", "Acción seleccionada", JOptionPane.INFORMATION_MESSAGE);
            radioIniciar.setSelected(false);
            txtArea.setEnabled(true);
            btnDetener.setEnabled(true);
            mostrarInformacion();
            hacerFocus();
            actualizarEstatus();
        }
    }//GEN-LAST:event_radioDetenerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetener;
    private javax.swing.ButtonGroup btnGrupo1;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEstatus;
    private javax.swing.JLabel lblGrado;
    private javax.swing.JLabel lblGradoRecuperado;
    private javax.swing.JLabel lblGrupo;
    private javax.swing.JLabel lblGrupoRecuperado;
    private javax.swing.JLabel lblInformacion;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombreRecuperado;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JLabel lblTurnoRecuperado;
    private javax.swing.JRadioButton radioDetener;
    private javax.swing.JRadioButton radioIniciar;
    private javax.swing.JTextArea txtArea;
    // End of variables declaration//GEN-END:variables
}
