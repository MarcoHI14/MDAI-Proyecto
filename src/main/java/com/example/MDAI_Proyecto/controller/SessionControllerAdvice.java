package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Usuario;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@ControllerAdvice
public class SessionControllerAdvice {

    @ModelAttribute("sessionUsuario")
    public Usuario sessionUsuario() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            Object u = attrs.getAttribute("usuario", RequestAttributes.SCOPE_SESSION);
            if (u instanceof Usuario) {
                return (Usuario) u;
            }
        }
        return null;
    }
}

