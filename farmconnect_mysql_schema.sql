-- ==============================================================================
-- FarmConnect - Production MySQL Database Schema & Initial Seed Data
-- ==============================================================================
-- Project: FarmConnect (Direct Digital Marketplace & Cultivation Support)
-- Compatible with: MySQL 8.0+, MariaDB 10.5+
-- Character Set: utf8mb4
-- Collation: utf8mb4_unicode_ci
-- ==============================================================================

-- 1. Create Database if not exists and switch context
CREATE DATABASE IF NOT EXISTS `farmconnect_db` 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

USE `farmconnect_db`;

-- Disable foreign key checks for clean creation/reset
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Drop existing tables if needed
DROP TABLE IF EXISTS `support_responses`;
DROP TABLE IF EXISTS `cultivation_support_requests`;
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `farming_tips`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `farmer_profiles`;
DROP TABLE IF EXISTS `users`;

SET FOREIGN_KEY_CHECKS = 1;

-- ==============================================================================
-- TABLE: users
-- Core authentication and platform identity
-- Roles: FARMER, BUYER, AGRONOMIST_EXPERT, ADMIN
-- ==============================================================================
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(255) NOT NULL,
    `id_proof` VARCHAR(255) DEFAULT NULL,
    `role` VARCHAR(50) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_users_role` (`role`),
    INDEX `idx_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- TABLE: farmer_profiles
-- Strict privacy separation: exact address is masked; public gets anonymized region code
-- ==============================================================================
CREATE TABLE `farmer_profiles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL UNIQUE,
    `farm_name` VARCHAR(255) NOT NULL,
    `anonymized_region_code` VARCHAR(255) NOT NULL,
    `exact_address` VARCHAR(500) NOT NULL,
    `farming_experience_years` INT DEFAULT NULL,
    `is_organic_certified` BOOLEAN NOT NULL DEFAULT FALSE,
    `bio` VARCHAR(1000) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_farmer_profiles_user` FOREIGN KEY (`user_id`) 
        REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- TABLE: products
-- Farm harvest offerings with category, unit pricing, stock & harvest dates
-- ==============================================================================
CREATE TABLE `products` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `farmer_id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `category` VARCHAR(50) NOT NULL,
    `description` VARCHAR(2000) DEFAULT NULL,
    `unit` VARCHAR(50) NOT NULL,
    `price_per_unit` DECIMAL(12, 2) NOT NULL,
    `stock_quantity` DECIMAL(12, 2) NOT NULL,
    `harvest_date` DATE DEFAULT NULL,
    `is_organic_certified` BOOLEAN NOT NULL DEFAULT FALSE,
    `image_url` VARCHAR(1000) DEFAULT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_products_category` (`category`),
    INDEX `idx_products_status` (`status`),
    INDEX `idx_products_farmer` (`farmer_id`),
    CONSTRAINT `fk_products_farmer` FOREIGN KEY (`farmer_id`) 
        REFERENCES `farmer_profiles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- TABLE: orders
-- Buyer procurement transactions
-- ==============================================================================
CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_tracking_number` VARCHAR(255) NOT NULL UNIQUE,
    `buyer_id` BIGINT NOT NULL,
    `order_status` VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    `total_amount` DECIMAL(12, 2) NOT NULL,
    `delivery_address` VARCHAR(500) NOT NULL,
    `delivery_contact_number` VARCHAR(50) NOT NULL,
    `order_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `notes` VARCHAR(1000) DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_orders_tracking` (`order_tracking_number`),
    INDEX `idx_orders_status` (`order_status`),
    INDEX `idx_orders_buyer` (`buyer_id`),
    CONSTRAINT `fk_orders_buyer` FOREIGN KEY (`buyer_id`) 
        REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- TABLE: order_items
