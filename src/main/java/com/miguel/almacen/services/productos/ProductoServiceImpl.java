package com.miguel.almacen.services.productos;

import com.miguel.almacen.dto.productos.ProductoRequest;
import com.miguel.almacen.dto.productos.ProductoResponse;
import com.miguel.almacen.entities.Producto;
import com.miguel.almacen.enums.Categoria;
import com.miguel.almacen.exceptions.RecursoNoEncontradoException;
import com.miguel.almacen.mappers.ProductoMapper;
import com.miguel.almacen.repositories.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional //Siempre hace commit o rollback
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        log.info("Listando toods los productos");
        return productoRepository.findAll().stream()
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        //return productoMapper.entidadAResponse(obtenerProductoOException(id));
        return productoMapper.entidadAResponse(productoRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("Producto no encontrado con id: "+id)
        ));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto");

        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria());

        Producto producto = productoMapper.requestAEntidad(request, categoria);

        productoRepository.save(producto);

        log.info("Nuevo producto {} registrado", producto.getNombre());

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoOException(id);

        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria());

        producto.actualizar(
                request.nombre(),
                categoria,
                request.precio(),
                request.cantidad()
        );

        log.info("Producto con id {} actualizado", id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoOException(id);

        productoRepository.delete(producto);

        log.info("Producto con id {} eliminado", id);
    }

    private Producto obtenerProductoOException(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("Producto no encontrado con id: "+id)
        );
    }
}
