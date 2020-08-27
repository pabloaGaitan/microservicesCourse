package com.microservicios.items.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservicios.commons.models.Producto;
import com.microservicios.items.model.Item;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public List<Item> findAll() {
		List<Producto> productos = Arrays.asList(restTemplate.getForObject("http://servicio-productos", Producto[].class));
		return productos.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("id", cantidad.toString());
		Producto producto = restTemplate.getForObject("http://servicio-productos/{id}", Producto.class, uriVariables);
		return new Item(producto,cantidad);
	}

	@Override
	public Producto createProducto(Producto producto) {
		HttpEntity<Producto> entity = new HttpEntity<Producto>(producto);
		ResponseEntity<Producto> response = restTemplate.exchange("http://servicio-productos", HttpMethod.POST,entity, Producto.class );
		Producto productoResponse = response.getBody();
		return productoResponse;
	}

	@Override
	public Producto updateProducto(Producto producto, Long id) {
		HttpEntity<Producto> entity = new HttpEntity<Producto>(producto);
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("id", id.toString());
		ResponseEntity<Producto> response = restTemplate.exchange("http://servicio-productos/{id}", 
				HttpMethod.PUT,	entity, Producto.class, uriVariables);
		return response.getBody();
	}

	@Override
	public void deleteProducto(Long id) {
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("id", id.toString());
		restTemplate.delete("http://servicio-productos/{id}", uriVariables);
	}

}
