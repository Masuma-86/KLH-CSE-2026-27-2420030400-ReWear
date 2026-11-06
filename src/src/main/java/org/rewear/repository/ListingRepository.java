package org.rewear.repository;

import org.rewear.model.Listing;
import org.rewear.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findByStatusOrderByCreatedAtDesc(String status);

    List<Listing> findByOwnerOrderByCreatedAtDesc(User owner);

    @Query("SELECT l FROM Listing l WHERE l.status = 'Available' AND (l.listingType = 'Donation' OR l.listingType = 'Both') ORDER BY l.createdAt DESC")
    List<Listing> findDonations();

    @Query("SELECT l FROM Listing l WHERE l.status = 'Available' ORDER BY l.createdAt DESC")
    List<Listing> findAvailableRecent();

    @Query("SELECT l FROM Listing l WHERE l.status = 'Available' " +
           "AND (:category IS NULL OR :category = '' OR l.category = :category) " +
           "AND (:size IS NULL OR :size = '' OR l.size = :size) " +
           "AND (:condition IS NULL OR :condition = '' OR l.condition = :condition) " +
           "AND (:type IS NULL OR :type = '' OR l.listingType = :type OR l.listingType = 'Both') " +
           "AND (:query IS NULL OR :query = '' OR LOWER(l.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "     OR LOWER(l.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "     OR LOWER(l.brand) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY l.createdAt DESC")
    List<Listing> searchListings(@Param("query") String query,
                                 @Param("category") String category,
                                 @Param("size") String size,
                                 @Param("condition") String condition,
                                 @Param("type") String type);

    long countByStatus(String status);
}
