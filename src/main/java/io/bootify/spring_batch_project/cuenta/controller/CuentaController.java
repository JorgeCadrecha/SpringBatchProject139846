package io.bootify.spring_batch_project.cuenta.controller;

import io.bootify.spring_batch_project.cliente.domain.Cliente;
import io.bootify.spring_batch_project.cliente.repos.ClienteRepository;
import io.bootify.spring_batch_project.cuenta.model.CuentaDTO;
import io.bootify.spring_batch_project.cuenta.service.CuentaService;
import io.bootify.spring_batch_project.util.CustomCollectors;
import io.bootify.spring_batch_project.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final ClienteRepository clienteRepository;

    public CuentaController(final CuentaService cuentaService,
            final ClienteRepository clienteRepository) {
        this.cuentaService = cuentaService;
        this.clienteRepository = clienteRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("clienteValues", clienteRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cliente::getId, Cliente::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cuentas", cuentaService.findAll());
        return "cuenta/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("cuenta") final CuentaDTO cuentaDTO) {
        return "cuenta/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("cuenta") @Valid final CuentaDTO cuentaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cuenta/add";
        }
        cuentaService.create(cuentaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cuenta.create.success"));
        return "redirect:/cuentas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("cuenta", cuentaService.get(id));
        return "cuenta/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("cuenta") @Valid final CuentaDTO cuentaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cuenta/edit";
        }
        cuentaService.update(id, cuentaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cuenta.update.success"));
        return "redirect:/cuentas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = cuentaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            cuentaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("cuenta.delete.success"));
        }
        return "redirect:/cuentas";
    }

}
