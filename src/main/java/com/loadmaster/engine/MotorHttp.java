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
    private final HttpClient cliente;

    public MotorHttp() {
        // Configuramos un cliente HTTP que no espere más de 5 segundos por respuesta
        this.cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    // Registro nativo de Java 17 para almacenar el resultado compacto
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

    // Acción 6 del Tester: Validación de Endpoint (Ping)
    public boolean validarEndpoint(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody()) // Solo pedimos cabeceras para hacer ping
                    .build();
            
            HttpResponse<Void> response = cliente.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() >= 200 && response.statusCode() < 400;
        } catch (Exception e) {
            return false;
        }
    }

    // Ejecuta la petición real y mide el tiempo
    public ResultadoPeticion hacerPeticion(String url) {
        long inicio = System.currentTimeMillis();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            
            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            long fin = System.currentTimeMillis();
            
            return new ResultadoPeticion((int) (fin - inicio), response.statusCode());
        } catch (Exception e) {
            long fin = System.currentTimeMillis();
            return new ResultadoPeticion((int) (fin - inicio), 500); // 500 simula un error interno de conexión
        }
    }
}
