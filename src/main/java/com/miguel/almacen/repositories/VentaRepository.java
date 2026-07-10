package com.miguel.almacen.repositories;

import com.miguel.almacen.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
