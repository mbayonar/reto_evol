package com.evol.marcosbr.servicio.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evol.marcosbr.entidad.Moneda;
import com.evol.marcosbr.entidad.TipoCambio;
import com.evol.marcosbr.enums.NombreEntidadEnum;
import com.evol.marcosbr.excepcion.EntidadDuplicadaExcepcion;
import com.evol.marcosbr.repositorio.MonedaRepositorio;
import com.evol.marcosbr.repositorio.TipoCambioRepositorio;
import com.evol.marcosbr.servicio.TipoCambioServicio;
import com.evol.marcosbr.util.Criterio;
import com.evol.marcosbr.util.RespuestaControlador;
import com.evol.marcosbr.util.RespuestaControladorServicio;
import com.evol.marcosbr.util.SistemaUtil;

/**
 *
 * @author Marcos Bayona Rijalba
 */
@Service
public class TipoCambioServicioImpl extends BaseServicioImpl<TipoCambio, Long> implements TipoCambioServicio {

    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private RespuestaControladorServicio respuestaControladorServicio;

    @Autowired
    private TipoCambioRepositorio tipoCambioRepositorio;

    @Autowired
    private MonedaRepositorio monedaRepositorio;

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    public TipoCambioServicioImpl(TipoCambioRepositorio tipoCambioRepositorio) {
        super(tipoCambioRepositorio);
    }

    @Override
    public RespuestaControlador crear(TipoCambio tipoCambio) throws EntidadDuplicadaExcepcion {
    	RespuestaControlador respuesta;
    	if (tipoCambio.esNuevo()) {
            this.tipoCambioRepositorio.crear(tipoCambio);
            respuesta = this.respuestaControladorServicio.obtenerRespuestaDeExitoCrearConData(NombreEntidadEnum.TIPO_CAMBIO.getValor(), tipoCambio.getId());
    	} else {
    		respuesta = this.actualizar(tipoCambio);
    	}
        return respuesta;
    }

    @Override
    public RespuestaControlador actualizar(TipoCambio tipoCambio) throws EntidadDuplicadaExcepcion {
    	RespuestaControlador respuesta;
    	if (tipoCambio.getEstado() == Boolean.TRUE) {
            this.tipoCambioRepositorio.actualizar(tipoCambio);
            respuesta = respuestaControladorServicio.obtenerRespuestaDeExitoActualizar(NombreEntidadEnum.TIPO_CAMBIO.getValor());
    	} else {
    		respuesta = respuestaControladorServicio.obtenerRespuestaDeErrorActualizar(NombreEntidadEnum.TIPO_CAMBIO.getValor()); 
    	}
        return respuesta;
    }

    @Override
    public RespuestaControlador eliminar(Long tipoCambioId) {
        RespuestaControlador respuesta;
        TipoCambio tipoCambio;
        Boolean puedeEliminar;

        puedeEliminar = true;

        if (puedeEliminar == null || !puedeEliminar) {
            respuesta = RespuestaControlador.obtenerRespuestaDeError("El " + NombreEntidadEnum.TIPO_CAMBIO.getValor().toLowerCase() + " ha sido asignado a uno o varios usuarios y no se puede eliminar");
        } else {
            tipoCambio = tipoCambioRepositorio.obtener(tipoCambioId);
            tipoCambio.setEstado(Boolean.FALSE);
            tipoCambioRepositorio.actualizar(tipoCambio);
            respuesta = respuestaControladorServicio.obtenerRespuestaDeExitoEliminar(NombreEntidadEnum.TIPO_CAMBIO.getValor());
        }

        return respuesta;
    }

    @Override
    public TipoCambio obtener(Long id) {
        TipoCambio tipoCambio;
        Criterio filtro = Criterio.forClass(TipoCambio.class);
        filtro.createAlias("moneda", "mon", JoinType.LEFT_OUTER_JOIN);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("id", id));
        tipoCambio = tipoCambioRepositorio.obtenerPorCriterio(filtro);

        return tipoCambio;
    }

    @Override
    public List<TipoCambio> obtenerTodos() {
        Criterio filtro = Criterio.forClass(TipoCambio.class);
        filtro.createAlias("moneda", "mon", JoinType.LEFT_OUTER_JOIN);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setProjection(null);
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (List<TipoCambio>) busqueda.list();
    }

    @Override
    public TipoCambio obtenerTipoCambioDeMoneda(Long monedaId) {
        TipoCambio tipoCambio;
        Criterio filtro = Criterio.forClass(TipoCambio.class);
        filtro.createAlias("moneda", "mon", JoinType.LEFT_OUTER_JOIN);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("mon.id", monedaId));
        filtro.add(Restrictions.eq("fechaCambio", new Date()));
        tipoCambio = tipoCambioRepositorio.obtenerPorCriterio(filtro);

        return tipoCambio;
    }

    @Override
    public Map<String, Object> procesarTipoDeCambio(Double monto, Long monedaOrigenId, Long monedaDestinoId) {
        Double montoEnDestino, multiplicador, multOrigen, multDestino;
        TipoCambio tipoCambioOrigen, tipoCambioDestino;
        Map<String, Object> resultado;

        resultado = new HashMap<>();
        tipoCambioOrigen = obtenerTipoCambioDeMoneda(monedaOrigenId);
        tipoCambioDestino = obtenerTipoCambioDeMoneda(monedaDestinoId);

        if (SistemaUtil.esNoNulo(tipoCambioOrigen) && SistemaUtil.esNoNulo(tipoCambioDestino)
                && SistemaUtil.esNoNulo(tipoCambioOrigen.getMoneda()) && SistemaUtil.esNoNulo(tipoCambioDestino.getMoneda())) {

            // Obtenemos el multiplicador de moneda origen a dólares
            multOrigen = this.obtenerMultiplicador(tipoCambioOrigen.getMoneda(), tipoCambioDestino.getMoneda(), tipoCambioOrigen.getPrecioVenta());
            // Obtenemos el multiplicador de dólares a moneda destino
            multDestino = this.obtenerMultiplicador(tipoCambioOrigen.getMoneda(), tipoCambioDestino.getMoneda(), tipoCambioDestino.getPrecioVenta());
            multiplicador = multOrigen * multDestino;
            montoEnDestino = monto * multiplicador;

            resultado.put("montoTipoCambio", montoEnDestino);
            resultado.put("tipoCambio", multiplicador);
            resultado.put("monedaOrigen", tipoCambioOrigen.getMoneda().getNombre());
            resultado.put("monedaDestino", tipoCambioDestino.getMoneda().getNombre());
            resultado.put("fecha", tipoCambioOrigen.getFechaCambio());
            resultado.put("extraInfo", "Operación exitosa");
        } else {
            resultado.put("precioCompra", null);
            resultado.put("tipoCambio", null);
            resultado.put("monedaOrigen", monedaRepositorio.obtener(monedaOrigenId));
            resultado.put("monedaDestino", monedaRepositorio.obtener(monedaDestinoId));
            resultado.put("extraInfo", "No se encontró el tipo de cambio para la fecha actual");
        }

        resultado.put("monto", monto);
        return resultado;
    }

    public Double obtenerMultiplicador(Moneda monedaOrigen, Moneda monedaDestino, Double precioVenta) {
        Double multiplicador;
        if (monedaOrigen.getJerarquia() < monedaDestino.getJerarquia()) {
            multiplicador = 1 / precioVenta;
        } else {
            multiplicador = precioVenta;
        }
        return multiplicador;
    }

}
