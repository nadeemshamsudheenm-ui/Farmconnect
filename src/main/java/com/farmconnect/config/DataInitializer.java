package com.farmconnect.config;

import com.farmconnect.entity.*;
import com.farmconnect.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CultivationSupportRequestRepository supportRequestRepository;
    private final SupportResponseRepository supportResponseRepository;
    private final FarmingTipRepository farmingTipRepository;

    public DataInitializer(UserRepository userRepository,
                           FarmerProfileRepository farmerProfileRepository,
                           ProductRepository productRepository,
                           OrderRepository orderRepository,
                           CultivationSupportRequestRepository supportRequestRepository,
                           SupportResponseRepository supportResponseRepository,
                           FarmingTipRepository farmingTipRepository) {
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.supportRequestRepository = supportRequestRepository;
        this.supportResponseRepository = supportResponseRepository;
        this.farmingTipRepository = farmingTipRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        // ==========================================
        // 1. SEED FARMERS (Strict Privacy Separation)
        // ==========================================
        User farmerUser1 = userRepository.save(new User(
                "ramesh.patel@farmconnect.org",
                "farmer123",
                "Ramesh Patel",
                "+91 98201 11222",
                Role.FARMER
        ));
        FarmerProfile farmer1 = farmerProfileRepository.save(new FarmerProfile(
                farmerUser1,
                "Patel Organic Orchards",
                "Zone-West-Valley-04",
                "Survey 42/B, Near Godavari Canal, Nashik Rural, MH", // Strictly Private PII
                18,
                true,
                "Third-generation natural farmer specializing in GI-tagged Alphonso mangoes, vine tomatoes, and chemical-free vegetables."
        ));

        User farmerUser2 = userRepository.save(new User(
                "savitri.devi@farmconnect.org",
                "farmer123",
                "Savitri Devi",
                "+91 94191 33444",
                Role.FARMER
        ));
        FarmerProfile farmer2 = farmerProfileRepository.save(new FarmerProfile(
                farmerUser2,
                "Highland Apple Haven",
                "Zone-Himalaya-Ridge-02",
                "Plot 18, Pine Hill Valley, Shimla Rural, HP", // Strictly Private PII
                14,
                true,
                "Eco-friendly alpine orchard practicing zero-budget natural farming and terrace horticulture."
        ));

        User farmerUser3 = userRepository.save(new User(
                "gurpreet.singh@farmconnect.org",
                "farmer123",
                "Gurpreet Singh",
                "+91 98722 55666",
                Role.FARMER
        ));
        FarmerProfile farmer3 = farmerProfileRepository.save(new FarmerProfile(
                farmerUser3,
                "Golden Plains Agro",
                "Zone-Indus-Canal-07",
                "Village Barnala East, Grand Trunk Corridor, Punjab", // Strictly Private PII
                22,
                false,
                "Large-scale producer of premium Sharbati golden wheat, farm-fresh carrots, and root tubers."
        ));

        // ==========================================
        // 2. SEED BUYERS
        // ==========================================
        User buyer1 = userRepository.save(new User(
                "procurement@freshdirect.com",
                "buyer123",
                "FreshDirect Supermarkets",
                "+91 98111 00001",
                Role.BUYER
        ));

        User buyer2 = userRepository.save(new User(
                "chef@farmhousebistro.com",
                "buyer123",
                "The Farmhouse Bistro & Kitchen",
                "+91 98222 00002",
                Role.BUYER
        ));

        // ==========================================
        // 3. SEED EXPERT AGRONOMIST & ADMIN
        // ==========================================
        User expert = userRepository.save(new User(
                "dr.meera@agriscience.org",
                "expert123",
                "Dr. Meera Swaminathan",
                "+91 99000 77889",
                Role.AGRONOMIST_EXPERT
        ));

        User adminUser = userRepository.save(new User(
                "admin@farmconnect.org",
                "admin123",
                "Platform Administrator",
                "+91 99999 00000",
                "AADHAAR-ADMIN-0001",
                Role.ADMIN
        ));

        // ==========================================
        // 4. SEED PRODUCTS
        // ==========================================
        Product p1 = productRepository.save(new Product(
                farmer1,
                "Organic Alphonso Mangoes (GI Tagged)",
                ProductCategory.FRUITS,
                "Naturally tree-ripened, intensely aromatic and golden pulp. Harvested without carbide or synthetic ripening agents.",
                "kg",
                new BigDecimal("145.00"),
                new BigDecimal("450.00"),
                LocalDate.now().plusDays(2),
                true,
                "https://images.unsplash.com/photo-1553279768-865429fa0078?w=600&auto=format&fit=crop&q=80"
        ));

        Product p2 = productRepository.save(new Product(
                farmer1,
                "Vine-Ripened Roma Tomatoes",
                ProductCategory.VEGETABLES,
                "Sun-kissed, high-lycopene firm tomatoes. Ideal for gourmet salads, sauces, and culinary processing.",
                "kg",
                new BigDecimal("32.00"),
                new BigDecimal("800.00"),
                LocalDate.now().minusDays(1),
                true,
                "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&auto=format&fit=crop&q=80"
        ));

        Product p3 = productRepository.save(new Product(
                farmer2,
                "Crisp Royal Gala Apples",
                ProductCategory.FRUITS,
                "High altitude cold-climate apples with crisp bite, natural wax coating, and balanced sweet-tart notes.",
                "kg",
                new BigDecimal("130.00"),
                new BigDecimal("600.00"),
                LocalDate.now().minusDays(3),
                true,
                "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=600&auto=format&fit=crop&q=80"
        ));

        Product p4 = productRepository.save(new Product(
                farmer3,
                "Premium Sharbati Golden Wheat",
                ProductCategory.GRAINS,
                "A-grade heavy golden grains rich in natural gluten and fiber. Freshly harvested and machine cleaned.",
                "kg",
                new BigDecimal("44.00"),
                new BigDecimal("2500.00"),
                LocalDate.now().minusDays(10),
                false,
                "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=600&auto=format&fit=crop&q=80"
        ));

        Product p5 = productRepository.save(new Product(
                farmer1,
                "Hydroponic & Organic Baby Spinach",
                ProductCategory.VEGETABLES,
                "Tender baby spinach bunches with zero pesticide residues. Washed with ozonated spring water.",
                "bunch",
                new BigDecimal("22.00"),
                new BigDecimal("350.00"),
                LocalDate.now(),
                true,
                "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=600&auto=format&fit=crop&q=80"
        ));

        Product p6 = productRepository.save(new Product(
                farmer3,
                "Crunchy Red Field Carrots",
                ProductCategory.VEGETABLES,
                "Sweet, nutrient-dense winter carrots dug fresh from deep alluvial soils.",
                "kg",
                new BigDecimal("28.00"),
                new BigDecimal("1200.00"),
                LocalDate.now().minusDays(2),
                false,
                "https://images.unsplash.com/photo-1598170845058-32b9d6a5c317?w=600&auto=format&fit=crop&q=80"
        ));

        Product p7 = productRepository.save(new Product(
                farmer2,
                "Ruby Red Pomegranate (Bhagwa Variety)",
                ProductCategory.FRUITS,
                "Large fruit size with deep red arils and soft seeds. Extremely juicy and rich in antioxidants.",
                "kg",
                new BigDecimal("155.00"),
                new BigDecimal("280.00"),
                LocalDate.now().minusDays(4),
                true,
                "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=600&auto=format&fit=crop&q=80"
        ));

        Product p8 = productRepository.save(new Product(
                farmer1,
                "Raw Organic Salem Turmeric Fingers",
                ProductCategory.HERBS_AND_SPICES,
                "High curcumin content (over 4.2%). Unpolished, sun-dried certified organic root rhizomes.",
                "kg",
                new BigDecimal("190.00"),
                new BigDecimal("180.00"),
                LocalDate.now().minusDays(15),
                true,
                "https://images.unsplash.com/photo-1615485500704-8e990f9900f7?w=600&auto=format&fit=crop&q=80"
        ));

        // ==========================================
        // 5. SEED INITIAL SAMPLE ORDER
        // ==========================================
        Order sampleOrder = new Order(
                "FC-10029-INIT",
                buyer1,
                new BigDecimal("5760.00"),
                "Distribution Terminal 4, Sector 18, Central City Wholesalers Hub",
                "+91 98111 00001",
                "Please deliver in sanitized reusable crates at morning bay 2."
        );
        sampleOrder.setOrderStatus(OrderStatus.CONFIRMED);

        OrderItem item1 = new OrderItem(p1, farmer1, new BigDecimal("20.00"), p1.getPricePerUnit(), new BigDecimal("2900.00"));
        item1.setItemStatus(OrderItemStatus.CONFIRMED_BY_FARMER);

        OrderItem item2 = new OrderItem(p3, farmer2, new BigDecimal("22.00"), p3.getPricePerUnit(), new BigDecimal("2860.00"));
        item2.setItemStatus(OrderItemStatus.CONFIRMED_BY_FARMER);

        sampleOrder.addItem(item1);
        sampleOrder.addItem(item2);
        orderRepository.save(sampleOrder);

        // ==========================================
        // 6. SEED CULTIVATION SUPPORT TICKETS
        // ==========================================
        CultivationSupportRequest ticket1 = new CultivationSupportRequest(
                farmer1,
                SupportTopic.CROP_CARE,
                "Leaf chlorosis and flower drop in greenhouse Roma tomatoes",
                "Noticed yellowing between leaf veins on mid-tier branches followed by premature blossom drop. Current drip schedule is twice daily. Need organic fertilizer dosage correction.",
                SupportUrgency.HIGH,
                "Roma Tomatoes"
        );
        ticket1.setStatus(SupportStatus.RESOLVED);
        CultivationSupportRequest savedTicket1 = supportRequestRepository.save(ticket1);

        SupportResponse response1 = new SupportResponse(
                savedTicket1,
                expert,
                "Interveinal chlorosis at the flowering stage in tomatoes is a classic symptom of magnesium deficiency coupled with calcium imbalance. " +
                "Action steps: 1) Apply foliar spray of Epsom salt (Magnesium sulfate) at 5 grams per liter in early morning. " +
                "2) Top-dress with fermented liquid Jeevamrut / Panchagavya (3% dilution) once weekly. 3) Reduce mid-day drip by 15% to maintain soil aeration.",
                "ICAR Technical Bulletin: Managing Secondary Micronutrient Deficiencies in Solanaceous Crops; Organic Soil Amendment Guide 2026."
        );
        supportResponseRepository.save(response1);
        savedTicket1.addResponse(response1);
        supportRequestRepository.save(savedTicket1);

        CultivationSupportRequest ticket2 = new CultivationSupportRequest(
                farmer2,
                SupportTopic.PEST_CONTROL,
                "Early Woolly Aphid sighting on Apple tree bark fissures",
                "Observed cottony white clusters on root suckers and lower scaffold limbs after recent showers. Looking for bio-control options before petal fall.",
                SupportUrgency.MEDIUM,
                "Royal Gala Apples"
        );
        supportRequestRepository.save(ticket2);

        // ==========================================
        // 7. SEED SEASONAL FARMING TIPS & GUIDES
        // ==========================================
        farmingTipRepository.save(new FarmingTip(
                "Drip Fertigation Protocol for Spring Vegetable Transplanting",
                "Irrigation & Nutrition",
                Season.SPRING,
                "Transition seedlings with precision fertigation: maintain an electrical conductivity (EC) between 1.4 to 1.8 dS/m during vegetative initiation. Inoculate root zones with Trichoderma viride and Pseudomonas fluorescens 48 hours prior to transplanting to immunize against damping-off fungal pathogens.",
                "Dr. Meera Swaminathan (National Agronomy Institute)"
        ));

        farmingTipRepository.save(new FarmingTip(
                "Heatwave Mitigation & Micro-Sprinkler Cooling in Orchards",
                "Climate Resilience",
                Season.SUMMER,
                "When ambient temperatures exceed 38°C, install low-volume canopy misting to reduce fruit sunburn by up to 65%. Apply light foliar kaolin clay film (5% suspension) as a natural solar reflective barrier on developing mangoes and pomegranates without impeding photosynthesis.",
                "Agri-Climatology Board"
        ));

        farmingTipRepository.save(new FarmingTip(
                "Monsoon Drainage and Phytophthora Root Rot Prevention",
                "Drainage & Plant Pathology",
                Season.MONSOON,
                "Establish raised broad-bed furrows with minimum 1.5% fall to prevent waterlogging around root collars. Drench root zones with Bordeaux mixture (1%) or bio-fungicide Bacillus subtilis cultures immediately following heavy precipitation spells.",
                "Horticulture Extension Services"
        ));

        farmingTipRepository.save(new FarmingTip(
                "Winter Cover Cropping & Soil Microbial Biomass Enrichment",
                "Soil Regenerative Science",
                Season.WINTER,
                "Sow legume cover crops like Hairy Vetch and Sunn Hemp between harvest cycles. These fix up to 120 kg/ha of biological atmospheric nitrogen, suppress stubborn weed seedbanks, and prevent topsoil erosion during dormant winter winds.",
                "Center for Regenerative Agriculture"
        ));

        farmingTipRepository.save(new FarmingTip(
                "On-Farm Compost Enrichment with Bio-Enzyme Accelerators",
                "Organic Soil Health",
                Season.ALL_SEASON,
                "Layer crop residues in a 30:1 Carbon-to-Nitrogen ratio. Accelerate aerobic breakdown by turning the compost pile every 14 days and introducing lignolytic fungal cultures. Maintain internal pile core heat between 55°C and 65°C for 3 consecutive days to sterilize weed seeds and pathogens.",
                "Organic Farming Council"
        ));
    }
}
