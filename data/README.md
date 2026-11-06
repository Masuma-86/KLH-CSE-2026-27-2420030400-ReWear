# ReWear - Sustainable Fashion, Clothing Exchange & Free Donation Platform

[![Python](https://img.shields.io/badge/Python-3.10%2B-blue.svg)](https://www.python.org/)
[![Flask](https://img.shields.io/badge/Flask-3.0%2B-green.svg)](https://flask.palletsprojects.com/)
[![MySQL](https://img.shields.io/badge/Database-MySQL%20%7C%20SQLite-orange.svg)](https://www.mysql.com/)
[![Sustainable](https://img.shields.io/badge/Goal-Zero%20Textile%20Waste-10B981.svg)](#sustainability-impact)

**ReWear** is a centralized digital web platform engineered to promote circular fashion by enabling users to exchange, donate, and reuse pre-owned clothing. By extending the functional lifecycle of garments, ReWear combats excessive textile waste, reduces fresh agricultural water consumption, averts greenhouse gas emissions, and broadens community access to quality apparel.

---

## 📋 System Architecture & Workflow

```text
User Registration / Login
          ↓
Browse or Upload Clothing Listings
          ↓
Search & Filter by Category, Size, Condition, Type
          ↓
Select an Available Clothing Item
          ↓
Send Exchange or Free Donation Request
          ↓
Clothing Owner Reviews the Request in Dashboard
          ↓
Accept / Reject Request
          ↓
Coordinate Clothing Handover & Notes
          ↓
Mark Handover as Completed
          ↓
Automatic Availability Update (Exchanged / Donated)
          ↓
Personal & Global Eco-Scorecard Calculation (+0.8 kg waste saved, +2,700 L water conserved)
```

---

## 🚀 Key Features

1. **User Authentication & Session Management**:
   - Secure account registration and authentication with salted password hashes.
   - User profile with personal sustainability scorecard (items circulated, kg textile diverted, liters of water saved, CO2 avoided).
   - Fast one-click demo login buttons for testing.

2. **Comprehensive Clothing Listings**:
   - Upload clothing photos with live preview during development.
   - Categorization by **Women**, **Men**, **Kids**, **Unisex**, **Outerwear**, **Footwear**, and **Accessories**.
   - Specifications for Size (`XS` to `XXL`, `Free Size`, footwear sizes), Condition (`New with tags`, `Like New`, `Gently Used`, `Well Worn`), Brand, Color, and Material.
   - Dual listing models: **Exchange Only**, **Free Community Donation**, or **Both**.

3. **Dynamic Search & Filtering**:
   - Instant client-side search across garment titles, descriptions, and brands.
   - Multi-criteria filtering by category, size, condition, and availability status.

4. **Exchange & Donation Request Workflow**:
   - Users can propose a direct swap by selecting an item from their own wardrobe.
   - Users can request free community donations with a coordination message.
   - Owners review, accept, or reject incoming requests.
   - Seamless transition to `Completed`, which automatically locks availability to `Exchanged` or `Donated`.

5. **User Dashboard**:
   - **My Wardrobe**: Manage personal listings, edit attributes, or remove items.
   - **Received Requests**: Review proposals from neighbors, accept/reject, or mark handovers complete.
   - **Sent Requests**: Track status of requests made to others and view contact numbers when approved.
   - **Sustainability Scorecard**: Real-time counter of personal environmental savings.

6. **Administrative & Moderation Dashboard**:
   - System overview metrics (total users, total listings, completed handovers, kilograms of textile waste diverted).
   - Flagged content moderation queue: Community users can report suspicious or inappropriate items; admins can dismiss or delete them.
   - User management: View users and toggle active/suspended status.
   - Listing management: Review and remove listings violating standards.

7. **Sustainability Hub & Eco-Calculator**:
   - Interactive slider calculator demonstrating real-time water, carbon, and landfill savings.
   - Educational resources on sustainable textile care (cold washing, air drying, visible mending).

---

## 🛠️ Technology Stack

- **Frontend**: HTML5, CSS3 (Modern, responsive, glassmorphic design system), Vanilla JavaScript (ES6+), Thymeleaf template engine.
- **Backend**: **Java 21**, **Spring Boot 3.2.5** (Spring MVC, Spring Data JPA / Hibernate, BCrypt security).
- **Build & Dependency Management**: Apache Maven (bundled Maven wrapper `mvnw` / `mvnw.cmd`).
- **Database**: 
  - **H2 Database** (Zero-configuration embedded file persistence at `./data/rewear_db` with built-in web console at `/h2-console`).
  - Supports MySQL / external RDBMS via `application.properties`.
- **Image Management**: Local storage in `static/uploads/` with automatic classpath and disk resource handling.

---

## 💻 Quick Start & Running Locally

### Step 1: Run the Application (Spring Boot)
Using the bundled Maven wrapper:

```bash
# On Windows PowerShell
.\mvnw.cmd spring-boot:run

# Or run the pre-built standalone executable JAR
java -jar target/rewear-platform-1.0.0.jar
```

The application automatically checks and seeds initial demo users, listings, and circular exchange requests upon first run!

Open your browser and navigate to:
👉 **`http://127.0.0.1:5000`**

Database Console (optional):
👉 **`http://127.0.0.1:5000/h2-console`** *(JDBC URL: `jdbc:h2:file:./data/rewear_db`)*

---

## 🔑 Demo Login Credentials

For quick evaluation, sample accounts are pre-populated with ready-to-test wardrobes:

| Role | Email | Password | Details |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin@rewear.org` | `admin123` | Full access to `/admin` moderation & user controls |
| **Active Trader** | `sarah@example.com` | `user123` | Wardrobe with dresses, jackets, received & sent requests |
| **Donor** | `david@example.com` | `user123` | Vintage denim, wool knitwear, commuter daypack |
| **Conscious Member** | `priya@example.com` | `user123` | Kids fleece, chinos, hand-dyed workwear |

*(Note: On the `/login` page, you can also click the quick-fill buttons to automatically insert credentials.)*

---

## 🗄️ Setting Up with MySQL

If you have MySQL Server (or XAMPP / WampServer) running on your machine:

1. **Import the Schema**:
   Open MySQL Workbench, phpMyAdmin, or your terminal:
   ```bash
   mysql -u root -p < schema.sql
   ```
   This creates the `rewear_db` database and all relational tables with foreign keys and indexes.

2. **Configure Environment Variables**:
   Create a `.env` file from `.env.example`:
   ```ini
   USE_MYSQL=true
   MYSQL_HOST=localhost
   MYSQL_PORT=3306
   MYSQL_USER=root
   MYSQL_PASSWORD=your_password_here
   MYSQL_DB=rewear_db
   ```

3. **Run the Seeder & App**:
   ```bash
   python seed.py
   python app.py
   ```

> **Smart Fallback Note**: If `USE_MYSQL=true` is set but your MySQL server is turned off, ReWear automatically falls back to SQLite so the application starts reliably without crashes!

---

## 🌍 Sustainability Impact Formulas

Calculations are grounded in United Nations Environment Programme (UNEP) and Ellen MacArthur Foundation textile lifecycle research:
- **Textile Landfill Diversion**: `Count × 0.8 kg`
- **Clean Water Saved**: `Count × 2,700 Liters` (equivalent to ~900 days of drinking water per cotton item)
- **Carbon Dioxide Averted**: `Count × 10.0 kg CO2e`
