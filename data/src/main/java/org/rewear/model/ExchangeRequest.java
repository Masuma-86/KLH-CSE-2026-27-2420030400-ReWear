package org.rewear.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_requests")
public class ExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offered_item_id")
    private Listing offeredItem;

    @Column(name = "request_type", nullable = false, length = 30)
    private String requestType; // Exchange or Donation

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 30)
    private String status = "Pending"; // Pending, Accepted, Rejected, Completed, Cancelled

    @Column(name = "handover_notes", columnDefinition = "TEXT")
    private String handoverNotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ExchangeRequest() {}

    public ExchangeRequest(Listing listing, User requester, User owner, Listing offeredItem,
                           String requestType, String message, String status) {
        this.listing = listing;
        this.requester = requester;
        this.owner = owner;
        this.offeredItem = offeredItem;
        this.requestType = requestType;
        this.message = message;
        this.status = status != null ? status : "Pending";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Listing getListing() { return listing; }
    public void setListing(Listing listing) { this.listing = listing; }

    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public Listing getOfferedItem() { return offeredItem; }
    public void setOfferedItem(Listing offeredItem) { this.offeredItem = offeredItem; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getHandoverNotes() { return handoverNotes; }
    public void setHandoverNotes(String handoverNotes) { this.handoverNotes = handoverNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
