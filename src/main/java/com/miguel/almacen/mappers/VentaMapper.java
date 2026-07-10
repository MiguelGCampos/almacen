package com.miguel.almacen.mappers;

import com.miguel.almacen.dto.sucursales.SucursalResponse;
import com.miguel.almacen.dto.ventas.DetalleVentaResponse;
import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;
import com.miguel.almacen.entities.DetalleVenta;
import com.miguel.almacen.entities.Sucursal;
import com.miguel.almacen.entities.Venta;
import com.miguel.almacen.enums.EstadoVenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VentaMapper {

    private static final DetalleVentaMapper detalleMapper = new DetalleVentaMapper();

    public Venta requestAEntidad(VentaRequest request, Sucursal sucursal, List<DetalleVenta> detalles) {
        if (request == null) return null;

        return Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .detalleVenta(detalles)
                .build();
    }

    public VentaResponse entidadAResponse(Venta venta) {
        if (venta == null) return null;

        List<DetalleVentaResponse> detallesResponse = new ArrayList<>();
        if (venta.getDetalleVenta() != null) {
            for (DetalleVenta detalle : venta.getDetalleVenta()) {
                detallesResponse.add(detalleMapper.entidadAResponse(detalle));
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        if (venta.getDetalleVenta() != null) {
            for (DetalleVenta detalle : venta.getDetalleVenta()) {
                BigDecimal subtotal = detalle.getPrecioProducto()
                        .multiply(BigDecimal.valueOf(detalle.getCantidadProducto()));
                total = total.add(subtotal);
            }
        }

        SucursalResponse sucursalResponse = null;
        if (venta.getSucursal() != null) {
            sucursalResponse = new SucursalResponse(
                    venta.getSucursal().getId(),
                    venta.getSucursal().getNombre(),
                    venta.getSucursal().getDireccion()
            );
        }

        return new VentaResponse(
                venta.getId(),
                venta.getFecha().toString(),
                venta.getEstadoVenta().name(),
                sucursalResponse,
                detallesResponse,
                total
        );
    }

}
