package Interfaces;

import java.io.InputStream;
import java.sql.Connection;
import javax.swing.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JRViewer;

public class Reportes extends JInternalFrame {

    public Reportes(Connection cn) {
        super("Reporte de Estudiantes por Curso", true, true, true, true);
        try {
            // Verificar conexión
            if (cn == null || cn.isClosed()) {
                JOptionPane.showMessageDialog(null, "Error: No hay conexión a la base de datos");
                return;
            }
            
            // Cargar el reporte (.jrxml) desde recursos
            InputStream archivo = getClass().getResourceAsStream("/Reportes/ReporteTodosEstudiantes.jrxml");
            
            if (archivo == null) {
                JOptionPane.showMessageDialog(null, "Error: No se encontró el archivo del reporte");
                return;
            }
            
            JasperReport reporte = JasperCompileManager.compileReport(archivo);
            archivo.close(); // Cerrar el stream

            // Llenar el reporte con la conexión
            JasperPrint print = JasperFillManager.fillReport(reporte, null, cn);

            // Crear visor embebido
            JRViewer viewer = new JRViewer(print);
            getContentPane().add(viewer);
            
            this.pack();
            this.setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generando reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
}