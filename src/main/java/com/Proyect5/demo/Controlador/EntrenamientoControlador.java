package com.Proyect5.demo.Controlador;

import com.Proyect5.demo.Entidades.Entrenamiento;
import com.Proyect5.demo.Entidades.Usuario;
import com.Proyect5.demo.Excepciones.MiException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.Proyect5.demo.Servicio.EntrenamientoServicio;
import com.Proyect5.demo.Servicio.UsuarioServicio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/entrenamiento")
public class EntrenamientoControlador {

    @Autowired
    private EntrenamientoServicio entrenamientoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/cargar")
    public String cargar(ModelMap modelo) {
        List<Usuario> profesores = usuarioServicio.listarProfesores();
//        if (!profesores.isEmpty()) {
        modelo.addAttribute("profesores", profesores);
        //}
        return "entrenamiento_cargar.html";
    }

    @PostMapping("/guardada")
    public String llenarEntrenamiento(@RequestParam String disciplina,
            @RequestParam String descripcion,
            @RequestParam String rutina,
            @RequestParam String fechaInicio,
            @RequestParam String idProfesor,
            ModelMap modelo) throws MiException {

        try {
            System.out.println(idProfesor);
            Usuario profesor = usuarioServicio.getOne(idProfesor);
            entrenamientoServicio.crearEntrenamiento(disciplina, descripcion, rutina, convertirFecha(fechaInicio), idProfesor);
            modelo.put("exito", "El entretenimiento fue cargado correctamente");
            return "entrenamiento_cargar.html";

        } catch (MiException ex) {
            System.out.println(ex);
            System.out.println(ex.toString());
            modelo.put("error", ex.getMessage());
            modelo.put("descripcion", descripcion);
            modelo.put("rutina", rutina);
            return "entrenamiento_cargar.html";
        }
    }

    @GetMapping("/listar")
    public String listarEntrenamientos(ModelMap modelo
    ) {
        List<Entrenamiento> entrenamientos = entrenamientoServicio.listarEntrenamientos();
        List<Entrenamiento> lanzadores = entrenamientoServicio.listarLanzadores("Lanzamiento");
        List<Entrenamiento> saltadores = entrenamientoServicio.listarSaltadores("Salto");
        List<Entrenamiento> corredores = entrenamientoServicio.listarCorredores("Carrera");
        List<Usuario> usuario = usuarioServicio.listarUsuarios();

        if (!entrenamientos.isEmpty()) {
            
            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("entrenamiento", entrenamientos);
            modelo.addAttribute("lanzadores", lanzadores);
            modelo.addAttribute("saltadores", saltadores);
            modelo.addAttribute("corredores", corredores);

            

            return "entrenamiento_listado.html";
        }
        return "entrenamiento_listado.html";
    }

    @GetMapping("/editar/{id}")//localhost:8080/noticia/editar/id
    public String editar(@PathVariable String id, ModelMap modelo) {

        List<Usuario> profesores = usuarioServicio.listarProfesores();
        if (!profesores.isEmpty()) {
            modelo.addAttribute("profesores", profesores);
        }
        modelo.put("entrenamiento", entrenamientoServicio.getOne(id));

        return "entrenamiento_editar.html";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable String id,
            @RequestParam String disciplina,
            @RequestParam String descripcion,
            @RequestParam String rutina,
            @RequestParam String fechaInicio,
            @RequestParam String idProfesor,
            ModelMap modelo) throws MiException {

        try {
            System.out.println(idProfesor);
            Usuario profesor = usuarioServicio.getOne(idProfesor);
            entrenamientoServicio.modificarEntrenamiento(id, disciplina, descripcion, rutina, convertirFecha(fechaInicio), idProfesor);

            return "redirect:../listar";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.put("entrenamiento", entrenamientoServicio.getOne(id));
            return "entrenamiento_editar.html";
        }
    }

    @GetMapping("/completa/{id}")
    public String completo(@PathVariable String id, ModelMap modelo
    ) {

        modelo.put("entrenamiento", entrenamientoServicio.getOne(id));
        return "entrenamiento_mostrar";
    }

    @GetMapping("/borrar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
        entrenamientoServicio.borrar(id);
        return "redirect:/entrenamiento/listar";
    }

    private LocalDate convertirFecha(String fechaString) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {

            LocalDate fechaConvertida = LocalDate.parse(fechaString, formato);

            return fechaConvertida;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
