# AmulStock

A Java Spring Boot application for managing and tracking Amul product stock. This project provides APIs and utilities to handle stock operations, product data, and integrates with external services using a configurable WebClient.

## Table of Contents
- [Overview](#overview)
- [Setup & Build](#setup--build)
- [Project Structure](#project-structure)
- [Changelog](#changelog)

## Overview
AmulStock is designed to streamline stock management for Amul products. It features modular DTOs, controllers, services, and utility classes for authentication and API integration.

## Setup & Build
1. **Prerequisites:**
   - Java 17 or higher
   - Maven 3.8+

2. **Build & Run:**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

3. **Configuration:**
   - Edit `src/main/resources/application.properties` as needed for your environment.

## Project Structure
- `controller/` — API endpoints for stock operations
- `service/` — Business logic and API integrations
- `dto/` — Data transfer objects for product and stock data
- `utils/` — Utility classes (e.g., authentication manager)
- `config/` — WebClient and other configuration files

## Changelog
All incremental changes published to git are summarized below:

### 610b379 — 2025-07-11
**dto working**
- Implemented DTOs for product data handling.
- Improved data transfer between layers.

### 61c8e33 — 2025-07-10
**stocking**
- Added core stock management logic.
- Integrated stock operations in controllers and services.

### b767c62 — 2025-07-10
**second comms working**
- Established second round of API communications.
- Enhanced service integration and error handling.

### 33dc5f0 — 2025-07-10
**preconfig**
- Added preliminary configuration files.
- Set up WebClient configuration.

### bdfc315 — 2025-07-10
**before config**
- Initial groundwork before adding configuration.
- Set up basic project structure.

### 2bebfe8 — 2025-07-10
**first commit**
- Project scaffolding and initial file structure.
- Added base packages and entry point.

---

_Last updated: 2025-07-12_
