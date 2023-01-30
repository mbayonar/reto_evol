package com.evol.marcosbr.repositorio.impl;

import org.springframework.stereotype.Repository;

import com.evol.marcosbr.entidad.Usuario;
import com.evol.marcosbr.repositorio.UsuarioRepositorio;

@Repository
public class UsuarioRepositorioImpl extends BaseRepositorioImpl<Usuario, Long> implements UsuarioRepositorio {

}
