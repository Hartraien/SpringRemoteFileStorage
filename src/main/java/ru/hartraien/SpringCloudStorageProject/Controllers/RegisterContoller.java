package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hartraien.SpringCloudStorageProject.DAOs.RoleDAO;
import ru.hartraien.SpringCloudStorageProject.DAOs.UserDAO;

@Controller
@RequestMapping("/register")
public class RegisterContoller
{
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    @Autowired
    public RegisterContoller( UserDAO userDAO, RoleDAO roleDAO )
    {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @GetMapping("")
    public String getPage()
    {
        return "register";
    }

    @PostMapping("")
    public String registerUser(
            @RequestParam String fusername,
            @RequestParam String fpassword,
            Model model
    )
    {
        String message = userDAO.registerUser(fusername, fpassword, roleDAO.findRoleByName( "user" ));


        if(!message.isBlank())
        {
            model.addAttribute( "error_message", message );
            return "register";
        }
        else
        {
            return "mainpage";
        }
    }
}
