package ru.hartraien.SpringCloudStorageProject.Controllers.Advices;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler
{
    private final String errorPage = "error";

    /*public ModelAndView defaultErrorHandler( HttpServletRequest request, Exception e ) throws Exception
    {

    }*/
}
