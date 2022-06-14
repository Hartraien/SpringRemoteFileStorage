package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.UserControllers;

import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Validators.PasswordConstraintValidator;

import java.util.List;

@Controller
@RequestMapping("/changepassword")
public class ChangePasswordController {

    private final UserService userService;
    private final PasswordValidator validator;

    @Autowired
    public ChangePasswordController(UserService userService) {
        this.userService = userService;
        validator = new PasswordValidator(new PasswordConstraintValidator().getRules());
    }

    @PostMapping
    public String changePassword(
            @RequestParam("new_password") String new_pass
            , @RequestParam("new_password_copy") String new_pass_copy
            , Authentication authentication
            , RedirectAttributes redirectAttributes) {
        String error = validate(new_pass, new_pass_copy);
        if (error.isBlank()) {
            userService.updatePassword(getCurrentUser(authentication), new_pass);
            redirectAttributes.addFlashAttribute("change_password_message", "Successfully changed password!");
        } else {
            redirectAttributes.addFlashAttribute("change_password_error", error);
        }
        return "redirect:/userinfo";
    }

    private String validate(String new_pass, String new_pass_copy) {
        String passwordEqualsMessage = new_pass.equals(new_pass_copy) ? "" : "Passwords do not match";
        RuleResult result = validator.validate(new PasswordData(new_pass));
        if (result.isValid())
            return passwordEqualsMessage;
        else {
            List<String> errors = validator.getMessages(result);
            if (!passwordEqualsMessage.isBlank())
                errors.add(passwordEqualsMessage);
            return String.join(", ", errors);
        }
    }

    private UserEntity getCurrentUser(Authentication authentication) {
        return userService.findByUsername(authentication.getName());
    }

}
