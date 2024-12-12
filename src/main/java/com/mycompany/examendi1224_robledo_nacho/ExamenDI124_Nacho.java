/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.examendi1224_robledo_nacho;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Table;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author nacho
 */
public class ExamenDI124_Nacho extends javax.swing.JFrame {
    
String[] Clase1 = {"Nacho Robledo", "Sergio Martin", "Victor Ruiz","Persona 4","Persona 5","Persona 6","Persona 7","Persona 8"};
String[] Clase2 = {"Juan Marin", "Alba Gonzalez" ,"Persona 3","Persona 4","Persona 5","Persona 6","Persona 7","Persona 8"};


    /**
     * Creates new form ExamenDI124_Nacho
     */
    public ExamenDI124_Nacho() {
        initComponents();
        equipo.addItem("Clase1");  
        equipo.addItem("Clase2");  
        equipo.addActionListener(evt -> seleccionarEquipo());
        Guardar.addActionListener(evt -> crearExcel());
        //pdf.addActionListener(evt -> pdf());

        
    }
    
   
    
    private void seleccionarEquipo() {
        String cogerequipo = (String) equipo.getSelectedItem();
        jugadores.removeAllItems(); // Elimina todos los elementos previos

        if ("Clase1".equals(cogerequipo)) {
            // Agrega los jugadores de la Clase1
            for (String jugador : Clase1) {
                jugadores.addItem(jugador);
            }
        } else if ("Clase2".equals(cogerequipo)) {
            // Agrega los jugadores de la Clase2
            for (String jugador : Clase2) {
                jugadores.addItem(jugador);
            }
        }
    }

