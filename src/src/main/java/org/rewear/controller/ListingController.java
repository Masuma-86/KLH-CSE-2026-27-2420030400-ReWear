package org.rewear.controller;

import jakarta.servlet.http.HttpSession;
import org.rewear.model.Listing;
import org.rewear.model.User;
import org.rewear.service.ListingService;
import org.rewear.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;
    private final UserService userService;

    public ListingController(ListingService listingService, UserService userService) {
        this.listingService = listingService;
        this.userService = userService;
    }

    private User getSessionUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        return userId != null ? userService.findById(userId).orElse(null) : null;
    }

    @GetMapping
    public String browseListings(@RequestParam(value = "q", required = false) String query,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "size", required = false) String size,
                                 @RequestParam(value = "condition", required = false) String condition,
                                 @RequestParam(value = "type", required = false) String type,
                                 Model model) {
        List<Listing> listings = listingService.search(query, category, size, condition, type);
        model.addAttribute("listings", listings);
        model.addAttribute("query", query);
        model.addAttribute("category", category);
        model.addAttribute("size", size);
        model.addAttribute("condition", condition);
        model.addAttribute("type", type);
        return "listings";
    }

    @GetMapping("/create")
    public String createListingPage(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        User user = getSessionUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("flashMessage", "Please sign in to list clothing items.");
            redirectAttributes.addFlashAttribute("flashCategory", "warning");
            return "redirect:/login";
        }
        return "create_listing";
    }

    @PostMapping("/create")
    public String handleCreateListing(@RequestParam("title") String title,
                                      @RequestParam("description") String description,
                                      @RequestParam("category") String category,
                                      @RequestParam("size") String size,
                                      @RequestParam("condition") String condition,
                                      @RequestParam("listing_type") String listingType,
                                      @RequestParam(value = "brand", required = false) String brand,
                                      @RequestParam(value = "color", required = false) String color,
                                      @RequestParam(value = "material", required = false) String material,
                                      @RequestParam(value = "image_url", required = false) String imageUrl,
                                      @RequestParam(value = "image_file", required = false) MultipartFile imageFile,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Listing created = listingService.createListing(
                    user, title, description, category, size, condition,
                    listingType, brand, color, material, imageUrl, imageFile
            );
            redirectAttributes.addFlashAttribute("flashMessage", "Garment listed successfully! Available to the circular community.");
            redirectAttributes.addFlashAttribute("flashCategory", "success");
            return "redirect:/listings/" + created.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Error listing clothing: " + e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
            return "redirect:/listings/create";
        }
    }

    @GetMapping("/{id}")
    public String listingDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        Optional<Listing> listingOpt = listingService.findById(id);
        if (listingOpt.isEmpty()) {
            return "redirect:/listings";
        }

        Listing listing = listingOpt.get();
        listingService.incrementViews(id);

        User currentUser = getSessionUser(session);
        List<Listing> userWardrobe = List.of();
        if (currentUser != null) {
            userWardrobe = listingService.getListingsByOwner(currentUser)
                    .stream()
                    .filter(Listing::isAvailable)
                    .toList();
        }

        model.addAttribute("listing", listing);
        model.addAttribute("userWardrobe", userWardrobe);
        return "listing_detail";
    }

    @GetMapping("/{id}/edit")
    public String editListingPage(@PathVariable("id") Long id, HttpSession session,
                                  RedirectAttributes redirectAttributes, Model model) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Listing> listingOpt = listingService.findById(id);
        if (listingOpt.isEmpty()) {
            return "redirect:/listings";
        }

        Listing listing = listingOpt.get();
        if (!listing.getOwner().getId().equals(user.getId()) && !user.isAdmin()) {
            redirectAttributes.addFlashAttribute("flashMessage", "You are not authorized to edit this listing.");
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
            return "redirect:/listings/" + id;
        }

        model.addAttribute("listing", listing);
        return "edit_listing";
    }

    @PostMapping("/{id}/edit")
    public String handleEditListing(@PathVariable("id") Long id,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam("category") String category,
                                    @RequestParam("size") String size,
                                    @RequestParam("condition") String condition,
                                    @RequestParam("listing_type") String listingType,
                                    @RequestParam(value = "brand", required = false) String brand,
                                    @RequestParam(value = "color", required = false) String color,
                                    @RequestParam(value = "material", required = false) String material,
                                    @RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "image_url", required = false) String imageUrl,
                                    @RequestParam(value = "image_file", required = false) MultipartFile imageFile,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            listingService.updateListing(
                    id, user, title, description, category, size, condition,
                    listingType, brand, color, material, status, imageUrl, imageFile
            );
            redirectAttributes.addFlashAttribute("flashMessage", "Listing updated successfully.");
            redirectAttributes.addFlashAttribute("flashCategory", "success");
            return "redirect:/listings/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Error updating listing: " + e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
            return "redirect:/listings/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String handleDeleteListing(@PathVariable("id") Long id, HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            listingService.deleteListing(id, user);
            redirectAttributes.addFlashAttribute("flashMessage", "Clothing item removed from your wardrobe.");
            redirectAttributes.addFlashAttribute("flashCategory", "info");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Error deleting listing: " + e.getMessage());
            redirectAttributes.addFlashAttribute("flashCategory", "danger");
            return "redirect:/listings/" + id;
        }
    }
}
