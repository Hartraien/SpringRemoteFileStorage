package ru.hartraien.SpringRemoteFileStorage.Handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAccessDeniedHandler implements AccessDeniedHandler
{
    private static final Logger logger = LoggerFactory.getLogger( MyAccessDeniedHandler.class );

    @Override
    public void handle( HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException ) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth != null )
        {
            logger.warn( "User " + auth.getName() + " tried to access protected URL " + request.getRequestURI() + " Due to " + accessDeniedException.getMessage() );
        }
        else
        {
            logger.warn( "Anonymous user tried to access protected URL " + request.getRequestURI() + " Due to " + accessDeniedException.getMessage());
        }

        response.sendRedirect( request.getContextPath() + "/accessdenied" );
    }
}
