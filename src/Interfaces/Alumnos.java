/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaces;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josep
 */
public class Alumnos extends javax.swing.JInternalFrame {

    /**
     * Creates new form Alumnos
     */
    private static Alumnos instancia;
    
    public Alumnos() {
        initComponents();
        mostrarEstudiantes();
        cargarCamposTabla();
        botonesInicio();
        TextosInicio();
    }
    
    public static Alumnos getInstancia() {
    if (instancia == null || instancia.isClosed()) {
        instancia = new Alumnos();
    }
    return instancia;
    }
    
    public void botonesEliminarActualizar(){
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(true);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
    }
    
    public void botonesInicio(){
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
    }
    
    public void TextosInicio(){
        jtxtCedula.setEnabled(false);
        jtxtNombre.setEnabled(false);
        jtxtApellido.setEnabled(false);
        jtxtDireccion.setEnabled(false);
        jtxtTelefono.setEnabled(false);
        jtxtCedula.setText("");
        jtxtNombre.setText("");
        jtxtApellido.setText("");
        jtxtDireccion.setText("");
        jtxtTelefono.setText("");
    }
    
    public void botonesNuevo(){
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(true);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
        jtxtCedula.setText("");
        jtxtNombre.setText("");
        jtxtApellido.setText("");
        jtxtDireccion.setText("");
        jtxtTelefono.setText("");
    }
    
    public void TextosNuevo(){
        jtxtCedula.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtDireccion.setEnabled(true);
        jtxtTelefono.setEnabled(true);
    }
    
    
    public void guardarEstudiante(){
    
    
    if (jtxtCedula.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La cédula es obligatoria");
        jtxtCedula.requestFocus();
        return;
    }
    else if (jtxtCedula.getText().length() != 10) {
        JOptionPane.showMessageDialog(this, "La cédula debe tener exactamente 10 caracteres");
        jtxtCedula.requestFocus();
        jtxtCedula.selectAll();
        return;
    }
    
    
    if (jtxtNombre.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
        jtxtNombre.requestFocus();
        return;
    }
    else if (jtxtNombre.getText().length() > 20) {
        JOptionPane.showMessageDialog(this, 
            "Nombre muy largo: " + jtxtNombre.getText().length() + "/20 caracteres");
        jtxtNombre.requestFocus();
        jtxtNombre.selectAll();
        return;
    }
    
    
    if (jtxtApellido.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El apellido es obligatorio");
        jtxtApellido.requestFocus();
        return;
    }
    else if (jtxtApellido.getText().length() > 20) {
        JOptionPane.showMessageDialog(this, 
            "Apellido muy largo: " + jtxtApellido.getText().length() + "/20 caracteres");
        jtxtApellido.requestFocus();
        jtxtApellido.selectAll();
        return;
    }
    
    
    if (!jtxtDireccion.getText().isEmpty() && jtxtDireccion.getText().length() > 50) {
        JOptionPane.showMessageDialog(this, 
            "Dirección muy larga: " + jtxtDireccion.getText().length() + "/100 caracteres");
        jtxtDireccion.requestFocus();
        jtxtDireccion.selectAll();
        return;
    }
    
    
    if (!jtxtTelefono.getText().isEmpty() && jtxtTelefono.getText().length() > 10) {
        JOptionPane.showMessageDialog(this, 
            "Teléfono muy largo: " + jtxtTelefono.getText().length() + "/15 caracteres");
        jtxtTelefono.requestFocus();
        jtxtTelefono.selectAll();
        return;
    }

    
    Conexion cn = new Conexion();
    Connection cc = cn.conectar();
    String SqlInsert = "INSERT INTO ESTUDIANTES VALUES(?,?,?,?,?);";
    try {
        PreparedStatement stmt = cc.prepareStatement(SqlInsert);
        stmt.setString(1, jtxtCedula.getText());
        stmt.setString(2, jtxtNombre.getText());
        stmt.setString(3, jtxtApellido.getText());
        stmt.setString(4, (jtxtDireccion.getText().trim().isEmpty())?"Sin Dirección":jtxtDireccion.getText());
        stmt.setString(5, (jtxtTelefono.getText().trim().isEmpty())?"S/T":jtxtTelefono.getText());
        int n=stmt.executeUpdate();
        if (n>0) {
            JOptionPane.showMessageDialog(this, "Estudiante Insertado");
            mostrarEstudiantes();
            botonesInicio();
            TextosInicio();
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage());
    }
}
    

    
    public void mostrarEstudiantes(){
        String[] titulos = {"Cédula","Nombre","Apellido","Dirección","Teléfono"};
        String registros[]= new String[5];
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);
        Conexion cn = new Conexion();
        Connection cc = cn.conectar();
        String SqlSelect = "SELECT * FROM ESTUDIANTES;";
        try {
            Statement stmt = cc.createStatement();
            ResultSet rs = stmt.executeQuery(SqlSelect);
            while (rs.next()) {
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]=rs.getString(4);
                registros[4]=rs.getString(5);
                modelo.addRow(registros);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        jtblAlumnos.setModel(modelo);

    }
    
