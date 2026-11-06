package org.rewear.controller;

import jakarta.servlet.http.HttpSession;
import org.rewear.model.ExchangeRequest;
import org.rewear.model.Listing;
import org.rewear.model.User;
import org.rewear.service.ExchangeRequestService;
import org.rewear.service.ListingService;
import org.rewear.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final ListingService listingService;
    private final ExchangeRequestService exchangeRequestService;

    public DashboardController(UserService userService,
                               ListingService listingService,
                               ExchangeRequestService exchangeRequestService) {
        this.userService = userService;
        this.listingService = listingService;
        this.exchangeRequestService = exchangeRequestService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "tab", defaultValue = "wardrobe") String tab,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("flashMessage", "Please sign in to access your dashboard.");
            redirectAttributes.addFlashAttribute("flashCategory", "warning");
            return "redirect:/login";
        }

        User user = userService.findById(userId).orElse(null);
        if (user == null || !user.isActive()) {
            session.invalidate();
            return "redirect:/login";
        }

        List<Listing> userListings = listingService.getListingsByOwner(user);
        List<ExchangeRequest> receivedRequests = exchangeRequestService.getReceivedRequests(user);
        List<ExchangeRequest> sentRequests = exchangeRequestService.getSentRequests(user);

        model.addAttribute("activeTab", tab);
        model.addAttribute("userListings", userListings);
        model.addAttribute("receivedRequests", receivedRequests);
        model.addAttribute("sentRequests", sentRequests);
        model.addAttribute("impact", user.getImpactStats());

        return "dashboard";
    }
}
