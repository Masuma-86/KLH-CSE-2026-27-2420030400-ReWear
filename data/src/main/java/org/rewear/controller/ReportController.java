package org.rewear.controller;

import jakarta.servlet.http.HttpSession;
import org.rewear.model.User;
import org.rewear.service.ReportService;
import org.rewear.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @PostMapping("/report/{listingId}")
    public String submitReport(@PathVariable("listingId") Long listingId,
                               @RequestParam("reason") String reason,
                               @RequestParam(value = "details", required = false) String details,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("flashMessage", "Please sign in to report a listing.");
            redirectAttributes.addFlashAttribute("flashCategory", "warning");
            return "redirect:/login";
        }

        User user = userService.findById(userId).orElse(null);
        if (user == null) return "redirect:/login";

        try {
            reportService.createReport(listingId, user, reason, details);
            redirectAttributes.addFlashAttribute("flashMessage", "Report submitted for moderator review. Thank you for keeping our community trustworthy.");
            redirectAttributes.addFlashAttribute("flashCategory", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Could not submit report: " + e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
        }

        return "redirect:/listings/" + listingId;
    }
}
