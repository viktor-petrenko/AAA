package com.viktor.aaalife.setup.ui.driver;

import org.openqa.selenium.WebDriver;
/**
 * Manages the WebDriver lifecycle for UI tests.
 *
 * <p>The driver is stored in a {@link ThreadLocal} to keep browser instances isolated
 * between test threads. This makes the implementation safe for future parallel test
 * execution, even though the current assessment suite runs in a simple sequential mode.</p>
 *
 * <p>The driver is created lazily: no browser is opened until {@link #getDriver()}
 * is called for the first time in a test.</p>
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    /**
     * Returns the current WebDriver without creating a new browser.
     *
     * <p>This method is used by listeners and failure handlers where creating a new
     * browser would be incorrect. For example, when capturing a screenshot after a
     * test failure, we only want to use the browser that already belongs to the failed test.</p>
     *
     * @return current WebDriver instance, or {@code null} if no driver was created
     */
    public static WebDriver getCurrentDriver() {
        return DRIVER.get();
    }

    /**
     * Returns the active WebDriver for the current thread.
     *
     * <p>If no driver exists yet, a new one is created through {@link BrowserFactory}.
     * This keeps browser creation centralized and hidden from test classes.</p>
     *
     * @return active WebDriver instance for the current test thread
     */
    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(BrowserFactory.createDriver());
        }

        return DRIVER.get();
    }

    /**
     * Closes the current browser and removes the WebDriver from ThreadLocal storage.
     *
     * <p>Removing the ThreadLocal value is important to avoid memory leaks and stale
     * browser references between tests.</p>
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();

        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}