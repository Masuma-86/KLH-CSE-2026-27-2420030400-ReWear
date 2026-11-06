package org.rewear.service;

import org.rewear.model.ExchangeRequest;
import org.rewear.model.Listing;
import org.rewear.model.User;
import org.rewear.repository.ExchangeRequestRepository;
import org.rewear.repository.ListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRequestService {

    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ListingRepository listingRepository;

    public ExchangeRequestService(ExchangeRequestRepository exchangeRequestRepository,
                                  ListingRepository listingRepository) {
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.listingRepository = listingRepository;
    }

    public List<ExchangeRequest> getReceivedRequests(User owner) {
        return exchangeRequestRepository.findByOwnerOrderByCreatedAtDesc(owner);
    }

    public List<ExchangeRequest> getSentRequests(User requester) {
        return exchangeRequestRepository.findByRequesterOrderByCreatedAtDesc(requester);
    }

    public Optional<ExchangeRequest> findById(Long id) {
        return exchangeRequestRepository.findById(id);
    }

    @Transactional
    public ExchangeRequest createRequest(Long listingId, User requester, Long offeredItemId,
                                         String requestType, String message) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Target listing not found"));

        if (listing.getOwner().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("You cannot submit an exchange or donation request for your own clothing.");
        }

        if (!listing.isAvailable()) {
            throw new IllegalArgumentException("This item is no longer available.");
        }

        Listing offeredItem = null;
        if (offeredItemId != null) {
            offeredItem = listingRepository.findById(offeredItemId).orElse(null);
            if (offeredItem != null && !offeredItem.getOwner().getId().equals(requester.getId())) {
                throw new IllegalArgumentException("Offered item must belong to your wardrobe.");
            }
        }

        ExchangeRequest request = new ExchangeRequest(
                listing, requester, listing.getOwner(), offeredItem,
                requestType != null ? requestType : "Exchange", message, "Pending"
        );
        return exchangeRequestRepository.save(request);
    }

    @Transactional
    public ExchangeRequest updateStatus(Long requestId, String newStatus, String handoverNotes, User currentUser) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        boolean isOwner = request.getOwner().getId().equals(currentUser.getId());
        boolean isRequester = request.getRequester().getId().equals(currentUser.getId());

        if (!isOwner && !isRequester && !currentUser.isAdmin()) {
            throw new SecurityException("Unauthorized to modify this request");
        }

        request.setStatus(newStatus);
        if (handoverNotes != null && !handoverNotes.isBlank()) {
            request.setHandoverNotes(handoverNotes);
        }
        request.setUpdatedAt(LocalDateTime.now());
        return exchangeRequestRepository.save(request);
    }

    @Transactional
    public ExchangeRequest completeHandover(Long requestId, String handoverNotes, User currentUser) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        boolean isOwner = request.getOwner().getId().equals(currentUser.getId());
        boolean isRequester = request.getRequester().getId().equals(currentUser.getId());

        if (!isOwner && !isRequester && !currentUser.isAdmin()) {
            throw new SecurityException("Unauthorized to complete this request");
        }

        request.setStatus("Completed");
        if (handoverNotes != null && !handoverNotes.isBlank()) {
            request.setHandoverNotes(handoverNotes);
        }
        request.setUpdatedAt(LocalDateTime.now());

        // Update listing status automatically
        Listing target = request.getListing();
        if ("Donation".equalsIgnoreCase(request.getRequestType())) {
            target.setStatus("Donated");
        } else {
            target.setStatus("Exchanged");
        }
        listingRepository.save(target);

        // If an offered item was involved, mark it exchanged too
        if (request.getOfferedItem() != null) {
            Listing offered = request.getOfferedItem();
            offered.setStatus("Exchanged");
            listingRepository.save(offered);
        }

        return exchangeRequestRepository.save(request);
    }

    public long countCompleted() {
        return exchangeRequestRepository.countByStatus("Completed");
    }
}
