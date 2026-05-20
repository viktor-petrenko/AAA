package com.viktor.aaalife.setup.ui.base;

import com.viktor.aaalife.setup.ui.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;

/**
 * Base class for UI tests.
 *
 * <p>Provides access to the current WebDriver and guarantees browser cleanup
 * after each UI test method. Browser creation is delegated to {@link DriverManager}
 * and happens lazily when the test first requests a driver.</p>
 */
public abstract class UiBaseTest {

    /**
     * Returns the WebDriver instance for the current test.
     *
     * <p>Tests should use this method instead of storing their own driver field.
     * This keeps driver lifecycle management centralized in {@link DriverManager}.</p>
     *
     * @return active WebDriver instance for the current test
     */
    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    /**
     * Closes the browser after each UI test.
     *
     * <p>The method always runs, even if the test fails, to avoid leaving browser
     * processes open after the test run.</p>
     */
    @AfterMethod(alwaysRun = true)
    public void tearDownUi() {
        DriverManager.quitDriver();
    }
}