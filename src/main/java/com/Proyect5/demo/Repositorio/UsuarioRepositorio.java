package com.Proyect5.demo.Repositorio;

import com.Proyect5.demo.Entidades.Usuario;
import com.Proyect5.demo.Enumeraciones.Rol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u Where u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);

    public List<Usuario> findByRol(Rol rol);

}
