package Interfaces;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.sql.*;

public class ReporteBarras extends JInternalFrame {
    
    public ReporteBarras() {
        initComponents();
        crearGrafico();
    }
    
    private void initComponents() {
        setTitle("Gráfico de Estudiantes por Curso");
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(800, 600);
        setLocation(50, 50);
    }
    
    private void crearGrafico() {
        try {
            Conexion cc = new Conexion();
            Connection conexion = cc.conectar();
            
            // Crear dataset para el gráfico
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            // Consulta para contar estudiantes por curso
            String sql = "SELECT c.curnombre, COUNT(e.estcedula) as cantidad " +
                        "FROM cursos c " +
                        "LEFT JOIN estudiantes e ON c.estcedula = e.estcedula " +
                        "GROUP BY c.curid, c.curnombre";
            
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Llenar el dataset con los datos
            while (rs.next()) {
                String curso = rs.getString("curnombre");
                int cantidad = rs.getInt("cantidad");
                dataset.addValue(cantidad, "Estudiantes", curso);
            }
            
            // Crear el gráfico de barras CON TODOS LOS PARÁMETROS
            JFreeChart chart = ChartFactory.createBarChart(
                "Cantidad de Estudiantes por Curso", // Título
                "Cursos",                            // Etiqueta eje X
                "Cantidad de Estudiantes",           // Etiqueta eje Y
                dataset,                             // Datos
                org.jfree.chart.plot.PlotOrientation.VERTICAL, // Orientación
                true,                                // Mostrar leyenda
                true,                                // Mostrar tooltips
                false                                // No generar URLs
            );
            
            // Personalizar el gráfico
            chart.setBackgroundPaint(java.awt.Color.white);
            
            // Crear el panel del gráfico
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(750, 550));
            chartPanel.setMouseZoomable(true);
            
            // Agregar el panel al JInternalFrame
            setContentPane(chartPanel);
            
            // Cerrar recursos
            rs.close();
            stmt.close();
            conexion.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el gráfico: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}