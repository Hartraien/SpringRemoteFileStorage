package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hartraien.SpringCloudStorageProject.DAOs.UserDAO;

@Controller
@RequestMapping("/login")
public class LoginController
{
    private final UserDAO userDAO;

    @Autowired
    public LoginController( UserDAO userDAO )
    {
        this.userDAO = userDAO;
    }

    @GetMapping("")
    public String loginPage()
    {
        return "loginpage";
    }

    @PostMapping("")
    public String tryLogIn(
            @RequestParam String fusername,
            @RequestParam String fpassword,
            Model model)
    {
        String message = userDAO.checkUser(fusername, fpassword);

        model.addAttribute( "message", message );

        return "loginpage";
    }
}
