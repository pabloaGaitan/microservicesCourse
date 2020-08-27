package com.microservicios.productos.service;

import java.util.List;

import com.microservicios.commons.models.Producto;

public interface IProductoService {
	
	List<Producto> findAll();
	Producto findById(Long id);
	Producto save(Producto producto);
	void deleteById(Long id);
}
