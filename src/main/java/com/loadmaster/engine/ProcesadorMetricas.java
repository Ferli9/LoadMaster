/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.engine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author ferli
 */
public class ProcesadorMetricas {
    // Acción 8 del Tester: Cálculo de Percentiles, Máximo, Mínimo y Promedio
    public static double calcularPromedio(ArrayList<Integer> latencias) {
        if (latencias == null || latencias.isEmpty()) return 0;
        double suma = 0;
        for (int l : latencias) suma += l;
        return suma / latencias.size();
    }

    public static int obtenerMinimo(ArrayList<Integer> latencias) {
        if (latencias == null || latencias.isEmpty()) return 0;
        return Collections.min(latencias);
    }

    public static int obtenerMaximo(ArrayList<Integer> latencias) {
        if (latencias == null || latencias.isEmpty()) return 0;
        return Collections.max(latencias);
    }

    // Acción 9 del SysAdmin: Cálculo de Disponibilidad (Uptime)
    public static double calcularUptime(ArrayList<Integer> codigosHttp) {
        if (codigosHttp == null || codigosHttp.isEmpty()) return 0;
        int exitos = 0;
        for (int codigo : codigosHttp) {
            if (codigo >= 200 && codigo < 400) {
                exitos++;
            }
        }
        return ((double) exitos / codigosHttp.size()) * 100.0;
    }
}
