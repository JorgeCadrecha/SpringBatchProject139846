package io.bootify.spring_batch_project.cuenta.repos;

import io.bootify.spring_batch_project.cliente.domain.Cliente;
import io.bootify.spring_batch_project.cuenta.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {

    Cuenta findFirstByCliente(Cliente cliente);

}
