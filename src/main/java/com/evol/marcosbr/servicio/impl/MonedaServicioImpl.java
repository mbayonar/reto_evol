package com.evol.marcosbr.servicio.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evol.marcosbr.entidad.Moneda;
import com.evol.marcosbr.enums.NombreEntidadEnum;
import com.evol.marcosbr.excepcion.EntidadDuplicadaExcepcion;
import com.evol.marcosbr.repositorio.MonedaRepositorio;
import com.evol.marcosbr.servicio.MonedaServicio;
import com.evol.marcosbr.util.RespuestaControlador;
import com.evol.marcosbr.util.RespuestaControladorServicio;

/**
 *
 * @author Marcos Bayona Rijalba
 */
@Service
public class MonedaServicioImpl extends BaseServicioImpl<Moneda, Long> implements MonedaServicio {

    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private RespuestaControladorServicio respuestaControladorServicio;

    @Autowired
    private MonedaRepositorio monedaRepositorio;

    @Autowired
    public MonedaServicioImpl(MonedaRepositorio monedaRepositorio) {
        super(monedaRepositorio);
    }

    @Override
    public RespuestaControlador crear(Moneda moneda) throws EntidadDuplicadaExcepcion {
    	RespuestaControlador respuesta;
    	if (moneda.esNuevo()) {
            this.monedaRepositorio.crear(moneda);
            respuesta = this.respuestaControladorServicio.obtenerRespuestaDeExitoCrearConData(NombreEntidadEnum.MONEDA.getValor(), moneda.getId());
    	} else {
    		respuesta = this.actualizar(moneda);
    	}
        return respuesta;
    }

    @Override
    public RespuestaControlador actualizar(Moneda moneda) throws EntidadDuplicadaExcepcion {
    	RespuestaControlador respuesta;
    	if (moneda.getEstado() == Boolean.TRUE) {
            this.monedaRepositorio.actualizar(moneda);
            respuesta = respuestaControladorServicio.obtenerRespuestaDeExitoActualizar(NombreEntidadEnum.MONEDA.getValor());
    	} else {
    		respuesta = respuestaControladorServicio.obtenerRespuestaDeErrorActualizar(NombreEntidadEnum.MONEDA.getValor()); 
    	}
        return respuesta;
    }

    @Override
    public RespuestaControlador eliminar(Long monedaId) {
        RespuestaControlador respuesta;
        Moneda moneda;
        Boolean puedeEliminar;

        puedeEliminar = true;

        if (puedeEliminar == null || !puedeEliminar) {
            respuesta = RespuestaControlador.obtenerRespuestaDeError("El " + NombreEntidadEnum.MONEDA.getValor().toLowerCase() + " ha sido asignado a uno o varios usuarios y no se puede eliminar");
        } else {
            moneda = monedaRepositorio.obtener(monedaId);
            moneda.setEstado(Boolean.FALSE);
            monedaRepositorio.actualizar(moneda);
            respuesta = respuestaControladorServicio.obtenerRespuestaDeExitoEliminar(NombreEntidadEnum.MONEDA.getValor());
        }

        return respuesta;
    }
}
