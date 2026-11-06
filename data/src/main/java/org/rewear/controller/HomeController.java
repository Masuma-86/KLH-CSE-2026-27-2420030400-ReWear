package org.rewear.controller;

import org.rewear.model.Listing;
import org.rewear.repository.ExchangeRequestRepository;
import org.rewear.repository.ListingRepository;
import org.rewear.repository.UserRepository;
import org.rewear.service.ListingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final ListingService listingService;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;

    public HomeController(ListingService listingService,
                          ListingRepository listingRepository,
                          UserRepository userRepository,
                          ExchangeRequestRepository exchangeRequestRepository) {
        this.listingService = listingService;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.exchangeRequestRepository = exchangeRequestRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Listing> recentListings = listingService.getAvailableRecent();
        if (recentListings.size() > 8) {
            recentListings = recentListings.subList(0, 8);
        }

        long completedCount = exchangeRequestRepository.countByStatus("Completed");
        long totalListings = listingRepository.count();
        long totalUsers = userRepository.count();

        // Calculate global metrics
        long totalDiverted = completedCount > 0 ? completedCount : totalListings;
        double textileSavedKg = Math.round(totalDiverted * 0.8 * 10.0) / 10.0;
        long waterSavedL = totalDiverted * 2700;
        double co2AvoidedKg = Math.round(totalDiverted * 10.0 * 10.0) / 10.0;

        model.addAttribute("recentListings", recentListings);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("totalListings", totalListings);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("textileSavedKg", textileSavedKg);
        model.addAttribute("waterSavedL", waterSavedL);
        model.addAttribute("co2AvoidedKg", co2AvoidedKg);

        return "index";
    }

    @GetMapping("/impact")
    public String impact(Model model) {
        long totalCompleted = exchangeRequestRepository.countByStatus("Completed");
        long totalListings = listingRepository.count();
        long totalItems = Math.max(totalCompleted, totalListings);

        model.addAttribute("itemsSaved", totalItems);
        model.addAttribute("textileKg", Math.round(totalItems * 0.8 * 10.0) / 10.0);
        model.addAttribute("waterLiters", totalItems * 2700);
        model.addAttribute("co2Kg", Math.round(totalItems * 10.0 * 10.0) / 10.0);

        return "impact";
    }

    @GetMapping("/donations")
    public String donations(Model model) {
        List<Listing> freeDonations = listingService.getDonations();
        model.addAttribute("donations", freeDonations);
        return "donations";
    }

    @GetMapping("/calculator/data")
    @ResponseBody
    public Map<String, Object> calculatorData(@RequestParam(name = "garments", defaultValue = "5") int garments) {
        int count = Math.max(1, garments);
        Map<String, Object> data = new HashMap<>();
        data.put("garments", count);
        data.put("textile_saved_kg", Math.round(count * 0.8 * 10.0) / 10.0);
        data.put("water_saved_liters", count * 2700);
        data.put("co2_saved_kg", Math.round(count * 10.0 * 10.0) / 10.0);
        return data;
    }
}
