package org.rewear.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    private String category; // Men, Women, Kids, Unisex, Outerwear, Footwear, Accessories

    @Column(nullable = false, length = 30)
    private String size; // XS, S, M, L, XL, XXL, Free Size, etc.

    @Column(nullable = false, length = 40)
    private String condition; // New with tags, Like New, Gently Used, Well Worn

    @Column(name = "listing_type", nullable = false, length = 30)
    private String listingType; // Exchange, Donation, Both

    @Column(length = 100)
    private String brand = "Unbranded";

    @Column(length = 50)
    private String color;

    @Column(length = 80)
    private String material;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(nullable = false, length = 30)
    private String status = "Available"; // Available, Pending, Exchanged, Donated, Removed

    @Column(nullable = false)
    private int views = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExchangeRequest> requests = new ArrayList<>();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public Listing() {}

    public Listing(User owner, String title, String description, String category, String size,
                   String condition, String listingType, String brand, String color, String material,
                   String imageUrl, String status, int views) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.category = category;
        this.size = size;
        this.condition = condition;
        this.listingType = listingType;
        this.brand = (brand != null && !brand.isBlank()) ? brand : "Unbranded";
        this.color = color;
        this.material = material;
        this.imageUrl = imageUrl;
        this.status = status != null ? status : "Available";
        this.views = views;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getListingType() { return listingType; }
    public void setListingType(String listingType) { this.listingType = listingType; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getImageUrl() {
        if (imageUrl != null && !imageUrl.isBlank()) return imageUrl;
        return "/static/img/placeholder_cloth.webp";
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<ExchangeRequest> getRequests() { return requests; }
    public void setRequests(List<ExchangeRequest> requests) { this.requests = requests; }

    public List<Report> getReports() { return reports; }
    public void setReports(List<Report> reports) { this.reports = reports; }

    // Helpers
    public boolean isAvailable() { return "Available".equalsIgnoreCase(status); }
    public boolean isDonation() { return "Donation".equalsIgnoreCase(listingType) || "Both".equalsIgnoreCase(listingType); }
    public boolean isExchange() { return "Exchange".equalsIgnoreCase(listingType) || "Both".equalsIgnoreCase(listingType); }
}
