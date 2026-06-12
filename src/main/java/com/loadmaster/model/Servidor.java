/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.model;

/**
 *
 * @author ferli
 */
public class Servidor {
    private int id;
    private String nombre;
    private String ipUrl;

    public Servidor() {}

    public Servidor(int id, String nombre, String ipUrl) {
        this.id = id;
        this.nombre = nombre;
        this.ipUrl = ipUrl;
    }

    public Servidor(String nombre, String ipUrl) {
        this.nombre = nombre;
        this.ipUrl = ipUrl;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIpUrl() { return ipUrl; }
    public void setIpUrl(String ipUrl) { this.ipUrl = ipUrl; }

    @Override
    public String toString() {
        return nombre + " (" + ipUrl + ")";
    }
}
