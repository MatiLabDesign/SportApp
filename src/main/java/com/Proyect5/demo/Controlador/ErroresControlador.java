package com.Proyect5.demo.Controlador;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErroresControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView paginaError = new ModelAndView("error");
        String MensajeError = "";
        int CodigoError = ObtenerCodigoError(httpRequest);

        switch (CodigoError) {
            case 400: {
                MensajeError = "El recurso solicitado no existe.";
                break;
            }
            case 403: {
                MensajeError = "No tiene permisos para acceder al recurso.";
                break;
            }
            case 401: {
                MensajeError = "No se encuentra autorizado.";
                break;
            }
            case 404: {
                MensajeError = "El recurso solicitado no fue encontrado.";
                break;
            }
            case 500: {
                MensajeError = "Ocurrió un error interno.";
                break;
            }
        }
        paginaError.addObject("codigo", CodigoError);
        paginaError.addObject("mensaje", MensajeError);
        return paginaError;
    }

    private int ObtenerCodigoError(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    public String paginaDeError() {
        return "/error.html";
    }
}
