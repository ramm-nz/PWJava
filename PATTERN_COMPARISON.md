# Page Object Initialization Patterns - Comparison Guide

This guide compares different approaches for initializing Page Objects in your Playwright tests.

## Approach 1: Manual Initialization (Original)

### Code:
```java
private LoginPage loginPage;
private ProductsPage productsPage;
private CartPage cartPage;
private CheckoutPage checkoutPage;

@BeforeEach
void initializePages() {
    loginPage = new LoginPage(page);
    productsPage = new ProductsPage(page);
    cartPage = new CartPage(page);
    checkoutPage = new CheckoutPage(page);
}
```

### Pros:
- Simple and straightforward
- Easy to understand for beginners
- Direct access to page objects

### Cons:
- Creates ALL page objects for EVERY test (wasteful)
- Need to add new field and initialization for each new page
- Repetitive code
- Memory overhead for unused page objects

---

## Approach 2: PageManager with Lazy Initialization (Recommended)

### Code:
```java
protected PageManager pageManager;

@BeforeEach
void initializePageManager() {
    pageManager = new PageManager(page);
}

// In tests:
pageManager.getLoginPage().navigate();
pageManager.getProductsPage().addProductToCart("item");
```

### Pros:
- **Lazy initialization** - creates pages only when needed
- Memory efficient - only used pages are created
- Clean test code
- Easy to add new pages (just add getter to PageManager)
- No repetitive initialization code

### Cons:
- Slightly more verbose (need to call `getLoginPage()`)
- One additional class to maintain

### Best For:
- Small to medium projects
- Teams new to design patterns
- When you want simplicity with efficiency

---

## Approach 3: PageFactory Pattern (Advanced)

### Code:
```java
protected PageFactory pageFactory;

@BeforeEach
void initializePageFactory() {
    pageFactory = new PageFactory(page);
}

// Generic approach:
LoginPage loginPage = pageFactory.getPage(LoginPage.class);

// Convenience method approach:
pageFactory.loginPage().navigate();
pageFactory.productsPage().addProductToCart("item");
```

### Pros:
- **Most flexible** - uses generics and reflection
- Built-in caching mechanism
- Easy to extend with custom logic
- Can work with any page object class dynamically
- Professional/enterprise-grade pattern

### Cons:
- More complex implementation
- Uses reflection (minor performance overhead)
- Requires understanding of generics

### Best For:
- Large enterprise projects
- When you need dynamic page creation
- Teams comfortable with advanced Java patterns

---

## Approach 4: Inheritance with Getters in BaseTest

### Code:
```java
// In BaseTest
protected LoginPage getLoginPage() {
    return new LoginPage(page);
}

protected ProductsPage getProductsPage() {
    return new ProductsPage(page);
}

// In tests:
getLoginPage().navigate();
getProductsPage().addProductToCart("item");
```

### Pros:
- Simple and clean test code
- No additional classes needed
- Direct method calls

### Cons:
- Creates new instance every time (no caching)
- Can lead to bloated BaseTest class
- Not memory efficient

---

## Migration Guide: From Manual to PageManager

### Before:
```java
private LoginPage loginPage;
private ProductsPage productsPage;

@BeforeEach
void initializePages() {
    loginPage = new LoginPage(page);
    productsPage = new ProductsPage(page);
}

@Test
void testLogin() {
    loginPage.navigate();
    loginPage.login("user", "pass");
    assertTrue(productsPage.isDisplayed());
}
```

### After (PageManager):
```java
protected PageManager pageManager;

@BeforeEach
void initializePageManager() {
    pageManager = new PageManager(page);
}

@Test
void testLogin() {
    pageManager.getLoginPage().navigate();
    pageManager.getLoginPage().login("user", "pass");
    assertTrue(pageManager.getProductsPage().isDisplayed());
}
```

### After (PageFactory):
```java
protected PageFactory pageFactory;

@BeforeEach
void initializePageFactory() {
    pageFactory = new PageFactory(page);
}

@Test
void testLogin() {
    pageFactory.loginPage().navigate();
    pageFactory.loginPage().login("user", "pass");
    assertTrue(pageFactory.productsPage().isDisplayed());
}
```

---

## Performance Comparison

| Approach | Memory Usage | Initialization Time | Code Maintainability |
|----------|--------------|---------------------|---------------------|
| Manual | High (all pages created) | Fast | Low |
| PageManager | Low (lazy) | Fast | High |
| PageFactory | Low (cached) | Slightly slower | Very High |
| BaseTest Getters | Medium (recreated) | Medium | Medium |

---

## Recommendation

**For this project, use PageManager** because:
1. Simple to understand and implement
2. Efficient (lazy initialization)
3. Clean test code
4. Easy to maintain
5. No reflection overhead

**Use PageFactory if:**
- You have 10+ page objects
- You need dynamic page creation
- You want enterprise-grade architecture
- Your team is experienced with design patterns

---

## Example Files

- `PageManager.java` - Simple lazy initialization approach
- `PageFactory.java` - Advanced factory pattern approach
- `SauceDemoTestWithPageManager.java` - Example tests using PageManager
- `SauceDemoTestWithFactory.java` - Example tests using PageFactory

Choose the approach that fits your project size and team expertise!
