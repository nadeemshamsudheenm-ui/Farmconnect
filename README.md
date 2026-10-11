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
- **Database**: MySQL 8.0+ / MariaDB (Production & Local) with In-Memory H2 fallback profile
- **Frontend**: Vanilla HTML5, CSS3 (Modern Glassmorphic UI), JavaScript (Single Page Architecture)
- **Build Tool**: Apache Maven 3.9+
- **Testing**: JUnit 5, Spring Boot Starter Test (Integration and Unit Tests)

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.8+ (or Maven wrapper)
- MySQL Server 8.0+ or MariaDB (optional if running with H2 profile)
- Git

### Database Configuration (MySQL)

1. **Option A: Automatic Schema Initialization via Spring Boot**
   When you run the application with MySQL connected, Spring Boot Data JPA automatically creates/updates all tables in `farmconnect_db` and seeds initial accounts and catalog items on startup (`createDatabaseIfNotExist=true`).

2. **Option B: Manual Script Execution**
   You can also run the provided complete SQL script [`farmconnect_mysql_schema.sql`](file:///c:/Users/USER/Documents/OOP%20PROJECT%20S3/farmconnect_mysql_schema.sql):
   ```bash
   mysql -u root -p < farmconnect_mysql_schema.sql
   ```
   Or open and execute `farmconnect_mysql_schema.sql` inside **MySQL Workbench**, **phpMyAdmin**, or **DBeaver**.

3. **Database Connection Variables:**
   You can customize your database credentials either in `src/main/resources/application.yml` or via environment variables:
   - `MYSQL_HOST` (Default: `localhost`)
   - `MYSQL_PORT` (Default: `3306`)
   - `MYSQL_DB` (Default: `farmconnect_db`)
   - `MYSQL_USER` (Default: `root`)
   - `MYSQL_PASSWORD` (Default: `root`)

### Installation & Run

1. **Clone the repository:**
   ```bash
   git clone git@github.com:nadeemshamsudheenm-ui/Farmconnect.git
   cd Farmconnect
   ```

2. **Run with MySQL (Default):**
   ```bash
   mvn spring-boot:run
   ```
   Or with custom credentials:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--MYSQL_USER=your_user --MYSQL_PASSWORD=your_password"
   ```

3. **Run with H2 In-Memory Database (No MySQL required):**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=h2
   ```

4. **Access the Application:**
   - **Web UI**: Open your browser at [http://localhost:8080](http://localhost:8080)
   - **H2 Database Console** *(when using `--spring.profiles.active=h2`)*: Open [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

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
