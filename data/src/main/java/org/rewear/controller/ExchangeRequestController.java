package org.rewear.controller;

import jakarta.servlet.http.HttpSession;
import org.rewear.model.User;
import org.rewear.service.ExchangeRequestService;
import org.rewear.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExchangeRequestController {

    private final ExchangeRequestService exchangeRequestService;
    private final UserService userService;

    public ExchangeRequestController(ExchangeRequestService exchangeRequestService,
                                     UserService userService) {
        this.exchangeRequestService = exchangeRequestService;
        this.userService = userService;
    }

    private User getSessionUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        return userId != null ? userService.findById(userId).orElse(null) : null;
    }

    @PostMapping("/listings/{id}/request")
    public String submitRequest(@PathVariable("id") Long listingId,
                                @RequestParam("request_type") String requestType,
                                @RequestParam("message") String message,
                                @RequestParam(value = "offered_item_id", required = false) Long offeredItemId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = getSessionUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("flashMessage", "Please sign in to send exchange or donation requests.");
            redirectAttributes.addFlashAttribute("flashCategory", "warning");
            return "redirect:/login";
        }

        try {
            exchangeRequestService.createRequest(listingId, user, offeredItemId, requestType, message);
            redirectAttributes.addFlashAttribute("flashMessage", "Your " + requestType.toLowerCase() + " proposal was submitted! The owner has been notified.");
            redirectAttributes.addFlashAttribute("flashCategory", "success");
            return "redirect:/dashboard?tab=sent";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
            return "redirect:/listings/" + listingId;
        }
    }

    @PostMapping("/requests/{id}/action")
    public String handleRequestAction(@PathVariable("id") Long requestId,
                                      @RequestParam("action") String action,
                                      @RequestParam(value = "handover_notes", required = false) String handoverNotes,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            String newStatus;
            if ("reject".equalsIgnoreCase(action)) {
                newStatus = "Rejected";
            } else if ("cancel".equalsIgnoreCase(action)) {
                newStatus = "Cancelled";
            } else {
                newStatus = "Accepted";
            }
            exchangeRequestService.updateStatus(requestId, newStatus, handoverNotes, user);
            redirectAttributes.addFlashAttribute("flashMessage", "Request status updated to " + newStatus + ".");
            redirectAttributes.addFlashAttribute("flashCategory", "info");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Error updating request: " + e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
        }

        return "redirect:/dashboard?tab=received";
    }

    @PostMapping("/requests/{id}/complete")
    public String handleCompleteHandover(@PathVariable("id") Long requestId,
                                         @RequestParam(value = "handover_notes", required = false) String handoverNotes,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            exchangeRequestService.completeHandover(requestId, handoverNotes, user);
            redirectAttributes.addFlashAttribute("flashMessage", "🎉 Handover marked as Completed! Impact score updated (+0.8kg textile saved, +2,700L water).");
            redirectAttributes.addFlashAttribute("flashCategory", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Error completing handover: " + e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
        }

        return "redirect:/dashboard?tab=received";
    }
}
