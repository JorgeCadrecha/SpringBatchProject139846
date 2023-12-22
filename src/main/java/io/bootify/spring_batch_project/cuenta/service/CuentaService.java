package io.bootify.spring_batch_project.cuenta.service;

import io.bootify.spring_batch_project.cliente.domain.Cliente;
import io.bootify.spring_batch_project.cliente.repos.ClienteRepository;
import io.bootify.spring_batch_project.cuenta.domain.Cuenta;
import io.bootify.spring_batch_project.cuenta.model.CuentaDTO;
import io.bootify.spring_batch_project.cuenta.repos.CuentaRepository;
import io.bootify.spring_batch_project.transaccion.domain.Transaccion;
import io.bootify.spring_batch_project.transaccion.repos.TransaccionRepository;
import io.bootify.spring_batch_project.util.NotFoundException;
import io.bootify.spring_batch_project.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final TransaccionRepository transaccionRepository;

    public CuentaService(final CuentaRepository cuentaRepository,
            final ClienteRepository clienteRepository,
            final TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public List<CuentaDTO> findAll() {
        final List<Cuenta> cuentas = cuentaRepository.findAll(Sort.by("id"));
        return cuentas.stream()
                .map(cuenta -> mapToDTO(cuenta, new CuentaDTO()))
                .toList();
    }

    public CuentaDTO get(final Integer id) {
        return cuentaRepository.findById(id)
                .map(cuenta -> mapToDTO(cuenta, new CuentaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CuentaDTO cuentaDTO) {
        final Cuenta cuenta = new Cuenta();
        mapToEntity(cuentaDTO, cuenta);
        return cuentaRepository.save(cuenta).getId();
    }

    public void update(final Integer id, final CuentaDTO cuentaDTO) {
        final Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cuentaDTO, cuenta);
        cuentaRepository.save(cuenta);
    }

    public void delete(final Integer id) {
        cuentaRepository.deleteById(id);
    }

    private CuentaDTO mapToDTO(final Cuenta cuenta, final CuentaDTO cuentaDTO) {
        cuentaDTO.setId(cuenta.getId());
        cuentaDTO.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDTO.setSaldo(cuenta.getSaldo());
        cuentaDTO.setCliente(cuenta.getCliente() == null ? null : cuenta.getCliente().getId());
        return cuentaDTO;
    }

    private Cuenta mapToEntity(final CuentaDTO cuentaDTO, final Cuenta cuenta) {
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setSaldo(cuentaDTO.getSaldo());
        final Cliente cliente = cuentaDTO.getCliente() == null ? null : clienteRepository.findById(cuentaDTO.getCliente())
                .orElseThrow(() -> new NotFoundException("cliente not found"));
        cuenta.setCliente(cliente);
        return cuenta;
    }

    public String getReferencedWarning(final Integer id) {
        final Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Transaccion cuentaTransaccion = transaccionRepository.findFirstByCuenta(cuenta);
        if (cuentaTransaccion != null) {
            return WebUtils.getMessage("cuenta.transaccion.cuenta.referenced", cuentaTransaccion.getId());
        }
        return null;
    }

}
