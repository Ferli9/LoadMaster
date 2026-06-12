/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 *
 * @author ferli
 */
public class MenuController {
    @FXML
    private Button btnTester;

    @FXML
    private Button btnSysAdmin;

    @FXML
    private void abrirTester(ActionEvent event) {
        cambiarVentana("/com/loadmaster/gui/TesterView.fxml", "LoadMaster - Módulo de Pruebas", btnTester);
    }

    @FXML
    private void abrirSysAdmin(ActionEvent event) {
        cambiarVentana("/com/loadmaster/gui/SysAdminView.fxml", "LoadMaster - Módulo Administrativo", btnSysAdmin);
    }

    // Método reutilizable para cambiar de pantallas
    private void cambiarVentana(String rutaFxml, String titulo, Button botonOrigen) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFxml));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root, 1000, 700)); // Tamaño estándar para ambas ventanas
            stage.show();

            // Cerrar la ventana del menú principal
            Stage menuStage = (Stage) botonOrigen.getScene().getWindow();
            menuStage.close();

        } catch (Exception e) {
            System.err.println("Error al abrir la ventana " + rutaFxml + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
