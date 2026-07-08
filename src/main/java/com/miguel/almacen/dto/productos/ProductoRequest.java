package com.miguel.almacen.dto.productos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min=5, max=30, message = "El nombre es requerido y debe tener entre 5 y 30 caracteres")
        String nombre,

        @NotBlank(message = "La categoria es reqeurida")
        String categoria,

        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser positivo")
        BigDecimal precio,

        @NotNull(message = "La cantidad es requerido")
        @Positive(message = "La cantidad debe ser positiva      ")
        Integer cantidad
) {
}
