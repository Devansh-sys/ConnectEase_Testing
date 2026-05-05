package com.cts.connectease.base;

import com.cts.connectease.constants.AppConstants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected final String BASE_URL          = AppConstants.BASE_URL;
    protected final String CUSTOMER_EMAIL    = AppConstants.CUSTOMER_EMAIL;
    protected final String CUSTOMER_PASSWORD = AppConstants.CUSTOMER_PASSWORD;
    protected final String VENDOR_EMAIL      = AppConstants.VENDOR_EMAIL;
    protected final String VENDOR_PASSWORD   = AppConstants.VENDOR_PASSWORD;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().browserVersion("146").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
