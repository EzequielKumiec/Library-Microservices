package com.library.biblioteca.service.Impl;

import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.repository.LibroRepository;
import com.library.biblioteca.service.LibroService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public Libro registrarLibro(Libro libro) {
        //TODO
        /**
         * Completar el metodo de registro
         * el estado inicial del libro debe ser DISPONIBLE
         */
        return null;
    }

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        //TODO
        /**
         * Completar el metodo 
         */
        return null;
    }

    @Override
    public void eliminarLibro(Long id) {
        //TODO
        /**
         * Completar el metodo
         */
    }

    @Override
    public Libro actualizarLibro(Libro libro) {
        //TODO
        /**
         * Completar el metodo
         */
        return null;
    }
}
