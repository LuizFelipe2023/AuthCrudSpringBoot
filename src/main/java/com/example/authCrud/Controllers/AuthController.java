package com.example.authCrud.Controllers;

import com.example.authCrud.DTO.UserDTO;
import com.example.authCrud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login"; 
    }

    @GetMapping("/greeting")
    public String greeting() {
        return "auth/greeting";
    }

    @GetMapping("/error")
    public String error() {
        return "auth/error";
    }
}
