/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loadmaster.dao;
import com.loadmaster.database.ConexionBD;
import com.loadmaster.model.Prueba;
import com.loadmaster.model.RegistroLatencia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ferli
 */
public class PruebaDAO {
    public int insertarPrueba(Prueba prueba) {
        String sql = "INSERT INTO pruebas(id_servidor, nombre_prueba, num_usuarios, tiempo_rafaga, "
            + "fecha, latencia_min, latencia_max, latencia_promedio, uptime, tester_nombre) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, prueba.getIdServidor());
            pstmt.setString(2, prueba.getNombrePrueba());
            pstmt.setInt(3, prueba.getNumUsuarios());
            pstmt.setInt(4, prueba.getTiempoRafaga());
            pstmt.setString(5, prueba.getFecha());
            pstmt.setDouble(6, prueba.getLatenciaMin());
            pstmt.setDouble(7, prueba.getLatenciaMax());
            pstmt.setDouble(8, prueba.getLatenciaPromedio());
            pstmt.setDouble(9, prueba.getUptime());
            pstmt.setString(10, prueba.getTesterNombre());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retorna el ID generado para enlazar los registros en tiempo real
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar la prueba: " + e.getMessage());
        }
        return -1;
    }

    public void insertarRegistroLatencia(RegistroLatencia registro) {
        String sql = "INSERT INTO registros_latencia(id_prueba, segundo, latencia_ms, codigo_http) VALUES(?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, registro.getIdPrueba());
            pstmt.setInt(2, registro.getSegundo());
            pstmt.setInt(3, registro.getLatenciaMs());
            pstmt.setInt(4, registro.getCodigoHttp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar registro de latencia: " + e.getMessage());
        }
    }

   public List<Prueba> obtenerHistorial() {
        List<Prueba> lista = new ArrayList<>();
        String sql = "SELECT * FROM pruebas ORDER BY id DESC;";
        
        // Al abrir la conexión dentro del try (), se cerrará sola al terminar el método, 
        // liberando los datos inmediatamente para la interfaz gráfica.
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Prueba p = new Prueba();
                p.setId(rs.getInt("id"));
                p.setNombrePrueba(rs.getString("nombre_prueba"));
                p.setFecha(rs.getString("fecha"));
                p.setNumUsuarios(rs.getInt("num_usuarios"));
                p.setTiempoRafaga(rs.getInt("tiempo_rafaga"));
                p.setLatenciaPromedio(rs.getDouble("latencia_promedio"));
                p.setUptime(rs.getDouble("uptime"));
                p.setTesterNombre(rs.getString("tester_nombre"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el historial de datos: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroLatencia> obtenerRegistrosDePrueba(int idPrueba) {
        List<RegistroLatencia> registros = new ArrayList<>();
        String sql = "SELECT * FROM registros_latencia WHERE id_prueba = ? ORDER BY segundo ASC";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPrueba);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RegistroLatencia r = new RegistroLatencia();
                    r.setId(rs.getInt("id"));
                    r.setIdPrueba(rs.getInt("id_prueba"));
                    r.setSegundo(rs.getInt("segundo"));
                    r.setLatenciaMs(rs.getInt("latencia_ms"));
                    r.setCodigoHttp(rs.getInt("codigo_http"));
                    registros.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener registros de latencia: " + e.getMessage());
        }
        return registros;
    }
    public void actualizarMetricasFinales(int idPrueba, double min, double max, double promedio, double uptime) {
        String sql = "UPDATE pruebas "
            + "SET latencia_min = ?, latencia_max = ?, latencia_promedio = ?, uptime = ? "
            + "WHERE id = ?;";
            
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, min);
            pstmt.setDouble(2, max);
            pstmt.setDouble(3, promedio);
            pstmt.setDouble(4, uptime);
            pstmt.setInt(5, idPrueba);
            pstmt.executeUpdate();
            System.out.println("Métricas finales actualizadas en la BD para la prueba ID: " + idPrueba);
        } catch (SQLException e) {
            System.err.println("Error al actualizar métricas finales: " + e.getMessage());
        }
    }
}
