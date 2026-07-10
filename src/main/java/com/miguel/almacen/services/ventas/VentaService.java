package com.miguel.almacen.services.ventas;

import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VentaService {

    List<VentaResponse> listarActivas();

    List<VentaResponse> listarCanceladas();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    VentaResponse cancelar(Long id);
}
