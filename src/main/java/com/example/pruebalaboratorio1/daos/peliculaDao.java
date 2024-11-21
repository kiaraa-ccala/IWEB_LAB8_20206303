package com.example.pruebalaboratorio1.daos;

import com.example.pruebalaboratorio1.beans.genero;
import com.example.pruebalaboratorio1.beans.pelicula;
import com.example.pruebalaboratorio1.beans.streaming;

import java.sql.*;
import java.util.ArrayList;

public class peliculaDao extends baseDao {

    @Override
    public boolean validarBorrado(pelicula movie) {
        try {
            // Convertir la duración a un número entero, eliminando caracteres no numéricos
            int duracion = Integer.parseInt(movie.getDuracion().replaceAll("[^0-9]", ""));
            // Validar si la duración es mayor a 120 minutos y no ha ganado un premio Oscar
            return duracion > 120 && !movie.isPremioOscar();
        } catch (NumberFormatException e) {
            // Si la duración no es un número válido, no puede borrarse
            return false;
        }
    }

    public ArrayList<pelicula> listarPeliculas() {
        ArrayList<pelicula> listaPeliculas = new ArrayList<>();

        String sql = "SELECT p.*, g.nombre AS nombreGenero, s.nombreServicio AS nombreStreaming " +
                "FROM pelicula p " +
                "INNER JOIN genero g ON p.idGenero = g.idGenero " +
                "INNER JOIN streaming s ON p.idStreaming = s.idStreaming " +
                "ORDER BY p.rating DESC, p.boxOffice DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                pelicula movie = new pelicula();
                genero genero = new genero();
                streaming streaming = new streaming();

                movie.setIdPelicula(rs.getInt("idPelicula"));
                movie.setTitulo(rs.getString("titulo"));
                movie.setDirector(rs.getString("director"));
                movie.setAnoPublicacion(rs.getInt("anoPublicacion"));
                movie.setRating(rs.getDouble("rating"));
                movie.setBoxOffice(rs.getDouble("boxOffice"));
                movie.setDuracion(rs.getString("duracion"));
                movie.setPremioOscar(rs.getBoolean("premioOscar"));

                genero.setIdGenero(rs.getInt("idGenero"));
                genero.setNombre(rs.getString("nombreGenero"));
                movie.setGenero(genero);

                streaming.setIdStreaming(rs.getInt("idStreaming"));
                streaming.setNombreServicio(rs.getString("nombreStreaming"));
                movie.setStreaming(streaming);

                listaPeliculas.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaPeliculas;
    }

    public ArrayList<pelicula> listarPeliculasFiltradas(int idGenero, int idStreaming) {
        ArrayList<pelicula> listaPeliculasFiltradas = new ArrayList<>();

        String sql = "SELECT p.*, g.nombre AS nombreGenero, s.nombreServicio AS nombreStreaming " +
                "FROM pelicula p " +
                "INNER JOIN genero g ON p.idGenero = g.idGenero " +
                "INNER JOIN streaming s ON p.idStreaming = s.idStreaming " +
                "WHERE 1=1";

        if (idGenero != 0) {
            sql += " AND p.idGenero = ?";
        }
        if (idStreaming != 0) {
            sql += " AND p.idStreaming = ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (idGenero != 0) {
                pstmt.setInt(index++, idGenero);
            }
            if (idStreaming != 0) {
                pstmt.setInt(index++, idStreaming);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pelicula movie = new pelicula();
                    genero genero = new genero();
                    streaming streaming = new streaming();

                    movie.setIdPelicula(rs.getInt("idPelicula"));
                    movie.setTitulo(rs.getString("titulo"));
                    movie.setDirector(rs.getString("director"));
                    movie.setAnoPublicacion(rs.getInt("anoPublicacion"));
                    movie.setRating(rs.getDouble("rating"));
                    movie.setBoxOffice(rs.getDouble("boxOffice"));
                    movie.setDuracion(rs.getString("duracion"));
                    movie.setPremioOscar(rs.getBoolean("premioOscar"));

                    genero.setIdGenero(rs.getInt("idGenero"));
                    genero.setNombre(rs.getString("nombreGenero"));
                    movie.setGenero(genero);

                    streaming.setIdStreaming(rs.getInt("idStreaming"));
                    streaming.setNombreServicio(rs.getString("nombreStreaming"));
                    movie.setStreaming(streaming);

                    listaPeliculasFiltradas.add(movie);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaPeliculasFiltradas;
    }

    public pelicula obtenerPeliculaPorId(int idPelicula) {
        pelicula movie = null;
        String sql = "SELECT p.*, g.nombre AS nombreGenero, s.nombreServicio AS nombreStreaming " +
                "FROM pelicula p " +
                "INNER JOIN genero g ON p.idGenero = g.idGenero " +
                "INNER JOIN streaming s ON p.idStreaming = s.idStreaming " +
                "WHERE p.idPelicula = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPelicula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    movie = new pelicula();
                    genero genero = new genero();
                    streaming streaming = new streaming();

                    movie.setIdPelicula(rs.getInt("idPelicula"));
                    movie.setTitulo(rs.getString("titulo"));
                    movie.setDirector(rs.getString("director"));
                    movie.setAnoPublicacion(rs.getInt("anoPublicacion"));
                    movie.setRating(rs.getDouble("rating"));
                    movie.setBoxOffice(rs.getDouble("boxOffice"));
                    movie.setDuracion(rs.getString("duracion"));
                    movie.setPremioOscar(rs.getBoolean("premioOscar"));

                    genero.setIdGenero(rs.getInt("idGenero"));
                    genero.setNombre(rs.getString("nombreGenero"));
                    movie.setGenero(genero);

                    streaming.setIdStreaming(rs.getInt("idStreaming"));
                    streaming.setNombreServicio(rs.getString("nombreStreaming"));
                    movie.setStreaming(streaming);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movie;
    }

    public void editarPelicula(pelicula movie) {
        String sql = "UPDATE pelicula SET titulo = ?, director = ?, anoPublicacion = ?, rating = ?, " +
                "boxOffice = ?, idGenero = ?, idStreaming = ?, duracion = ?, premioOscar = ? " +
                "WHERE idPelicula = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, movie.getTitulo());
            pstmt.setString(2, movie.getDirector());
            pstmt.setInt(3, movie.getAnoPublicacion());
            pstmt.setDouble(4, movie.getRating());
            pstmt.setDouble(5, movie.getBoxOffice());
            pstmt.setInt(6, movie.getGenero().getIdGenero());
            pstmt.setInt(7, movie.getStreaming().getIdStreaming());
            pstmt.setString(8, movie.getDuracion());
            pstmt.setBoolean(9, movie.isPremioOscar());
            pstmt.setInt(10, movie.getIdPelicula());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarPelicula(pelicula movie) {
        String sql = "INSERT INTO pelicula (titulo, director, anoPublicacion, rating, boxOffice, idGenero, " +
                "idStreaming, duracion, premioOscar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, movie.getTitulo());
            pstmt.setString(2, movie.getDirector());
            pstmt.setInt(3, movie.getAnoPublicacion());
            pstmt.setDouble(4, movie.getRating());
            pstmt.setDouble(5, movie.getBoxOffice());
            pstmt.setInt(6, movie.getGenero().getIdGenero());
            pstmt.setInt(7, movie.getStreaming().getIdStreaming());
            pstmt.setString(8, movie.getDuracion());
            pstmt.setBoolean(9, movie.isPremioOscar());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
