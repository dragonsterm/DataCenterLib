<div align="center">
  <h1>🏢 DataCenterLib (DataCore)</h1>
  <p>A Java-based GUI application for managing and visualizing data center environments.</p>

  <img src="https://img.shields.io/badge/Java-22-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Swing-Native-4A4A55?style=for-the-badge" alt="Swing"/>

  <br />
</div>

## 📖 About The Project
**DataCenterLib** is a final project assignment for the Object-Oriented Programming (PBO) laboratory course. It provides a visual, interactive top-down map interface to manage Data Center rooms, server racks, and network pathing, integrated with a local database to retain metrics, hardware configurations, and spatial coordination.

### 👥 Team Members
1. **Jauza Ilham Mahardhika Putra** (123240174)
2. **Dimas Hafid Fathoni** (123240159)

---

## ✨ Features
* **Interactive Map Viewport**:
    * Real-time grid-based room management with fluid camera controls (`W`, `A`, `S`, `D`).
    * Add Data Center Rooms (Small: 32x32, Medium: 64x64, Large: 128x128).
* **Rack & Spatial Management**:
    * Build 3x3 Server Racks (supports user-defined U capacity and Zoning fields).
    * Build 1x1 floor pathways for visual layout.
    * Drag and drop capabilities to dynamically relocate installed Racks across the coordinate map.
* **Server & Equipment Tracking**:
    * Granular visibility into individual racks (displaying empty, filled, or out-of-order U slots).
    * Batch management: Add, Update, Move, and Delete Servers in bulk.
    * Live monitoring visualizers tracking Server specific hardware utilization (CPU, RAM, Storage).
* **Dashboard Suite**:
    * Read-only metrics overlay summarizing total racks, active paths, available space, and systemic hardware health.
    * "Purge Room" utility to safely reset environmental states.

---

## 🛠 Prerequisites
Before running the application, ensure your environment meets these requirements:

* **[Java Development Kit (JDK) 22+](https://jdk.java.net/22/)**
* **MySQL Server** (Using XAMPP, WAMP, or standalone)
* **MySQL Connector/J** (`mysql-connector-j-9.7.0.jar` included in `tools/`)

---

## 🚀 Installation & Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/dragonsterm/DataCenterLib.git
   cd DataCenterLib
   ```
2. **Set up the Database:**
   * Open your MySQL client (e.g., phpMyAdmin, DBeaver, or MySQL CLI).
   * Create a new database named `datacorelibrary`.
   * Import the provided foundational schema located at: `sql/datacorelibrary.sql`
   * *Note: By default, the application accesses `localhost:3306` with the username `root` and an empty password.*

3. **Build the Application (Ant / Terminal):**
   * If you have Apache Ant installed, you can build a runnable jar:
     ```sh
     ant clean jar
     java -jar dist/DataCenterProject.jar
     ```

## 💻 Tech Stack
* **Language:** Java 22 SE
* **GUI Framework:** Java Swing & AWT (Graphics2D)
* **Database:** MySQL Relational Database
* **Build Tool:** Apache Ant / IntelliJ IDEA configs

---
<p align="center"> 
  <i>Developed for UPN "Veteran" Yogyakarta - Praktikum PBO</i> 
</p>