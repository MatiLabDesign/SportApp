package com.Proyect5.demo.Servicio;
import com.Proyect5.demo.Entidades.Imagen;
import com.Proyect5.demo.Excepciones.MiException;
import com.Proyect5.demo.Repositorio.ImagenRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {

    @Autowired // permite inyectar unas dependencias con otras dentro de Spring
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws MiException{

        if (archivo != null) {

            try {
                Imagen imagen = new Imagen();

                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getName());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }//cierra guardar
    
    public Imagen editarImagen(MultipartFile archivo, String idImagen) throws MiException{
        
        if (archivo != null) {

            try {
                Imagen imagen = new Imagen();
                
                if(idImagen != null){
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    
                    if(respuesta.isPresent()){
                        
                        imagen = respuesta.get();
                    }
                }
                
                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getName());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }//cierra actualizar
    
}//cierra ImagenServicio
