package io.bootify.spring_batch_project.cuenta.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CuentaDTO {

    private Integer id;

    @Size(max = 50)
    private String tipoCuenta;

    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "45.08")
    private BigDecimal saldo;

    private Integer cliente;

}
