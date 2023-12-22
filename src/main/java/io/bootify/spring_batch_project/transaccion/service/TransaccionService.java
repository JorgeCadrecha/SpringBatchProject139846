package io.bootify.spring_batch_project.transaccion.service;

import io.bootify.spring_batch_project.cuenta.domain.Cuenta;
import io.bootify.spring_batch_project.cuenta.repos.CuentaRepository;
import io.bootify.spring_batch_project.transaccion.domain.Transaccion;
import io.bootify.spring_batch_project.transaccion.model.TransaccionDTO;
import io.bootify.spring_batch_project.transaccion.repos.TransaccionRepository;
import io.bootify.spring_batch_project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public TransaccionService(final TransaccionRepository transaccionRepository,
            final CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<TransaccionDTO> findAll() {
        final List<Transaccion> transaccions = transaccionRepository.findAll(Sort.by("id"));
        return transaccions.stream()
                .map(transaccion -> mapToDTO(transaccion, new TransaccionDTO()))
                .toList();
    }

    public TransaccionDTO get(final Integer id) {
        return transaccionRepository.findById(id)
                .map(transaccion -> mapToDTO(transaccion, new TransaccionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TransaccionDTO transaccionDTO) {
        final Transaccion transaccion = new Transaccion();
        mapToEntity(transaccionDTO, transaccion);
        return transaccionRepository.save(transaccion).getId();
    }

    public void update(final Integer id, final TransaccionDTO transaccionDTO) {
        final Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transaccionDTO, transaccion);
        transaccionRepository.save(transaccion);
    }

    public void delete(final Integer id) {
        transaccionRepository.deleteById(id);
    }

    private TransaccionDTO mapToDTO(final Transaccion transaccion,
            final TransaccionDTO transaccionDTO) {
        transaccionDTO.setId(transaccion.getId());
        transaccionDTO.setTipoTransaccion(transaccion.getTipoTransaccion());
        transaccionDTO.setCantidad(transaccion.getCantidad());
        transaccionDTO.setFechaHora(transaccion.getFechaHora());
        transaccionDTO.setCuenta(transaccion.getCuenta() == null ? null : transaccion.getCuenta().getId());
        return transaccionDTO;
    }

    private Transaccion mapToEntity(final TransaccionDTO transaccionDTO,
            final Transaccion transaccion) {
        transaccion.setTipoTransaccion(transaccionDTO.getTipoTransaccion());
        transaccion.setCantidad(transaccionDTO.getCantidad());
        transaccion.setFechaHora(transaccionDTO.getFechaHora());
        final Cuenta cuenta = transaccionDTO.getCuenta() == null ? null : cuentaRepository.findById(transaccionDTO.getCuenta())
                .orElseThrow(() -> new NotFoundException("cuenta not found"));
        transaccion.setCuenta(cuenta);
        return transaccion;
    }

}
