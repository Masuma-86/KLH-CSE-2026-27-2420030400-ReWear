package org.rewear.repository;

import org.rewear.model.ExchangeRequest;
import org.rewear.model.Listing;
import org.rewear.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    List<ExchangeRequest> findByOwnerOrderByCreatedAtDesc(User owner);
    List<ExchangeRequest> findByRequesterOrderByCreatedAtDesc(User requester);
    List<ExchangeRequest> findByListing(Listing listing);
    long countByStatus(String status);
}
