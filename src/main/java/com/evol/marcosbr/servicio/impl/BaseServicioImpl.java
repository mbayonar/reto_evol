package com.evol.marcosbr.servicio.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.evol.marcosbr.repositorio.BaseRepositorio;
import com.evol.marcosbr.servicio.BaseServicio;

/**
*
* @author Marcos Bayona Rijalba
* @param <Entidad>
* @param <TipoLlave>
*/
@Transactional
public abstract class BaseServicioImpl<Entidad, TipoLlave> implements BaseServicio<Entidad, TipoLlave> {

   private final BaseRepositorio<Entidad, TipoLlave> baseRepositorio;

   @SuppressWarnings("unused")
   private Class<Entidad> domainClass = null;

   @SuppressWarnings("unchecked")
   protected BaseServicioImpl(BaseRepositorio<Entidad, TipoLlave> baseHibernate) {
       this.baseRepositorio = baseHibernate;
       this.domainClass = (Class<Entidad>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
   }

   @Override
   public Entidad obtener(TipoLlave id) {
       return this.baseRepositorio.obtener(id);
   }

   @Override
   public void grabarTodos(List<Entidad> list) {
       this.baseRepositorio.grabarTodos(list);
   }

   @Override
   public List<Entidad> obtenerTodos() {
       return this.baseRepositorio.obtenerTodos();
   }

}
