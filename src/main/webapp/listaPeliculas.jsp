<%@page import="java.util.ArrayList"%>
<%@page import="com.example.pruebalaboratorio1.beans.pelicula"%>
<%@page import="com.example.pruebalaboratorio1.beans.genero"%>
<%@page import="com.example.pruebalaboratorio1.beans.streaming"%>
<%@page import="java.text.NumberFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ArrayList<pelicula> listaPeliculas = (ArrayList<pelicula>) request.getAttribute("listaPeliculas");
    ArrayList<genero> listaGeneros = (ArrayList<genero>) request.getAttribute("listaGeneros");
    ArrayList<streaming> listaStreaming = (ArrayList<streaming>) request.getAttribute("listaStreaming");
    String mensajeError = (String) request.getAttribute("mensajeError");
    NumberFormat formatter = NumberFormat.getInstance();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de Películas</title>
    </head>
    <body>

        <h1>Lista de Películas</h1>

        <% if (mensajeError != null) { %>
        <p style="color: red;"><%= mensajeError %></p>
        <% } %>

        <form action="listaPeliculas" method="POST">
            <label for="idGenero">Género:</label>
            <select name="idGenero" id="idGenero">
                <option value="0">Seleccione Género</option>
                <% if (listaGeneros != null) {
                    for (genero gen : listaGeneros) { %>
                <option value="<%= gen.getIdGenero() %>"><%= gen.getNombre() %></option>
                <%  }
                } %>
            </select>

            <label for="idStreaming">Streaming:</label>
            <select name="idStreaming" id="idStreaming">
                <option value="0">Seleccione Streaming</option>
                <% if (listaStreaming != null) {
                    for (streaming stream : listaStreaming) { %>
                <option value="<%= stream.getIdStreaming() %>"><%= stream.getNombreServicio() %></option>
                <%  }
                } %>
            </select>

            <input type="hidden" name="action" value="filtrar">
            <button type="submit">Filtrar</button>
        </form>

        <form action="listaPeliculas?action=listar" method="GET">
            <button type="submit">Limpiar</button>
        </form>

        <table border="1">
            <tr>
                <th>Título</th>
                <th>Director</th>
                <th>Año Publicación</th>
                <th>Rating</th>
                <th>Box Office</th>
                <th>Género</th>
                <th>Duración</th>
                <th>Streaming</th>
                <th>Premios Oscar</th>
                <th>Actores</th>
                <th>Borrar</th>
            </tr>
            <% if (listaPeliculas != null) {
                for (pelicula movie : listaPeliculas) { %>
            <tr>
                <td><a href="viewPelicula?idPelicula=<%= movie.getIdPelicula() %>"><%= movie.getTitulo() %></a></td>
                <td><%= movie.getDirector() %></td>
                <td><%= movie.getAnoPublicacion() %></td>
                <td><%= movie.getRating() %>/10</td>
                <td>$<%= formatter.format(movie.getBoxOffice()) %></td>
                <td><%= movie.getGenero().getNombre() %></td>
                <td><%= movie.getDuracion() %></td>
                <td><%= movie.getStreaming().getNombreServicio() %></td>
                <td><%= movie.isPremioOscar() ? "Sí" : "No" %></td>
                <td><a href="listaActores?idPelicula=<%= movie.getIdPelicula() %>">Ver Actores</a></td>
                <td><a href="listaPeliculas?action=borrar&idPelicula=<%= movie.getIdPelicula() %>">Borrar</a></td>
            </tr>
            <%  }
            } %>
        </table>

    </body>
</html>
