package com.evol.marcosbr.controlador;

import org.springframework.http.ResponseEntity;

import com.evol.marcosbr.util.RespuestaControlador;

/**
*
* @author Marcos Bayona Rijalba
*/
public interface BaseControlador<Entidad, TipoLlave> {

   public ResponseEntity<RespuestaControlador> crear(Entidad entidad);

   public ResponseEntity<RespuestaControlador> obtener(TipoLlave id);

   public ResponseEntity<RespuestaControlador> actualizar(Entidad entidad);

   public ResponseEntity<RespuestaControlador> eliminar(TipoLlave entidadId);

   public ResponseEntity<RespuestaControlador> obtenerTodos();

}
