package com.Proyect5.demo.Servicio;

import com.Proyect5.demo.Entidades.Imagen;
import com.Proyect5.demo.Entidades.Noticia;
import com.Proyect5.demo.Excepciones.MiException;
import com.Proyect5.demo.Repositorio.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearNoticia(MultipartFile imagen, String titulo, String subtitulo, String cuerpo) throws MiException {

        validarDatos(titulo, subtitulo, cuerpo, imagen);

        Noticia noticia = new Noticia();

        noticia.setTitulo(titulo.toUpperCase());
        noticia.setSubtitulo(subtitulo);
        noticia.setCuerpo(cuerpo);
        noticia.setFechaPublicacion(new Date());

        Imagen contenido = imagenServicio.guardar(imagen);

        noticia.setImagen(contenido);

        noticiaRepositorio.save(noticia);

    }//cierra crearNoticia

    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.findAll();

        return noticias;
    }//cierra listarNoticias

    @Transactional


    public void modificarNoticia(String id, String titulo, String subtitulo, String cuerpo, MultipartFile imagen) throws MiException {

        if (id == null || id.isEmpty()) {
            throw new MiException("El id seleccionado no corresponde a una noticia");
        }

        validarDatos(titulo, subtitulo, cuerpo, imagen);

        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Noticia noticia = respuesta.get();

            noticia.setTitulo(titulo.toUpperCase());
            noticia.setSubtitulo(subtitulo);
            noticia.setCuerpo(cuerpo);

            Imagen contenido = imagenServicio.guardar(imagen);

            noticia.setImagen(contenido);

            noticiaRepositorio.save(noticia);
        }
    }//cierra modificarNoticia

    @Transactional
    public void borrar(String id) throws MiException {

        if (id == null || id.isEmpty()) {
            throw new MiException("El id seleccionado no corresponde a una noticia");
        }

        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            noticiaRepositorio.deleteById(id);
        }

    }//cierra modificarNoticia

    private void validarDatos(String titulo, String subtitulo, String cuerpo, MultipartFile imagen) throws MiException {

        if (titulo == null || titulo.isEmpty()) {
            throw new MiException("El titulo no puede estar vacio");
        }
        if (subtitulo == null || subtitulo.isEmpty()) {
            throw new MiException("El subtitulo no puede estar vacio");
        }
        if (cuerpo == null || cuerpo.isEmpty()) {
            throw new MiException("El cuerpo no puede estar vacio");
        }
        if (imagen == null || imagen.isEmpty()) {
            throw new MiException("La noticia debe contener una imagen");
        }
    }//cierra validarDatos

    public Noticia getOne(String id) {
        return noticiaRepositorio.getOne(id);
    }

    public List<Noticia> findByTitulo(String q) throws MiException, Exception {
        try {
            List<Noticia> entradas = this.noticiaRepositorio.finfByTitulo(q.toUpperCase());
            return entradas;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}//cierra NoticiaServicio
