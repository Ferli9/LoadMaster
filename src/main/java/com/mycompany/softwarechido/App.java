package com.mycompany.softwarechido;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

  @Override
    public void start(Stage primaryStage) {
        try {
            // Verificar e inicializar la base de datos antes de cargar la GUI
            com.loadmaster.database.ConexionBD.inicializarBaseDatos(); 

            Parent root = FXMLLoader.load(getClass().getResource("/com/loadmaster/gui/MenuView.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("LoadMaster Engineering Suite - Inicio");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error crítico al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Lanzar la aplicación JavaFX
        launch(args);
    }

}