    public void editarEstudiante(){
    try {
        // Primero validar que la cédula no esté vacía
        if (jtxtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula es obligatoria para editar");
            jtxtCedula.requestFocus();
            return;
        }
        
        Conexion cn = new Conexion();
        Connection cc = cn.conectar();
        
        // 1. VERIFICAR SI LA CÉDULA EXISTE EN LA BD
        String sqlVerificar = "SELECT COUNT(*) FROM estudiantes WHERE estcedula = ?";
        PreparedStatement stmtVerificar = cc.prepareStatement(sqlVerificar);
        stmtVerificar.setString(1, jtxtCedula.getText());
        
        ResultSet rs = stmtVerificar.executeQuery();
        rs.next();
        int existe = rs.getInt(1);
        
        // Si no existe, mostrar error y salir
        if (existe == 0) {
            JOptionPane.showMessageDialog(this, 
                "La cédula " + jtxtCedula.getText() + " no existe en la base de datos\n" +
                "No se puede editar un estudiante que no existe");
            jtxtCedula.requestFocus();
            
            return;
        }
        
        // 2. Si existe, proceder con la actualización
        String SqlUpdate = "UPDATE Estudiantes SET estnombre=?, estapellido=?, estdireccion=?, esttelefono=? WHERE estcedula=?";
        
        PreparedStatement pdst = cc.prepareStatement(SqlUpdate);
        pdst.setString(1, jtxtNombre.getText());
        pdst.setString(2, jtxtApellido.getText());
        pdst.setString(3, jtxtDireccion.getText());
        pdst.setString(4, jtxtTelefono.getText());
        pdst.setString(5, jtxtCedula.getText());
        
        int filasAfectadas = pdst.executeUpdate();
        if(filasAfectadas > 0){
            JOptionPane.showMessageDialog(this, "Se actualizó el Estudiante correctamente");
            mostrarEstudiantes();   
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estudiante");
        }
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al editar: " + ex.getMessage());
    }
}
    
    public void eliminarEstudiante(){
    try {
        // Primero validar que la cédula no esté vacía
        if (jtxtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula es obligatoria para eliminar");
            jtxtCedula.requestFocus();
            return;
        }
        
        // Verificar si el estudiante existe antes de mostrar confirmación
        Conexion cn = new Conexion();
        Connection cc = cn.conectar();
        
        // 1. VERIFICAR SI LA CÉDULA EXISTE EN LA BD
        String sqlVerificar = "SELECT estnombre, estapellido FROM estudiantes WHERE estcedula = ?";
        PreparedStatement stmtVerificar = cc.prepareStatement(sqlVerificar);
        stmtVerificar.setString(1, jtxtCedula.getText());
        
        ResultSet rs = stmtVerificar.executeQuery();
        
        // Si no existe, mostrar error y salir
        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, 
                "Error: La cédula " + jtxtCedula.getText() + " no existe\n" +
                "No se puede eliminar un estudiante que no está registrado");
            jtxtCedula.requestFocus();
            jtxtCedula.selectAll();
            return;
        }
        
        // Obtener nombre del estudiante para el mensaje de confirmación
        String nombreEstudiante = rs.getString("estnombre") + " " + rs.getString("estapellido");
        
