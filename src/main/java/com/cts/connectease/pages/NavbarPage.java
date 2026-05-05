package com.cts.connectease.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NavbarPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By signInBtn      = By.cssSelector("a.btn-signin");
    private final By getStartedBtn  = By.cssSelector("a.btn-signup");
    private final By chatsLink      = By.cssSelector("a[routerlink='/chats']");
    private final By avatarBtn      = By.cssSelector(".avatar-btn");
    private final By signOutBtn     = By.cssSelector("button.logout");
    private final By logoContainer  = By.cssSelector(".logo-container");

    public NavbarPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isSignInVisible() {
        return !driver.findElements(signInBtn).isEmpty();
    }

    public boolean isGetStartedVisible() {
        return !driver.findElements(getStartedBtn).isEmpty();
    }

    public boolean isChatsLinkVisible() {
        return !driver.findElements(chatsLink).isEmpty();
    }

    public boolean isAvatarVisible() {
        return !driver.findElements(avatarBtn).isEmpty();
    }

    public void clickAvatar() {
        wait.until(ExpectedConditions.elementToBeClickable(avatarBtn)).click();
    }

    public void clickSignOut() {
        clickAvatar();
        wait.until(ExpectedConditions.elementToBeClickable(signOutBtn)).click();
    }

    public void clickSignIn() {
        wait.until(ExpectedConditions.elementToBeClickable(signInBtn)).click();
    }

    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logoContainer)).click();
    }
}
