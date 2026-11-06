package org.rewear.service;

import org.rewear.model.ExchangeRequest;
import org.rewear.model.Listing;
import org.rewear.model.Report;
import org.rewear.model.User;
import org.rewear.repository.ExchangeRequestRepository;
import org.rewear.repository.ListingRepository;
import org.rewear.repository.ReportRepository;
import org.rewear.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializerService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ReportRepository reportRepository;
    private final UserService userService;

    public DataInitializerService(UserRepository userRepository,
                                  ListingRepository listingRepository,
                                  ExchangeRequestRepository exchangeRequestRepository,
                                  ReportRepository reportRepository,
                                  UserService userService) {
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.reportRepository = reportRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("[ReWear Java] Database already seeded. " + userRepository.count() + " users present.");
            return;
        }

        System.out.println("[ReWear Java] Initializing demo data and circular fashion listings...");

        // 1. Users
        User admin = new User(
                "Elena Vance (Admin)",
                "admin@rewear.org",
                userService.hashPassword("admin123"),
                "+1 555-0100",
                "Seattle Eco-District",
                "ReWear Community Administrator and Circular Fashion Coordinator.",
                true
        );

        User sarah = new User(
                "Sarah Jenkins",
                "sarah@example.com",
                userService.hashPassword("user123"),
                "+1 555-0122",
                "Brooklyn Green Hub, NY",
                "Vintage enthusiast and slow-fashion collector. Believer in zero-waste wardrobes.",
                false
        );

        User david = new User(
                "David Kim",
                "david@example.com",
                userService.hashPassword("user123"),
                "+1 555-0144",
                "Mission District, San Francisco",
                "Downsizing my wardrobe to 30 timeless pieces. Happy to give away or swap gently worn gear.",
                false
        );

        User priya = new User(
                "Priya Sharma",
                "priya@example.com",
                userService.hashPassword("user123"),
                "+1 555-0177",
                "Austin Green District, TX",
                "Mending, textile upcycling, and sustainable community swaps.",
                false
        );

        userRepository.saveAll(List.of(admin, sarah, david, priya));

        // 2. Listings
        Listing l1 = new Listing(
                david, "Vintage 90s Indigo Denim Trucker Jacket",
                "Sturdy heavyweight 100% cotton denim with an authentic washed patina. No tears, brass buttons fully intact. Great boxy fit for layering over hoodies or flannels.",
                "Men", "L", "Like New", "Exchange", "Levi's", "Indigo Washed", "100% Raw Cotton Denim",
                "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&auto=format&fit=crop&q=80",
                "Available", 42
        );

        Listing l2 = new Listing(
                sarah, "Sage Linen Wrap Midi Dress",
                "Breathable European flax linen dress with adjustable tie waist and hidden side pockets. Ideal for warm weather, farmer's markets, or relaxed evenings.",
                "Women", "M", "New with tags", "Both", "Reformation", "Sage Green", "100% Organic Linen",
                "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=800&auto=format&fit=crop&q=80",
                "Available", 78
        );

        Listing l3 = new Listing(
                david, "Merino Wool Crewneck Knit Sweater",
                "Superfine Australian merino wool. Extremely soft against the skin, natural temperature regulating and odor-resistant. Hand-washed in wool shampoo.",
                "Men", "M", "Gently Used", "Exchange", "Uniqlo U", "Olive Green", "100% Extrafine Merino Wool",
                "https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=800&auto=format&fit=crop&q=80",
                "Available", 31
        );

        Listing l4 = new Listing(
                priya, "Kids Cozy Sherpa Fleece Pullover",
                "Warm and durable snap-neck pullover for active toddlers/kids. Outgrown by my 5-year-old. Clean, odor-free, freshly laundered with hypoallergenic detergent. Available completely free to any family in need!",
                "Kids", "S", "Gently Used", "Donation", "Patagonia", "Oatmeal & Navy", "100% Recycled Polyester",
                "https://images.unsplash.com/photo-1519238263530-99bdd11df2ea?w=800&auto=format&fit=crop&q=80",
                "Available", 59
        );

        Listing l5 = new Listing(
                sarah, "Classic Camel Double-Breasted Wool Trench",
                "Warm tailored overcoat with wide lapels and horn buttons. Lined in cupro satin. Perfect structure for transitional autumn and winter days.",
                "Outerwear", "M", "Like New", "Exchange", "Vintage London", "Warm Camel", "80% Wool, 20% Recycled Polyamide",
                "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=800&auto=format&fit=crop&q=80",
                "Available", 84
        );

        Listing l6 = new Listing(
                priya, "High-Rise Organic Cotton Chino Trousers",
                "Comfortable relaxed taper chinos with front pleats and deep slash pockets. Donating free to promote circular fashion in the community.",
                "Women", "S", "Like New", "Donation", "Everlane", "Bone Khaki", "98% Organic Cotton, 2% Elastane",
                "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=800&auto=format&fit=crop&q=80",
                "Available", 45
        );

        Listing l7 = new Listing(
                david, "Recycled Canvas Everyday Commuter Daypack",
                "Heavy-duty 16oz duck canvas backpack with padded laptop sleeve and brass hardware. Water-repellent finish. Donating to a student or commuter.",
                "Accessories", "Free Size", "Gently Used", "Donation", "Fjällräven", "Forest Green", "G-1000 HeavyDuty Canvas",
                "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&auto=format&fit=crop&q=80",
                "Available", 62
        );

        Listing l8 = new Listing(
                sarah, "Sustainable White Leather Sneakers (Campo)",
                "Ethically made low-top trainers crafted from ChromeFree leather with wild Amazonian rubber soles. Insole sanitized. Worn only a few times.",
                "Footwear", "40", "Like New", "Exchange", "Veja", "White & Forest Green", "Eco Leather & Wild Rubber",
                "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=800&auto=format&fit=crop&q=80",
                "Available", 95
        );

        Listing l9 = new Listing(
                priya, "Unisex Hand-Dyed Indigo Workwear Overshirt",
                "Natural fermented plant indigo dye on upcycled vintage deadstock cotton canvas. Beautiful marbling and heavy contrast stitching.",
                "Unisex", "L", "New with tags", "Both", "Artisanal Slow Craft", "Deep Indigo", "100% Upcycled Deadstock Cotton",
                "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=800&auto=format&fit=crop&q=80",
                "Available", 112
        );

        Listing l10 = new Listing(
                david, "Suspicious Commercial Fast-Fashion Mystery Bundle",
                "Sample listing flagged for commercial resale and incorrect fabric content tag.",
                "Men", "M", "Well Worn", "Exchange", "FastFashion Bulk", "Assorted", "Synthetic",
                "https://images.unsplash.com/photo-1489987707025-afc232f7ea0f?w=800&auto=format&fit=crop&q=80",
                "Available", 8
        );

        Listing completedItem = new Listing(
                sarah, "Fisherman Cable-Knit Wool Cardigan",
                "Warm heritage Irish wool cardigan exchanged last month with David.",
                "Women", "M", "Like New", "Exchange", "Aran Crafts", "Cream", "100% Pure New Wool",
                "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=800&auto=format&fit=crop&q=80",
                "Exchanged", 64
        );

        listingRepository.saveAll(List.of(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, completedItem));

        // 3. Requests
        ExchangeRequest req1 = new ExchangeRequest(
                l1, sarah, david, l2, "Exchange",
                "Hi David! I've been searching for an authentic vintage trucker jacket. I'd love to swap my unworn linen wrap dress or we can meet at the Green Park swap event!",
                "Pending"
        );
        req1.setHandoverNotes("Available weekday afternoons near Brooklyn Green Hub or postal swap.");

        ExchangeRequest req2 = new ExchangeRequest(
                l4, sarah, priya, null, "Donation",
                "Hello Priya! I run a local community clothing drive for young families. This jacket would be deeply appreciated by one of our neighborhood toddlers.",
                "Accepted"
        );
        req2.setHandoverNotes("Can pick up anytime from your porch box.");

        ExchangeRequest completedReq = new ExchangeRequest(
                completedItem, david, sarah, null, "Exchange",
                "Thanks Sarah, perfect swap!",
                "Completed"
        );
        completedReq.setHandoverNotes("Handover completed at Community Center.");

        exchangeRequestRepository.saveAll(List.of(req1, req2, completedReq));

        // 4. Sample Report
        Report report = new Report(
                l10, sarah, "Counterfeit",
                "This appears to be bulk commercial liquidation rather than genuine pre-owned community clothing. Labels do not match pictures.",
                "Pending"
        );
        reportRepository.save(report);

        System.out.println("[ReWear Java] Seed completed successfully!");
    }
}
