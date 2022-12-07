package com.Proyect5.demo.Entidades;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table
public class Noticia {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "subtitulo")
    private String subtitulo;

    @Column(name = "cuerpo")
    private String cuerpo;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fechaPublicacion;

    @Column(name = "estado")
    private Boolean estado;

    @OneToOne
    private Imagen imagen;



}
