package com.pasfinal.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("")
    @CrossOrigin("*")
    public String welcomeMessage() {
        return "Sistema Tele Pizza 1.0";
    }
}
