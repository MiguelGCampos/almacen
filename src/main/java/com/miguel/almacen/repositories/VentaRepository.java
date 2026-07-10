package com.miguel.almacen.repositories;

import com.miguel.almacen.dto.reportes.ReporteVentasResponse;
import com.miguel.almacen.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query("SELECT new com.miguel.almacen.dto.reportes.ReporteVentasResponse(" +
            "dv.venta.sucursal.id, " +
            "dv.venta.sucursal.nombre, " +
            "SUM(dv.cantidadProducto * dv.precioProducto), " +
            "SUM(dv.cantidadProducto)" +
            ") " +
            "FROM DetalleVenta dv " +
            "WHERE dv.venta.estadoVenta = 'REGISTRADA' " +
            "GROUP BY dv.venta.sucursal.id, dv.venta.sucursal.nombre")
    List<ReporteVentasResponse> obtenerResumenVentas();
}
