/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.proyecto.view;

import com.proyecto.data.AlumnosDAOJDBC;
import com.proyecto.data.AsistenciasDAOJDBC;
import com.proyecto.objects.AlumnosDTO;
import com.proyecto.objects.AsistenciasDTO;
import com.proyecto.objects.EncriptadorDTO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Optional;
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
    
    private final Timer timer;
    private final Pattern pattern;
    private AlumnosDAOJDBC alumnosDAO;
    private AsistenciasDAOJDBC asistenciaDAO;
    private AlumnosDTO alumno;
    private final EncriptadorDTO desencriptar;
    private String matricula;

    // Regular expression for valid matricula
    private static final String REGEX_MATRICULA = "^(?i)(1[7-9]|[2-9][0-9])(AL|BL)([0-9]{7})$";

    /**
     * Creates new form Asistencias
     */
    public Asistencias() throws Exception {
        alumno = null;
        desencriptar = new EncriptadorDTO();
        pattern = Pattern.compile(REGEX_MATRICULA);
        initComponents();
        initializeComponents();

        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleTimerAction();
                } catch (Exception ex) {
                    Logger.getLogger(Asistencias.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        txtArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not needed for plain-text components
            }
        });
    }
    
    private void initializeComponents() {
        btnGrupo1.add(radioIniciar);
        btnGrupo1.add(radioDetener);
        radioIniciar.setEnabled(false);
        radioDetener.setEnabled(false);
        btnDetener.setEnabled(false);
        txtArea.setEnabled(false);
    }
    
    private void handleTimerAction() throws Exception {
        if ((radioIniciar.isSelected() || radioDetener.isSelected()) && !txtArea.getText().trim().isEmpty()) {
            matricula = desencriptar.decrypt(txtArea.getText().trim());
            if (pattern.matcher(matricula).matches()) {
                if (radioIniciar.isSelected()) {
                    registrarEntrada(matricula);
                } else if (radioDetener.isSelected()) {
                    registrarSalida(matricula);
                }
            } else {
                mostrarError("El dato escaneado no corresponde a una matrícula válida");
            }
        }
    }
    
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(lblNombre, mensaje, "Dato inválido", JOptionPane.ERROR_MESSAGE);
        txtArea.setText(null);
    }

    private void completarInformacion() {
        lblNombreRecuperado.setText(Optional.ofNullable(alumno).map(AlumnosDTO::getNombreCompleto).orElse(""));
        lblGradoRecuperado.setText(Optional.ofNullable(alumno).map(AlumnosDTO::getGrado).orElse(""));
        lblGrupoRecuperado.setText(Optional.ofNullable(alumno).map(AlumnosDTO::getGrupo).orElse(""));
        lblTurnoRecuperado.setText(Optional.ofNullable(alumno).map(AlumnosDTO::getTurno).orElse(""));
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
        setInformacionVisible(true);
    }
    
    private void ocultarInformacion() {
        setInformacionVisible(false);
    }
    
    private void setInformacionVisible(boolean visible) {
        lblInformacion.setEnabled(visible);
        lblNombre.setEnabled(visible);
        lblGrado.setEnabled(visible);
        lblGrupo.setEnabled(visible);
        lblTurno.setEnabled(visible);
        lblNombreRecuperado.setVisible(visible);
        lblGradoRecuperado.setVisible(visible);
        lblGrupoRecuperado.setVisible(visible);
        lblTurnoRecuperado.setVisible(visible);
    }
    
    private void registrarEntrada(String matricula) throws Exception{
        try {
            asistenciaDAO = new AsistenciasDAOJDBC();
            alumnosDAO = new AlumnosDAOJDBC();
            alumno = alumnosDAO.selectOne(matricula);

            if (alumno != null) {
                completarInformacion();
                asistenciaDAO.registrarEntrada(alumno);
                txtArea.setText(null);
            } else {
                mostrarError("La matrícula " + matricula + " no está registrada en la base de datos");
            }
        } catch (SQLException ex) {
            mostrarError("Ha ocurrido un error al registrar la entrada: " + ex.getMessage());
            Logger.getLogger(Asistencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void registrarSalida(String matricula) throws Exception{
        try {
            asistenciaDAO = new AsistenciasDAOJDBC();
            alumnosDAO = new AlumnosDAOJDBC();
            alumno = alumnosDAO.selectOne(matricula);

            if (alumno != null) {
                AsistenciasDTO validarHora = asistenciaDAO.validarHoraEntrada(alumno);

                if (validarHora.getHoraEntrada() == null) {
                    System.out.println(validarHora.toString());
                    asistenciaDAO.registrarSalidaSinEntrada(alumno);
                } else {
                    asistenciaDAO.registrarSalida(alumno);
                }

                completarInformacion();
                txtArea.setText(null);
            } else {
                mostrarError("La matrícula " + matricula + " no está registrada en la base de datos");
            }
        } catch (SQLException ex) {
            mostrarError("Ha ocurrido un error al registrar la salida: " + ex.getMessage());
            Logger.getLogger(Asistencias.class.getName()).log(Level.SEVERE, null, ex);
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
        btnIniciar.setIcon(new javax.swing.ImageIcon("/home/aspxe/NetBeansProjects/AsistenciasQr/src/main/resources/images/codigo-qr.png")); // NOI18N
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
        btnDetener.setIcon(new javax.swing.ImageIcon("/home/aspxe/NetBeansProjects/AsistenciasQr/src/main/resources/images/detener(1).png")); // NOI18N
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
        jScrollPane1.setBounds(487, 151, 254, 101);

        radioIniciar.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        radioIniciar.setText("Registrar Entrada");
        radioIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIniciarActionPerformed(evt);
            }
        });
        add(radioIniciar);
        radioIniciar.setBounds(403, 49, 200, 30);

        radioDetener.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        radioDetener.setText("Registrar Salida");
        radioDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioDetenerActionPerformed(evt);
            }
        });
        add(radioDetener);
        radioDetener.setBounds(696, 49, 180, 30);

        lblEstatus.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblEstatus.setForeground(new java.awt.Color(255, 0, 51));
        lblEstatus.setText("ESTATUS DE ACCIONES: NO SE HA SELECCIONADO UNA ACCIÓN");
        add(lblEstatus);
        lblEstatus.setBounds(302, 93, 557, 26);

        lblInformacion.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblInformacion.setText("Información del asistente");
        add(lblInformacion);
        lblInformacion.setBounds(490, 270, 250, 26);

        lblNombre.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblNombre.setText("Nombre Completo:");
        add(lblNombre);
        lblNombre.setBounds(362, 302, 170, 26);

        lblGrado.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblGrado.setText("Grado:");
        add(lblGrado);
        lblGrado.setBounds(360, 330, 59, 26);

        lblGrupo.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblGrupo.setText("Grupo:");
        add(lblGrupo);
        lblGrupo.setBounds(360, 360, 60, 26);

        lblTurno.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblTurno.setText("Turno:");
        add(lblTurno);
        lblTurno.setBounds(360, 390, 58, 26);
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
