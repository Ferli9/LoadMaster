/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.gui;
import com.loadmaster.dao.PruebaDAO;
import com.loadmaster.engine.MotorHttp;
import com.loadmaster.engine.ProcesadorMetricas;
import com.loadmaster.engine.StressTask;
import com.loadmaster.model.Prueba;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class TesterController implements Initializable {
    @FXML private TextField txtTesterNombre;
    @FXML private TextField txtNombrePrueba, txtUrl, txtUsuarios, txtDuracion;
    @FXML private Button btnIniciar, btnAbortar, btnPing;
    @FXML private LineChart<Number, Number> chartLatencia;
    @FXML private ProgressBar progressTest;
    @FXML private Label lblStatus, lblPercentiles;

    private XYChart.Series<Number, Number> serieLatencia;
    private StressTask currentTask;
    private int idPruebaActual = -1;
    private final PruebaDAO pruebaDAO = new PruebaDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serieLatencia = new XYChart.Series<>();
        serieLatencia.setName("Latencia Promedio (ms)");
        chartLatencia.getData().add(serieLatencia);
    }

    @FXML
    private void handlePing() {
        String url = txtUrl.getText();
        if (url.isEmpty()) return;
        
        lblStatus.setText("Estado: Validando conexión...");
        new Thread(() -> {
            boolean ok = new MotorHttp().validarEndpoint(url);
            Platform.runLater(() -> {
                if (ok) {
                    lblStatus.setText("Estado: Servidor disponible (200 OK)");
                    mostrarAlerta("Éxito", "El servidor respondió correctamente.", Alert.AlertType.INFORMATION);
                } else {
                    lblStatus.setText("Estado: Error de conexión");
                    mostrarAlerta("Error", "No se pudo contactar al servidor.", Alert.AlertType.ERROR);
                }
            });
        }).start();
    }

    @FXML
    private void handleIniciar() {
        try {
         // Captura de datos
        String nombre = txtNombrePrueba.getText();
        String url = txtUrl.getText();
        int usuarios = Integer.parseInt(txtUsuarios.getText());
        int segundos = Integer.parseInt(txtDuracion.getText());
        String nombreTester = txtTesterNombre.getText(); // <-- Capturamos el nuevo campo

        // Añadimos nombreTester a la validación para que no lo dejen en blanco
        if (nombre.isEmpty() || url.isEmpty() || nombreTester.isEmpty()) throw new Exception("Faltan campos."); 

        // Acción 7 del Tester: Asignación de Etiquetas y Guardado inicial
        Prueba p = new Prueba();
        p.setNombrePrueba(nombre);
        p.setIdServidor(1); // Por ahora asignamos al ID 1 (puedes mejorarlo luego)
        p.setNumUsuarios(usuarios);
        p.setTiempoRafaga(segundos);
        p.setFecha(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        p.setTesterNombre(nombreTester); // <-- Asignamos la variable en lugar del texto fijo
            idPruebaActual = pruebaDAO.insertarPrueba(p);

            // Preparar Gráfica
            handleLimpiar();
            btnIniciar.setDisable(true);
            btnAbortar.setDisable(false);
            lblStatus.setText("Estado: Ejecutando prueba de estrés...");

            // Iniciar Hilo de Negocio (StressTask)
           currentTask = new StressTask(url, usuarios, segundos, idPruebaActual);

            // Vincular progreso
            progressTest.progressProperty().bind(currentTask.progressProperty());

            // Acción 3 del Tester: Monitoreo en Tiempo Real (Listener del mensaje)
            currentTask.messageProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    String[] datos = newVal.split(":"); // "segundo:latencia:codigo"
                    int seg = Integer.parseInt(datos[0]);
                    int lat = Integer.parseInt(datos[1]);
                    
                    Platform.runLater(() -> {
                        serieLatencia.getData().add(new XYChart.Data<>(seg, lat));
                    });
                }
            });

            currentTask.setOnSucceeded(e -> finalizarPrueba());
            currentTask.setOnCancelled(e -> lblStatus.setText("Estado: Prueba ABORTADA por el usuario."));
            currentTask.setOnFailed(e -> lblStatus.setText("Estado: Error crítico en la prueba."));

            new Thread(currentTask).start();

        } catch (Exception ex) {
            mostrarAlerta("Error de entrada", "Verifica los datos: " + ex.getMessage(), Alert.AlertType.WARNING);
        }
    }

   private void finalizarPrueba() {
        btnIniciar.setDisable(false);
        btnAbortar.setDisable(true);
        lblStatus.setText("Estado: Prueba completada con éxito.");

        // 1. Cálculo de Percentiles finales
        double prom = ProcesadorMetricas.calcularPromedio(currentTask.getTodasLatencias());
      int min = ProcesadorMetricas.obtenerMinimo(currentTask.getTodasLatencias());
int max = ProcesadorMetricas.obtenerMaximo(currentTask.getTodasLatencias()); 
        double uptime = ProcesadorMetricas.calcularUptime(currentTask.getTodosCodigos());

        lblPercentiles.setText(String.format("Min: %dms | Max: %dms | Prom: %.2fms", min, max, prom));
        
        // 2. Actualizar la base de datos con los resultados finales
        if (idPruebaActual != -1) {
            pruebaDAO.actualizarMetricasFinales(idPruebaActual, min, max, prom, uptime);
        }

        mostrarAlerta("Completado", "La prueba ha finalizado y las métricas fueron consolidadas en el historial.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleAbortar() {
        if (currentTask != null) {
            currentTask.cancel();
            btnIniciar.setDisable(false);
            btnAbortar.setDisable(true);
        }
    }

    @FXML
    private void handleLimpiar() {
        // Acción 9: Limpieza de Caché visual
        serieLatencia.getData().clear();
        lblPercentiles.setText("Min: 0ms | Max: 0ms | Prom: 0ms");
        progressTest.progressProperty().unbind();
        progressTest.setProgress(0);
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
