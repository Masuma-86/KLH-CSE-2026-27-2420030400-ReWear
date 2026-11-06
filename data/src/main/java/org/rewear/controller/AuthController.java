package org.rewear.controller;

import jakarta.servlet.http.HttpSession;
import org.rewear.model.User;
import org.rewear.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("user_id") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              @RequestParam(value = "next", required = false) String next,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.authenticate(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("user_id", user.getId());
            redirectAttributes.addFlashAttribute("flashMessage", "Welcome back, " + user.getName() + "!");
            redirectAttributes.addFlashAttribute("flashCategory", "success");

            if (next != null && !next.isBlank() && !next.contains("://")) {
                return "redirect:" + next;
            }
            return user.isAdmin() ? "redirect:/admin" : "redirect:/dashboard";
        }

        redirectAttributes.addFlashAttribute("flashMessage", "Invalid email address or password.");
        redirectAttributes.addFlashAttribute("flashCategory", "danger");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("user_id") != null) {
            return "redirect:/dashboard";
        }
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam("name") String name,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam(value = "location", required = false) String location,
                                 @RequestParam(value = "bio", required = false) String bio,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.registerUser(name, email, password, phone, location, bio);
            session.setAttribute("user_id", user.getId());
            redirectAttributes.addFlashAttribute("flashMessage", "Account created successfully! Welcome to ReWear.");
            redirectAttributes.addFlashAttribute("flashCategory", "success");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("flashMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("flashMessage", "You have been signed out.");
        redirectAttributes.addFlashAttribute("flashCategory", "info");
        return "redirect:/";
    }
}
