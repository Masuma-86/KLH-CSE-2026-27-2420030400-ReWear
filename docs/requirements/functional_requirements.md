# Functional Requirements
## ReWear – A Community-Based Clothing Exchange and Reuse Platform

This document lists the expected functional behavior of ReWear. Requirement IDs can be used for implementation and testing.

| ID | Requirement | Expected behavior | Priority |
|---|---|---|---|
| FR-01 | User registration | The system shall allow a new user to create an account using required registration details. | High |
| FR-02 | User login | The system shall authenticate a registered user and handle invalid login attempts with a clear message. | High |
| FR-03 | Logout | The system shall allow a signed-in user to end their session. | High |
| FR-04 | Create listing | A user shall be able to publish a clothing item with its description, category, size, condition, and availability. | High |
| FR-05 | Upload image | A user shall be able to attach an image to a clothing listing, subject to file validation. | High |
| FR-06 | Manage own listings | A user shall be able to view and, where permitted, edit or remove their own listings. | High |
| FR-07 | Browse listings | A user shall be able to view available clothing listings and open item details. | High |
| FR-08 | Search listings | A user shall be able to search listings using supported text fields. | High |
| FR-09 | Filter listings | A user shall be able to filter by supported attributes such as category, size, condition, and availability. | High |
| FR-10 | Submit exchange request | A user shall be able to submit a request for an available item or propose an exchange. | High |
| FR-11 | Submit donation request | A user shall be able to request an item marked as available for donation. | High |
| FR-12 | Review requests | An item owner shall be able to view requests received for their listings. | High |
| FR-13 | Respond to request | An item owner shall be able to accept or reject a request. | High |
| FR-14 | Track request status | Users shall be able to view the current status of their sent and received requests. | High |
| FR-15 | Complete transaction | Authorized users shall be able to mark an exchange or donation as completed when the handover is complete. | Medium |
| FR-16 | Update availability | On completion, the system shall update the item’s availability to prevent further requests for an unavailable item. | High |
| FR-17 | User dashboard | The system shall display a user’s listings, sent requests, received requests, and their statuses. | Medium |
| FR-18 | Report content | A user shall be able to report inappropriate listings or activity if reporting is included in the implementation. | Medium |
| FR-19 | Admin user management | An administrator shall be able to review and manage user accounts within assigned permissions. | High |
| FR-20 | Admin listing moderation | An administrator shall be able to review listings and take moderation actions. | High |
| FR-21 | Admin report handling | An administrator shall be able to review reported content and record an action or status. | Medium |
| FR-22 | Input validation | The system shall validate required fields and reject invalid or incomplete submissions. | High |

## Business Rules

1. Only authenticated users may perform actions that require an account.
2. Users may modify or remove only listings they own, unless an administrator has authorized moderation privileges.
3. Requests should be accepted only for items currently available.
4. A request must have a recognizable status, such as **Pending**, **Accepted**, **Rejected**, or **Completed**.
5. A completed item should no longer be shown as available for new requests.
6. Administrative functions must be accessible only to authorized administrators.
7. Exact status transitions and whether users can cancel requests should be finalized during implementation.

## Suggested Test Coverage

- Valid and invalid registration/login.
- Missing required listing fields.
- Valid and invalid image uploads.
- Search and filtering combinations.
- Request submission for available and unavailable items.
- Accept/reject status changes.
- Availability update after completion.
- Attempts to access another user's listing-management actions.
- Administrator-only access to moderation features.

**Implementation note:** These are target requirements. Mark a requirement as complete only after it has been implemented and tested.
