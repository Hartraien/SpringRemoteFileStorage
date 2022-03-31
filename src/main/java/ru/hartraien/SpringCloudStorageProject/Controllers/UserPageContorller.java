package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.DAOs.UserDAO;
import ru.hartraien.SpringCloudStorageProject.POJO.UserEntity;

import java.util.List;

@Controller
@RequestMapping("/userlist")
public class UserPageContorller
{
    private final UserDAO userDAO;

    @Autowired
    public UserPageContorller( UserDAO userDAO )
    {
        this.userDAO = userDAO;
    }

    @GetMapping("")
    public String getPage( Model model)
    {
        List<UserEntity> users = userDAO.getAllUsers();

        model.addAttribute( "userList", users );

        return "userlist";
    }
}
