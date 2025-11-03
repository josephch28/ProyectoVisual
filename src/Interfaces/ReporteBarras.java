package Interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import java.util.*;
import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class ReporteBarras extends JInternalFrame {

    private static final String SERIE_TOTAL = "Estudiantes";
    private static final String SERIE_H = "Hombres";
    private static final String SERIE_M = "Mujeres";

    private final JTabbedPane tabs = new JTabbedPane();

    public ReporteBarras() {
        super("Reportes generales por curso en gráfico de barra", true, true, true, true);
        setSize(860, 520);
        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);

        tabs.addTab("Estudiantes por curso", crearPanelEstudiantesPorCurso());
        tabs.addTab("Distribución Hombres y Mujeres por curso", crearPanelHMporCurso());
    }

    // =================== GRAF 1: Estudiantes por curso ===================
    private JPanel crearPanelEstudiantesPorCurso() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        // Nota: LEFT JOIN para incluir cursos sin matrículas
        final String sql = """
            SELECT c.curnombre AS curso, COUNT(m.id) AS cantidad
            FROM cursos c
            LEFT JOIN matriculas m ON m.curso = c.curid
            GROUP BY c.curnombre
            ORDER BY c.curnombre
        """;

        try (Connection cn = new Conexion().conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean algunCurso = false;
            while (rs.next()) {
                algunCurso = true;
                String curso = rs.getString("curso");
                int cantidad = rs.getInt("cantidad");
                ds.addValue(cantidad, SERIE_TOTAL, curso);
            }

            if (!algunCurso) {
                return panelInfo("""
                    No hay cursos registrados o no hay datos en la tabla.
                    Verifica que la base 'cuartouta' tenga cursos y/o matrículas.
                """);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Estudiantes por curso",      // title
                "Curso",                      // category axis
                "Cantidad",                   // value axis
                ds,                           // dataset
                PlotOrientation.VERTICAL,     // orientation
                true,                         // legend
                true,                         // tooltips
                false                         // urls
            );

            aplicarEstilo(chart);
            return new ChartPanel(chart);

        } catch (Exception e) {
            return panelError("Error consultando Estudiantes por curso:\n" + e.getMessage());
        }
    }

    // ========== GRAF 2: Distribución H/M por curso (agrupado) ==========
    private JPanel crearPanelHMporCurso() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        // LEFT JOIN encadenado para:
        // - incluir cursos sin matrículas
        // - cuando haya matrículas, traer sexo desde estudiantes
        final String sql = """
            SELECT c.curnombre AS curso, e.estsexo AS sexo, COUNT(m.id) AS cantidad
            FROM cursos c
            LEFT JOIN matriculas m ON m.curso = c.curid
            LEFT JOIN estudiantes e ON e.estcedula = m.estudiante
            GROUP BY c.curnombre, e.estsexo
            ORDER BY c.curnombre, e.estsexo
        """;

        // Guardamos cursos para completar series faltantes (poner 0)
        Set<String> cursos = new LinkedHashSet<>();
        Map<String, Integer> hombresPorCurso = new HashMap<>();
        Map<String, Integer> mujeresPorCurso = new HashMap<>();

        try (Connection cn = new Conexion().conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean algunCurso = false;
            while (rs.next()) {
                algunCurso = true;
                String curso = rs.getString("curso");
                cursos.add(curso);

                String sexo = rs.getString("sexo"); // puede venir null si no hay matrículas
                int cantidad = rs.getInt("cantidad");

                if (sexo == null) continue; // no agregar barra "sin sexo"; completaremos abajo con ceros

                if ("M".equalsIgnoreCase(sexo)) {
                    hombresPorCurso.put(curso, cantidad);
                } else if ("F".equalsIgnoreCase(sexo)) {
                    mujeresPorCurso.put(curso, cantidad);
                }
            }

            if (!algunCurso) {
                return panelInfo("""
                    No hay cursos registrados.
                    Agrega cursos y/o matrículas para ver la distribución H/M.
                """);
            }

            // Completar ceros y cargar dataset
            for (String curso : cursos) {
                int h = hombresPorCurso.getOrDefault(curso, 0);
                int m = mujeresPorCurso.getOrDefault(curso, 0);
                ds.addValue(h, SERIE_H, curso);
                ds.addValue(m, SERIE_M, curso);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Distribución H/M por curso",
                "Curso",
                "Cantidad",
                ds,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
            );

            aplicarEstilo(chart);
            return new ChartPanel(chart);

        } catch (Exception e) {
            return panelError("Error consultando H/M por curso:\n" + e.getMessage());
        }
    }

    // =================== ESTILO / UTILES ===================
    private void aplicarEstilo(JFreeChart chart) {
        Font fTitulo = new Font("SansSerif", Font.BOLD, 16);
        Font fEjes   = new Font("SansSerif", Font.PLAIN, 12);

        chart.getTitle().setFont(fTitulo);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(fEjes);
        plot.getDomainAxis().setTickLabelFont(fEjes);
        plot.getRangeAxis().setLabelFont(fEjes);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setItemMargin(0.03);

        chart.setBackgroundPaint(new Color(0,0,0,0));
        plot.setBackgroundPaint(UIManager.getColor("Panel.background"));
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(180,180,180));
    }

    private JPanel panelError(String msg) {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea(msg);
        ta.setEditable(false);
        ta.setForeground(new Color(160, 0, 0));
        ta.setBackground(UIManager.getColor("Panel.background"));
        ta.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        p.add(ta, BorderLayout.CENTER);
        return p;
    }

    private JPanel panelInfo(String msg) {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea(msg);
        ta.setEditable(false);
        ta.setForeground(new Color(60, 60, 60));
        ta.setBackground(UIManager.getColor("Panel.background"));
        ta.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        p.add(ta, BorderLayout.CENTER);
        return p;
    }
}
