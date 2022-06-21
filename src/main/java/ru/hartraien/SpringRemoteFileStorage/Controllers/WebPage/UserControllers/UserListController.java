package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.UserControllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hartraien.SpringRemoteFileStorage.Services.UserServicePackage.UserService;


@Controller
@RequestMapping("userlist")
public class UserListController
{
    private final UserService userService;

    public UserListController( UserService userService )
    {
        this.userService = userService;
    }

    @GetMapping("")
    public String getPartialList(
            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "100") int size,
            Model model
    )
    {
        if ( pageNumber < 0 )
            pageNumber = 0;
        if ( size <= 0 )
            size = 100;
        Pageable request = PageRequest.of( pageNumber, size );
        model.addAttribute( "users", userService.getAllUsersPaging( request ) );
        return "userlist";
    }
}
