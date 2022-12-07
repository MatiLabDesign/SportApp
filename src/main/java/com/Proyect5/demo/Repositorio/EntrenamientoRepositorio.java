package com.Proyect5.demo.Repositorio;

import com.Proyect5.demo.Entidades.Entrenamiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntrenamientoRepositorio extends JpaRepository<Entrenamiento, String> {

    public List<Entrenamiento> findByDisciplina(String disciplina);

    @Query(value = "SELECT * FROM entrenamiento WHERE entrenamiento.disciplina LIKE %:e%", nativeQuery = true)
    List<Entrenamiento> filtroDisciplina(@Param("e") String e);

}
