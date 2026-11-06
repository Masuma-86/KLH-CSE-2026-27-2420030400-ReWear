package org.rewear.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.rewear.model.User;
import org.rewear.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId != null) {
            return userService.findById(userId).orElse(null);
        }
        return null;
    }

    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
