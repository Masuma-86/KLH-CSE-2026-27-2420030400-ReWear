-- ==========================================================
-- ReWear Sustainable Fashion Platform - MySQL Database Schema
-- Database: rewear_db
-- ==========================================================

CREATE DATABASE IF NOT EXISTS `rewear_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `rewear_db`;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(25) NULL,
    `location` VARCHAR(120) NULL DEFAULT 'Community Hub',
    `bio` TEXT NULL,
    `avatar` VARCHAR(255) NULL,
    `is_admin` BOOLEAN NOT NULL DEFAULT FALSE,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_users_email` (`email`),
    INDEX `idx_users_role` (`is_admin`, `is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Clothing Listings Table
CREATE TABLE IF NOT EXISTS `listings` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `title` VARCHAR(180) NOT NULL,
    `description` TEXT NOT NULL,
    `category` VARCHAR(50) NOT NULL,        -- 'Men', 'Women', 'Kids', 'Unisex', 'Outerwear', 'Footwear', 'Accessories'
    `size` VARCHAR(30) NOT NULL,            -- 'XS', 'S', 'M', 'L', 'XL', 'XXL', 'Free Size'
    `condition` VARCHAR(40) NOT NULL,       -- 'New with tags', 'Like New', 'Gently Used', 'Well Worn'
    `listing_type` VARCHAR(30) NOT NULL,    -- 'Exchange', 'Donation', 'Both'
    `brand` VARCHAR(100) NULL,
    `color` VARCHAR(50) NULL,
    `material` VARCHAR(80) NULL,
    `image_url` VARCHAR(255) NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'Available', -- 'Available', 'Pending', 'Exchanged', 'Donated', 'Hidden'
    `views` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_listings_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    INDEX `idx_listings_category` (`category`),
    INDEX `idx_listings_status` (`status`),
    INDEX `idx_listings_type` (`listing_type`),
    INDEX `idx_listings_size` (`size`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Exchange / Donation Requests Table
CREATE TABLE IF NOT EXISTS `exchange_requests` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `listing_id` INT NOT NULL,
    `requester_id` INT NOT NULL,
    `owner_id` INT NOT NULL,
    `offered_item_id` INT NULL,              -- Proposed clothing from requester's wardrobe
    `request_type` VARCHAR(30) NOT NULL,    -- 'Exchange' or 'Donation'
    `message` TEXT NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'Pending', -- 'Pending', 'Accepted', 'Rejected', 'Completed', 'Cancelled'
    `handover_notes` TEXT NULL,             -- Delivery/pickup coordination details
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_requests_listing` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_requests_requester` FOREIGN KEY (`requester_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_requests_owner` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_requests_offered_item` FOREIGN KEY (`offered_item_id`) REFERENCES `listings` (`id`) ON DELETE SET NULL,
    INDEX `idx_requests_status` (`status`),
    INDEX `idx_requests_requester` (`requester_id`),
    INDEX `idx_requests_owner` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Content Moderation & Reports Table
CREATE TABLE IF NOT EXISTS `reports` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `listing_id` INT NOT NULL,
    `reporter_id` INT NOT NULL,
    `reason` VARCHAR(80) NOT NULL,          -- 'Inappropriate', 'Counterfeit', 'Misleading', 'Broken/Damaged', 'Spam'
    `details` TEXT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'Pending', -- 'Pending', 'Reviewed', 'Dismissed', 'Actioned'
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_reports_listing` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_reports_reporter` FOREIGN KEY (`reporter_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    INDEX `idx_reports_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
