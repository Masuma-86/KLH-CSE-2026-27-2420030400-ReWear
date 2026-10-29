# Software Requirements Specification (SRS)
## ReWear – A Community-Based Clothing Exchange and Reuse Platform

**Version:** 1.0  
**Project Type:** Academic web application  
**Course:** Adaptive Software Engineering  
**Supervisor:** Ms. G. Lavanya

---

## 1. Introduction

### 1.1 Purpose
This Software Requirements Specification defines the functional and non-functional requirements for ReWear. It describes the expected behavior, users, interfaces, constraints, and quality requirements of the platform.

### 1.2 Scope
ReWear is a web-based platform intended to help community members exchange or donate pre-owned clothing. Users can create accounts, publish clothing listings, browse and filter available items, submit exchange or donation requests, and track request status. Administrators can review listings, manage users, and handle reported content.

### 1.3 Intended Users
- **Registered user:** Creates listings, searches for clothing, and manages requests.
- **Administrator:** Oversees user activity, listings, and reports.
- **Visitor:** May access only public pages if guest browsing is supported by the implementation.

### 1.4 Definitions
| Term | Meaning |
|---|---|
| Listing | A clothing item published on ReWear |
| Exchange request | A request to obtain an item or propose an exchange |
| Donation request | A request for an item offered at no cost |
| Item owner | The user who published a listing |
| Administrator | A privileged user responsible for platform moderation |

## 2. Overall Description

### 2.1 Product Perspective
ReWear is a browser-based application. The proposed technology stack is HTML5, CSS3, and JavaScript for the frontend; Python Flask for the backend; and MySQL for structured data. Clothing images are stored as local uploads during development. Git and GitHub support version control, and Render is the intended deployment platform.

### 2.2 Product Functions
The system is intended to:
- Register users and authenticate account access.
- Create, view, edit, and remove clothing listings, subject to ownership and permissions.
- Store clothing details and image references.
- Search and filter available clothing.
- Submit, review, accept, reject, and track requests.
- Offer a donation option for items listed at no cost.
- Display a user dashboard with listings and request statuses.
- Provide administrative tools for user management, listing review, and reported content.
- Update item availability when a transaction is completed.

### 2.3 Operating Environment
- Modern desktop or mobile web browser.
- Python environment compatible with the selected Flask version.
- MySQL database server.
- Local filesystem storage for uploaded images during development.
- Render or another compatible hosting environment, subject to deployment configuration.

### 2.4 Constraints and Assumptions
- Users need a network connection to access a deployed instance.
- Database credentials and application secrets must be configured outside source code.
- Uploaded images must be validated and stored using safe filenames.
- Local image storage is intended for development; production deployment may require persistent storage.
- The exact authentication, password hashing, moderation, and deployment configuration must be confirmed against the implementation.

## 3. External Interface Requirements

### 3.1 User Interface
The interface should provide clearly labeled forms and pages for:
- Registration and login.
- Clothing listing creation and editing.
- Clothing browsing, search, and filtering.
- Item details.
- Sent and received requests.
- User dashboard.
- Administrative management.

### 3.2 Software Interfaces
| Interface | Purpose |
|---|---|
| Flask routes / REST endpoints | Process frontend requests and return pages or data |
| MySQL | Persist user, listing, request, report, and status data |
| Local image storage | Store and retrieve uploaded clothing images |
| Git / GitHub | Track source code and support team collaboration |
| Render | Host the application when deployment is configured |

## 4. Functional Requirements
Detailed functional requirements are maintained in `functional_requirements.md`.

## 5. Non-Functional Requirements
Detailed non-functional requirements are maintained in `non_functional_requirements.md`.

## 6. Data Requirements
The database is expected to represent, at minimum:
- **Users:** account identity and authentication-related fields.
- **Clothing items:** owner, title/description, category, size, condition, image reference, and availability.
- **Exchange/donation requests:** requester, item, request type, status, and timestamps.
- **Reports:** reported listing or user, reason, status, and administrative action where applicable.

These are logical data entities; exact table and column names should match the final MySQL schema.

## 7. Use Cases
Primary use cases include:
1. Register or log in.
2. Create or manage a clothing listing.
3. Browse, search, and filter items.
4. View item details.
5. Submit an exchange or donation request.
6. Accept or reject a received request.
7. Track request status and mark a completed exchange.
8. Report content, if reporting is enabled.
9. Manage users, review listings, and handle reports as an administrator.

## 8. Acceptance Criteria
The project can be evaluated against the following criteria:
- Valid users can register and log in, and invalid credentials are handled appropriately.
- Users can create listings with required details and an image where required.
- Search and filters return items matching the selected criteria.
- Requests can be submitted and their status can be viewed.
- Only authorized users can manage their own listings and requests.
- Completing a transaction updates the item’s availability.
- Administrative functions are restricted to administrators.
- Inputs and uploaded files are validated, and errors are presented clearly.

## 9. Future Enhancements
Possible future improvements include notifications, persistent cloud image storage, richer search, accessibility improvements, and additional reporting.

---
**Note:** This document specifies intended requirements. Update it after implementation and testing so it reflects only the features actually delivered.
