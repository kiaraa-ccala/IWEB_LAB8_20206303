package com.example.pruebalaboratorio1.servlets;

import com.example.pruebalaboratorio1.beans.genero;
import com.example.pruebalaboratorio1.beans.pelicula;
import com.example.pruebalaboratorio1.beans.streaming;
import com.example.pruebalaboratorio1.daos.listasDao;
import com.example.pruebalaboratorio1.daos.peliculaDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PeliculaServlet", urlPatterns = {"/listaPeliculas"})
public class PeliculaServlet extends HttpServlet {

    private static final String ACTION_LISTAR = "listar";
    private static final String ACTION_EDITAR = "editar";
    private static final String ACTION_ACTUALIZAR = "actualizar";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        peliculaDao peliculaDao = new peliculaDao();
        listasDao listasDao = new listasDao();


        String action = request.getParameter("action") == null ? ACTION_LISTAR : request.getParameter("action");

        switch (action) {
            case ACTION_LISTAR:
                ArrayList<pelicula> listaPeliculas = peliculaDao.listarPeliculas();
                request.setAttribute("listaPeliculas", listaPeliculas);

                RequestDispatcher listaView = request.getRequestDispatcher("listaPeliculas.jsp");
                listaView.forward(request, response);
                break;

            case ACTION_EDITAR:
                int idPelicula = Integer.parseInt(request.getParameter("idPelicula"));

                // Obtener detalles de la película
                pelicula movie = peliculaDao.obtenerPeliculaPorId(idPelicula);
                request.setAttribute("pelicula", movie);

                // Obtener listas para los ComboBox
                ArrayList<genero> listaGeneros = listasDao.listarGeneros();
                ArrayList<streaming> listaStreaming = listasDao.listarStreaming();
                request.setAttribute("listaGeneros", listaGeneros);
                request.setAttribute("listaStreaming", listaStreaming);

                RequestDispatcher editarView = request.getRequestDispatcher("editarPelicula.jsp");
                editarView.forward(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/listaPeliculas");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        peliculaDao peliculaDao = new peliculaDao();

        String action = request.getParameter("action");

        if (ACTION_ACTUALIZAR.equals(action)) {
            int idPelicula = Integer.parseInt(request.getParameter("idPelicula"));
            String titulo = request.getParameter("titulo");
            String director = request.getParameter("director");
            int anoPublicacion = Integer.parseInt(request.getParameter("anoPublicacion"));
            double rating = Double.parseDouble(request.getParameter("rating"));
            double boxOffice = Double.parseDouble(request.getParameter("boxOffice"));
            int idGenero = Integer.parseInt(request.getParameter("idGenero"));
            int idStreaming = Integer.parseInt(request.getParameter("idStreaming"));
            String duracion = request.getParameter("duracion");
            boolean premioOscar = request.getParameter("premioOscar") != null;

            // Crear objeto película
            pelicula movie = new pelicula();
            movie.setIdPelicula(idPelicula);
            movie.setTitulo(titulo);
            movie.setDirector(director);
            movie.setAnoPublicacion(anoPublicacion);
            movie.setRating(rating);
            movie.setBoxOffice(boxOffice);
            movie.setDuracion(duracion);
            movie.setPremioOscar(premioOscar);

            genero genero = new genero();
            genero.setIdGenero(idGenero);
            movie.setGenero(genero);

            streaming streaming = new streaming();
            streaming.setIdStreaming(idStreaming);
            movie.setStreaming(streaming);

            // Actualizar película
            peliculaDao.editarPelicula(movie);
            response.sendRedirect(request.getContextPath() + "/listaPeliculas");
        }
    }
}
