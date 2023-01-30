package com.evol.marcosbr.servicio;

import java.util.List;

import com.evol.marcosbr.excepcion.EntidadDuplicadaExcepcion;
import com.evol.marcosbr.util.RespuestaControlador;

/**
 *
 * @author Marcos Bayona Rijalba
 * @param <Entidad>
 * @param <TipoLlave>
 */
public interface BaseServicio<Entidad, TipoLlave> {

    Entidad obtener(TipoLlave id);

    void grabarTodos(List<Entidad> list) throws EntidadDuplicadaExcepcion;

    List<Entidad> obtenerTodos();

    public RespuestaControlador crear(Entidad entidad) throws EntidadDuplicadaExcepcion;

    public RespuestaControlador actualizar(Entidad entidad) throws EntidadDuplicadaExcepcion;

    public RespuestaControlador eliminar(TipoLlave entidadId);

}
