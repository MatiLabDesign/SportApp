package com.Proyect5.demo.Entidades;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table
public class Entrenamiento {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "disciplina")
    private String disciplina;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "diasDeEntrenamiento")
    private int diasDeEntrenamiento;

    @Column(name = "rutina")
    private String rutina;

    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "fechaFin")
    private LocalDate fechaFin;

    @ManyToOne
    private Usuario profesor;

}
