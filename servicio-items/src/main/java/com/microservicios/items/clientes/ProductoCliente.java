package com.microservicios.items.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservicios.commons.models.Producto;

@FeignClient(name = "servicio-productos")
public interface ProductoCliente {
	
	@GetMapping
	public List<Producto> listar();
	
	@GetMapping("/{id}")
	public Producto detalle(@PathVariable Long id);
	
	@PostMapping
	public Producto crearProducto(@RequestBody Producto producto);
	
	@PutMapping("/{id}")
	public Producto editarProducto(@PathVariable Long id, @RequestBody Producto producto);
	
	@DeleteMapping("/{id}")
	public void deleteProducto(@PathVariable Long id);

}
