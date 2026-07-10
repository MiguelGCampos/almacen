package com.miguel.almacen.dto.reportes;

import java.math.BigDecimal;

public record ReporteVentasResponse(
        Long idSucursal,
        String nombreSucursal,
        BigDecimal totalFacturado,
        Long cantProductosVendidos
) {
}
