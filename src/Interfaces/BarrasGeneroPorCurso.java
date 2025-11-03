package Interfaces;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import javax.swing.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JRViewer;

public class BarrasGeneroPorCurso extends JInternalFrame {

    private JComboBox<CursoItem> cboCurso;
    private JButton btnVer;
    private JPanel pnlViewer;

    public BarrasGeneroPorCurso() {
        super("Gráfico Estadístico hombres y mujeres por curso", true, true, true, true);
        setSize(900, 650);
        initUI();
        cargarCursos();
    }

    private void initUI() {
        cboCurso = new JComboBox<>();
        btnVer   = new JButton("Ver reporte");
        pnlViewer = new JPanel(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("Curso:"));
        top.add(cboCurso);
        top.add(btnVer);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(pnlViewer, BorderLayout.CENTER);

        btnVer.addActionListener(e -> verReporte());
    }

    private void cargarCursos() {
        final String sql = "SELECT curid, curnombre FROM cursos ORDER BY curnombre";
        try (Connection cn = new Conexion().conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            DefaultComboBoxModel<CursoItem> model = new DefaultComboBoxModel<>();
            while (rs.next()) {
                model.addElement(new CursoItem(rs.getInt("curid"), rs.getString("curnombre")));
            }
            cboCurso.setModel(model);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando cursos: " + ex.getMessage());
        }
    }

    private void verReporte() {
        CursoItem sel = (CursoItem) cboCurso.getSelectedItem();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Seleccione un curso."); return; }

        Map<String, Object> params = new HashMap<>();
        params.put("CURSO_ID", sel.id); // Integer (coincide con INT en DB)

        try (Connection cn = new Conexion().conectar()) {
            // Usa el .jasper compilado por iReport 5.6
                JasperPrint jp = null;

                // 1️⃣ Primero intentamos cargar desde el classpath (build/classes)
                InputStream jasper = getClass().getResourceAsStream("/Reportes/BarrasGeneroPorCurso.jasper");

                if (jasper != null) {
                    jp = JasperFillManager.fillReport(jasper, params, cn);
                } else {
                    // 2️⃣ Si no está en el classpath (como tu caso actual), cargamos desde la carpeta src
                    String ruta = System.getProperty("user.dir") + "/src/Reportes/BarrasGeneroPorCurso.jasper";
                    try {
                        jp = JasperFillManager.fillReport(ruta, params, cn);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                            "No se encontró el reporte.\nRuta buscada:\n" + ruta + "\n\n" + e.getMessage());
                        return;
                    }
                }


            pnlViewer.removeAll();
            pnlViewer.add(new JRViewer(jp), BorderLayout.CENTER);
            pnlViewer.revalidate();
            pnlViewer.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Item de combo
    private static class CursoItem {
        final int id; final String nombre;
        CursoItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return nombre; }
    }
}
