# Sauce Demo - Playwright Java Automation

This project contains UI automation tests for the [Sauce Demo](https://www.saucedemo.com/) website using Playwright with Java.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Git

## Project Structure

```
PWJava/
├── pom.xml                           # Maven configuration
├── README.md                         # Project documentation
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/
│   │           └── pages/           # Page Object Models
│   │               ├── LoginPage.java
│   │               ├── ProductsPage.java
│   │               ├── CartPage.java
│   │               └── CheckoutPage.java
│   └── test/
│       └── java/
│           └── com/example/
│               ├── tests/
│               │   └── SauceDemoTest.java
│               └── base/
│                   └── BaseTest.java
└── .gitignore                       # Git ignore file
```

## Setup Instructions

### 1. Install Dependencies

```bash
mvn clean install
```

### 2. Install Playwright Browsers

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

Or alternatively:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
```

## Running Tests

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=SauceDemoTest
```

### Run Specific Test Method

```bash
mvn test -Dtest=SauceDemoTest#testSuccessfulLogin
```

### Run Tests in Headless Mode

Edit `BaseTest.java` and change:
```java
.setHeadless(false)  // Change to true
```

## Test Scenarios Covered

1. **Login Tests**
   - Successful login with valid credentials
   - Login with invalid credentials
   - Login with locked out user

2. **Product Tests**
   - Adding single product to cart
   - Adding multiple products to cart
   - Sorting products by price
   - Sorting products by name

3. **Cart Tests**
   - Viewing cart items
   - Removing products from cart
   - Continue shopping from cart

4. **Checkout Tests**
   - Complete checkout flow
   - Checkout with missing information
   - Order confirmation

5. **Navigation Tests**
   - Logout functionality
   - Navigation between pages

## Test Credentials

The following test users are available on the Sauce Demo website:

- **Standard User**: `standard_user` / `secret_sauce`
- **Locked Out User**: `locked_out_user` / `secret_sauce`
- **Problem User**: `problem_user` / `secret_sauce`
- **Performance Glitch User**: `performance_glitch_user` / `secret_sauce`

## Page Object Model

This project follows the Page Object Model (POM) design pattern for better maintainability and reusability:

- **LoginPage**: Handles login page interactions
- **ProductsPage**: Handles product listing and cart operations
- **CartPage**: Handles shopping cart operations
- **CheckoutPage**: Handles checkout process
- **BaseTest**: Base test class with setup and teardown methods

## Configuration

### Browser Configuration

Browser settings can be modified in `BaseTest.java`:

```java
browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
    .setHeadless(false)  // Set to true for headless mode
    .setSlowMo(50));     // Adjust delay between actions
```

### Viewport Size

```java
context = browser.newContext(new Browser.NewContextOptions()
    .setViewportSize(1920, 1080));
```

## Reporting

Test results are displayed in the console after test execution. For detailed HTML reports, you can integrate additional reporting libraries like:

- Allure Reports
- Extent Reports
- Surefire Reports

## Continuous Integration

For CI/CD pipelines:

1. Set browser to headless mode in `BaseTest.java`
2. Run tests using: `mvn clean test`
3. Configure your CI tool (Jenkins, GitHub Actions, etc.) to execute the Maven command

### Example GitHub Actions Workflow

```yaml
name: Playwright Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Install dependencies
        run: mvn clean install
      - name: Install Playwright browsers
        run: mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
      - name: Run tests
        run: mvn test
```

## Troubleshooting

### Common Issues

1. **Browser not found**: Run `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"`
2. **Tests failing**: Check if the Sauce Demo website is accessible
3. **Timeout errors**: Increase wait times or check network connection

## Contributing

Feel free to submit issues and enhancement requests!

## License

This project is for educational purposes.
