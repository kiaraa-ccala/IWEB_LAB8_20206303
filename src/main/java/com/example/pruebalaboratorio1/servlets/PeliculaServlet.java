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

@WebServlet(name = "pelicula-servlet", value = "/listaPeliculas")
public class PeliculaServlet extends HttpServlet {

    private static final String ACTION_LISTAR = "listar";
    private static final String ACTION_FILTRAR = "filtrar";
    private static final String ACTION_EDITAR = "editar";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String action = request.getParameter("action");
        peliculaDao peliculaDao = new peliculaDao();
        listasDao listaDao = new listasDao();

        // Obtener los géneros y servicios de streaming para los ComboBox
        ArrayList<genero> listaGeneros = listaDao.listarGeneros();
        ArrayList<streaming> listaStreaming = listaDao.listarStreaming();
        request.setAttribute("listaGeneros", listaGeneros);
        request.setAttribute("listaStreaming", listaStreaming);

        if (action == null || ACTION_LISTAR.equals(action)) {
            ArrayList<pelicula> listaPeliculas = peliculaDao.listarPeliculas();
            request.setAttribute("listaPeliculas", listaPeliculas);
            RequestDispatcher view = request.getRequestDispatcher("listaPeliculas.jsp");
            view.forward(request, response);
        } else if (ACTION_EDITAR.equals(action)) {
            int idPelicula = Integer.parseInt(request.getParameter("idPelicula"));
            pelicula movie = peliculaDao.obtenerPeliculaPorId(idPelicula);
            request.setAttribute("pelicula", movie);

            RequestDispatcher view = request.getRequestDispatcher("editarPelicula.jsp");
            view.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String action = request.getParameter("action");
        peliculaDao peliculaDao = new peliculaDao();

        if (ACTION_FILTRAR.equals(action)) {
            int idGenero;
            int idStreaming;

            try {
                idGenero = Integer.parseInt(request.getParameter("idGenero"));
                idStreaming = Integer.parseInt(request.getParameter("idStreaming"));
            } catch (NumberFormatException e) {
                request.setAttribute("mensajeError", "Seleccione un Género o Streaming válidos");
                RequestDispatcher view = request.getRequestDispatcher("listaPeliculas.jsp");
                view.forward(request, response);
                return;
            }

            // Validar selección
            if (idGenero == 0 && idStreaming == 0) {
                request.setAttribute("mensajeError", "Seleccione un Género o Streaming");
            } else {
                ArrayList<pelicula> listaPeliculas = peliculaDao.listarPeliculasFiltradas(idGenero, idStreaming);
                request.setAttribute("listaPeliculas", listaPeliculas);
            }

            RequestDispatcher view = request.getRequestDispatcher("listaPeliculas.jsp");
            view.forward(request, response);
        } else if (ACTION_EDITAR.equals(action)) {
            int idPelicula = Integer.parseInt(request.getParameter("idPelicula"));
            String titulo = request.getParameter("titulo");
            String director = request.getParameter("director");
            int anoPublicacion = Integer.parseInt(request.getParameter("anoPublicacion"));
            double rating = Double.parseDouble(request.getParameter("rating"));
            double boxOffice = Double.parseDouble(request.getParameter("boxOffice"));
            int idGenero = Integer.parseInt(request.getParameter("idGenero"));
            int idStreaming = Integer.parseInt(request.getParameter("idStreaming"));

            peliculaDao.editarPelicula(idPelicula, titulo, director, anoPublicacion, rating, boxOffice, idGenero, idStreaming);
            response.sendRedirect(request.getContextPath() + "/listaPeliculas?action=listar");
        }
    }
}
