package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.ControllerAdvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ConstraintViolationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleException(HttpServletRequest request
            , ConstraintViolationException exception
            , RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error_message", exception.getMessage());
        return "redirect:/viewfiles/" + request.getParameter("path");
    }
}
