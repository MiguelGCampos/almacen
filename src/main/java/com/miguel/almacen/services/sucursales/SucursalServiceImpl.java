package com.miguel.almacen.services.sucursales;

import com.miguel.almacen.dto.sucursales.SucursalRequest;
import com.miguel.almacen.dto.sucursales.SucursalResponse;
import com.miguel.almacen.entities.Producto;
import com.miguel.almacen.entities.Sucursal;
import com.miguel.almacen.enums.Categoria;
import com.miguel.almacen.exceptions.RecursoNoEncontradoException;
import com.miguel.almacen.mappers.SucursalMapper;
import com.miguel.almacen.repositories.SucursalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional //Siempre hace commit o rollback
@Slf4j
public class SucursalServiceImpl implements SucursalService{

    private final SucursalRepository sucursalRepository;

    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        log.info("Listando todas las sucursales");
        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(sucursalRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("No se encontro una sucursal con el id: "+id)));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {
        log.info("Registrando nuevo producto");

        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);

        sucursalRepository.save(sucursal);

        log.info("Nueva sucursal {} registrada", sucursal.getNombre());

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);

        log.info("Actualizando sucursal con id: {}", id);

        validarCambiosUnicos(request, id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion()
        );

        log.info("Sucursal con id {} actualizado", id);

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);

        sucursalRepository.delete(sucursal);

        log.info("Sucursal con id {} eliminada", id);
    }

    private Sucursal obtenerSucursalOException(Long id){
        log.info("Buscando sucursal con id: {}", id);

        return sucursalRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("Sucursal no encontrado con id: "+id)
        );
    }

    private void validarDatosUnicos(SucursalRequest request){
        log.info("Validando nombre único...");
        if(sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de :"
            + request.nombre());
    }

    private void validarCambiosUnicos(SucursalRequest request, Long id){
        log.info("Validando nombre único...");
        if(sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de :"
                    + request.nombre());
    }
}
