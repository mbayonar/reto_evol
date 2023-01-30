package com.evol.marcosbr.controlador;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.evol.marcosbr.entidad.TipoCambio;
import com.evol.marcosbr.enums.NombreEntidadEnum;
import com.evol.marcosbr.servicio.TipoCambioServicio;

/**
*
* @author Marcos Bayona Rijalba
*/
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tipoCambio")
public class TipoCambioControlador extends BaseControladorImpl<TipoCambio, Long> implements BaseControlador<TipoCambio, Long> {

   @Autowired
   private TipoCambioServicio tipoCambioServicio;
   
   @Autowired
   public TipoCambioControlador(TipoCambioServicio tipoCambioServicio) {
       super(tipoCambioServicio, NombreEntidadEnum.TIPO_CAMBIO.getValor());
   }

   @RequestMapping("procesarTipoDeCambio")
   @ResponseBody
   public Map<String, Object> procesarTipoDeCambio(@RequestParam(value = "monto", required = false) Double monto,
                                           @RequestParam(value = "monedaOrigenId", required = false) Long monedaOrigenId,
                                           @RequestParam(value = "monedaDestinoId", required = false) Long monedaDestinoId) {
       Map<String, Object> paramsMap = tipoCambioServicio.procesarTipoDeCambio(monto, monedaOrigenId, monedaDestinoId);

       return paramsMap;
   }
}
