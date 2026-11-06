package org.rewear.service;

import org.rewear.model.Listing;
import org.rewear.model.User;
import org.rewear.repository.ListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ListingService {

    private final ListingRepository listingRepository;
    private final FileStorageService fileStorageService;

    public ListingService(ListingRepository listingRepository, FileStorageService fileStorageService) {
        this.listingRepository = listingRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Listing> getAvailableRecent() {
        return listingRepository.findAvailableRecent();
    }

    public List<Listing> getDonations() {
        return listingRepository.findDonations();
    }

    public List<Listing> search(String query, String category, String size, String condition, String type) {
        return listingRepository.searchListings(
                (query != null && !query.isBlank()) ? query.trim() : null,
                (category != null && !category.isBlank()) ? category.trim() : null,
                (size != null && !size.isBlank()) ? size.trim() : null,
                (condition != null && !condition.isBlank()) ? condition.trim() : null,
                (type != null && !type.isBlank()) ? type.trim() : null
        );
    }

    public List<Listing> getListingsByOwner(User owner) {
        return listingRepository.findByOwnerOrderByCreatedAtDesc(owner);
    }

    public Optional<Listing> findById(Long id) {
        return listingRepository.findById(id);
    }

    @Transactional
    public void incrementViews(Long id) {
        listingRepository.findById(id).ifPresent(l -> {
            l.setViews(l.getViews() + 1);
            listingRepository.save(l);
        });
    }

    @Transactional
    public Listing createListing(User owner, String title, String description, String category,
                                 String size, String condition, String listingType, String brand,
                                 String color, String material, String imageUrl, MultipartFile imageFile) {
        String finalImageUrl = imageUrl;
        if (imageFile != null && !imageFile.isEmpty()) {
            String uploaded = fileStorageService.storeFile(imageFile);
            if (uploaded != null) {
                finalImageUrl = uploaded;
            }
        }

        if (finalImageUrl == null || finalImageUrl.isBlank()) {
            finalImageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&auto=format&fit=crop&q=80";
        }

        Listing listing = new Listing(
                owner, title, description, category, size, condition,
                listingType, brand, color, material, finalImageUrl, "Available", 0
        );
        return listingRepository.save(listing);
    }

    @Transactional
    public Listing updateListing(Long id, User currentUser, String title, String description,
                                 String category, String size, String condition, String listingType,
                                 String brand, String color, String material, String status,
                                 String imageUrl, MultipartFile imageFile) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        if (!listing.getOwner().getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new SecurityException("Unauthorized to edit this listing");
        }

        listing.setTitle(title);
        listing.setDescription(description);
        listing.setCategory(category);
        listing.setSize(size);
        listing.setCondition(condition);
        listing.setListingType(listingType);
        listing.setBrand(brand != null ? brand : "Unbranded");
        listing.setColor(color);
        listing.setMaterial(material);
        if (status != null && !status.isBlank()) {
            listing.setStatus(status);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String uploaded = fileStorageService.storeFile(imageFile);
            if (uploaded != null) {
                listing.setImageUrl(uploaded);
            }
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            listing.setImageUrl(imageUrl);
        }

        listing.setUpdatedAt(LocalDateTime.now());
        return listingRepository.save(listing);
    }

    @Transactional
    public void deleteListing(Long id, User currentUser) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        if (!listing.getOwner().getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new SecurityException("Unauthorized to delete this listing");
        }

        listingRepository.delete(listing);
    }

    public long countAll() {
        return listingRepository.count();
    }

    public long countByStatus(String status) {
        return listingRepository.countByStatus(status);
    }
}
