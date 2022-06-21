package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.FileControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlingController
{
    private final Logger logger = LoggerFactory.getLogger( ExceptionHandlingController.class );

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleError( HttpServletRequest request, Exception exception )
    {
        logger.error( "request " + request.getRequestURI() + " raised exception " + exception );
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject( "exception", exception );
        modelAndView.addObject( "url", request.getRequestURI() );
        modelAndView.setViewName( "errorpage" );
        return modelAndView;
    }

}
