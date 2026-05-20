package com.viktor.aaalife.setup.ui.listeners;

import com.viktor.aaalife.setup.ui.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener responsible for capturing UI failure artifacts.
 *
 * <p>The listener is triggered only when a test fails. It keeps screenshot and HTML
 * dump logic outside of test methods and base classes, so tests remain focused on
 * business flow validation.</p>
 */
public class UiFailureListener implements ITestListener {

    /**
     * Captures screenshot and HTML dump after a failed UI test.
     *
     * <p>The listener uses {@link DriverManager#getCurrentDriver()} instead of
     * {@link DriverManager#getDriver()} to avoid accidentally creating a new browser
     * after a failure.</p>
     *
     * @param result TestNG result object containing failed test metadata
     */
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverManager.getCurrentDriver();

        FailureArtifactManager.capture(
                driver,
                result.getMethod().getMethodName()
        );
    }
}