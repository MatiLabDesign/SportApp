package com.Proyect5.demo.Servicio;

import com.Proyect5.demo.Entidades.Imagen;
import com.Proyect5.demo.Entidades.Usuario;
import com.Proyect5.demo.Enumeraciones.Rol;
import static com.Proyect5.demo.Enumeraciones.Rol.ATLETA;
import static com.Proyect5.demo.Enumeraciones.Rol.PROFESOR;
import com.Proyect5.demo.Excepciones.MiException;
import com.Proyect5.demo.Repositorio.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2, String disciplina) throws MiException {

        validar(nombre, email, password, password2, disciplina);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setDisciplina(disciplina);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setDisciplina(disciplina);
        usuario.setRol(Rol.ATLETA);

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);

    }//cierra registrar 

    private void validar(String nombre, String email, String password, String password2, String disciplina) throws MiException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacio");
        }
        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo o estar vacio");
        }
        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiException("La contraseña no puede ser nula o estar vacía, y debe tener más de 5 dígitos");
        }
        if (!(password.equals(password2))) {
            throw new MiException("Las contraseñas no coinciden");
        }

        if (disciplina == null || disciplina.isEmpty()) {

            throw new MiException(("Las disciplina no pueden ser Nulas, ni vacias"));
        }
    }//cierra validar

    //cuando un usuario loguea otorga permisos al usuario
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuarioActivo", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    @Transactional
    public void cambiarRol(String id) {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.ATLETA)) {

                usuario.setRol(Rol.PROFESOR);

            } else if (usuario.getRol().equals(Rol.PROFESOR)) {
                usuario.setRol(Rol.ATLETA);
            }
            usuarioRepositorio.save(usuario);
        }
    }//cierra cambiarRol

    @Transactional
    public void modificarUsuario(String id, MultipartFile archivo, String nombre, String email, String password, String password2, String disciplina) throws MiException {

        if (id == null || id.isEmpty()) {
            throw new MiException("El id ingresado no corresponde a un usuario");
        }

        validar(nombre, email, password, password2, disciplina);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setDisciplina(disciplina);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(usuario.getRol());

            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.editarImagen(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }
    }//cierra modificarUsuario

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findByRol(ATLETA);
        return usuarios;
    }//cierra listarUsuarios

    public List<Usuario> listarProfesores() {
        List<Usuario> profesores = new ArrayList();
        profesores = usuarioRepositorio.findByRol(PROFESOR);
        return profesores;
    }//cierra listarProfesores

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

}//cierra UsuarioServicio

