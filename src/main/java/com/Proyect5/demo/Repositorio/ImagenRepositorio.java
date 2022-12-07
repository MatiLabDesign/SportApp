package com.Proyect5.demo.Repositorio;

import com.Proyect5.demo.Entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.data.repository.query.QueryCreationException;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String> {

}
