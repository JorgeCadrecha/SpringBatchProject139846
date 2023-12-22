package io.bootify.spring_batch_project.cuenta.rest;

import io.bootify.spring_batch_project.cuenta.model.CuentaDTO;
import io.bootify.spring_batch_project.cuenta.service.CuentaService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/cuentas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CuentaResource {

    private final CuentaService cuentaService;

    public CuentaResource(final CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> getAllCuentas() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> getCuenta(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(cuentaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createCuenta(@RequestBody @Valid final CuentaDTO cuentaDTO) {
        final Integer createdId = cuentaService.create(cuentaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateCuenta(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final CuentaDTO cuentaDTO) {
        cuentaService.update(id, cuentaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCuenta(@PathVariable(name = "id") final Integer id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
