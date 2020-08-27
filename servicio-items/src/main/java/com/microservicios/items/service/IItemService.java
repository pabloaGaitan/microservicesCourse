package com.microservicios.items.service;

import java.util.List;

import com.microservicios.commons.models.Producto;
import com.microservicios.items.model.Item;

public interface IItemService {
	
	public List<Item> findAll();
	public Item findById(Long id, Integer cantidad);
	
	public Producto createProducto(Producto producto);
	public Producto updateProducto(Producto producto, Long id);
	public void deleteProducto(Long id);
}
