package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


public class MyErrorController implements ErrorController
{
    @GetMapping("/error")
    public String handleError()
    {
        return "errorpage";
    }
}
