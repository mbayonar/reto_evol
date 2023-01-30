package com.evol.marcosbr.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import lombok.Data;

/**
 *
 * @author Marcos Bayona Rijalba
 */
@Entity
@Table(name = "usuario")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate
@Data
public class Usuario extends AuditoriaEntidad {

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Transient
    private String token;

    public Usuario() {

    }

}
