package com.microservicios.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservicios.commons.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
