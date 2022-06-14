package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.UserControllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController extends AbstractUserController
{
    @GetMapping
    public String getPage( Authentication authentication )
    {
        if ( isAuthenticated( authentication ) )
            return "redirect:/userinfo";
        return "loginpage";
    }

}
