package com.microservicios.items.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservicios.commons.models.Producto;
import com.microservicios.items.clientes.ProductoCliente;
import com.microservicios.items.model.Item;

@Service("serviceFeign")
public class ItemServiceImplFeign implements IItemService {

	@Autowired
	private ProductoCliente productoCliente;
	
	@Override
	public List<Item> findAll() {
		List<Producto> productos = productoCliente.listar();
		return productos.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Producto producto = productoCliente.detalle(id);
		return new Item(producto,cantidad);
	}

	@Override
	public Producto createProducto(Producto producto) {
		return productoCliente.crearProducto(producto);
	}

	@Override
	public Producto updateProducto(Producto producto, Long id) {
		return productoCliente.editarProducto(id, producto);
	}

	@Override
	public void deleteProducto(Long id) {
		productoCliente.deleteProducto(id);
		
	}

}