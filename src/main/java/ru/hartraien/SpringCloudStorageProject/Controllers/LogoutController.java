package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/logout")
public class LogoutController
{
    @PostMapping
    @GetMapping
    public String getPage( HttpServletRequest request )
    {
        System.err.println( "Logged out using " + request.getMethod() );
        return "logoutpage";
    }
}
