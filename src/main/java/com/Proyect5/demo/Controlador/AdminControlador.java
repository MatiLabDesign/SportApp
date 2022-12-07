package com.Proyect5.demo.Controlador;

import com.Proyect5.demo.Entidades.Entrenamiento;
import com.Proyect5.demo.Entidades.Noticia;
import com.Proyect5.demo.Entidades.Usuario;
import com.Proyect5.demo.Excepciones.MiException;
import com.Proyect5.demo.Servicio.EntrenamientoServicio;
import com.Proyect5.demo.Servicio.NoticiaServicio;
import com.Proyect5.demo.Servicio.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private EntrenamientoServicio entrenamientoServicio;
    @Autowired
    private NoticiaServicio noticiaServicio;

    @GetMapping("/dashboard")//localhost:8080/admin/dashboard
    public String panelAdministrativo() {
        return "dashboard.html";
    }

    @GetMapping("/usuarios")//localhost:8080/admin/usuarios
    public String listarUsuario(ModelMap modelo) {
        List<Usuario> usuario = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuario", usuario);

        return "usuario_listar.html";
    }

    @GetMapping("/profesores")//localhost:8080/admin/usuarios
    public String listarProfesores(ModelMap modelo) {
        List<Usuario> profesor = usuarioServicio.listarProfesores();
        modelo.addAttribute("profesor", profesor);

        return "profesor_listar.html";
    }

    @GetMapping("/noticias")//localhost:8080/admin/noticias
    public String listarNoticias(ModelMap modelo) {
        List<Noticia> noticia = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticia", noticia);

        return "noticias_listar.html";
    }

    @GetMapping("/entrenamientos")//localhost:8080/admin/entrenamientos
    public String listarEntrenamientos(ModelMap modelo) {
        List<Entrenamiento> entrenamiento = entrenamientoServicio.listarEntrenamientos();
        modelo.addAttribute("entrenamiento", entrenamiento);

        return "entrenamiento_listar.html";
    }

    @GetMapping("/filtrar_entrenamiento")
    public String listaEntrenamiento(ModelMap modelo, @RequestParam(value = "query") String e) {

        try {
            List<Entrenamiento> entrenamientoDisciplina = entrenamientoServicio.filtroDisciplina(e);

            modelo.addAttribute("entrenamiento", entrenamientoDisciplina);

            return "entrenamiento_filtrar.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "index.html";
        }
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id) {

        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/modificarUsuario/{id}")
    public String modificarUsuario(ModelMap modelo, @PathVariable String id) throws MiException {

        Usuario usuarioObtenido = usuarioServicio.getOne(id);

        modelo.put("usuario", usuarioObtenido);
        return "admin_editar_usuario.html";
    }

    @Transactional
    @PostMapping("/modificarUsuario/{id}")
    public String modificarUsuarioAdmin(MultipartFile imagen, @PathVariable String id, @RequestParam String nombre,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam String disciplina, ModelMap modelo) {

        try {
            usuarioServicio.modificarUsuario(id, imagen, nombre, email, password, password2, disciplina);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "dashboard.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_editar.html";
        }
    }
}
