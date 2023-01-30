package com.evol.marcosbr.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import lombok.Data;

/**
 *
 * @author Marcos Bayona Rijalba
 */
@Entity
@Table(name = "moneda")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate
@Data
public class Moneda extends AuditoriaEntidad {

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "abreviatura", nullable = false)
    private String abreviatura;

    @Column(name = "jerarquia", nullable = false)
    private int jerarquia;

    public Moneda() {

    }

}
