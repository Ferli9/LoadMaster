/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.engine;
import com.loadmaster.dao.PruebaDAO;
import com.loadmaster.model.RegistroLatencia;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author ferli
 */
public class StressTask extends Task<Void> {
    private final String url;
    private final int numUsuarios;
    private final int tiempoRafagaSegundos;
    private final int idPruebaGenerado;
    
    private final MotorHttp motor;
    private final PruebaDAO pruebaDAO;

    // Listas dinámicas para guardar el total y procesar al final
    private final ArrayList<Integer> todasLatencias = new ArrayList<>();
    private final ArrayList<Integer> todosCodigos = new ArrayList<>();

    public StressTask(String url, int numUsuarios, int tiempoRafagaSegundos, int idPruebaGenerado) {
        this.url = url;
        this.numUsuarios = numUsuarios;
        this.tiempoRafagaSegundos = tiempoRafagaSegundos;
        this.idPruebaGenerado = idPruebaGenerado;
        this.motor = new MotorHttp();
        this.pruebaDAO = new PruebaDAO();
    }

    @Override
    protected Void call() throws Exception {
        // Configuramos un pool de hilos igual al número de usuarios concurrentes
        ExecutorService executor = Executors.newFixedThreadPool(numUsuarios > 0 ? numUsuarios : 1);

        for (int segundoActual = 1; segundoActual <= tiempoRafagaSegundos; segundoActual++) {
            
            // Acción 4 del Tester: Abortar Operación
            if (isCancelled()) {
                break;
            }

            ArrayList<Integer> latenciasDelSegundo = new ArrayList<>();
            ArrayList<Integer> codigosDelSegundo = new ArrayList<>();

            // Lanzamos las peticiones de los "N" usuarios en este segundo
            for (int u = 0; u < numUsuarios; u++) {
                executor.submit(() -> {
                    MotorHttp.ResultadoPeticion res = motor.hacerPeticion(url);
                    synchronized (latenciasDelSegundo) {
                        latenciasDelSegundo.add(res.getLatenciaMs());
    codigosDelSegundo.add(res.getCodigoHttp());
                    }
                });
            }

            // Esperamos un poco a que terminen las peticiones de este segundo
            Thread.sleep(1000); 

            // Procesamos lo que pasó en este segundo para enviarlo a la gráfica y a la BD
            int latenciaPromedioSegundo = 0;
            int ultimoCodigo = 200; // Por defecto

            synchronized (latenciasDelSegundo) {
                if (!latenciasDelSegundo.isEmpty()) {
                    todasLatencias.addAll(latenciasDelSegundo);
                    todosCodigos.addAll(codigosDelSegundo);
                    
                    int suma = 0;
                    for (int l : latenciasDelSegundo) suma += l;
                    latenciaPromedioSegundo = suma / latenciasDelSegundo.size();
                    ultimoCodigo = codigosDelSegundo.get(codigosDelSegundo.size() - 1);
                }
            }

            // Guardamos el punto en la base de datos
            RegistroLatencia registroBD = new RegistroLatencia(idPruebaGenerado, segundoActual, latenciaPromedioSegundo, ultimoCodigo);
            pruebaDAO.insertarRegistroLatencia(registroBD);

            // ACTUALIZAMOS LA GUI (Acción 3 del Tester)
            // Enviamos un String con formato "segundo:latencia:codigo" al controlador
            updateMessage(segundoActual + ":" + latenciaPromedioSegundo + ":" + ultimoCodigo);
            updateProgress(segundoActual, tiempoRafagaSegundos);
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        return null;
    }

    public ArrayList<Integer> getTodasLatencias() {
        return todasLatencias;
    }

    public ArrayList<Integer> getTodosCodigos() {
        return todosCodigos;
    }
}
