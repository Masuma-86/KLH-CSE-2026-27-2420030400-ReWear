package org.rewear.controller;

import jakarta.servlet.http.HttpSession;
import org.rewear.model.Report;
import org.rewear.model.User;
import org.rewear.repository.ExchangeRequestRepository;
import org.rewear.repository.ListingRepository;
import org.rewear.service.ListingService;
import org.rewear.service.ReportService;
import org.rewear.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final ListingService listingService;
    private final ListingRepository listingRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ReportService reportService;

    public AdminController(UserService userService,
                           ListingService listingService,
                           ListingRepository listingRepository,
                           ExchangeRequestRepository exchangeRequestRepository,
                           ReportService reportService) {
        this.userService = userService;
        this.listingService = listingService;
        this.listingRepository = listingRepository;
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.reportService = reportService;
    }

    private User getSessionAdmin(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId != null) {
            User user = userService.findById(userId).orElse(null);
            if (user != null && user.isAdmin()) {
                return user;
            }
        }
        return null;
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        User admin = getSessionAdmin(session);
        if (admin == null) {
            redirectAttributes.addFlashAttribute("flashMessage", "Administrator access required.");
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
            return "redirect:/login";
        }

        long totalUsers = userService.findAllUsers().size();
        long totalListings = listingRepository.count();
        long completedExchanges = exchangeRequestRepository.countByStatus("Completed");
        long pendingReports = reportService.countPending();
        double totalTextileSaved = Math.round(completedExchanges * 0.8 * 10.0) / 10.0;

        List<Report> reports = reportService.getAllReports();
        List<User> users = userService.findAllUsers();
        var allListings = listingRepository.findAll();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalListings", totalListings);
        model.addAttribute("completedExchanges", completedExchanges);
        model.addAttribute("pendingReports", pendingReports);
        model.addAttribute("totalTextileSaved", totalTextileSaved);
        model.addAttribute("reports", reports);
        model.addAttribute("users", users);
        model.addAttribute("allListings", allListings);

        return "admin";
    }

    @PostMapping("/admin/users/{id}/toggle")
    public String toggleUserStatus(@PathVariable("id") Long userId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User admin = getSessionAdmin(session);
        if (admin == null) return "redirect:/login";

        boolean success = userService.toggleActive(userId);
        if (success) {
            redirectAttributes.addFlashAttribute("flashMessage", "User account status toggled.");
            redirectAttributes.addFlashAttribute("flashCategory", "info");
        } else {
            redirectAttributes.addFlashAttribute("flashMessage", "Cannot modify admin user status.");
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/listings/{id}/delete")
    public String adminDeleteListing(@PathVariable("id") Long listingId,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User admin = getSessionAdmin(session);
        if (admin == null) return "redirect:/login";

        try {
            listingService.deleteListing(listingId, admin);
            redirectAttributes.addFlashAttribute("flashMessage", "Listing removed by administrator.");
            redirectAttributes.addFlashAttribute("flashCategory", "info");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Error deleting listing: " + e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/reports/{id}/dismiss")
    public String dismissReport(@PathVariable("id") Long reportId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User admin = getSessionAdmin(session);
        if (admin == null) return "redirect:/login";

        reportService.dismissReport(reportId);
        redirectAttributes.addFlashAttribute("flashMessage", "Report dismissed.");
        redirectAttributes.addFlashAttribute("flashCategory", "info");
        return "redirect:/admin";
    }
}
