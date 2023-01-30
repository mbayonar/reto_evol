package com.evol.marcosbr.controlador;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evol.marcosbr.entidad.Usuario;
import com.evol.marcosbr.enums.NombreEntidadEnum;
import com.evol.marcosbr.servicio.UsuarioServicio;
import com.evol.marcosbr.util.RespuestaControlador;
import com.evol.marcosbr.util.RespuestaControladorServicio;

/**
*
* @author Marcos Bayona Rijalba
*/
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class UsuarioControlador extends BaseControladorImpl<Usuario, Long> implements BaseControlador<Usuario, Long> {

    private final Logger logger = LogManager.getLogger(getClass());

    private final UsuarioServicio usuarioServicio;

    @Autowired
    private RespuestaControladorServicio respuestaControladorServicio;

    @Autowired
    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        super(usuarioServicio, NombreEntidadEnum.USUARIO.getValor());
        this.usuarioServicio = usuarioServicio;
    }

    @PostMapping("logeo")
    public ResponseEntity<RespuestaControlador> logeo(@RequestParam("usuario") String usuario, @RequestParam("contrasena") String contrasena) {
        RespuestaControlador respuestaControlador;
        try {
            respuestaControlador = usuarioServicio.validarCredenciales(usuario, contrasena);
        } catch (Exception exception) {
            logger.error(exception, exception);
            respuestaControlador = respuestaControladorServicio.obtenerRespuestaDeErrorObtener(nombreEntidad);
        }
        return new ResponseEntity<>(respuestaControlador, HttpStatus.OK);
    }
}
