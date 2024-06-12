package com.example.pruebalaboratorio1.daos;

import com.example.pruebalaboratorio1.beans.pelicula;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class baseDao {



    public boolean validarBorrado(pelicula movie) {
        boolean validador = true;
        return validador;
    }
}

