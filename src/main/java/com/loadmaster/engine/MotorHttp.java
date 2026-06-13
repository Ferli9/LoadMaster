/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.engine;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 *
 * @author ferli
 */
public class MotorHttp {
    
    // --- REFACTOR: Constantes para evitar "Números Mágicos" ---
    private static final int TIMEOUT_SEGUNDOS = 5;
    private static final int HTTP_OK_MIN = 200;
    private static final int HTTP_ERROR_CLIENTE = 400;
    private static final int HTTP_ERROR_INTERNO = 500;
    
    private final HttpClient cliente;

    public MotorHttp() {
        // Configuramos un cliente HTTP que no espere más del tiempo límite establecido
        this.cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SEGUNDOS))
                .build();
    }

    // Registro para almacenar el resultado compacto
    public static class ResultadoPeticion {
        private final int latenciaMs;
        private final int codigoHttp;

        public ResultadoPeticion(int latenciaMs, int codigoHttp) {
            this.latenciaMs = latenciaMs;
            this.codigoHttp = codigoHttp;
        }

        public int getLatenciaMs() {
            return latenciaMs;
        }

        public int getCodigoHttp() {
            return codigoHttp;
        }
    }

    /**
     * Acción 6 del Tester: Validación de Endpoint (Ping).
     * Realiza una petición rápida HEAD para verificar si el servidor está en línea
     * antes de lanzar la prueba de estrés.
     */
    public boolean validarEndpoint(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody()) // Solo pedimos cabeceras para hacer ping
                    .build();
            
            HttpResponse<Void> response = cliente.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() >= HTTP_OK_MIN && response.statusCode() < HTTP_ERROR_CLIENTE;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ejecuta la petición GET real hacia el servidor objetivo y mide la latencia exacta.
     * REFACTOR: Se agregaron unidades de medida "Ms" a las variables de tiempo.
     */
    public ResultadoPeticion hacerPeticion(String url) {
        long tiempoInicioMs = System.currentTimeMillis();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            
            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            long tiempoFinMs = System.currentTimeMillis();
            
            return new ResultadoPeticion((int) (tiempoFinMs - tiempoInicioMs), response.statusCode());
        } catch (Exception e) {
            long tiempoFinMs = System.currentTimeMillis();
            // Simula un error interno de conexión si la petición falla
            return new ResultadoPeticion((int) (tiempoFinMs - tiempoInicioMs), HTTP_ERROR_INTERNO); 
        }
    }
}