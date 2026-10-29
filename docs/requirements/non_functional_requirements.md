# Non-Functional Requirements
## ReWear – A Community-Based Clothing Exchange and Reuse Platform

Non-functional requirements define the quality attributes and operational constraints expected of ReWear. The targets below are proposed acceptance goals and should be measured during testing.

| ID | Quality attribute | Requirement / target |
|---|---|---|
| NFR-01 | Usability | Pages, forms, and buttons should use clear labels and consistent navigation. |
| NFR-02 | Responsive design | Core pages should remain usable on common desktop, tablet, and mobile screen sizes. |
| NFR-03 | Performance | For a small test dataset and normal development conditions, common pages should target a response time of 3 seconds or less; measure and revise based on the hosting environment. |
| NFR-04 | Reliability | Database or upload errors should be handled gracefully without displaying raw stack traces to users. |
| NFR-05 | Security | Passwords must not be stored in plaintext; use a suitable password-hashing method. |
| NFR-06 | Authorization | Users must be prevented from editing other users’ listings or managing requests they are not permitted to access. |
| NFR-07 | Input validation | User inputs must be validated on the server, even when client-side validation is also used. |
| NFR-08 | File upload security | Restrict upload types and sizes, validate file content where practical, and generate safe filenames. |
| NFR-09 | Data integrity | Listing ownership, request status, and item availability must remain consistent when requests change. |
| NFR-10 | Maintainability | Organize application code into understandable modules and document setup/configuration steps. |
| NFR-11 | Portability | The application should run in a documented Python/Flask environment with a compatible MySQL server. |
| NFR-12 | Availability | A deployed instance should be reachable through its configured URL when the hosting service is operational. |
| NFR-13 | Privacy | Collect only information needed for platform functions and avoid exposing private account details in public listings. |
| NFR-14 | Accessibility | Use semantic HTML, form labels, keyboard-accessible controls, and sufficient text contrast. |
| NFR-15 | Backup and recovery | Database backup and recovery procedures should be documented before production use. |
| NFR-16 | Deployment configuration | Secrets and database credentials should be supplied through environment configuration, not committed to GitHub. |

## Security and Deployment Notes

- Do not commit `.env`, passwords, secret keys, or real user data.
- Configure environment variables in the hosting dashboard.
- Local image storage is suitable for development, but may not persist across redeployments on some hosting platforms. Confirm the behavior of the chosen deployment configuration or use persistent/object storage.
- Use HTTPS for a public deployment.
- Keep dependencies updated and remove unnecessary packages.
- Restrict administrative routes using server-side authorization checks.

## Verification

| Requirement area | How to verify |
|---|---|
| Usability and responsiveness | Test core pages at desktop and mobile viewport sizes. |
| Performance | Measure page and endpoint response times under a defined test setup. |
| Security | Test authentication, authorization, validation, and upload restrictions. |
| Reliability | Test database connection failures and invalid form submissions. |
| Data integrity | Verify request transitions and availability updates. |
| Deployment | Test the configured public URL and confirm required environment variables are set. |

**Note:** Performance numbers are proposed targets, not measured results. Update this document with actual test results before final submission.
