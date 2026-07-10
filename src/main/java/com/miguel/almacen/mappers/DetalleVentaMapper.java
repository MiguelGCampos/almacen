package com.miguel.almacen.mappers;

import com.miguel.almacen.dto.ventas.DetalleVentaRequest;
import com.miguel.almacen.dto.ventas.DetalleVentaResponse;
import com.miguel.almacen.entities.DetalleVenta;
import com.miguel.almacen.entities.Producto;
import com.miguel.almacen.entities.Venta;
import jdk.jfr.Category;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DetalleVentaMapper {

    public DetalleVenta requestAEntidad(DetalleVentaRequest request, Venta ventaPadre, Producto producto) {
        if (request == null) return null;

        return DetalleVenta.builder()
                .cantidadProducto(request.cantidadProducto())
                .precioProducto(producto.getPrecio())
                .producto(producto)
                .venta(ventaPadre)
                .build();
    }

    public DetalleVentaResponse entidadAResponse(DetalleVenta detalle) {
        if (detalle == null) return null;

        Producto prod = detalle.getProducto();
        BigDecimal cantidad = new BigDecimal(detalle.getCantidadProducto());
        BigDecimal precio = detalle.getPrecioProducto();
        BigDecimal subtotal = cantidad.multiply(precio); // Cálculo del subtotal

        return new DetalleVentaResponse(
                prod != null ? prod.getId() : null,
                prod != null ? prod.getNombre() : null,
                detalle.getCantidadProducto(),
                precio,
                subtotal
        );
    }
}
