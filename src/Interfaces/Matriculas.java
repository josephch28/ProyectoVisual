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
 * @author Mateo Auz
 */
public class Matriculas extends javax.swing.JFrame {

   private static Matriculas instancia;
    DefaultTableModel modelo;
    
    public Matriculas() {
        initComponents();
        mostrarMatriculas();
        cargarEstudiantes();
        cargarCursos();
    }
    
    public static Matriculas getInstancia() {
        if (instancia == null) {
            instancia = new Matriculas();
        }
        return instancia;
    }
    
    public void mostrarMatriculas() {
    String[] titulos = {"ID", "Estudiante", "Curso"};
    String registros[] = new String[3];
    modelo = new DefaultTableModel(null, titulos);
    Conexion cn = new Conexion();
    Connection cc = cn.conectar();

    String SqlSelect = """
        SELECT m.id, e.estcedula, c.curnombre
        FROM matriculas m
        INNER JOIN estudiantes e ON m.estudiante = e.estcedula
        INNER JOIN cursos c ON m.curso = c.curid;
        """;

    try {
        Statement stmt = cc.createStatement();
        ResultSet rs = stmt.executeQuery(SqlSelect);
        while (rs.next()) {
            registros[0] = rs.getString("id");
            registros[1] = rs.getString("estcedula");
            registros[2] = rs.getString("curnombre");
            modelo.addRow(registros);
        }
        jtblMatriculas.setModel(modelo);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al mostrar matrículas: " + ex.getMessage());
    }
}
    
    public void cargarEstudiantes() {
        Conexion cn = new Conexion();
        Connection cc = cn.conectar();
        try {
            String sql = "SELECT estcedula FROM estudiantes";
            PreparedStatement ps = cc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            cbxEstudiantes.removeAllItems();
            cbxEstudiantes.addItem("Seleccione un estudiante");
            while (rs.next()) {
                cbxEstudiantes.addItem(rs.getString("estcedula"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + e.getMessage());
        }
    }
    
    public void cargarCursos() {
        Conexion cn = new Conexion();
        Connection cc = cn.conectar();
        try {
            String sql = "SELECT curid, curnombre FROM cursos";
            PreparedStatement ps = cc.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            cbxCursos.removeAllItems();
            cbxCursos.addItem("Seleccione un curso");
            while (rs.next()) {
                cbxCursos.addItem(rs.getInt("curid") + " - " + rs.getString("curnombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cursos: " + e.getMessage());
        }
    }
    
    public void matricularEstudiante() {
        String estudiante = (String) cbxEstudiantes.getSelectedItem();
        String cursoStr = (String) cbxCursos.getSelectedItem();

        if (estudiante == null || estudiante.equals("Seleccione un estudiante")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante.");
            return;
        }
        if (cursoStr == null || cursoStr.equals("Seleccione un curso")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un curso.");
            return;
        }

        int cursoId;
        try {
            cursoId = Integer.parseInt(cursoStr.split(" - ")[0]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID del curso.");
            return;
        }

        Conexion cn = new Conexion();
        Connection cc = cn.conectar();

        try {
            String sqlCheck = "SELECT COUNT(*) FROM matriculas WHERE estudiante = ? AND curso = ?";
            PreparedStatement psCheck = cc.prepareStatement(sqlCheck);
            psCheck.setString(1, estudiante);
            psCheck.setInt(2, cursoId);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "El estudiante ya está matriculado en este curso.");
                return;
            }

            String sqlInsert = "INSERT INTO matriculas (estudiante, curso) VALUES (?, ?)";
            PreparedStatement psInsert = cc.prepareStatement(sqlInsert);
            psInsert.setString(1, estudiante);
            psInsert.setInt(2, cursoId);
            int n = psInsert.executeUpdate();

            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Matrícula registrada correctamente.");
                mostrarMatriculas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la matrícula.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al matricular: " + ex.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbxEstudiantes = new javax.swing.JComboBox<>();
        cbxCursos = new javax.swing.JComboBox<>();
        jbtnMatricular = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblMatriculas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Matrículas");

        jLabel2.setText("Estudiantes:");

        jLabel3.setText("Cursos:");

        cbxEstudiantes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbxCursos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jbtnMatricular.setText("Matricular");
        jbtnMatricular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMatricularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(118, 118, 118))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(cbxCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnMatricular)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbxEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jbtnMatricular)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxCursos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jtblMatriculas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblMatriculas);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnMatricularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMatricularActionPerformed
        matricularEstudiante();
    }//GEN-LAST:event_jbtnMatricularActionPerformed

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
            java.util.logging.Logger.getLogger(Matriculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Matriculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Matriculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Matriculas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Matriculas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbxCursos;
    private javax.swing.JComboBox<String> cbxEstudiantes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnMatricular;
    private javax.swing.JTable jtblMatriculas;
    // End of variables declaration//GEN-END:variables
}