-- Individual line items in an order, mapped to product and farmer
-- ==============================================================================
CREATE TABLE `order_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `farmer_id` BIGINT NOT NULL,
    `quantity` DECIMAL(12, 2) NOT NULL,
    `unit_price` DECIMAL(12, 2) NOT NULL,
    `subtotal` DECIMAL(12, 2) NOT NULL,
    `item_status` VARCHAR(50) NOT NULL DEFAULT 'PENDING_CONFIRMATION',
    PRIMARY KEY (`id`),
    INDEX `idx_order_items_order` (`order_id`),
    INDEX `idx_order_items_product` (`product_id`),
    INDEX `idx_order_items_farmer` (`farmer_id`),
    CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) 
        REFERENCES `orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_order_items_product` FOREIGN KEY (`product_id`) 
        REFERENCES `products` (`id`),
    CONSTRAINT `fk_order_items_farmer` FOREIGN KEY (`farmer_id`) 
        REFERENCES `farmer_profiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- TABLE: cultivation_support_requests
-- Farmer technical and agronomist support tickets
-- ==============================================================================
CREATE TABLE `cultivation_support_requests` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `farmer_id` BIGINT NOT NULL,
    `topic` VARCHAR(50) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` VARCHAR(3000) NOT NULL,
    `urgency` VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    `crop_type` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_support_status` (`status`),
    INDEX `idx_support_urgency` (`urgency`),
    INDEX `idx_support_farmer` (`farmer_id`),
    CONSTRAINT `fk_support_requests_farmer` FOREIGN KEY (`farmer_id`) 
        REFERENCES `farmer_profiles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- TABLE: support_responses
-- Expert agronomist answers and remedial guidance
-- ==============================================================================
CREATE TABLE `support_responses` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `request_id` BIGINT NOT NULL,
    `responder_id` BIGINT NOT NULL,
    `guidance_text` TEXT NOT NULL,
    `recommended_resources` VARCHAR(1000) DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_responses_request` (`request_id`),
    INDEX `idx_responses_responder` (`responder_id`),
    CONSTRAINT `fk_support_responses_request` FOREIGN KEY (`request_id`) 
        REFERENCES `cultivation_support_requests` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_support_responses_responder` FOREIGN KEY (`responder_id`) 
        REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- TABLE: farming_tips
-- Seasonal agricultural advice curated by agronomy specialists
-- ==============================================================================
CREATE TABLE `farming_tips` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `category` VARCHAR(100) NOT NULL,
    `season` VARCHAR(50) NOT NULL,
    `content` TEXT NOT NULL,
    `author` VARCHAR(255) NOT NULL,
    `published_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_farming_tips_season` (`season`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ==============================================================================
-- SEED DATA
-- Default baseline test data matching FarmConnect DataInitializer
-- ==============================================================================

-- 1. Users
INSERT INTO `users` (`id`, `email`, `password`, `full_name`, `phone`, `id_proof`, `role`, `created_at`) VALUES
(1, 'ramesh.patel@farmconnect.org', 'farmer123', 'Ramesh Patel', '+91 98201 11222', NULL, 'FARMER', NOW()),
(2, 'savitri.devi@farmconnect.org', 'farmer123', 'Savitri Devi', '+91 94191 33444', NULL, 'FARMER', NOW()),
(3, 'gurpreet.singh@farmconnect.org', 'farmer123', 'Gurpreet Singh', '+91 98722 55666', NULL, 'FARMER', NOW()),
(4, 'procurement@freshdirect.com', 'buyer123', 'FreshDirect Supermarkets', '+91 98111 00001', NULL, 'BUYER', NOW()),
(5, 'chef@farmhousebistro.com', 'buyer123', 'The Farmhouse Bistro & Kitchen', '+91 98222 00002', NULL, 'BUYER', NOW()),
(6, 'dr.meera@agriscience.org', 'expert123', 'Dr. Meera Swaminathan', '+91 99000 77889', NULL, 'AGRONOMIST_EXPERT', NOW()),
(7, 'admin@farmconnect.org', 'admin123', 'Platform Administrator', '+91 99999 00000', 'AADHAAR-ADMIN-0001', 'ADMIN', NOW());

-- 2. Farmer Profiles
INSERT INTO `farmer_profiles` (`id`, `user_id`, `farm_name`, `anonymized_region_code`, `exact_address`, `farming_experience_years`, `is_organic_certified`, `bio`) VALUES
(1, 1, 'Patel Organic Orchards', 'Zone-West-Valley-04', 'Survey 42/B, Near Godavari Canal, Nashik Rural, MH', 18, 1, 'Third-generation natural farmer specializing in GI-tagged Alphonso mangoes, vine tomatoes, and chemical-free vegetables.'),
(2, 2, 'Highland Apple Haven', 'Zone-Himalaya-Ridge-02', 'Plot 18, Pine Hill Valley, Shimla Rural, HP', 14, 1, 'Eco-friendly alpine orchard practicing zero-budget natural farming and terrace horticulture.'),
(3, 3, 'Golden Plains Agro', 'Zone-Indus-Canal-07', 'Village Barnala East, Grand Trunk Corridor, Punjab', 22, 0, 'Large-scale producer of premium Sharbati golden wheat, farm-fresh carrots, and root tubers.');

-- 3. Products
INSERT INTO `products` (`id`, `farmer_id`, `name`, `category`, `description`, `unit`, `price_per_unit`, `stock_quantity`, `harvest_date`, `is_organic_certified`, `image_url`, `status`, `created_at`) VALUES
(1, 1, 'Organic Alphonso Mangoes (GI Tagged)', 'FRUITS', 'Naturally tree-ripened, intensely aromatic and golden pulp. Harvested without carbide or synthetic ripening agents.', 'kg', 145.00, 450.00, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1, 'https://images.unsplash.com/photo-1553279768-865429fa0078?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW()),
(2, 1, 'Vine-Ripened Roma Tomatoes', 'VEGETABLES', 'Sun-kissed, high-lycopene firm tomatoes. Ideal for gourmet salads, sauces, and culinary processing.', 'kg', 32.00, 800.00, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 1, 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW()),
(3, 2, 'Crisp Royal Gala Apples', 'FRUITS', 'High altitude cold-climate apples with crisp bite, natural wax coating, and balanced sweet-tart notes.', 'kg', 130.00, 600.00, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 1, 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW()),
(4, 3, 'Premium Sharbati Golden Wheat', 'GRAINS', 'A-grade heavy golden grains rich in natural gluten and fiber. Freshly harvested and machine cleaned.', 'kg', 44.00, 2500.00, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 0, 'https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW()),
(5, 1, 'Hydroponic & Organic Baby Spinach', 'VEGETABLES', 'Tender baby spinach bunches with zero pesticide residues. Washed with ozonated spring water.', 'bunch', 22.00, 350.00, CURDATE(), 1, 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW()),
(6, 3, 'Crunchy Red Field Carrots', 'VEGETABLES', 'Sweet, nutrient-dense winter carrots dug fresh from deep alluvial soils.', 'kg', 28.00, 1200.00, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 0, 'https://images.unsplash.com/photo-1598170845058-32b9d6a5c317?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW()),
(7, 2, 'Ruby Red Pomegranate (Bhagwa Variety)', 'FRUITS', 'Large fruit size with deep red arils and soft seeds. Extremely juicy and rich in antioxidants.', 'kg', 155.00, 280.00, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 1, 'https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW()),
(8, 1, 'Raw Organic Salem Turmeric Fingers', 'HERBS_AND_SPICES', 'High curcumin content (over 4.2%). Unpolished, sun-dried certified organic root rhizomes.', 'kg', 190.00, 180.00, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 1, 'https://images.unsplash.com/photo-1615485500704-8e990f9900f7?w=600&auto=format&fit=crop&q=80', 'AVAILABLE', NOW());

-- 4. Sample Order
INSERT INTO `orders` (`id`, `order_tracking_number`, `buyer_id`, `order_status`, `total_amount`, `delivery_address`, `delivery_contact_number`, `order_date`, `notes`) VALUES
(1, 'FC-10029-INIT', 4, 'CONFIRMED', 5760.00, 'Distribution Terminal 4, Sector 18, Central City Wholesalers Hub', '+91 98111 00001', NOW(), 'Please deliver in sanitized reusable crates at morning bay 2.');

-- 5. Order Items
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `farmer_id`, `quantity`, `unit_price`, `subtotal`, `item_status`) VALUES
(1, 1, 1, 1, 20.00, 145.00, 2900.00, 'CONFIRMED_BY_FARMER'),
(2, 1, 3, 2, 22.00, 130.00, 2860.00, 'CONFIRMED_BY_FARMER');

-- 6. Cultivation Support Requests
INSERT INTO `cultivation_support_requests` (`id`, `farmer_id`, `topic`, `title`, `description`, `urgency`, `crop_type`, `status`, `created_at`) VALUES
(1, 1, 'CROP_CARE', 'Leaf chlorosis and flower drop in greenhouse Roma tomatoes', 'Noticed yellowing between leaf veins on mid-tier branches followed by premature blossom drop. Current drip schedule is twice daily. Need organic fertilizer dosage correction.', 'HIGH', 'Roma Tomatoes', 'RESOLVED', NOW()),
(2, 2, 'PEST_CONTROL', 'Early Woolly Aphid sighting on Apple tree bark fissures', 'Observed cottony white clusters on root suckers and lower scaffold limbs after recent showers. Looking for bio-control options before petal fall.', 'MEDIUM', 'Royal Gala Apples', 'OPEN', NOW());

-- 7. Support Responses
INSERT INTO `support_responses` (`id`, `request_id`, `responder_id`, `guidance_text`, `recommended_resources`, `created_at`) VALUES
(1, 1, 6, 'Interveinal chlorosis at the flowering stage in tomatoes is a classic symptom of magnesium deficiency coupled with calcium imbalance. Action steps: 1) Apply foliar spray of Epsom salt (Magnesium sulfate) at 5 grams per liter in early morning. 2) Top-dress with fermented liquid Jeevamrut / Panchagavya (3% dilution) once weekly. 3) Reduce mid-day drip by 15% to maintain soil aeration.', 'ICAR Technical Bulletin: Managing Secondary Micronutrient Deficiencies in Solanaceous Crops; Organic Soil Amendment Guide 2026.', NOW());

-- 8. Farming Tips
INSERT INTO `farming_tips` (`id`, `title`, `category`, `season`, `content`, `author`, `published_at`) VALUES
(1, 'Drip Fertigation Protocol for Spring Vegetable Transplanting', 'Irrigation & Nutrition', 'SPRING', 'Transition seedlings with precision fertigation: maintain an electrical conductivity (EC) between 1.4 to 1.8 dS/m during vegetative initiation. Inoculate root zones with Trichoderma viride and Pseudomonas fluorescens 48 hours prior to transplanting to immunize against damping-off fungal pathogens.', 'Dr. Meera Swaminathan (National Agronomy Institute)', NOW()),
(2, 'Heatwave Mitigation & Micro-Sprinkler Cooling in Orchards', 'Climate Resilience', 'SUMMER', 'When ambient temperatures exceed 38°C, install low-volume canopy misting to reduce fruit sunburn by up to 65%. Apply light foliar kaolin clay film (5% suspension) as a natural solar reflective barrier on developing mangoes and pomegranates without impeding photosynthesis.', 'Agri-Climatology Board', NOW()),
(3, 'Monsoon Drainage and Phytophthora Root Rot Prevention', 'Drainage & Plant Pathology', 'MONSOON', 'Establish raised broad-bed furrows with minimum 1.5% fall to prevent waterlogging around root collars. Drench root zones with Bordeaux mixture (1%) or bio-fungicide Bacillus subtilis cultures immediately following heavy precipitation spells.', 'Horticulture Extension Services', NOW()),
(4, 'Winter Cover Cropping & Soil Microbial Biomass Enrichment', 'Soil Regenerative Science', 'WINTER', 'Sow legume cover crops like Hairy Vetch and Sunn Hemp between harvest cycles. These fix up to 120 kg/ha of biological atmospheric nitrogen, suppress stubborn weed seedbanks, and prevent topsoil erosion during dormant winter winds.', 'Center for Regenerative Agriculture', NOW()),
(5, 'On-Farm Compost Enrichment with Bio-Enzyme Accelerators', 'Organic Soil Health', 'ALL_SEASON', 'Layer crop residues in a 30:1 Carbon-to-Nitrogen ratio. Accelerate aerobic breakdown by turning the compost pile every 14 days and introducing lignolytic fungal cultures. Maintain internal pile core heat between 55°C and 65°C for 3 consecutive days to sterilize weed seeds and pathogens.', 'Organic Farming Council', NOW());
