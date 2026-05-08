package com.cts.connectease.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object — AI Chat Page (/ai-chat)
 * A conversational interface: the user types a query and the AI responds with
 * service recommendations or category information.
 */
public class AIChatPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By[] pageReadyLocators = {
            By.cssSelector(".ai-chat"),
            By.cssSelector(".ai-chat-page"),
            By.cssSelector("[class*='ai-chat']"),
            By.cssSelector(".chat-container"),
            By.cssSelector("[class*='chat-container']")
    };

    private final By[] queryInputLocators = {
            By.cssSelector("input[placeholder*='ask' i]"),
            By.cssSelector("input[placeholder*='type' i]"),
            By.cssSelector("input[placeholder*='search' i]"),
            By.cssSelector("input[placeholder*='message' i]"),
            By.cssSelector("input[placeholder*='question' i]"),
            By.cssSelector("textarea[placeholder*='ask' i]"),
            By.cssSelector("textarea[placeholder*='message' i]"),
            By.cssSelector(".chat-input input"),
            By.cssSelector(".ai-input input"),
            By.cssSelector("[class*='query'] input"),
            By.cssSelector("input[type='text']"),
            By.cssSelector("textarea")
    };

    private final By[] sendButtonLocators = {
            By.cssSelector("button[type='submit']"),
            By.cssSelector(".send-btn"),
            By.cssSelector("[class*='send']"),
            By.xpath("//button[contains(translate(normalize-space(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'send')]"),
            By.xpath("//button[contains(translate(normalize-space(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'ask')]"),
            By.xpath("//button[contains(translate(normalize-space(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'go')]")
    };

    private final By aiResponseMessages = By.cssSelector(
            ".ai-message, .bot-message, [class*='ai-message'], [class*='bot-message'], " +
            ".response, [class*='response'], .chat-bubble");

    private final By loadingIndicator = By.cssSelector(
            ".loading, .spinner, [class*='loading'], [class*='typing'], [class*='spinner']");

    private final By serviceCards = By.cssSelector(
            ".service-card, .result-card, [class*='service-card'], [class*='result-card']");

    // ── Constructor ───────────────────────────────────────────────────────────

    public AIChatPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Visual helper ─────────────────────────────────────────────────────────

    private void highlight(WebElement el) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.outline='3px solid #f59e0b';arguments[0].style.backgroundColor='#fef9c3';", el);
            Thread.sleep(400);
            js.executeScript("arguments[0].style.outline='';arguments[0].style.backgroundColor='';", el);
        } catch (Exception ignored) {}
    }

    private WebElement findFirst(By[] locators) {
        for (By loc : locators) {
            try {
                List<WebElement> els = driver.findElements(loc);
                for (WebElement el : els) {
                    if (el.isDisplayed()) return el;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/ai-chat");
        wait.until(d -> {
            for (By loc : pageReadyLocators) {
                if (!d.findElements(loc).isEmpty()) return true;
            }
            // Fallback: any input is present
            return !d.findElements(By.cssSelector("input, textarea")).isEmpty();
        });
    }

    // ── Verifications ─────────────────────────────────────────────────────────

    public boolean isPageDisplayed() {
        for (By loc : pageReadyLocators) {
            if (!driver.findElements(loc).isEmpty()) return true;
        }
        return !driver.findElements(By.cssSelector("input, textarea")).isEmpty();
    }

    public boolean isQueryInputVisible() { return findFirst(queryInputLocators) != null; }

    public boolean isSendButtonVisible() { return findFirst(sendButtonLocators) != null; }

    public boolean isOnAiChatPage() { return driver.getCurrentUrl().contains("/ai-chat"); }

    public int getAiResponseCount() {
        return (int) driver.findElements(aiResponseMessages).stream().filter(e -> {
            try { return e.isDisplayed(); } catch (Exception ex) { return false; }
        }).count();
    }

    public boolean hasServiceResultCards() {
        return driver.findElements(serviceCards).stream().anyMatch(e -> {
            try { return e.isDisplayed(); } catch (Exception ex) { return false; }
        });
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /** Types a question and clicks Send (or presses Enter if no Send button found). */
    public void askQuestion(String question) {
        WebElement input = findFirst(queryInputLocators);
        if (input != null) {
            highlight(input);
            input.clear();
            input.sendKeys(question);

            WebElement sendBtn = findFirst(sendButtonLocators);
            if (sendBtn != null) {
                highlight(sendBtn);
                try { sendBtn.click(); }
                catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sendBtn); }
            } else {
                input.sendKeys(Keys.RETURN);
            }
        }
    }

    /**
     * Waits up to 30 s for an AI response to appear (AI calls can be slow).
     * Returns true if at least one response message is visible.
     */
    public boolean waitForAiResponse(int previousCount) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(d -> {
                // Wait for loading spinner to disappear first
                List<WebElement> spinners = d.findElements(loadingIndicator);
                boolean loading = spinners.stream().anyMatch(e -> {
                    try { return e.isDisplayed(); } catch (Exception ex) { return false; }
                });
                if (loading) return false;

                // Then wait for a new message to appear
                long count = d.findElements(aiResponseMessages).stream().filter(e -> {
                    try { return e.isDisplayed(); } catch (Exception ex) { return false; }
                }).count();
                return count > previousCount;
            });
            return true;
        } catch (Exception e) { return false; }
    }
}
