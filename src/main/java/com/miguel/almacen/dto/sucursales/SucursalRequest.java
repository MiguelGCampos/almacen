package com.miguel.almacen.dto.sucursales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SucursalRequest(
    @NotBlank(message = "El nombre es requerido")
    @Size(min=5, max=50, message = "El nombre es requerido y debe tener entre 5 y 30 caracteres")
    String nombre,

    @NotBlank(message = "La dirección es requerida")
    @Size(min=5, max=150, message = "La dirección es requerida y debe tener entre 5 y 30 caracteres")
    String direccion
){
}
