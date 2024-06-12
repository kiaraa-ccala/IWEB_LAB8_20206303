package com.example.pruebalaboratorio1.daos;

import com.example.pruebalaboratorio1.beans.actor;
import com.example.pruebalaboratorio1.beans.pelicula;

import java.sql.*;
import java.util.ArrayList;

public class actorDao {

    public ArrayList<actor> listarActores(int idPelicula) {

        ArrayList<actor> listaActores = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/mydb?serverTimezone=America/Lima";
        String username = "root";
        String password = "root";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();

            String sql = "SELECT A.*\n" +
                    "FROM \n" +
                    "(SELECT * FROM ACTOR ) AS A\n" +
                    "INNER JOIN\n" +
                    "(SELECT * FROM PROTAGONISTAS WHERE IDPELICULA = \n" +
                    idPelicula +
                    ") AS B\n" +
                    "on a.idactor = b.idactor\n";


            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                actor actuador = new actor();
                int idActor = rs.getInt(1);
                actuador.setIdActor(idActor);
                String nombre = rs.getString("nombre");
                actuador.setNombre(nombre);
                String apellido = rs.getString("apellido");
                actuador.setApellido(apellido);
                int anoNacimiento = rs.getInt("anoNacimiento");
                actuador.setAnoNacimiento(anoNacimiento);
                boolean oscar = rs.getBoolean("premioOscar");
                actuador.setPremioOscar(oscar);


                listaActores.add(actuador);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaActores;
    }
}
