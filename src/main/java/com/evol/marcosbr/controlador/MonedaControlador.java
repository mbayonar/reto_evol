package com.evol.marcosbr.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evol.marcosbr.entidad.Moneda;
import com.evol.marcosbr.enums.NombreEntidadEnum;
import com.evol.marcosbr.servicio.MonedaServicio;

/**
*
* @author Marcos Bayona Rijalba
*/
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/moneda")
public class MonedaControlador extends BaseControladorImpl<Moneda, Long> implements BaseControlador<Moneda, Long> {

    @Autowired
    public MonedaControlador(MonedaServicio monedaServicio) {
        super(monedaServicio, NombreEntidadEnum.MONEDA.getValor());
    }

}
