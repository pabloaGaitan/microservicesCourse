package com.microservicios.productos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.commons.models.Producto;
import com.microservicios.productos.service.IProductoService;

@RestController
@RequestMapping
public class ProductoController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IProductoService productoService;
	
	@GetMapping
	public List<Producto> listar(){
		return productoService.findAll().stream().map(p ->{
			p.setPort(Integer.parseInt(env.getProperty("local.server.port")));
			return p;
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public Producto detalle(@PathVariable Long id){
		Producto producto = productoService.findById(id);
		producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
//		try {
//			Thread.sleep(3000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return producto;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crearProducto(@RequestBody Producto producto) {
		return productoService.save(producto);
	}
	
//	@PutMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public Producto editarProducto(@RequestBody Producto producto) {
//		return productoService.save(producto);
//	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editarProducto(@PathVariable Long id, @RequestBody Producto producto) {
		Producto productoDb = productoService.findById(id);
		
		productoDb.setNombre(producto.getNombre());
		productoDb.setPrecio(producto.getPrecio());
		
		return productoService.save(productoDb);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProducto(@PathVariable Long id) {
		productoService.deleteById(id);
	}
}
