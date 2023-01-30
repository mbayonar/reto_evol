package com.evol.marcosbr.servicio;

import java.util.Map;

import com.evol.marcosbr.entidad.TipoCambio;

/**
 *
 * @author Marcos Bayona Rijalba
 */
public interface TipoCambioServicio extends BaseServicio<TipoCambio, Long> {

    public TipoCambio obtenerTipoCambioDeMoneda(Long monedaId);

    public Map<String, Object> procesarTipoDeCambio(Double monto, Long monedaOrigenId, Long monedaDestinoId);

}
