/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.model;

/**
 *
 * @author ferli
 */
public class RegistroLatencia {
    private int id;
    private int idPrueba;
    private int segundo;
    private int latenciaMs;
    private int codigoHttp;

    public RegistroLatencia() {}

    public RegistroLatencia(int idPrueba, int segundo, int latenciaMs, int codigoHttp) {
        this.idPrueba = idPrueba;
        this.segundo = segundo;
        this.latenciaMs = latenciaMs;
        this.codigoHttp = codigoHttp;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }
    public int getSegundo() { return segundo; }
    public void setSegundo(int segundo) { this.segundo = segundo; }
    public int getLatenciaMs() { return latenciaMs; }
    public void setLatenciaMs(int latenciaMs) { this.latenciaMs = latenciaMs; }
    public int getCodigoHttp() { return codigoHttp; }
    public void setCodigoHttp(int codigoHttp) { this.codigoHttp = codigoHttp; }
}

