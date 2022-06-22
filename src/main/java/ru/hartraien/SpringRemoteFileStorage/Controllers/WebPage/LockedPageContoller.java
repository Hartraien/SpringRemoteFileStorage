package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lockedpage")
public class LockedPageContoller
{
    @GetMapping
    public String getPage()
    {
        return "lockedpage";
    }
}