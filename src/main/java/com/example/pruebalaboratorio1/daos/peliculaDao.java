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

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT A.*, B.NOMBRE AS nombreGenero, C.NOMBRESERVICIO AS nombreServicio " +
                    "FROM PELICULA A " +
                    "INNER JOIN GENERO B ON A.IDGENERO = B.IDGENERO " +
                    "INNER JOIN STREAMING C ON A.IDSTREAMING = C.IDSTREAMING " +
                    "ORDER BY RATING DESC, BOXOFFICE DESC";

            ResultSet rs = stmt.executeQuery(sql);

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

                // Asigna el Bean genero
                genero.setIdGenero(rs.getInt("idGenero"));
                genero.setNombre(rs.getString("nombreGenero"));
                movie.setGenero(genero);

                // Asigna el Bean streaming
                streaming.setIdStreaming(rs.getInt("idStreaming"));
                streaming.setNombreServicio(rs.getString("nombreServicio"));
                movie.setStreaming(streaming);

                listaPeliculas.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaPeliculas;
    }

    public ArrayList<pelicula> listarPeliculasFiltradas(int idGenero, int idStreaming) {
        ArrayList<pelicula> listaPeliculasFiltradas = new ArrayList<>();
        String sql = "SELECT A.*, B.NOMBRE AS nombreGenero, C.NOMBRESERVICIO AS nombreServicio " +
                "FROM PELICULA A " +
                "INNER JOIN GENERO B ON A.IDGENERO = B.IDGENERO " +
                "INNER JOIN STREAMING C ON A.IDSTREAMING = C.IDSTREAMING " +
                "WHERE 1=1";

        if (idGenero != 0) {
            sql += " AND A.IDGENERO = ?";
        }
        if (idStreaming != 0) {
            sql += " AND A.IDSTREAMING = ?";
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

            ResultSet rs = pstmt.executeQuery();
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

                // Asigna el Bean genero
                genero.setIdGenero(rs.getInt("idGenero"));
                genero.setNombre(rs.getString("nombreGenero"));
                movie.setGenero(genero);

                // Asigna el Bean streaming
                streaming.setIdStreaming(rs.getInt("idStreaming"));
                streaming.setNombreServicio(rs.getString("nombreServicio"));
                movie.setStreaming(streaming);

                listaPeliculasFiltradas.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPeliculasFiltradas;
    }

    public void editarPelicula(int idPelicula, String titulo, String director, int anoPublicacion, double rating, double boxOffice, int idGenero, int idStreaming) {
        String sql = "UPDATE pelicula SET titulo = ?, director = ?, anoPublicacion = ?, rating = ?, boxOffice = ?, idGenero = ?, idStreaming = ? WHERE idPelicula = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, director);
            pstmt.setInt(3, anoPublicacion);
            pstmt.setDouble(4, rating);
            pstmt.setDouble(5, boxOffice);
            pstmt.setInt(6, idGenero);
            pstmt.setInt(7, idStreaming);
            pstmt.setInt(8, idPelicula);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void borrarPelicula(int idPelicula) {
        try {
            Connection conn = getConnection();
            pelicula movie = obtenerPeliculaPorId(idPelicula);
            boolean puedeBorrarse = validarBorrado(movie);

            if (puedeBorrarse) {
                // Elimina las referencias de la tabla PROTAGONISTAS
                String sqlDeleteProtagonistas = "DELETE FROM PROTAGONISTAS WHERE idPelicula = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteProtagonistas)) {
                    pstmt.setInt(1, idPelicula);
                    pstmt.executeUpdate();
                }

                // Luego, elimina la película de la tabla PELICULA
                String sqlDeletePelicula = "DELETE FROM PELICULA WHERE idPelicula = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDeletePelicula)) {
                    pstmt.setInt(1, idPelicula);
                    pstmt.executeUpdate();
                }
            } else {
                System.out.println("La película no cumple con los requisitos para ser borrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public pelicula obtenerPeliculaPorId(int idPelicula) {
        pelicula movie = new pelicula();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM PELICULA WHERE idPelicula = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idPelicula);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    movie.setIdPelicula(rs.getInt("idPelicula"));
                    movie.setTitulo(rs.getString("titulo"));
                    movie.setDirector(rs.getString("director"));
                    movie.setAnoPublicacion(rs.getInt("anoPublicacion"));
                    movie.setRating(rs.getDouble("rating"));
                    movie.setBoxOffice(rs.getDouble("boxOffice"));
                    movie.setDuracion(rs.getString("duracion"));
                    movie.setPremioOscar(rs.getBoolean("premioOscar"));
                    // Asigna otros atributos si es necesario
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }
}
