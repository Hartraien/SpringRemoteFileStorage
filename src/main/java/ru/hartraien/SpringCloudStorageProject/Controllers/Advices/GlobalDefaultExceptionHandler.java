package ru.hartraien.SpringCloudStorageProject.Controllers.Advices;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalDefaultExceptionHandler
{
    private final String errorPage = "error";

    /*public ModelAndView defaultErrorHandler( HttpServletRequest request, Exception e ) throws Exception
    {

    }*/
}
