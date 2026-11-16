
# Enterprise Sales Management System

## 1. Project Objective

Develop a modern enterprise Android application for field sales representatives, sales managers, and administrators.

The application aims to support sales operations, retailer management, order processing, and reliable offline data synchronization.

## 2. User Roles

The backend currently supports the following roles:

- Sales Representative (`SALES_REP`)
- Sales Manager (`MANAGER`)
- Administrator (`ADMIN`)

New user registrations are assigned the `SALES_REP` role by default.

## 3. Project Modules and Implementation Status

### Implemented

- User registration API
- User login API
- JWT-based authentication
- Current authenticated user API
- Password hashing using BCrypt
- PostgreSQL database integration
- Database schema migration using Flyway
- Android login screen using Jetpack Compose
- Authentication architecture using Clean Architecture and MVVM
- Dependency injection using Hilt
- Login input validation
- Backend and Android unit tests

### Planned / In Progress

- Dashboard
- Retailer Management
- Product Catalogue
- Order Management
- Payment Management
- Offline data access using Room
- Background synchronization using WorkManager
- Role-based application navigation
- Token persistence and session management
- Additional integration and UI testing

## 4. Technical Stack

### Android

- Kotlin
- Jetpack Compose
- Clean Architecture
- MVVM
- Hilt
- Retrofit
- Room (planned for offline data storage)
- Coroutines and Flow
- WorkManager (planned for background synchronization)

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT
- BCrypt
- PostgreSQL
- Flyway
- Maven

### Testing

- JUnit
- Android unit testing
- Spring Boot testing
- API integration testing

## 5. Key Requirements

- Secure user authentication and authorization
- Reliable communication between Android and backend APIs
- Role-based access control
- Offline data access
- Background data synchronization
- Maintainable and scalable application architecture
- Automated testing
- Secure handling of credentials and access tokens

## 6. Development Approach

The project is being developed incrementally.

Each module will be implemented, tested, and documented before moving to the next major feature.
