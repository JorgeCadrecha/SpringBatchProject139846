package io.bootify.spring_batch_project.cliente.repos;

import io.bootify.spring_batch_project.cliente.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
