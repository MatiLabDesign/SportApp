package com.Proyect5.demo.Controlador;

import org.springframework.stereotype.Controller;
import com.Proyect5.demo.Entidades.Noticia;
import com.Proyect5.demo.Excepciones.MiException;
import com.Proyect5.demo.Repositorio.NoticiaRepositorio;
import com.Proyect5.demo.Servicio.NoticiaServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @GetMapping("/cargar")//localhost:8080/noticia/cargar
    public String cargar() {
        return "cargar_noticia.html";
    }//cierra cargar

    @Transactional
    @PostMapping("/guardada")//localhost:8080/noticia/guardada
    public String llenarNoticia(@RequestParam String titulo, @RequestParam String subtitulo, @RequestParam String cuerpo, @RequestParam MultipartFile imagen, ModelMap modelo) throws MiException {
        try {
            noticiaServicio.crearNoticia(imagen, titulo, subtitulo, cuerpo);
            modelo.put("exito", "La noticia fue cargada correctamente");

            return "redirect:../noticia/listar";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("subtitulo", subtitulo);
            modelo.put("cuerpo", cuerpo);

            return "cargar_noticia.html";
        }
    }

    @GetMapping("/listar")//localhost:8080/noticia/listar
    public String listaNoticias(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        return "listado_noticias.html";

    }

    @GetMapping("/editar/{id}")//localhost:8080/noticia/editar/id
    public String editar(@PathVariable String id, ModelMap modelo) {

        //Noticia noticia = noticiaServicio.getOne(id);
        modelo.put("noticia", noticiaServicio.getOne(id));
        //modelo.addAttribute("noticia", noticia);
        //modelo.put("noticia", noticia);

        return "noticias_editar.html";
    }

    @Transactional
    @PostMapping("/editar/{id}")//localhost:8080/noticia/editar/id
    public String editar(@PathVariable String id, String titulo, String subtitulo, String cuerpo, MultipartFile imagen, ModelMap modelo) throws MiException {
        try {
            noticiaServicio.modificarNoticia(id, titulo, subtitulo, cuerpo, imagen);
            return "redirect:../listar";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            modelo.put("noticia", noticiaServicio.getOne(id));
            //modelo.put("titulo", titulo);
            //modelo.put("subtitulo", subtitulo);
            //modelo.put("cuerpo", cuerpo);
            return "noticias_editar.html";
        }
    }

    @GetMapping("/completa/{id}")
    public String completo(@PathVariable String id, ModelMap modelo) {

        Noticia noticia = noticiaServicio.getOne(id);

        modelo.put("noticia", noticia);

        return "completa_noticia.html";
    }

    @GetMapping("/individual/{id}")//localhost:8080/noticia/individual/id
    public String mostrarNoticia(@PathVariable String id, ModelMap modelo) {

        modelo.put("noticia", noticiaServicio.getOne(id));

        return "mostrar_noticia.html";
    }

    @GetMapping("/borrar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
        noticiaServicio.borrar(id);
        return "redirect:/noticia/listar";
    }

    @GetMapping(value = "/busqueda")
    public String busquedaNoticia(ModelMap modelo, @RequestParam(value = "query") String q) {
        try {
            List<Noticia> noticias = this.noticiaServicio.findByTitulo(q);

            modelo.addAttribute("noticias", noticias);

            return "busqueda.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "index.html";
        }
    }
}
