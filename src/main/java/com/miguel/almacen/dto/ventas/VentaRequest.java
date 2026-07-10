package com.miguel.almacen.dto.ventas;

import com.miguel.almacen.enums.EstadoVenta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record VentaRequest(

        @NotNull(message = "El id de la sucursal es requerido")
        @Positive(message = "El id debe ser positivo")
        Long idSucursal,
        @NotEmpty(message = "La lista de productos es requerida y no debe estar vacio")
        List<@Valid DetalleVentaRequest> productos,
        @NotNull(message = "La lista de productos es requerida y no debe estar vacio")
        EstadoVenta estadoVenta

) {
}
