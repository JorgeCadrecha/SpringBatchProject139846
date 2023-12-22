package io.bootify.spring_batch_project.cliente.service;

import io.bootify.spring_batch_project.cliente.domain.Cliente;
import io.bootify.spring_batch_project.cliente.model.ClienteDTO;
import io.bootify.spring_batch_project.cliente.repos.ClienteRepository;
import io.bootify.spring_batch_project.cuenta.domain.Cuenta;
import io.bootify.spring_batch_project.cuenta.repos.CuentaRepository;
import io.bootify.spring_batch_project.util.NotFoundException;
import io.bootify.spring_batch_project.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public ClienteService(final ClienteRepository clienteRepository,
            final CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<ClienteDTO> findAll() {
        final List<Cliente> clientes = clienteRepository.findAll(Sort.by("id"));
        return clientes.stream()
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .toList();
    }

    public ClienteDTO get(final Integer id) {
        return clienteRepository.findById(id)
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ClienteDTO clienteDTO) {
        final Cliente cliente = new Cliente();
        mapToEntity(clienteDTO, cliente);
        return clienteRepository.save(cliente).getId();
    }

    public void update(final Integer id, final ClienteDTO clienteDTO) {
        final Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(clienteDTO, cliente);
        clienteRepository.save(cliente);
    }

    public void delete(final Integer id) {
        clienteRepository.deleteById(id);
    }

    private ClienteDTO mapToDTO(final Cliente cliente, final ClienteDTO clienteDTO) {
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setTelefono(cliente.getTelefono());
        clienteDTO.setDireccionEnvio(cliente.getDireccionEnvio());
        return clienteDTO;
    }

    private Cliente mapToEntity(final ClienteDTO clienteDTO, final Cliente cliente) {
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setDireccionEnvio(clienteDTO.getDireccionEnvio());
        return cliente;
    }

    public String getReferencedWarning(final Integer id) {
        final Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Cuenta clienteCuenta = cuentaRepository.findFirstByCliente(cliente);
        if (clienteCuenta != null) {
            return WebUtils.getMessage("cliente.cuenta.cliente.referenced", clienteCuenta.getId());
        }
        return null;
    }

}
