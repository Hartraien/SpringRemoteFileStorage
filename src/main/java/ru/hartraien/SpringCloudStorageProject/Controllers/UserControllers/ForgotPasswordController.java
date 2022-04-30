package ru.hartraien.SpringCloudStorageProject.Controllers.UserControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserServiceException;
import ru.hartraien.SpringCloudStorageProject.Utility.RandomStringProducer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/forgot_password")
public class ForgotPasswordController
{

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Autowired
    public ForgotPasswordController( JavaMailSender mailSender, UserService userService )
    {
        this.mailSender = mailSender;
        this.userService = userService;
    }


    @GetMapping
    public String getPage()
    {
        return "forgot_password";
    }

    @PostMapping
    public String processForgotPassword( HttpServletRequest request, Model model )
    {
        String email = request.getParameter( "email" );
        String token = new RandomStringProducer().getString( 30 );

        try
        {
            userService.updateResetPasswordToken( token, email );
            String resetPasswordLink = getSiteURL( request ) + "/reset_password?token=" + token;
            sendEmail( email, resetPasswordLink );
            model.addAttribute( "message", "We have sent a reset password link to your email. Please check." );
        }
        catch ( UserServiceException e )
        {
            model.addAttribute( "error", e.getMessage() );
        }
        catch ( MessagingException | UnsupportedEncodingException e )
        {
            model.addAttribute( "error", "Could not send email" );
        }
        return "forgot_password";
    }

    private void sendEmail( String email, String resetPasswordLink ) throws MessagingException, UnsupportedEncodingException
    {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper( message );

        helper.setFrom( "contact@shopme.com", "Shopme Support" );
        helper.setTo( email );

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject( subject );

        helper.setText( content, true );

        mailSender.send( message );
    }

    private String getSiteURL( HttpServletRequest request )
    {
        return request.getRequestURL().toString().replace( request.getServletPath(), "" );
    }
}
