package com.library.biblioteca.service.Impl;


import com.library.biblioteca.dto.ClienteDTO;
import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.model.Registro;
import com.library.biblioteca.repository.LibroRepository;
import com.library.biblioteca.repository.RegistroRepository;
import com.library.biblioteca.service.BibliotecaService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BibliotecaServiceImpl implements BibliotecaService {
    private final RegistroRepository registroRepository;
    private final LibroRepository libroRepository;
    private final RestTemplate restTemplate;

    public BibliotecaServiceImpl(LibroRepository libroRepository, RegistroRepository registroRepository,  RestTemplate restTemplate) {
        this.registroRepository = registroRepository;
        this.libroRepository = libroRepository;
        this.restTemplate = restTemplate;
    }


    @Override
    public Registro alquilarLibros(List<String> isbns) {
        List<Libro> libros = new ArrayList<>();
        for (String isbn : isbns) {
            Libro libro = libroRepository.findByIsbn(isbn);
            if (!libro.getEstado().equals(EstadoLibro.DISPONIBLE)){
                throw new IllegalStateException("Uno o más libros ya están reservados.");
            }
            libro.setEstado(EstadoLibro.RESERVADO);
            libros.add(libro);
        }
        libroRepository.saveAll(libros);
        Registro registro = new Registro();
        registro.setLibrosReservados(libros);
        registro.setClienteId(restTemplate.getForObject("/api/personas/aleatorio",ClienteDTO.class).getId());
        registro.setNombreCliente(restTemplate.getForObject("/api/personas/aleatorio",ClienteDTO.class).getNombre());
        registro.setFechaReserva(LocalDate.now());
        return registro;
    }

    @Override
    public Registro devolverLibros(Long registroId) {
        Registro registro = registroRepository.findById(registroId).get();
        registro.setFechaDevolucion(LocalDate.now());
        registro.setTotal(calcularCostoAlquiler(registro.getFechaReserva(),LocalDate.now(),registro.getLibrosReservados().size()));
        for (Libro libro:registro.getLibrosReservados()){
            libro.setEstado(EstadoLibro.DISPONIBLE);
            libroRepository.save(libro);
        }
        return registroRepository.save(registro);
    }

    @Override
    public List<Registro> verTodosLosAlquileres() {
        return registroRepository.findAll();
    }

    // Cálculo de costo de alquiler
    private BigDecimal calcularCostoAlquiler(LocalDate inicio, LocalDate fin, int cantidadLibros) {
        BigDecimal total = new BigDecimal(cantidadLibros);
        long diferenciaDias = ChronoUnit.DAYS.between(inicio, fin);
        if (diferenciaDias<=2){
            total = total.multiply(BigDecimal.valueOf(100));
        } else if (diferenciaDias<=5) {
            total = total.multiply(BigDecimal.valueOf(150));
        }else {
            BigDecimal costoBase = BigDecimal.valueOf(150);
            total = total.multiply(costoBase);
            long diasExtra = diferenciaDias - 5;
            BigDecimal costoAdicional =
                    BigDecimal.valueOf(30)
                            .multiply(BigDecimal.valueOf(diasExtra)).multiply(BigDecimal.valueOf(cantidadLibros));
            total = total.add(costoAdicional);
        }
        return total;
    }

    @Override
    public List<Registro> informeSemanal(LocalDate fechaInicio) {
        return registroRepository.obtenerRegistrosSemana(fechaInicio,fechaInicio.plusDays(7));
    }

    @Override
    public List<Object[]> informeLibrosMasAlquilados() {
        //TODO
        /**
         * Completar el metodo de reporte de libros mas alquilados
         * se debe retornar la lista de libros mas alquilados
         */
        return registroRepository.obtenerLibrosMasAlquilados();
    }

}
