package io.bootify.spring_batch_project.cliente.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClienteDTO {

    private Integer id;

    @Size(max = 100)
    private String nombre;

    @Size(max = 100)
    private String email;

    @Size(max = 15)
    private String telefono;

    @Size(max = 255)
    private String direccionEnvio;

}
