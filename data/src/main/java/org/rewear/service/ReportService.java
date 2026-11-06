package org.rewear.service;

import org.rewear.model.Listing;
import org.rewear.model.Report;
import org.rewear.model.User;
import org.rewear.repository.ListingRepository;
import org.rewear.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ListingRepository listingRepository;

    public ReportService(ReportRepository reportRepository, ListingRepository listingRepository) {
        this.reportRepository = reportRepository;
        this.listingRepository = listingRepository;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Report createReport(Long listingId, User reporter, String reason, String details) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        Report report = new Report(listing, reporter, reason, details, "Pending");
        return reportRepository.save(report);
    }

    @Transactional
    public void dismissReport(Long reportId) {
        reportRepository.findById(reportId).ifPresent(r -> {
            r.setStatus("Dismissed");
            reportRepository.save(r);
        });
    }

    public long countPending() {
        return reportRepository.countByStatus("Pending");
    }
}
