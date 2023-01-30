package com.evol.marcosbr.servicio;

import com.evol.marcosbr.entidad.Usuario;
import com.evol.marcosbr.util.RespuestaControlador;

public interface UsuarioServicio extends BaseServicio<Usuario, Long> {
    
    public RespuestaControlador validarCredenciales(String usuario, String contrasena);
    
}
