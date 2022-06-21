package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.ErrorControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/accessdenied")
public class AccessDeniedController
{
    @GetMapping
    public String getPage( HttpServletRequest request, Model model )
    {
        return "accessdenied";
    }
}
