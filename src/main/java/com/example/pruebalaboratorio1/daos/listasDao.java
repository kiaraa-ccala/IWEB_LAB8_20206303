package com.example.pruebalaboratorio1.daos;

import com.example.pruebalaboratorio1.beans.genero;
import com.example.pruebalaboratorio1.beans.streaming;
import com.example.pruebalaboratorio1.beans.pelicula;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class listasDao extends baseDao {

    @Override
    public boolean validarBorrado(pelicula movie) {
        // Implementación vacía o de ejemplo del método abstracto
        return false; // Puedes ajustar esta lógica según lo que necesites
    }

    public ArrayList<genero> listarGeneros() {
        ArrayList<genero> listaGeneros = new ArrayList<>();
        String sql = "SELECT * FROM genero";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                genero gen = new genero();
                gen.setIdGenero(rs.getInt("idGenero"));
                gen.setNombre(rs.getString("nombre"));
                listaGeneros.add(gen);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considera usar un logger para un manejo de errores más robusto
        }
        return listaGeneros;
    }

    public ArrayList<streaming> listarStreaming() {
        ArrayList<streaming> listaStreaming = new ArrayList<>();
        String sql = "SELECT * FROM streaming";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                streaming stream = new streaming();
                stream.setIdStreaming(rs.getInt("idStreaming"));
                stream.setNombreServicio(rs.getString("nombreServicio"));
                listaStreaming.add(stream);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considera usar un logger para un manejo de errores más robusto
        }
        return listaStreaming;
    }
}