    private void crearExcel() {
        String equipoSeleccionado = (String) equipo.getSelectedItem();
        String jugadorSeleccionado = (String) jugadores.getSelectedItem();

        if (jugadorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una clase.");
            return;
        }

        // Notas asignadas
        int di = (int) dispinnier.getValue();
        int psp = (int) pspspinner.getValue();
        int pdmd = (int) pdmdspinnier.getValue();
        int fol = (int) folspiner.getValue();

        String filePath = "C:\\Users\\nacho\\Desktop\\Notas_" + equipoSeleccionado + ".xlsx";

        try {
            guardarDatosExcel(filePath, equipoSeleccionado, jugadorSeleccionado, di, psp, pdmd, fol);
            JOptionPane.showMessageDialog(this, "Archivo actualizado: " + filePath);
            calcularMedias(filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al crear el archivo Excel: " + e.getMessage());
        }
    }

    private void guardarDatosExcel(String filePath, String equipo, String alumno, int di, int psp, int pdmd, int fol) throws IOException {
        Workbook workbook;
        Sheet hoja;

        File archivo = new File(filePath);
        if (archivo.exists()) {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                workbook = new XSSFWorkbook(fis);
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        hoja = workbook.getSheet(equipo);
        if (hoja == null) {
            hoja = workbook.createSheet(equipo);
            Row encabezado = hoja.createRow(0);
            encabezado.createCell(0).setCellValue("Alumno");
            encabezado.createCell(1).setCellValue("DI");
            encabezado.createCell(2).setCellValue("PSP");
            encabezado.createCell(3).setCellValue("PDMD");
            encabezado.createCell(4).setCellValue("FOL");
        }

        Row row = hoja.createRow(hoja.getPhysicalNumberOfRows());
        row.createCell(0).setCellValue(alumno);
        row.createCell(1).setCellValue(di);
        row.createCell(2).setCellValue(psp);
        row.createCell(3).setCellValue(pdmd);
        row.createCell(4).setCellValue(fol);

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            workbook.write(fos);
        }
    }

    private void calcularMedias(String filePath) throws IOException {
        File archivo = new File(filePath);

        try (FileInputStream fis = new FileInputStream(archivo)) {
            Workbook workbook = new XSSFWorkbook(fis);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                double totalDI = 0, totalPSP = 0, totalPDMD = 0, totalFOL = 0;
                int numFilas = sheet.getPhysicalNumberOfRows() - 1; // Ignorar encabezado

                for (int rowNum = 1; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    totalDI += row.getCell(1).getNumericCellValue();
                    totalPSP += row.getCell(2).getNumericCellValue();
                    totalPDMD += row.getCell(3).getNumericCellValue();
                    totalFOL += row.getCell(4).getNumericCellValue();
                }

                if (numFilas > 0) {
                    double mediaDI = totalDI / numFilas;
                    double mediaPSP = totalPSP / numFilas;
                    double mediaPDMD = totalPDMD / numFilas;
                    double mediaFOL = totalFOL / numFilas;

                    Row mediaRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
                    mediaRow.createCell(0).setCellValue("Media");
                    mediaRow.createCell(1).setCellValue(mediaDI);
                    mediaRow.createCell(2).setCellValue(mediaPSP);
                    mediaRow.createCell(3).setCellValue(mediaPDMD);
                    mediaRow.createCell(4).setCellValue(mediaFOL);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                workbook.write(fos);
            }
        }
    }
    


    public void generarPDFConGrafico(Document document, String[] asignaturas, double[] medias) throws IOException {
        try {
            // Crear gráfico de barras
            JFreeChart chart = crearGraficoDeBarras(medias, asignaturas);

            // Ruta donde guardar la imagen del gráfico
            String rutaGrafico = "C:\\Users\\nacho\\Desktop\\Estadisticas\\mediaPorAsignatura.png";

            // Guardar el gráfico como imagen
            guardarGraficoComoImagen(chart, rutaGrafico);

            // Cargar la imagen del gráfico
            ImageData grafico = ImageDataFactory.create(rutaGrafico);
            Image graficoImage = new Image(grafico);
            graficoImage.scaleToFit(500, 300);

            // Añadir la imagen del gráfico al PDF
            document.add(new Paragraph("Gráfico de Medias por Asignatura:"));
            document.add(graficoImage);
        } catch (Exception e) {
            document.add(new Paragraph("No se pudo generar el gráfico de medias."));
            e.printStackTrace();
        }
    }

    public JFreeChart crearGraficoDeBarras(double[] medias, String[] asignaturas) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Añadir los datos al dataset
        for (int i = 0; i < medias.length; i++) {
            dataset.addValue(medias[i], "Media", asignaturas[i]);
        }

        // Crear el gráfico de barras
        return ChartFactory.createBarChart(
                "Media por Asignatura",  // Título del gráfico
                "Asignaturas",           // Eje X
                "Media",                 // Eje Y
                dataset,                 // Dataset
                PlotOrientation.VERTICAL, // Tipo de gráfico
                true,                    // Leyenda
                true,                    // Herramientas
                false                    // URL
        );
    }

    public void guardarGraficoComoImagen(JFreeChart chart, String rutaArchivo) throws IOException {
        BufferedImage image = chart.createBufferedImage(600, 400);
        File archivo = new File(rutaArchivo);
        ImageIO.write(image, "PNG", archivo);
    }

   


    
    
    
    




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        componente1 = new com.mycompany.examendi1224_robledo_nacho.componente();
        componente2 = new com.mycompany.examendi1224_robledo_nacho.componente();
        componente3 = new com.mycompany.examendi1224_robledo_nacho.componente();
        componente4 = new com.mycompany.examendi1224_robledo_nacho.componente();
        componente5 = new com.mycompany.examendi1224_robledo_nacho.componente();
        pspspinner = new javax.swing.JSpinner();
        alumno = new javax.swing.JLabel();
        pdmdspinnier = new javax.swing.JSpinner();
        dispinnier = new javax.swing.JSpinner();
        jugadores = new javax.swing.JComboBox<>();
        Equipo = new javax.swing.JLabel();
        folspiner = new javax.swing.JSpinner();
        equipo = new javax.swing.JComboBox<>();
        Guardar = new javax.swing.JButton();
        componente6 = new com.mycompany.examendi1224_robledo_nacho.componente();
        componente7 = new com.mycompany.examendi1224_robledo_nacho.componente();
        componente8 = new com.mycompany.examendi1224_robledo_nacho.componente();
        componente9 = new com.mycompany.examendi1224_robledo_nacho.componente();
        pdf = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        tamaño1 = new javax.swing.JMenu();
        pequeño = new javax.swing.JRadioButtonMenuItem();
        normal = new javax.swing.JRadioButtonMenuItem();
        grande = new javax.swing.JRadioButtonMenuItem();
        condicionesServicio1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        alumno.setText("Alumno");

