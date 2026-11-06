package org.rewear.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 25)
    private String phone;

    @Column(length = 120)
    private String location = "Green Community";

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 255)
    private String avatar;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Listing> listings = new ArrayList<>();

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExchangeRequest> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExchangeRequest> receivedRequests = new ArrayList<>();

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportsSubmitted = new ArrayList<>();

    public User() {}

    public User(String name, String email, String passwordHash, String phone, String location, String bio, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.location = location != null ? location : "Green Community";
        this.bio = bio;
        this.isAdmin = isAdmin;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Listing> getListings() { return listings; }
    public void setListings(List<Listing> listings) { this.listings = listings; }

    public List<ExchangeRequest> getSentRequests() { return sentRequests; }
    public void setSentRequests(List<ExchangeRequest> sentRequests) { this.sentRequests = sentRequests; }

    public List<ExchangeRequest> getReceivedRequests() { return receivedRequests; }
    public void setReceivedRequests(List<ExchangeRequest> receivedRequests) { this.receivedRequests = receivedRequests; }

    public List<Report> getReportsSubmitted() { return reportsSubmitted; }
    public void setReportsSubmitted(List<Report> reportsSubmitted) { this.reportsSubmitted = reportsSubmitted; }

    // Computed / Helper methods
    public int getTotalItemsListed() {
        return listings != null ? listings.size() : 0;
    }

    public int getCompletedCount() {
        int count = 0;
        if (receivedRequests != null) {
            for (ExchangeRequest req : receivedRequests) {
                if ("Completed".equalsIgnoreCase(req.getStatus())) count++;
            }
        }
        if (sentRequests != null) {
            for (ExchangeRequest req : sentRequests) {
                if ("Completed".equalsIgnoreCase(req.getStatus())) count++;
            }
        }
        return count;
    }

    public Map<String, Object> getImpactStats() {
        int items = getCompletedCount();
        int totalItems = Math.max(items, getTotalItemsListed());
        Map<String, Object> stats = new HashMap<>();
        stats.put("items_diverted", totalItems);
        stats.put("textile_kg", Math.round(totalItems * 0.8 * 10.0) / 10.0);
        stats.put("water_liters", totalItems * 2700);
        stats.put("co2_kg", Math.round(totalItems * 10.0 * 10.0) / 10.0);
        return stats;
    }
}
