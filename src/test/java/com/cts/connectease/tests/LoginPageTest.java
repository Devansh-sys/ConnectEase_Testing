package com.cts.connectease.tests;

import com.cts.connectease.base.BaseTest;
import com.cts.connectease.pages.LoginPage;
import com.cts.connectease.pages.NavbarPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Login / Authentication Test Cases — Gobika
 * Covers: CE_FE_TS_04 to CE_FE_TS_10
 */
public class LoginPageTest extends BaseTest {

    private LoginPage loginPage;
    private NavbarPage navbarPage;

    @BeforeMethod
    public void initPages() {
        loginPage  = new LoginPage(driver);
        navbarPage = new NavbarPage(driver);
    }

    // CE_FE_TS_04 — Customer login redirects to Home page
    @Test(description = "CE_FE_TS_04 - Customer login should redirect to Home page /")
    public void testCustomerLoginRedirectsToHome() {
        loginPage.navigateTo(BASE_URL);
        loginPage.login(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);

        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
            currentUrl.equals(BASE_URL + "/") || currentUrl.equals(BASE_URL),
            "Customer should be redirected to Home page. Actual URL: " + currentUrl
        );
        Assert.assertTrue(navbarPage.isAvatarVisible(), "Avatar should be visible after login");
    }

    // CE_FE_TS_05 — Vendor login redirects to Vendor Dashboard
    @Test(description = "CE_FE_TS_05 - Vendor login should redirect to /vendor/dashboard")
    public void testVendorLoginRedirectsToDashboard() {
        loginPage.navigateTo(BASE_URL);
        loginPage.login(VENDOR_EMAIL, VENDOR_PASSWORD);

        wait.until(ExpectedConditions.urlContains("/vendor/dashboard"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
            currentUrl.contains("/vendor/dashboard"),
            "Vendor should be redirected to /vendor/dashboard. Actual URL: " + currentUrl
        );
    }

    // CE_FE_TS_06 — Wrong credentials shows error without redirect
    @Test(description = "CE_FE_TS_06 - Wrong credentials should show error message and stay on /login")
    public void testWrongCredentialsShowsError() {
        loginPage.navigateTo(BASE_URL);
        loginPage.login("wrong@email.com", "wrongpassword");

        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for wrong credentials"
        );
        Assert.assertTrue(
            loginPage.isOnLoginPage(),
            "User should remain on /login page after failed login"
        );
    }

    // CE_FE_TS_07 — Logout clears session and redirects to Home
    @Test(description = "CE_FE_TS_07 - Logout should clear session and redirect to /")
    public void testLogoutClearsSessionAndRedirectsHome() {
        loginPage.navigateTo(BASE_URL);
        loginPage.login(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));

        navbarPage.clickSignOut();
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.cssSelector("a.btn-signin")));

        Assert.assertTrue(navbarPage.isSignInVisible(), "Sign In button should appear after logout");
        Assert.assertTrue(navbarPage.isGetStartedVisible(), "Get Started button should appear after logout");
        Assert.assertFalse(navbarPage.isAvatarVisible(), "Avatar should NOT be visible after logout");

        Object role = ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('role');");
        Assert.assertNull(role, "localStorage role should be cleared after logout");
    }

    // CE_FE_TS_08 — Navbar reflects login/logout state correctly
    @Test(description = "CE_FE_TS_08 - Navbar should show Login/Signup when logged out")
    public void testNavbarLoggedOutState() {
        driver.get(BASE_URL);
        Assert.assertTrue(navbarPage.isSignInVisible(), "Sign In button should be visible when logged out");
        Assert.assertTrue(navbarPage.isGetStartedVisible(), "Get Started button should be visible when logged out");
        Assert.assertFalse(navbarPage.isAvatarVisible(), "Avatar should NOT be visible when logged out");
    }

    @Test(description = "CE_FE_TS_08 - Navbar should show avatar and hide Login/Signup when logged in")
    public void testNavbarLoggedInState() {
        loginPage.navigateTo(BASE_URL);
        loginPage.login(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));

        Assert.assertTrue(navbarPage.isAvatarVisible(), "Avatar should be visible when logged in");
        Assert.assertFalse(navbarPage.isSignInVisible(), "Sign In button should NOT be visible when logged in");
        Assert.assertFalse(navbarPage.isGetStartedVisible(), "Get Started button should NOT be visible when logged in");
    }

    // CE_FE_TS_09 — authGuard redirects unauthenticated user to /login
    @Test(description = "CE_FE_TS_09 - Accessing /profile without login should redirect to /login")
    public void testAuthGuardRedirectsProfileToLogin() {
        driver.get(BASE_URL + "/profile");
        wait.until(ExpectedConditions.urlContains("/login"));

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/login"),
            "Unauthenticated access to /profile should redirect to /login"
        );
    }

    @Test(description = "CE_FE_TS_09 - Accessing /chats without login should redirect to /login")
    public void testAuthGuardRedirectsChatsToLogin() {
        driver.get(BASE_URL + "/chats");
        wait.until(ExpectedConditions.urlContains("/login"));

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/login"),
            "Unauthenticated access to /chats should redirect to /login"
        );
    }

    // CE_FE_TS_10 — vendorGuard redirects Customer from /vendor/dashboard to /
    @Test(description = "CE_FE_TS_10 - Customer accessing /vendor/dashboard should be redirected to Home")
    public void testVendorGuardRedirectsCustomerToHome() {
        loginPage.navigateTo(BASE_URL);
        loginPage.login(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));

        driver.get(BASE_URL + "/vendor/dashboard");
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
            currentUrl.equals(BASE_URL + "/") || currentUrl.equals(BASE_URL),
            "Customer should be redirected to Home from /vendor/dashboard. Actual: " + currentUrl
        );
    }
}