        jugadores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        Equipo.setText("Clases");

        equipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        equipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equipoActionPerformed(evt);
            }
        });

        Guardar.setText("Guadar");
        Guardar.setPreferredSize(new java.awt.Dimension(72, 30));
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });

        componente6.setText("PSP");

        componente7.setText("DI");

        componente8.setText("PDMD");

        componente9.setText("FOL");

        pdf.setText("PDF");

        tamaño1.setText("susbrayado");
        tamaño1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tamaño1ActionPerformed(evt);
            }
        });

        pequeño.setSelected(true);
        pequeño.setText("rojo");
        pequeño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pequeñoActionPerformed(evt);
            }
        });
        tamaño1.add(pequeño);

        normal.setSelected(true);
        normal.setText("Mediano");
        normal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normalActionPerformed(evt);
            }
        });
        tamaño1.add(normal);

        grande.setSelected(true);
        grande.setText("Grande");
        tamaño1.add(grande);

        jMenuBar2.add(tamaño1);

        condicionesServicio1.setText("Condiciones de servicio");
        condicionesServicio1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                condicionesServicio1MouseClicked(evt);
            }
        });
        condicionesServicio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                condicionesServicio1ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        condicionesServicio1.add(jMenuItem1);

        jMenuBar2.add(condicionesServicio1);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addComponent(componente7, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(componente6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(componente8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dispinnier, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pspspinner, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pdmdspinnier, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(alumno, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(46, 46, 46)
                                        .addComponent(Equipo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(componente9, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(folspiner, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(equipo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pdf)
                                    .addComponent(Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45)))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alumno, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Equipo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(equipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dispinnier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(componente7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pspspinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(componente6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pdmdspinnier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(componente8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(folspiner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(componente9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pdf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void equipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equipoActionPerformed

    }//GEN-LAST:event_equipoActionPerformed

    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GuardarActionPerformed

    private void pequeñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pequeñoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pequeñoActionPerformed

    private void normalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_normalActionPerformed

    private void tamaño1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tamaño1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tamaño1ActionPerformed

    private void condicionesServicio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_condicionesServicio1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_condicionesServicio1ActionPerformed

    private void condicionesServicio1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_condicionesServicio1MouseClicked

    }//GEN-LAST:event_condicionesServicio1MouseClicked

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1MouseClicked

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
            java.util.logging.Logger.getLogger(ExamenDI124_Nacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExamenDI124_Nacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExamenDI124_Nacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExamenDI124_Nacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExamenDI124_Nacho().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Equipo;
    private javax.swing.JButton Guardar;
    private javax.swing.JLabel alumno;
    private com.mycompany.examendi1224_robledo_nacho.componente componente1;
    private com.mycompany.examendi1224_robledo_nacho.componente componente2;
    private com.mycompany.examendi1224_robledo_nacho.componente componente3;
    private com.mycompany.examendi1224_robledo_nacho.componente componente4;
    private com.mycompany.examendi1224_robledo_nacho.componente componente5;
    private com.mycompany.examendi1224_robledo_nacho.componente componente6;
    private com.mycompany.examendi1224_robledo_nacho.componente componente7;
    private com.mycompany.examendi1224_robledo_nacho.componente componente8;
    private com.mycompany.examendi1224_robledo_nacho.componente componente9;
    private javax.swing.JMenu condicionesServicio1;
    private javax.swing.JSpinner dispinnier;
    private javax.swing.JComboBox<String> equipo;
    private javax.swing.JSpinner folspiner;
    private javax.swing.JRadioButtonMenuItem grande;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JComboBox<String> jugadores;
    private javax.swing.JRadioButtonMenuItem normal;
    private javax.swing.JButton pdf;
    private javax.swing.JSpinner pdmdspinnier;
    private javax.swing.JRadioButtonMenuItem pequeño;
    private javax.swing.JSpinner pspspinner;
    private javax.swing.JMenu tamaño1;
    // End of variables declaration//GEN-END:variables
}
