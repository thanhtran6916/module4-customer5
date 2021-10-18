package com.customer.controller;

import com.customer.model.Customer;
import com.customer.model.CustomerForm;
import com.customer.service.product.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @Value("${upload-file}")
    private String uploadFile;

    @GetMapping
    public ModelAndView showList(@RequestParam("q") Optional<String> q) {
        List<Customer> customers = null;
        if (q.isPresent()) {
            String name = q.get();
            customers = customerService.getByName(name);
        } else {
            customers = customerService.getAll();
        }
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreate() {
        CustomerForm customerForm = new CustomerForm();
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customerForm", customerForm);
        return modelAndView;
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute("customerForm") CustomerForm customerForm) {
        MultipartFile file = customerForm.getImage();
        Customer customer = new Customer();
        customer.setName(customerForm.getName());
        customer.setAddress(customerForm.getAddress());
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            customer.setImage(fileName);
            try {
                byte[] bytes = file.getBytes();
                File file1 = new File(uploadFile + fileName);
                FileCopyUtils.copy(bytes, file1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customerService.save(customer);
        return "redirect:/customer";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showCreate(@PathVariable("id") Long id) {
        Customer customer = customerService.getById(id);
        ModelAndView modelAndView = new ModelAndView("/customer/edit");
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String createCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.save(customer);
        return "redirect:/customer";
    }

    @GetMapping("/info/{id}")
    public String showInfo(Model model, @PathVariable("id") Long id) {
        Customer customer = customerService.getById(id);
        model.addAttribute("customer", customer);
        return "/customer/info";
    }

    @GetMapping("/delete/{id}")
    public String showDelete(Model model, @PathVariable("id") Long id) {
        Customer customer =  customerService.getById(id);
        model.addAttribute("customer", customer);
        return "/customer/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        customerService.delete(id);
        return "redirect:/customer";
    }


}
