package com.miguel.almacen.specification;

import com.miguel.almacen.entities.Producto;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductoSpecs {

    public static Specification<Producto> esCategoria(String categoria) {
        return (root, query, builder) -> {
            if (categoria == null || categoria.isBlank()) {
                return builder.conjunction();
            }
            return builder.equal(root.get("categoria"), categoria);
        };
    }

    public static Specification<Producto> tieneNombre(String nombre) {
        return (root, query, builder) -> {
            if (nombre == null || nombre.isBlank()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
        };
    }

    public static Specification<Producto> precioEntre(BigDecimal precioMin, BigDecimal precioMax) {
        return (root, query, builder) -> {
            if (precioMin == null && precioMax == null) {
                return builder.conjunction();
            }

            Path<BigDecimal> precioPath = root.get("precio");

            if (precioMin != null && precioMax != null) {
                return builder.between(precioPath, precioMin, precioMax);
            } else if (precioMin != null) {
                return builder.greaterThanOrEqualTo(precioPath, precioMin);
            } else {
                return builder.lessThanOrEqualTo(precioPath, precioMax);
            }
        };
    }
}
