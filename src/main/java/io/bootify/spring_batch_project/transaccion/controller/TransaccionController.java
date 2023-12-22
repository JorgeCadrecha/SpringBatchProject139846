package io.bootify.spring_batch_project.transaccion.controller;

import io.bootify.spring_batch_project.cuenta.domain.Cuenta;
import io.bootify.spring_batch_project.cuenta.repos.CuentaRepository;
import io.bootify.spring_batch_project.transaccion.model.TransaccionDTO;
import io.bootify.spring_batch_project.transaccion.service.TransaccionService;
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
@RequestMapping("/transaccions")
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final CuentaRepository cuentaRepository;

    public TransaccionController(final TransaccionService transaccionService,
            final CuentaRepository cuentaRepository) {
        this.transaccionService = transaccionService;
        this.cuentaRepository = cuentaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("cuentaValues", cuentaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cuenta::getId, Cuenta::getTipoCuenta)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("transaccions", transaccionService.findAll());
        return "transaccion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("transaccion") final TransaccionDTO transaccionDTO) {
        return "transaccion/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("transaccion") @Valid final TransaccionDTO transaccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "transaccion/add";
        }
        transaccionService.create(transaccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("transaccion.create.success"));
        return "redirect:/transaccions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("transaccion", transaccionService.get(id));
        return "transaccion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("transaccion") @Valid final TransaccionDTO transaccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "transaccion/edit";
        }
        transaccionService.update(id, transaccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("transaccion.update.success"));
        return "redirect:/transaccions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        transaccionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("transaccion.delete.success"));
        return "redirect:/transaccions";
    }

}
