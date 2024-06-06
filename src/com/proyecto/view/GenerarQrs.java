/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.proyecto.view;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.proyecto.data.AlumnosDAOJDBC;
import com.proyecto.objects.AlumnosDTO;
import com.proyecto.objects.EncriptadorDTO;
import static java.awt.SystemColor.text;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author aspxe
 */
public class GenerarQrs extends javax.swing.JPanel {
    
    private String ruta;

    /**
     * Creates new form generarQrs
     */
    public GenerarQrs() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGenerarQrs = new javax.swing.JButton();
        lblMensaje = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(900, 455));
        setMinimumSize(new java.awt.Dimension(900, 455));
        setPreferredSize(new java.awt.Dimension(900, 455));

        btnGenerarQrs.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnGenerarQrs.setIcon(new javax.swing.ImageIcon("/home/aspxe/NetBeansProjects/SoftwareAdministrativo/src/main/resources/images/codigo-qr.png")); // NOI18N
        btnGenerarQrs.setText("Generar QR");
        btnGenerarQrs.setMaximumSize(new java.awt.Dimension(250, 80));
        btnGenerarQrs.setMinimumSize(new java.awt.Dimension(250, 80));
        btnGenerarQrs.setPreferredSize(new java.awt.Dimension(250, 80));
        btnGenerarQrs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarQrsActionPerformed(evt);
            }
        });

        lblMensaje.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblMensaje.setForeground(new java.awt.Color(255, 0, 0));
        lblMensaje.setText("HAGA CLICK EN EL BOTÓN PARA GENERAR LOS QR'S");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addComponent(btnGenerarQrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(217, Short.MAX_VALUE)
                .addComponent(lblMensaje)
                .addGap(201, 201, 201))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(lblMensaje)
                .addGap(48, 48, 48)
                .addComponent(btnGenerarQrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(191, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private static File elegirRuta(){
        // Crear un JFileChooser
        JFileChooser directoryChooser = new JFileChooser();
        // Configurar el JFileChooser para solo seleccionar directorios
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Mostrar el diálogo de selección de directorios
        int result = directoryChooser.showOpenDialog(null);

        // Procesar el resultado de la selección
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = directoryChooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                return selectedFile;
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una carpeta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
    
    private void generarQrs(File ruta) throws SQLException, WriterException, Exception{
        int alto = 400, ancho = 400;
        
        EncriptadorDTO encriptar = new EncriptadorDTO();
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        AlumnosDAOJDBC alumnos = new AlumnosDAOJDBC();

        try {
            List<AlumnosDTO> alumnosRecuperados = new ArrayList<>();
            alumnosRecuperados = alumnos.selectAll();
            for (AlumnosDTO alumno : alumnosRecuperados) {
                String nombre = alumno.getNombreCompleto();
                String matricula = alumno.getMatricula();
                String turno = alumno.getTurno();
                String grado = alumno.getGrado();
                String grupo = alumno.getGrupo();
                
                // Encriptando la matrícula
                matricula = encriptar.encrypt(matricula);
                System.out.println(matricula);

                BitMatrix bitMatrix = qrCodeWriter.encode(matricula, BarcodeFormat.QR_CODE, ancho, alto, hints);

                Path directorio = Paths.get(ruta.getAbsolutePath(), turno, grado, grupo);
                Files.createDirectories(directorio);

                Path path = directorio.resolve("qrcode_" + nombre + ".png");
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

                System.out.println("QR Code generado en: " + path.toString());
            }
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error al tratar de crear el QR: " + e);
        }
        
        
    }
    
    private void ejecutarProceso() throws SQLException, WriterException, Exception{
         File rutaSeleccionada = elegirRuta();

        if (rutaSeleccionada != null) {
            System.out.println("Ruta seleccionada: " + rutaSeleccionada.toString());
            generarQrs(rutaSeleccionada);
        } else {
            System.out.println("No se encontró la ruta");
            JOptionPane.showMessageDialog(this, "No se encontró la ruta", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void btnGenerarQrsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarQrsActionPerformed
        
        int opcion = JOptionPane.showConfirmDialog(this, "El proceso de generación de Qr's puede tomar unos minutos. ¿Desea continuar?", "AVISO", JOptionPane.YES_NO_CANCEL_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                ejecutarProceso();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarQrs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriterException ex) {
                Logger.getLogger(GenerarQrs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GenerarQrs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnGenerarQrsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerarQrs;
    private javax.swing.JLabel lblMensaje;
    // End of variables declaration//GEN-END:variables
}
