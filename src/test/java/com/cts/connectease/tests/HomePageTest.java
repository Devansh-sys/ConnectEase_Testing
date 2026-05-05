package com.cts.connectease.tests;

import com.cts.connectease.base.BaseTest;
import com.cts.connectease.pages.HomePage;
import com.cts.connectease.pages.LoginPage;
import com.cts.connectease.pages.NavbarPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Home Page Test Cases — Gobika
 * Covers: CE_FE_TS_11 to CE_FE_TS_15
 */
public class HomePageTest extends BaseTest {

    private HomePage   homePage;
    private NavbarPage navbarPage;
    private LoginPage  loginPage;

    @BeforeMethod
    public void initPages() {
        homePage   = new HomePage(driver);
        navbarPage = new NavbarPage(driver);
        loginPage  = new LoginPage(driver);
    }

    // CE_FE_TS_11 — Hero section content and Browse Services CTA
    @Test(description = "CE_FE_TS_11 - Hero section should show ConnectEase brand name and search button")
    public void testHeroSectionContentAndCTA() {
        homePage.navigateTo(BASE_URL);

        Assert.assertTrue(
            homePage.isConnectEaseBrandVisible(),
            "Hero section should display 'ConnectEase' brand name"
        );
        Assert.assertTrue(
            homePage.isAiBadgeVisible(),
            "AI badge should be visible on the hero section"
        );
        Assert.assertTrue(
            homePage.isSearchButtonVisible(),
            "'Search' button should be visible in the hero section"
        );
    }

    @Test(description = "CE_FE_TS_11 - Search button should navigate to /ai-chat with query")
    public void testHeroSearchButtonNavigatesToAiChat() {
        homePage.navigateTo(BASE_URL);
        homePage.clickSearchButton("Find PG near OMR");

        wait.until(ExpectedConditions.urlContains("/ai-chat"));
        Assert.assertTrue(
            driver.getCurrentUrl().contains("/ai-chat"),
            "Search button should navigate to /ai-chat. Actual URL: " + driver.getCurrentUrl()
        );
    }

    // CE_FE_TS_12 — Category grid navigates to filtered listings
    @Test(description = "CE_FE_TS_12 - Category grid should be visible on Home page")
    public void testCategoryGridIsVisible() {
        homePage.navigateTo(BASE_URL);

        Assert.assertTrue(
            homePage.isCategoryGridVisible(),
            "Category grid should be visible on the home page"
        );
        int count = homePage.getCategoryCount();
        Assert.assertTrue(count > 0, "At least one category should be displayed. Found: " + count);
    }

    @Test(description = "CE_FE_TS_12 - Clicking a category should navigate to /services?categoryId={cid}")
    public void testCategoryCardNavigatesToFilteredListings() {
        homePage.navigateTo(BASE_URL);

        int count = homePage.getCategoryCount();
        Assert.assertTrue(count > 0, "No category cards found on home page");

        homePage.clickCategoryByIndex(0);

        wait.until(ExpectedConditions.urlContains("/services"));
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(
            currentUrl.contains("/services"),
            "Clicking category should navigate to /services. Actual: " + currentUrl
        );
        Assert.assertTrue(
            currentUrl.contains("categoryId="),
            "URL should contain categoryId filter. Actual: " + currentUrl
        );
    }

    // CE_FE_TS_13 — Featured Services section displays service cards
    @Test(description = "CE_FE_TS_13 - Category/Featured section should display service cards")
    public void testFeaturedServicesSectionIsVisible() {
        homePage.navigateTo(BASE_URL);

        Assert.assertTrue(
            homePage.isCategoryGridVisible(),
            "Featured/Categories section should be visible on the home page"
        );
        Assert.assertTrue(
            homePage.getCategoryCount() > 0,
            "Featured service category cards should be displayed"
        );
    }

    // CE_FE_TS_14 — AI Chat entry point navigates to /ai-chat
    @Test(description = "CE_FE_TS_14 - AI Search link in navbar should navigate to /ai-chat")
    public void testAiChatNavLinkNavigatesToAiChat() {
        homePage.navigateTo(BASE_URL);

        driver.findElement(By.cssSelector("a[routerlink='/ai-chat']")).click();

        wait.until(ExpectedConditions.urlContains("/ai-chat"));
        Assert.assertTrue(
            driver.getCurrentUrl().contains("/ai-chat"),
            "AI Search link should navigate to /ai-chat. Actual: " + driver.getCurrentUrl()
        );
    }

    // CE_FE_TS_15 — My Chats link visible only when authenticated
    @Test(description = "CE_FE_TS_15 - My Chats link should NOT be visible for guest users")
    public void testChatsLinkHiddenForGuest() {
        homePage.navigateTo(BASE_URL);

        Assert.assertFalse(
            navbarPage.isChatsLinkVisible(),
            "'Chats' link should NOT be visible in navbar for guest/unauthenticated users"
        );
    }

    @Test(description = "CE_FE_TS_15 - My Chats link SHOULD be visible when logged in as Customer")
    public void testChatsLinkVisibleWhenLoggedIn() {
        loginPage.navigateTo(BASE_URL);
        loginPage.login(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));

        Assert.assertTrue(
            navbarPage.isChatsLinkVisible(),
            "'Chats' link should be visible in navbar for authenticated users"
        );
    }
}
