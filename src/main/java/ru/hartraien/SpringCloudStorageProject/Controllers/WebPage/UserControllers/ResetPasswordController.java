package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.UserControllers;

import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Validators.PasswordConstraintValidator;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/reset_password")
public class ResetPasswordController {

    private final UserService userService;
    private final PasswordValidator validator;

    @Autowired
    public ResetPasswordController(UserService userService) {
        this.userService = userService;
        validator = new PasswordValidator(new PasswordConstraintValidator().getRules());
    }

    @GetMapping
    public String showResetPasswordForm(@Param(value = "token") String token, Model model, RedirectAttributes redirectAttributes) {
        UserEntity user = userService.findUserByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error_message", "Invalid Token");
            return "redirect:/";
        }

        return "reset_password";
    }

    @PostMapping
    public String processResetPassword(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        UserEntity user = userService.findUserByResetPasswordToken(token);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error_message", "Invalid Token");
        } else {
            RuleResult result = validate(password);
            if (result.isValid()) {
                userService.updatePassword(user, password);
                redirectAttributes.addFlashAttribute("message", "You have successfully changed your password.");
            } else
                redirectAttributes.addFlashAttribute("error_message",
                        "Could not change password due to: " + String.join(",", validator.getMessages(result)));

        }
        return "redirect:/";
    }

    private RuleResult validate(String password) {
        return validator.validate(new PasswordData(password));
    }
}
