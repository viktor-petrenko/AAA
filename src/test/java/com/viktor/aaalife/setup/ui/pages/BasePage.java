package com.viktor.aaalife.setup.ui.pages;

import com.viktor.aaalife.setup.config.PropertyReader;
import com.viktor.aaalife.setup.ui.utils.ClickHighlighter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base class for all UI Page Objects.
 *
 * <p>Contains common WebDriver interactions and explicit wait helpers.
 * Page classes should expose business-level actions while this class handles
 * low-level Selenium operations such as clicking, typing, and waiting.</p>
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Waits until an element is visible on the page.
     *
     * @param locator Selenium locator of the target element
     * @return visible WebElement
     */
    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until an element is ready for user interaction.
     *
     * @param locator Selenium locator of the target element
     * @return clickable WebElement
     */
    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        WebElement element = clickable(locator);
        boolean highlightClicks = Boolean.parseBoolean(
                PropertyReader.getOrDefault("ui.highlight.clicks", "false")
        );

        if (highlightClicks) {
            ClickHighlighter.markClick(driver, element);
        }

        element.click();

        if (highlightClicks) {
            try {
                ClickHighlighter.removeClickMarkers(driver);
            } catch (RuntimeException ignored) {
                // Page may navigate immediately after click, so the old DOM marker may no longer exist.
            }
        }
    }

    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String textOf(By locator) {
        return visible(locator).getText().trim();
    }

    protected void waitForUrlContains(String expectedUrlPart) {
        wait.until(ExpectedConditions.urlContains(expectedUrlPart));
    }
}