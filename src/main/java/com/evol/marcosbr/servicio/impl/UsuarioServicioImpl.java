package com.evol.marcosbr.servicio.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.evol.marcosbr.entidad.Usuario;
import com.evol.marcosbr.enums.NombreEntidadEnum;
import com.evol.marcosbr.excepcion.EntidadDuplicadaExcepcion;
import com.evol.marcosbr.repositorio.UsuarioRepositorio;
import com.evol.marcosbr.servicio.UsuarioServicio;
import com.evol.marcosbr.util.Constantes;
import com.evol.marcosbr.util.Criterio;
import com.evol.marcosbr.util.RespuestaControlador;
import com.evol.marcosbr.util.RespuestaControladorServicio;
import com.evol.marcosbr.util.SistemaUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
*
* @author Marcos Bayona Rijalba
*/
@Service
public class UsuarioServicioImpl extends BaseServicioImpl<Usuario, Long> implements UsuarioServicio {

    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private RespuestaControladorServicio respuestaControladorServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio) {
        super(usuarioRepositorio);
    }

    @Override
    public RespuestaControlador crear(Usuario usuario) throws EntidadDuplicadaExcepcion {
    	RespuestaControlador respuesta;
    	if (usuario.esNuevo()) {
            this.usuarioRepositorio.crear(usuario);
            respuesta = this.respuestaControladorServicio.obtenerRespuestaDeExitoCrearConData(NombreEntidadEnum.USUARIO.getValor(), usuario.getId());
    	} else {
    		respuesta = this.actualizar(usuario);
    	}
        return respuesta;
    }

    @Override
    public RespuestaControlador actualizar(Usuario usuario) throws EntidadDuplicadaExcepcion {
    	RespuestaControlador respuesta;
    	if (usuario.getEstado() == Boolean.TRUE) {
            this.usuarioRepositorio.actualizar(usuario);
            respuesta = respuestaControladorServicio.obtenerRespuestaDeExitoActualizar(NombreEntidadEnum.USUARIO.getValor());
    	} else {
    		respuesta = respuestaControladorServicio.obtenerRespuestaDeErrorActualizar(NombreEntidadEnum.USUARIO.getValor()); 
    	}
        return respuesta;
    }

    @Override
    public RespuestaControlador eliminar(Long usuarioId) {
        RespuestaControlador respuesta;
        Usuario usuario;
        Boolean puedeEliminar;

        puedeEliminar = true;

        if (puedeEliminar == null || !puedeEliminar) {
            respuesta = RespuestaControlador.obtenerRespuestaDeError("El " + NombreEntidadEnum.USUARIO.getValor().toLowerCase() + " ha sido asignado a uno o varios usuarios y no se puede eliminar");
        } else {
            usuario = usuarioRepositorio.obtener(usuarioId);
            usuario.setEstado(Boolean.FALSE);
            usuarioRepositorio.actualizar(usuario);
            respuesta = respuestaControladorServicio.obtenerRespuestaDeExitoEliminar(NombreEntidadEnum.USUARIO.getValor());
        }

        return respuesta;
    }

    @Override
    public RespuestaControlador validarCredenciales(String login, String contrasena) {
        RespuestaControlador respuesta = RespuestaControlador.obtenerRespuestaDeError(Constantes.RESPUESTA_CONTROLADOR.MENSAJE_ERROR_AUTENTICACION);
        
        Criterio filtro = Criterio.forClass(Usuario.class);
        filtro.add(Restrictions.eq("login", login));
        filtro.add(Restrictions.eq("contrasena", contrasena));
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));

        Usuario usuarioSession = usuarioRepositorio.obtenerPorCriterio(filtro);
        if (SistemaUtil.esNoNulo(usuarioSession)) {
            usuarioSession.setToken(this.generarJWToken(login));
            respuesta = RespuestaControlador.obtenerRespuestaExitoConData(usuarioSession);
        }
        return respuesta;
    }

    private String generarJWToken(String usuario) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder().setId("softtekJWT").setSubject(usuario)
                .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

}
