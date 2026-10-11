# FarmConnect

> **Direct Digital Marketplace connecting farmers and buyers with strict privacy protection and cultivation support.**

FarmConnect is a full-stack Spring Boot web application designed for agricultural commerce and advisory. It bridges local farmers directly with consumers and commercial buyers while safeguarding user privacy and providing comprehensive cultivation support.

---

## 🌟 Key Features

### 🌾 1. Direct Farmer-to-Buyer Marketplace
- **Product Management**: Farmers can add, update, and manage inventory across categories (Fruits, Vegetables, Grains, Pulses, Spices, Dairy).
- **Public Catalog**: Buyers can browse fresh produce, view pricing, unit availability, and seasonal indicators.
- **Direct Orders**: Streamlined purchasing flow with automated stock validation and status tracking (Pending, Confirmed, Shipped, Delivered, Cancelled).

### 🛡️ 2. Privacy & Data Protection
- **Contact Masking**: Direct buyer-farmer personal contact information is anonymized through a managed support gateway (`support@farmconnect.internal`).
- **Role-Based Access Control**: Strict segregation between Farmers, Buyers, and Platform Admins.

### 🌱 3. Cultivation Support & Advisory
- **Advisory Tickets**: Farmers can submit urgent or routine cultivation inquiries (crop health, soil, pests, irrigation).
- **Expert Replies**: Agronomy specialists and admins provide tailored advice and recommendations.
- **Farming Tips Library**: Curated repository of best practices and seasonal guidance.

---

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.5 (Spring MVC, Spring Data JPA, Validation API)
- **Database**: H2 In-Memory Database (with H2 Web Console)
- **Frontend**: Vanilla HTML5, CSS3 (Modern Glassmorphic UI), JavaScript (Single Page Architecture)
- **Build Tool**: Apache Maven 3.9+
- **Testing**: JUnit 5, Spring Boot Starter Test (Integration and Unit Tests)

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.8+ (or Maven wrapper)
- Git

### Installation & Run

1. **Clone the repository:**
   ```bash
   git clone git@github.com:nadeemshamsudheenm-ui/Farmconnect.git
   cd Farmconnect
   ```

2. **Build and Run:**
   ```bash
   mvn clean spring-boot:run
   ```

3. **Access the Application:**
   - **Web UI**: Open your browser at [http://localhost:8080](http://localhost:8080)
   - **H2 Database Console**: Open [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
     - JDBC URL: `jdbc:h2:mem:farmconnectdb`
     - Username: `sa`
     - Password: *(leave empty)*

---

## 🧪 Running Tests

To run the automated integration and unit test suite:
```bash
mvn test
```

Test coverage includes:
- `AdminRegistrationTest`: Admin authorization and registration code validation.
- `FarmConnectIntegrationTest`: Full end-to-end user flows and order processing.
- `OrderServiceTest`: Inventory deduction and order status transitions.
- `PrivacyProtectionTest`: Verification of anonymization and contact shielding.

---

## 📁 Project Structure

```
├── .vscode/               # VS Code workspace settings
├── .gitignore             # Git ignore configuration
├── pom.xml                # Maven project object model
├── README.md              # Project documentation
└── src/
    ├── main/
    │   ├── java/com/farmconnect/
    │   │   ├── config/      # App configuration and sample data initializer
    │   │   ├── controller/  # REST APIs for Auth, Catalog, Orders, Support
    │   │   ├── dto/         # Request & response data transfer objects
    │   │   ├── entity/      # JPA entities (User, Product, Order, Support)
    │   │   ├── exception/   # Global exception handling & custom errors
    │   │   ├── repository/  # Spring Data JPA repositories
    │   │   └── service/     # Business logic & domain services
    │   └── resources/
    │       ├── application.yml
    │       └── static/      # Frontend SPA assets (HTML, CSS, JS)
    └── test/java/com/farmconnect/
```

---

## 📄 License
This project is developed for educational and demonstration purposes.
