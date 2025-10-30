package Interfaces;

import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author andre
 */
public class Cursos extends javax.swing.JInternalFrame {

    /**
     * Creates new form Cursos
     */
    private static Cursos instancia;
    DefaultTableModel modelo;
    
    public Cursos() {
        initComponents();
        setClosable(true);
        mostrarCursos();
        cargarCamposTabla();
        botonesInicio();
        TextosInicio();
    }
    
    public static Cursos getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new Cursos();
        }
        return instancia;
    }
    
    public void mostrarCursos() {
        String[] titulos = {"ID", "Curso"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        try {
            Conexion cn = new Conexion();
            Connection cc = cn.conectar();
            String sql = "SELECT curid, curnombre FROM cursos ORDER BY curid";
            Statement st = cc.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            String[] registros = new String[2];
            while (rs.next()) {
                registros[0] = rs.getString("curid");
                registros[1] = rs.getString("curnombre");
                modelo.addRow(registros.clone());
            }
            jtblCursos.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void cargarCamposTabla() {
        jtblCursos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jtblCursos.getSelectedRow() != -1) {
                    int f = jtblCursos.getSelectedRow();
                    jtxtNombreCurso.setText(jtblCursos.getValueAt(f, 1).toString());

                    TextosNuevo();         // habilita el textfield
                    jbtnGuardar.setEnabled(false);
                    jbtnEditar.setEnabled(true);
                    jbtnEliminar.setEnabled(true);
                    jbtnNuevo.setEnabled(true);
                    jbtnCancelar.setEnabled(true);

                    jtxtNombreCurso.requestFocus();
                    jtxtNombreCurso.selectAll();
                }
            }
        });
    }
    
    public void buscarCursoPorNombre(String nombreBuscar) {
        String[] titulos = {"ID", "Curso"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);
        
        try {
            Conexion cn = new Conexion();
            Connection cc = cn.conectar();
            String sql = "SELECT curid, curnombre FROM cursos WHERE curnombre LIKE ?";
            PreparedStatement pst = cc.prepareStatement(sql);
            pst.setString(1, "%" + nombreBuscar + "%");
            ResultSet rs = pst.executeQuery();
            
            String[] registros = new String[2];
            while (rs.next()) {
                registros[0] = rs.getString("curid");
                registros[1] = rs.getString("curnombre");
                modelo.addRow(registros);
            }
            jtblCursos.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void guardarCurso() {
    try {
        Conexion cn = new Conexion();
        Connection cc = cn.conectar();
        String sql = "INSERT INTO cursos (curnombre) VALUES (?)";
        PreparedStatement pst = cc.prepareStatement(sql);
        pst.setString(1, jtxtNombreCurso.getText());
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Curso guardado correctamente");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}
    // Métodos CRUD
    public void editarCurso() {
        try {
            Conexion cn = new Conexion();
            Connection cc = cn.conectar();
            String sql = "UPDATE cursos SET curnombre=? WHERE curid=?";
            PreparedStatement pst = cc.prepareStatement(sql);
            pst.setString(1, jtxtNombreCurso.getText());
            int fila = jtblCursos.getSelectedRow();
            String id = jtblCursos.getValueAt(fila, 0).toString();
            pst.setString(2, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Curso actualizado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void eliminarCurso() {
        int confirmacion = JOptionPane.showConfirmDialog(null, 
            "¿Está seguro de eliminar este curso?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Conexion cn = new Conexion();
                Connection cc = cn.conectar();
                String sql = "DELETE FROM cursos WHERE curid=?";
                PreparedStatement pst = cc.prepareStatement(sql);
                int fila = jtblCursos.getSelectedRow();
                String id = jtblCursos.getValueAt(fila, 0).toString();
                pst.setString(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Curso eliminado correctamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    // Métodos de estados de botones
    public void botonesInicio() {
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
    }
    
    public void botonesNuevo() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(true);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
    }
    
    public void botonesEliminarActualizar() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(true);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
    }
    
    // Métodos de estados de textos
    public void TextosInicio() {
        jtxtNombreCurso.setEnabled(false);
        jtxtNombreCurso.setText("");
    }
    
    public void TextosNuevo() {
        jtxtNombreCurso.setEnabled(true);
        jtxtNombreCurso.setText("");
        jtxtNombreCurso.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtxtNombreCurso = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jbtnNuevo = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblCursos = new javax.swing.JTable();
        jtxtBuscarCurso = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(417, 562));

        jPanel1.setPreferredSize(new java.awt.Dimension(271, 267));

        jLabel2.setText("Nombre del curso:");

        jtxtNombreCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNombreCursoActionPerformed(evt);
            }
        });
        jtxtNombreCurso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNombreCursoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtxtNombreCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtNombreCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setPreferredSize(new java.awt.Dimension(128, 275));

        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
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
                .addGap(0, 9, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnNuevo)
                .addGap(7, 7, 7)
                .addComponent(jbtnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Buscar por nombre:");

        jtblCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtblCursos);

        jtxtBuscarCurso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtBuscarCursoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtBuscarCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtBuscarCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Cursos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(172, 172, 172))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtNombreCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNombreCursoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNombreCursoActionPerformed

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        // TODO add your handling code here:
        botonesNuevo();
        TextosNuevo();
        jtblCursos.clearSelection();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        // TODO add your handling code here:
        guardarCurso();
        mostrarCursos();
        botonesInicio();
        TextosInicio();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        // TODO add your handling code here:
        editarCurso();
        mostrarCursos();
        botonesInicio();
        TextosInicio();
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarCurso();
        mostrarCursos();
        botonesInicio();
        TextosInicio();
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        // TODO add your handling code here:
        botonesInicio();
        TextosInicio();
        jtblCursos.clearSelection();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jtxtBuscarCursoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBuscarCursoKeyReleased
        // TODO add your handling code here:
        buscarCursoPorNombre(jtxtBuscarCurso.getText().trim());
    }//GEN-LAST:event_jtxtBuscarCursoKeyReleased

    private void jtxtNombreCursoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreCursoKeyTyped
        // TODO add your handling code here:
        char ch = evt.getKeyChar();
        if (!Character.isLetter(ch) && !Character.isWhitespace(ch) && ch!='ñ' && ch!='Ñ') {
            evt.consume();
        }
        if (jtxtNombreCurso.getText().length() >= 20) { // VARCHAR(20)
            evt.consume();
        }
    }//GEN-LAST:event_jtxtNombreCursoKeyTyped

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
            java.util.logging.Logger.getLogger(Cursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cursos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JTable jtblCursos;
    private javax.swing.JTextField jtxtBuscarCurso;
    private javax.swing.JTextField jtxtNombreCurso;
    // End of variables declaration//GEN-END:variables
}
