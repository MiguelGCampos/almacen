package com.miguel.almacen.services.ventas;

import com.miguel.almacen.dto.ventas.DetalleVentaRequest;
import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;
import com.miguel.almacen.entities.DetalleVenta;
import com.miguel.almacen.entities.Producto;
import com.miguel.almacen.entities.Sucursal;
import com.miguel.almacen.entities.Venta;
import com.miguel.almacen.enums.EstadoVenta;
import com.miguel.almacen.exceptions.RecursoNoEncontradoException;
import com.miguel.almacen.mappers.DetalleVentaMapper;
import com.miguel.almacen.mappers.VentaMapper;
import com.miguel.almacen.repositories.ProductoRepository;
import com.miguel.almacen.repositories.SucursalRepository;
import com.miguel.almacen.repositories.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional //Siempre hace commit o rollback
@Slf4j
public class VentaServiceImpl implements VentaService{

    private final VentaRepository ventaRepository;

    private final VentaMapper ventaMapper;

    private final DetalleVentaMapper detalleVentaMapper;

    private final SucursalRepository sucursalRepository;

    private final ProductoRepository productoRepository;

    @Override
    public List<VentaResponse> listarActivas() {
        return ventaRepository.findAll().stream()
                .filter(venta->venta.getEstadoVenta()== EstadoVenta.REGISTRADA)
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    public List<VentaResponse> listarCanceladas() {
        return ventaRepository.findAll().stream()
                .filter(venta->venta.getEstadoVenta()== EstadoVenta.CANCELADA)
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    public VentaResponse obtenerPorIdActiva(Long id) {
        return ventaRepository.findById(id)
                .filter(venta -> venta.getEstadoVenta() == EstadoVenta.REGISTRADA)
                .map(ventaMapper::entidadAResponse)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "La venta con id " + id + " no está registrada o no existe"));
    }

    @Override
    @Transactional
    public VentaResponse registrar(VentaRequest request) {
        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada"));

        Venta venta = Venta.builder()
                .fecha(LocalDate.now())
                .estadoVenta(EstadoVenta.REGISTRADA)
                .sucursal(sucursal)
                .build();

        for (DetalleVentaRequest item : request.productos()) {
            Producto producto = productoRepository.findById(item.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

            if (producto.getCantidad() < item.cantidadProducto()) {
                throw new IllegalArgumentException("Stock insuficiente para ese producto");
            }

            producto.descontarCantidad(item.cantidadProducto());

            DetalleVenta detalle = detalleVentaMapper.requestAEntidad(item, null, producto);

            venta.agregarDetalle(detalle);
        }

        ventaRepository.save(venta);
        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse cancelar(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con id: "+id));
        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA) {
            throw new IllegalStateException("No puedes cancelar esta venta, ya está cancelada.");
        }
        venta.cancelar();
        for (DetalleVenta detalle : venta.getDetalleVenta()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
            productoRepository.save(producto);
        }
        ventaRepository.save(venta);
        return ventaMapper.entidadAResponse(venta);
    }

}
