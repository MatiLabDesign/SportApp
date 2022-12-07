package com.Proyect5.demo.Servicio;

import com.Proyect5.demo.Entidades.Entrenamiento;
import com.Proyect5.demo.Entidades.Usuario;
import com.Proyect5.demo.Excepciones.MiException;
import com.Proyect5.demo.Repositorio.EntrenamientoRepositorio;
import com.Proyect5.demo.Repositorio.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntrenamientoServicio {

    @Autowired
    private EntrenamientoRepositorio entrenamientoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Transactional
    public void crearEntrenamiento(String disciplina,
            String descripcion, String rutina, LocalDate
                    fechaInicio, String idProfesor) throws MiException {

        validarDatos(disciplina, descripcion, rutina, fechaInicio, idProfesor);

        Entrenamiento entrenamiento = new Entrenamiento();
        Usuario profesores = usuarioRepositorio.findById(idProfesor).get();
        entrenamiento.setDisciplina(disciplina);
        entrenamiento.setDescripcion(descripcion);
        entrenamiento.setRutina(rutina);
        entrenamiento.setFechaInicio(fechaInicio);
        entrenamiento.setProfesor(profesores);

        LocalDate finalizacion = fechaInicio.plusDays(7);

        entrenamiento.setFechaFin(finalizacion);
        entrenamiento.setDiasDeEntrenamiento(7);

        entrenamientoRepositorio.save(entrenamiento);
    }

    public List<Entrenamiento> listarEntrenamientos() {

        List<Entrenamiento> entrenamientos = new ArrayList();
        entrenamientos = entrenamientoRepositorio.findAll();

        return entrenamientos;
    }

    @Transactional
    public void modificarEntrenamiento(String id, String disciplina, String descripcion,
            String rutina, LocalDate fechaInicio, String idProfesor) throws MiException {

        if (id == null || id.isEmpty()) {
            throw new MiException("El id seleccionado no corresponde a un entrenamiento");
        }

        validarDatos(disciplina, descripcion, rutina, fechaInicio, idProfesor);

        Optional<Entrenamiento> respuesta = entrenamientoRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Entrenamiento entrenamiento = respuesta.get();

            entrenamiento.setDisciplina(disciplina);
            entrenamiento.setDescripcion(descripcion);
            entrenamiento.setRutina(rutina);
            entrenamiento.setFechaInicio(fechaInicio);

            LocalDate finalizacion = fechaInicio.plusDays(7);

            entrenamiento.setFechaFin(finalizacion);
            entrenamiento.setDiasDeEntrenamiento(7);

            entrenamientoRepositorio.save(entrenamiento);
        }
    }

    @Transactional
    public void borrar(String id) throws MiException {

        if (id == null || id.isEmpty()) {
            throw new MiException("El id seleccionado no corresponde a un entrenamiento");
        }

        Optional<Entrenamiento> respuesta = entrenamientoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            entrenamientoRepositorio.deleteById(id);
        }

    }

    private void validarDatos(String disciplina, String descripcion, String rutina, LocalDate fechaInicio, String idProfesor) throws MiException {

        if (disciplina == null || disciplina.isEmpty()) {
            throw new MiException("La disciplina no puede estar vacia");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException("La descripción no puede estar vacia");
        }
        if (rutina == null || rutina.isEmpty()) {
            throw new MiException("La rutina no puede estar vacia");
        }
        if (fechaInicio == null) {
            throw new MiException("La fecha de inicio no puede estar vacia");
        }
        if (idProfesor == null || idProfesor.isEmpty()) {
            throw new MiException("El entrenamiento debe tener asignado un profesor");
        }
    }

    public List<Entrenamiento> filtroDisciplina(String e) throws MiException, Exception {
        try {
            List<Entrenamiento> entradasEntrenamiento = this.entrenamientoRepositorio.filtroDisciplina(e.toUpperCase());
            return entradasEntrenamiento;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public List<Entrenamiento> listarLanzadores(String disciplina) {

        List<Entrenamiento> Lanzadores = entrenamientoRepositorio.findByDisciplina(disciplina);

        return Lanzadores;
    }

    public List<Entrenamiento> listarSaltadores(String disciplina) {

        List<Entrenamiento> Saltadores = entrenamientoRepositorio.findByDisciplina(disciplina);

        return Saltadores;
    }

    public List<Entrenamiento> listarCorredores(String disciplina) {

        List<Entrenamiento> Corredores = entrenamientoRepositorio.findByDisciplina(disciplina);

        return Corredores;
    }

    public Entrenamiento getOne(String id) {
        return entrenamientoRepositorio.getOne(id);
    }

}
