package io.bootify.spring_batch_project.transaccion.repos;

import io.bootify.spring_batch_project.cuenta.domain.Cuenta;
import io.bootify.spring_batch_project.transaccion.domain.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    Transaccion findFirstByCuenta(Cuenta cuenta);

}
