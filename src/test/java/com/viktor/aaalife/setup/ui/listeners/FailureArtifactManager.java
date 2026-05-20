package com.viktor.aaalife.setup.ui.listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Captures diagnostic artifacts when a UI test fails.
 *
 * <p>The manager writes failure evidence into {@code target/failure-artifacts}.
 * Captured artifacts include a screenshot and an HTML dump of the current page.
 * These files help debug UI failures without rerunning the test immediately.</p>
 */
public final class FailureArtifactManager {

    private static final Path FAILURE_ARTIFACTS_DIRECTORY = Path.of("target", "failure-artifacts");

    private FailureArtifactManager() {
    }

    /**
     * Captures all configured failure artifacts for the provided WebDriver session.
     *
     * <p>If the driver is {@code null}, the method exits quietly. This prevents listener
     * failures when a test fails before the browser is initialized.</p>
     *
     * @param driver active WebDriver instance from the failed test
     * @param testName name of the failed test method
     */
    public static void capture(WebDriver driver, String testName) {
        if (driver == null) {
            return;
        }

        String safeTestName = testName.replaceAll("[^a-zA-Z0-9-_]", "_");

        try {
            Files.createDirectories(FAILURE_ARTIFACTS_DIRECTORY);

            captureScreenshot(driver, safeTestName);
            captureHtmlDump(driver, safeTestName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to capture failure artifacts for test: " + testName, e);
        }
    }

    /**
     * Captures a PNG screenshot of the current browser state.
     *
     * @param driver active WebDriver instance
     * @param safeTestName sanitized test name used as a file name
     * @throws IOException if the screenshot file cannot be written
     */
    private static void captureScreenshot(WebDriver driver, String safeTestName) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path screenshotPath = FAILURE_ARTIFACTS_DIRECTORY.resolve(safeTestName + ".png");
        Files.copy(screenshot.toPath(), screenshotPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Saves the current page source as an HTML file.
     *
     * <p>The HTML dump is useful when a screenshot alone is not enough, for example
     * when verifying hidden elements, generated markup, or unexpected DOM state.</p>
     *
     * @param driver active WebDriver instance
     * @param safeTestName sanitized test name used as a file name
     * @throws IOException if the HTML file cannot be written
     */
    private static void captureHtmlDump(WebDriver driver, String safeTestName) throws IOException {
        Path htmlPath = FAILURE_ARTIFACTS_DIRECTORY.resolve(safeTestName + ".html");
        String pageSource = driver.getPageSource();
        if (pageSource == null) {
            pageSource = "";
        }
        Files.writeString(htmlPath, pageSource, StandardCharsets.UTF_8);
    }
}