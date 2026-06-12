/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.gui;
import com.loadmaster.dao.PruebaDAO;
import com.loadmaster.model.Prueba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
/**
 *
 * @author ferli
 */
public class SysAdminController implements Initializable {
    @FXML private TableView<Prueba> tablaHistorial;
    @FXML private TableColumn<Prueba, Integer> colId;
    @FXML private TableColumn<Prueba, String> colNombre;
    @FXML private TableColumn<Prueba, String> colFecha;
    @FXML private TableColumn<Prueba, Integer> colUsuarios;
    @FXML private TableColumn<Prueba, Integer> colRafaga;
    @FXML private TableColumn<Prueba, Double> colPromedio;
    @FXML private TableColumn<Prueba, Double> colUptime;
    @FXML private TableColumn<Prueba, String> colTester;

    private final PruebaDAO pruebaDAO = new PruebaDAO();
    private ObservableList<Prueba> listaPruebas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Enlazar las columnas de la tabla con los atributos de la clase Prueba
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombrePrueba"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colUsuarios.setCellValueFactory(new PropertyValueFactory<>("numUsuarios"));
        colRafaga.setCellValueFactory(new PropertyValueFactory<>("tiempoRafaga"));
        colPromedio.setCellValueFactory(new PropertyValueFactory<>("latenciaPromedio"));
        colUptime.setCellValueFactory(new PropertyValueFactory<>("uptime"));
        colTester.setCellValueFactory(new PropertyValueFactory<>("testerNombre"));

        cargarDatos();
    }

    @FXML
    private void cargarDatos() {
        List<Prueba> historial = pruebaDAO.obtenerHistorial();
        listaPruebas = FXCollections.observableArrayList(historial);
        tablaHistorial.setItems(listaPruebas);
    }

    // Acción 10 del SysAdmin: Exportar a CSV (Raw Data)
    @FXML
    private void exportarCSV() {
        if (listaPruebas == null || listaPruebas.isEmpty()) {
            mostrarAlerta("Atención", "No hay datos para exportar.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Historial CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("ID,Nombre,Fecha,Usuarios,Tiempo Rafaga,Latencia Promedio,Uptime,Tester");
                for (Prueba p : listaPruebas) {
                    writer.printf("%d,%s,%s,%d,%d,%.2f,%.2f,%s\n",
                            p.getId(), p.getNombrePrueba(), p.getFecha(), p.getNumUsuarios(),
                            p.getTiempoRafaga(), p.getLatenciaPromedio(), p.getUptime(), p.getTesterNombre());
                }
                mostrarAlerta("Éxito", "Archivo CSV guardado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo guardar el CSV: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // Acción 3 del SysAdmin: Reporte Gerencial en PDF
    @FXML
    private void generarPDF() {
        Prueba pruebaSeleccionada = tablaHistorial.getSelectionModel().getSelectedItem();
        
        if (pruebaSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una prueba de la tabla para generar su reporte.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte PDF");
        fileChooser.setInitialFileName("Reporte_" + pruebaSeleccionada.getNombrePrueba().replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    // Título
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                    contentStream.newLineAtOffset(50, 720);
                    contentStream.showText("Reporte de Desempeño: LoadMaster Engineering");
                    contentStream.endText();

                    // Contenido
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.setLeading(20.0f); // Espaciado entre líneas
                    contentStream.newLineAtOffset(50, 680);
                    
                    contentStream.showText("ID de Prueba: " + pruebaSeleccionada.getId());
                    contentStream.newLine();
                    contentStream.showText("Nombre: " + pruebaSeleccionada.getNombrePrueba());
                    contentStream.newLine();
                    contentStream.showText("Fecha de Ejecución: " + pruebaSeleccionada.getFecha());
                    contentStream.newLine();
                    contentStream.showText("Tester Asignado: " + pruebaSeleccionada.getTesterNombre());
                    contentStream.newLine();
                    contentStream.showText("--- Especificaciones Técnicas ---");
                    contentStream.newLine();
                    contentStream.showText("Usuarios Concurrentes: " + pruebaSeleccionada.getNumUsuarios());
                    contentStream.newLine();
                    contentStream.showText("Duración del Estrés: " + pruebaSeleccionada.getTiempoRafaga() + " segundos");
                    contentStream.newLine();
                    contentStream.showText("--- Resultados Obtenidos ---");
                    contentStream.newLine();
                    contentStream.showText("Latencia Promedio: " + pruebaSeleccionada.getLatenciaPromedio() + " ms");
                    contentStream.newLine();
                    contentStream.showText("Disponibilidad (Uptime): " + pruebaSeleccionada.getUptime() + "%");
                    
                    contentStream.endText();
                }

                document.save(file);
                mostrarAlerta("Éxito", "Reporte PDF generado correctamente.", Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                mostrarAlerta("Error", "Error al generar el PDF: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    @FXML
    private void volverAlMenu(ActionEvent event) {
        try {
            // Cargar el diseño del Menú Principal
            Parent root = FXMLLoader.load(getClass().getResource("/com/loadmaster/gui/MenuView.fxml"));
            Stage menuStage = new Stage();
            menuStage.setTitle("LoadMaster Engineering Suite - Inicio");
            menuStage.setScene(new Scene(root));
            menuStage.setResizable(false);
            menuStage.show();

            // Obtener la ventana actual a través del botón que se presionó y cerrarla
            Stage ventanaActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (Exception e) {
            System.err.println("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
