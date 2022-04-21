package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;


public class MyErrorController implements ErrorController
{
    @GetMapping("/error")
    public String handleError()
    {
        return "errorpage";
    }
}
