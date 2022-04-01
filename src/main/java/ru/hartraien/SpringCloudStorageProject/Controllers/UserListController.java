package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.Services.UserService;

@Controller
@RequestMapping("userlist")
public class UserListController
{
    private final UserService userService;

    public UserListController( UserService userService )
    {
        this.userService = userService;
    }

    @GetMapping
    public String getPage( Model model )
    {
        model.addAttribute( "users", userService.getAllUsers() );
        return "userlist";
    }
}
