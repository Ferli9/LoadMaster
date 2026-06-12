/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.database;

/**
 *
 * @author ferli
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    private static final String URL = "jdbc:sqlite:loadmaster.db";

    // 1. getConexion() ahora SIEMPRE devuelve una conexión nueva y activa el modo WAL
    public static Connection getConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;"); // Evita bloqueos entre pantallas
        }
        return conn;
    }
    
    // 2. Método para verificar e inicializar las tablas al arrancar la app
    public static void inicializarBaseDatos() {
        String tablaServidores = "CREATE TABLE IF NOT EXISTS servidores ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nombre TEXT NOT NULL, "
            + "ip_url TEXT NOT NULL UNIQUE"
            + ");";

        String tablaPruebas = "CREATE TABLE IF NOT EXISTS pruebas ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "id_servidor INTEGER, "
            + "nombre_prueba TEXT NOT NULL, "
            + "num_usuarios INTEGER NOT NULL, "
            + "tiempo_rafaga INTEGER NOT NULL, "
            + "fecha TEXT NOT NULL, "
            + "latencia_min REAL, "
            + "latencia_max REAL, "
            + "latencia_promedio REAL, "
            + "uptime REAL, "
            + "tester_nombre TEXT, "
            + "FOREIGN KEY (id_servidor) REFERENCES servidores(id)"
            + ");";

        String tablaRegistrosLatencia = "CREATE TABLE IF NOT EXISTS registros_latencia ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "id_prueba INTEGER, "
            + "segundo INTEGER NOT NULL, "
            + "latencia_ms INTEGER NOT NULL, "
            + "codigo_http INTEGER NOT NULL, "
            + "FOREIGN KEY (id_prueba) REFERENCES pruebas(id) ON DELETE CASCADE"
            + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("PRAGMA journal_mode=WAL;");
            stmt.execute(tablaServidores);
            stmt.execute(tablaPruebas);
            stmt.execute(tablaRegistrosLatencia);
            
            stmt.execute("INSERT OR IGNORE INTO servidores(id, nombre, ip_url) VALUES(1, 'Servidor Local Default', 'http://localhost');");
            System.out.println("Infraestructura de Base de Datos inicializada correctamente.");
            
        } catch (SQLException e) {
            System.err.println("Error al inicializar las tablas: " + e.getMessage());
        }
    }
}
