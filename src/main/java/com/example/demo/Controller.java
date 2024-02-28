package com.example.demo;

import com.example.demo.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {

    private final ContactService contactService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("contacts", contactService.findAll());
        return "index";
    }

    @GetMapping("/contact/create")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new Contact());

        return "create";
    }

    @PostMapping("/contact/create")
    public String createContact(@ModelAttribute Contact contact) {
        contactService.save(contact);

        return "redirect:/";
    }

    @GetMapping("/contact/edit/{id}")
    public String showEditFrom(@PathVariable Long id, Model model) {
        Contact contact = contactService.findById(id);
        if (contact != null) {
            model.addAttribute(contact);
            return "edit";
        }

        return "redirect:/";
    }

    @PostMapping("/contact/edit")
    public String editContact(@ModelAttribute Contact contact) {
        contactService.update(contact);


        return "redirect:/";
    }

    @GetMapping("/contact/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteById(id);
        return "redirect:/";
    }
}
