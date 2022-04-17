package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

public class MyErrorController implements ErrorController
{
    @RequestMapping("/error")
    public String handleError()
    {
        return "errorpage";
    }
}
