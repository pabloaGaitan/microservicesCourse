package com.microservicios.items.model;

import com.microservicios.commons.models.Producto;

public class Item {
	
	private Producto producto;
	private int cantidad;
	
	public Item() {}
	
	public Item(Producto producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
	}
	
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public Double getTotal() {
		return this.producto.getPrecio() * this.cantidad;
	}
	
	
}
