<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Editar Película</title>
    </head>
    <body>
        <h1>Editar Película</h1>
        <form action="${pageContext.request.contextPath}/listaPeliculas" method="post">
            <input type="hidden" name="action" value="actualizar">
            <input type="hidden" name="idPelicula" value="${pelicula.idPelicula}">

            <label for="titulo">Título:</label>
            <input type="text" name="titulo" id="titulo" value="${pelicula.titulo}" required><br>

            <label for="director">Director:</label>
            <input type="text" name="director" id="director" value="${pelicula.director}" required><br>

            <label for="anoPublicacion">Año de Publicación:</label>
            <input type="number" name="anoPublicacion" id="anoPublicacion" value="${pelicula.anoPublicacion}" required><br>

            <label for="rating">Rating:</label>
            <input type="number" step="0.1" name="rating" id="rating" value="${pelicula.rating}" required><br>

            <label for="boxOffice">Box Office:</label>
            <input type="number" step="0.01" name="boxOffice" id="boxOffice" value="${pelicula.boxOffice}" required><br>

            <label for="duracion">Duración:</label>
            <input type="text" name="duracion" id="duracion" value="${pelicula.duracion}" required><br>

            <label for="genero">Género:</label>
            <select name="idGenero" id="genero" required>
                <c:forEach var="genero" items="${listaGeneros}">
                    <option value="${genero.idGenero}" ${genero.idGenero == pelicula.genero.idGenero ? 'selected' : ''}>
                            ${genero.nombre}
                    </option>
                </c:forEach>
            </select><br>

            <label for="streaming">Streaming:</label>
            <select name="idStreaming" id="streaming" required>
                <c:forEach var="streaming" items="${listaStreaming}">
                    <option value="${streaming.idStreaming}" ${streaming.idStreaming == pelicula.streaming.idStreaming ? 'selected' : ''}>
                            ${streaming.nombreServicio}
                    </option>
                </c:forEach>
            </select><br>

            <label for="premioOscar">Premio Oscar:</label>
            <input type="checkbox" name="premioOscar" id="premioOscar" ${pelicula.premioOscar ? 'checked' : ''}><br>

            <button type="submit">Actualizar Película</button>
        </form>
    </body>
</html>
