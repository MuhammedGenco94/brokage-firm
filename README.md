# Brokage Firm Backend - Spring Boot Application

## Overview

This is a backend application built with Spring Boot that simulates the operations of a brokerage firm. It allows
employees to manage customers, create stock orders, deposit/withdraw money, and more. The project is secured using Basic
Authentication with role-based access control for USER (Customer) and ADMIN roles.

## Features

Create Orders: Employees can create stock orders (BUY/SELL) for customers.
Manage Customer Accounts: Customers can deposit/withdraw money from their TRY balance.
Cancel Orders: Pending orders can be canceled, and the reserved asset amounts are released.
List Assets and Orders: Retrieve all assets or orders for a specific customer.
Basic Authentication: Secure access to the API endpoints, differentiated by roles (ADMIN/USER).

## Requirements

Before building and running the application, ensure that you have the following installed:

Java Development Kit (JDK) 17 or higher
Maven (for managing dependencies and building the project)
Git (optional, for cloning the repository)

## Setup Instructions

1. Clone the Repository

- git clone https://github.com/MuhammedGenco94/brokage-firm.git
- cd <repository-directory>

2. Build the Project Using Maven

- mvn clean install

## Running the Application

1. Run Using Maven:

- mvn spring-boot:run

2. Run the Built JAR:

- java -jar target/brokage-firm-0.0.1-SNAPSHOT.jar

## Accessing the API

http://localhost:8080

## Default Admin user credentials:
- admin/admin123
