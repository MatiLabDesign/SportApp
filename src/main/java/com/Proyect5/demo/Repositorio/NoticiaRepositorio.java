package com.Proyect5.demo.Repositorio;

import com.Proyect5.demo.Entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String> {

    @Query(value = "SELECT * FROM noticia WHERE noticia.titulo LIKE %:q%", nativeQuery = true)
    List<Noticia> finfByTitulo(@Param("q") String q);

}
