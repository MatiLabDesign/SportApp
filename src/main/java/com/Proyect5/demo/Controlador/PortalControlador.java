package com.Proyect5.demo.Controlador;

import com.Proyect5.demo.Entidades.Usuario;
import com.Proyect5.demo.Excepciones.MiException;
import com.Proyect5.demo.Servicio.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")//localhost:8080:/
    public String index() {
        return "index.html";

    }

    @GetMapping("/registrar")//localhost:8080:/registrar
    public String registrar() {
        return "usuario_registrar.html";
    }

    @PostMapping("/registro")//localhost:8080:/registro
    @Transactional
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, ModelMap modelo, MultipartFile imagen, @RequestParam String disciplina) {

        try {
            usuarioServicio.registrar(imagen, nombre, email, password, password2, disciplina);

            modelo.put("exito", "Usuario registrado correctamente");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            modelo.put("password", password);
            return "usuario_registrar.html";
        }
    }

    @GetMapping("/login")//localhost:8080:/login
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña invalidos");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ATLETA','ROLE_PROFESOR','ROLE_ADMIN')")
    @GetMapping("/inicio")//localhost:8080:/inicio
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuarioActivo");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ATLETA','ROLE_PROFESOR','ROLE_ADMIN')")
    @GetMapping("/perfil")//localhost:8080:/perfil
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        modelo.put("usuario", usuario);
        return "usuario_editar.html";
    }//cierra perfil

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ATLETA','ROLE_PROFESOR','ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")//localhost:8080:/perfil/id
    public String actualizar(MultipartFile imagen, @PathVariable String id, @RequestParam String nombre,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam String disciplina, ModelMap modelo) {

        try {
            usuarioServicio.modificarUsuario(id, imagen, nombre, email, password, password2, disciplina);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "usuario_editar.html";
        }
    }//cierra actualizar
}
