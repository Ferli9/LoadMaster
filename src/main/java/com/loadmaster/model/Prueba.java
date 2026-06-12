/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.model;

/**
 *
 * @author ferli
 */
public class Prueba {
    private int id;
    private int idServidor;
    private String nombrePrueba;
    private int numUsuarios;
    private int tiempoRafaga;
    private String fecha;
    private double latenciaMin;
    private double latenciaMax;
    private double latenciaPromedio;
    private double uptime;
    private String testerNombre;

    public Prueba() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdServidor() { return idServidor; }
    public void setIdServidor(int idServidor) { this.idServidor = idServidor; }
    public String getNombrePrueba() { return nombrePrueba; }
    public void setNombrePrueba(String nombrePrueba) { this.nombrePrueba = nombrePrueba; }
    public int getNumUsuarios() { return numUsuarios; }
    public void setNumUsuarios(int numUsuarios) { this.numUsuarios = numUsuarios; }
    public int getTiempoRafaga() { return tiempoRafaga; }
    public void setTiempoRafaga(int tiempoRafaga) { this.tiempoRafaga = tiempoRafaga; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public double getLatenciaMin() { return latenciaMin; }
    public void setLatenciaMin(double latenciaMin) { this.latenciaMin = latenciaMin; }
    public double getLatenciaMax() { return latenciaMax; }
    public void setLatenciaMax(double latenciaMax) { this.latenciaMax = latenciaMax; }
    public double getLatenciaPromedio() { return latenciaPromedio; }
    public void setLatenciaPromedio(double latenciaPromedio) { this.latenciaPromedio = latenciaPromedio; }
    public double getUptime() { return uptime; }
    public void setUptime(double uptime) { this.uptime = uptime; }
    public String getTesterNombre() { return testerNombre; }
    public void setTesterNombre(String testerNombre) { this.testerNombre = testerNombre; }
}
