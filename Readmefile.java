# Selenium Test Automation Framework

This project is a Selenium-based test automation framework using JUnit 5 and Extent Reports for generating test reports. The tests are designed to interact with the Swag Labs website.

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
  - [Start Selenium Grid](#start-selenium-grid)
  - [Install Dependencies](#install-dependencies)
  - [Add Credentials](#add-credentials)
- [Running Tests](#running-tests)
  - [Running All Tests](#running-all-tests)
  - [Running Tests by Tags](#running-tests-by-tags)
- [Generating Reports](#generating-reports)

## Introduction

This framework provides a structured approach to automate end-to-end testing of web applications using Selenium WebDriver. It includes:
- Automated login tests
- Cart operations
- Purchase processes
- Sorting functionalities

## Prerequisites

Ensure you have the following installed:
- Java 22
- Maven
- Selenium Grid

## Setup
  Install sufficient depedency for test report

##project Structure
src
└── test
    └── java
        └── Base
            ├── BaseTests.java
            ├── HtmlReport.java
            ├── Credentials.java
            ├── credentials.json
            └── config.json
Test Cases
1. Successful Sign In
Tag: login
Description: Automates the login process using valid credentials.
  
2. Add Items to Cart and Remove Them from the Products Page
Tag: login
Tag: productpage
Description: Adds items to the cart from the products page and then removes them. Verifies the cart icon and that the “Add to Cart” option returns for the product tile.
  
3. Add Items to Cart and Remove Them from the Checkout Page
Tag: login
Tag:checkoutpage
Description: Adds items to the cart, proceeds to the checkout page, and removes them. Verifies that items are removed from the cart successfully.
  
4. Add Items to Cart and Remove Them from the Product Details Page
Tag: login
 Tag:productdeatilspage
  
Description: Adds items to the cart from the product details page (by clicking on the product) and then removes them. Verifies the cart icon and that the “Add to Cart” option returns.
  
5. Buy Items
Tag: purchase
 Tag:login
Description: Adds items to the cart, proceeds to checkout, and completes the purchase.
  
6. Add Items to Cart, Logout, and Login Again to Verify Cart Persistence
Tag: login:
Tag:logout
Description: Adds items to the cart, logs out, logs back in, and verifies that the cart retains the items.
  
7. Verify All Sorting Options on Products Page
Tag: sorting
Tag: login
Description: Verifies the functionality of all sorting options (e.g., price, name) on the products page.
