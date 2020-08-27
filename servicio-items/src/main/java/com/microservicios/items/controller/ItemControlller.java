package com.microservicios.items.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservicios.commons.models.Producto;
import com.microservicios.items.model.Item;
import com.microservicios.items.service.IItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RefreshScope
@RestController
public class ItemControlller {
	
	@Autowired
	private Environment env;
	
	@Autowired
	@Qualifier("serviceFeign")
	private IItemService itemService;
	
	@Value("${configuracion.texto}")
	private String texto;
	
	@GetMapping
	public List<Item> findAll(){
		return itemService.findAll();
	}
	
	@HystrixCommand(fallbackMethod = "metodoError")
	@GetMapping("{id}/cantidad/{cantidad}")
	public Item findById(@PathVariable("id") Long id, @PathVariable("cantidad") Integer cantidad) {
		return itemService.findById(id, cantidad);
	}
	
	public Item metodoError(Long id, Integer cantidad){
		Producto producto = new Producto();
		producto.setId(id);
		producto.setNombre("ERROR");
		producto.setPrecio(200D);
		return new Item(producto, cantidad);
	}
	
	@GetMapping("/config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
		Map<String,String> json = new HashMap<>();
		json.put("texto", texto);
		json.put("puerto",puerto);
		
		if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("email", env.getProperty("configuracion.autor.email"));
		}
		return new ResponseEntity<Map<String,String>>(json,HttpStatus.OK);
	}
	
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crearProducto(@RequestBody Producto producto) {
		return itemService.createProducto(producto);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto updateProducto(@PathVariable("id") Long id, @RequestBody Producto producto) {
		return itemService.updateProducto(producto, id);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProducto(@PathVariable("id") Long id) {
		itemService.deleteProducto(id);
	}
}
