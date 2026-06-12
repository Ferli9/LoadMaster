/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.dao;
import com.loadmaster.database.ConexionBD;
import com.loadmaster.model.Servidor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ferli
 */
public class ServidorDAO {
    public boolean insertar(Servidor servidor) {
        String sql = "INSERT INTO servidores(nombre, ip_url) VALUES(?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, servidor.getNombre());
            pstmt.setString(2, servidor.getIpUrl());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar servidor: " + e.getMessage());
            return false;
        }
    }

    public List<Servidor> listarTodos() {
        List<Servidor> lista = new ArrayList<>();
        String sql = "SELECT * FROM servidores ORDER BY nombre ASC";
        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Servidor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("ip_url")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar servidores: " + e.getMessage());
        }
        return lista;
    }
}
