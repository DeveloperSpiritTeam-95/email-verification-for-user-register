package email.verified.com.controller;

import email.verified.com.entity.Users;
import email.verified.com.service.UserServices;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AppController {

    private final UserServices userServices;

    @GetMapping("/home")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Users user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        userServices.register(user, getSiteURL(request));
        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<Users> listUsers = userServices.listAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userServices.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}
