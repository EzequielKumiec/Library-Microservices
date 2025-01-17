package com.library.biblioteca.service.Impl;

import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.repository.LibroRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarLibro() {
        Libro libro = new Libro(null, "12345", "Libro Test", "Autor Test", null);
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        Libro resultado = libroService.registrarLibro(libro);

        assertNotNull(resultado);
        assertEquals(EstadoLibro.DISPONIBLE, resultado.getEstado());
        verify(libroRepository, times(1)).save(libro);
    }

    @Test
    void testObtenerTodosLosLibros() {
        List<Libro> libros = Arrays.asList(
                new Libro(1L, "12345", "Libro 1", "Autor 1", EstadoLibro.DISPONIBLE),
                new Libro(2L, "67890", "Libro 2", "Autor 2", EstadoLibro.RESERVADO)
        );
        when(libroRepository.findAll()).thenReturn(libros);

        List<Libro> resultado = libroService.obtenerTodosLosLibros();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    void testEliminarLibro() {
        Long id = 1L;
        doNothing().when(libroRepository).deleteById(id);

        libroService.eliminarLibro(id);

        verify(libroRepository, times(1)).deleteById(id);
    }

    @Test
    void testActualizarLibro() {
        Libro libro = new Libro(1L, "12345", "Libro Actualizado", "Autor Actualizado", EstadoLibro.DISPONIBLE);
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        Libro resultado = libroService.actualizarLibro(libro);

        assertNotNull(resultado);
        assertEquals("Libro Actualizado", resultado.getTitulo());
        verify(libroRepository, times(1)).save(libro);
    }
}