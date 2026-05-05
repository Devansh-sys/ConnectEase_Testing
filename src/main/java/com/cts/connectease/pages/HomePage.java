package com.cts.connectease.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heroTitle       = By.cssSelector(".hero-title");
    private final By aiBadge         = By.cssSelector(".ai-badge");
    private final By heroSearchBtn   = By.cssSelector(".search-wrapper .btn-primary");
    private final By heroSearchInput = By.cssSelector(".search-wrapper input");
    private final By categoryCards   = By.cssSelector(".category-card");
    private final By categoriesGrid  = By.cssSelector(".categories-grid");
    private final By gradientText    = By.cssSelector(".gradient-text");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(heroTitle));
    }

    public boolean isHeroTitleVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(heroTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getHeroTitleText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(heroTitle)).getText();
    }

    public boolean isAiBadgeVisible() {
        return !driver.findElements(aiBadge).isEmpty() && driver.findElement(aiBadge).isDisplayed();
    }

    public boolean isSearchButtonVisible() {
        return !driver.findElements(heroSearchBtn).isEmpty() && driver.findElement(heroSearchBtn).isDisplayed();
    }

    public void clickSearchButton(String query) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(heroSearchInput));
        input.clear();
        input.sendKeys(query);
        driver.findElement(heroSearchBtn).click();
    }

    public boolean isCategoryGridVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(categoriesGrid)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getCategoryCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoriesGrid));
        List<WebElement> cards = driver.findElements(categoryCards);
        return cards.size();
    }

    public void clickCategoryByIndex(int index) {
        List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryCards));
        cards.get(index).click();
    }

    public boolean isConnectEaseBrandVisible() {
        try {
            WebElement brand = wait.until(ExpectedConditions.visibilityOfElementLocated(gradientText));
            return brand.getText().toLowerCase().contains("connectease");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnHomePage() {
        String url = driver.getCurrentUrl();
        return url.endsWith("/") || url.equals("http://localhost:4200");
    }
}