        // 2. Mostrar confirmación con el nombre del estudiante
        if (JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar al estudiante:\n" +
                nombreEstudiante + "\n" +
                "Cédula: " + jtxtCedula.getText() + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            
            // 3. Proceder con la eliminación (usando PreparedStatement seguro)
            String SqlDelete = "DELETE FROM Estudiantes WHERE estcedula = ?";
            PreparedStatement psd = cc.prepareStatement(SqlDelete);
            psd.setString(1, jtxtCedula.getText());
            
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Estudiante " + nombreEstudiante + " eliminado correctamente");
                mostrarEstudiantes();
                
            }
        }
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
    }
}
    
    public void cargarCamposTabla(){
        jtblAlumnos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtblAlumnos.getSelectedRow()!=-1) {
                botonesEliminarActualizar();
                TextosNuevo();
                int fila = jtblAlumnos.getSelectedRow();
                jtxtCedula.setText(jtblAlumnos.getValueAt(fila, 0).toString());
                jtxtNombre.setText(jtblAlumnos.getValueAt(fila, 1).toString());
                jtxtApellido.setText(jtblAlumnos.getValueAt(fila, 2).toString());
                jtxtDireccion.setText(jtblAlumnos.getValueAt(fila, 3).toString());
                jtxtTelefono.setText(jtblAlumnos.getValueAt(fila, 4).toString());
                }
            }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jtxtDireccion = new javax.swing.JTextField();
        jtxtTelefono = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblAlumnos = new javax.swing.JTable();

        jFormattedTextField1.setText("jFormattedTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Cédula:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido:");

        jLabel4.setText("Dirección:");

        jLabel5.setText("Teléfono:");

        jtxtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCedulaActionPerformed(evt);
            }
        });
        jtxtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCedulaKeyTyped(evt);
            }
        });

        jtxtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNombreKeyTyped(evt);
            }
        });

        jtxtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtApellidoKeyTyped(evt);
            }
        });

        jtxtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtDireccionKeyTyped(evt);
            }
        });

        jtxtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTelefonoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(27, 27, 27)
                        .addComponent(jtxtTelefono))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(24, 24, 24)
                        .addComponent(jtxtDireccion))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(29, 29, 29)
                        .addComponent(jtxtApellido))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnCancelar)
                    .addComponent(jbtnEliminar)
                    .addComponent(jbtnEditar)
                    .addComponent(jbtnGuardar)
                    .addComponent(jbtnNuevo))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jbtnNuevo)
                .addGap(18, 18, 18)
                .addComponent(jbtnGuardar)
                .addGap(18, 18, 18)
                .addComponent(jbtnEditar)
                .addGap(18, 18, 18)
                .addComponent(jbtnEliminar)
                .addGap(18, 18, 18)
                .addComponent(jbtnCancelar)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setViewportView(jtblAlumnos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        guardarEstudiante();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        eliminarEstudiante();
        botonesInicio();
        TextosInicio();
        jtblAlumnos.clearSelection();
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        editarEstudiante();
        botonesInicio();
        TextosInicio();
        jtblAlumnos.clearSelection();
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        botonesNuevo();
        TextosNuevo();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jtxtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCedulaActionPerformed

    private void jtxtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCedulaKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || jtxtCedula.getText().length()==10) {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtCedulaKeyTyped

    private void jtxtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTelefonoKeyTyped
        char c = evt.getKeyChar();
        String text = jtxtTelefono.getText();
        if (!Character.isDigit(c) || jtxtTelefono.getText().length()==10) {
            evt.consume();
        }
        if (text.length() == 0 && c != '0') {
            evt.consume();
        }
        if (text.length() == 1 && c != '9') {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtTelefonoKeyTyped

    private void jtxtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyTyped
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtNombreKeyTyped

    private void jtxtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyTyped
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtApellidoKeyTyped

    private void jtxtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtDireccionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDireccionKeyTyped

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        // TODO add your handling code here:
        botonesInicio();
        TextosInicio();
        jtblAlumnos.clearSelection();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Alumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Alumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Alumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Alumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Alumnos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JTable jtblAlumnos;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtDireccion;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtTelefono;
    // End of variables declaration//GEN-END:variables
}
