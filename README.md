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

### Quick Start - Using Shell Scripts

**For Mac/Linux:**
```bash
chmod +x run-tests.sh
./run-tests.sh
```

**For Windows:**
```bash
run-tests.bat
```

### Run All Tests (Maven)

```bash
mvn clean test
```

### Run with Different Configurations

**Run with Firefox:**
```bash
mvn test -Dbrowser=firefox
```

**Run in Headless Mode:**
```bash
mvn test -Dheadless=true
```

**Run with 8 Parallel Workers:**
```bash
mvn test -Dworkers=8
```

**Run with 3 Retries:**
```bash
mvn test -Dretries=3
```

**Take Screenshots on Success:**
```bash
mvn test -Dscreenshot.success=true
```

**Combine Multiple Options:**
```bash
mvn test -Dbrowser=webkit -Dheadless=true -Dworkers=4 -Dretries=2
```

### Using Maven Profiles

**Default Profile (Chromium, Headed, 4 workers):**
```bash
mvn test -Pdefault
```

**CI Profile (Chromium, Headless, 2 workers):**
```bash
mvn test -Pci
```

**Firefox Profile:**
```bash
mvn test -Pfirefox
```

**WebKit Profile:**
```bash
mvn test -Pwebkit
```

### Run Specific Tests

**Run Specific Test Class:**
```bash
mvn test -Dtest=SauceDemoTest
```

**Run Specific Test Method:**
```bash
mvn test -Dtest=SauceDemoTest#testSuccessfulLogin
```

**Run Multiple Test Classes:**
```bash
mvn test -Dtest=SauceDemoTest,AnotherTest
```

### Advanced Script Usage

**Using run-tests.sh (Mac/Linux):**
```bash
# Run with Firefox and 2 workers
./run-tests.sh -b firefox -w 2

# Run in headless mode with CI profile
./run-tests.sh --headless -p ci

# Run with WebKit and screenshot all tests
./run-tests.sh -b webkit --screenshot-all

# Show help
./run-tests.sh --help
```

**Using run-tests.bat (Windows):**
```bash
# Run with Firefox and 2 workers
run-tests.bat -b firefox -w 2

# Run in headless mode with CI profile
run-tests.bat --headless -p ci

# Run with WebKit and screenshot all tests
run-tests.bat -b webkit --screenshot-all

# Show help
run-tests.bat --help
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

### Configuration Options

All configuration is centralized in `TestConfig.java`. You can override settings via system properties:

| Property | Default | Description |
|----------|---------|-------------|
| `browser` | chromium | Browser to use (chromium, firefox, webkit) |
| `headless` | false | Run browser in headless mode |
| `slowmo` | 50 | Delay between actions in milliseconds |
| `workers` | 4 | Number of parallel test workers |
| `retries` | 2 | Number of retries for failed tests |
| `screenshot.failure` | true | Take screenshot on test failure |
| `screenshot.success` | false | Take screenshot on test success |
| `screenshot.dir` | target/screenshots | Directory for screenshots |
| `record.video` | false | Enable video recording |
| `video.dir` | target/videos | Directory for videos |
| `enable.trace` | false | Enable Playwright trace recording |
| `trace.dir` | target/traces | Directory for trace files |
| `viewport.width` | 1920 | Browser viewport width |
| `viewport.height` | 1080 | Browser viewport height |
| `default.timeout` | 30000 | Default timeout in milliseconds |
| `navigation.timeout` | 30000 | Navigation timeout in milliseconds |

### Advanced Features

#### 1. Parallel Execution
Tests run in parallel by default using JUnit 5's parallel execution:
- Configured in `junit-platform.properties`
- Number of workers controlled by `-Dworkers=N`
- Can be disabled in `junit-platform.properties`

#### 2. Test Retries
Failed tests are automatically retried:
- Configured via `-Dretries=N`
- Implemented in Maven Surefire plugin
- Retry extension available in `RetryExtension.java`

#### 3. Screenshots
Automatic screenshot capture:
- On failure: `screenshot.failure=true` (default)
- On success: `screenshot.success=false` (default)
- Saved to `target/screenshots/` with timestamp
- Full page screenshots supported

#### 4. Video Recording
Enable with `-Drecord.video=true`:
```bash
mvn test -Drecord.video=true -Dvideo.dir=target/videos
```

#### 5. Trace Recording
Enable Playwright trace for debugging:
```bash
mvn test -Denable.trace=true
```
View traces at: https://trace.playwright.dev/

#### 6. Browser Selection
Run tests on different browsers:
```bash
mvn test -Dbrowser=chromium  # Chrome/Edge
mvn test -Dbrowser=firefox   # Firefox
mvn test -Dbrowser=webkit    # Safari
```

### Custom Configuration Example

```bash
mvn test \
  -Dbrowser=firefox \
  -Dheadless=true \
  -Dworkers=8 \
  -Dretries=3 \
  -Dscreenshot.success=true \
  -Drecord.video=true \
  -Denable.trace=true
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
