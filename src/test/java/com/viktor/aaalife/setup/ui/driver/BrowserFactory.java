package com.viktor.aaalife.setup.ui.driver;

import com.viktor.aaalife.setup.config.PropertyReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Factory responsible for creating browser-specific WebDriver instances.
 *
 * <p>The current assessment implementation supports Chrome only, which is enough
 * for the required UI smoke flow. Firefox and Safari branches are intentionally
 * kept as extension points to show where cross-browser support would be added
 * without changing tests or Page Objects.</p>
 */
public final class BrowserFactory {

    private BrowserFactory() {
    }

    /**
     * Creates a WebDriver instance based on the {@code ui.browser} property.
     *
     * <p>Supported value for the current assessment scope: {@code chrome}.
     * Future extension points: {@code firefox}, {@code safari}.</p>
     *
     * @return configured WebDriver instance
     */
    public static WebDriver createDriver() {
        String browser = PropertyReader.getOrDefault("ui.browser", "chrome")
                .trim()
                .toLowerCase();

        return switch (browser) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "safari" -> createSafariDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000");

        if (isHeadless()) {
            options.addArguments("--headless=new");
        }

        return new ChromeDriver(options);
    }

    /**
     * Placeholder for future Firefox support.
     *
     * <p>Implementation would add WebDriverManager.firefoxdriver().setup(),
     * FirefoxOptions, headless configuration, and return FirefoxDriver.</p>
     */
    private static WebDriver createFirefoxDriver() {
        throw new UnsupportedOperationException(
                "Firefox is not implemented in this assessment scope. Use ui.browser=chrome."
        );
    }

    /**
     * Placeholder for future Safari support.
     *
     * <p>Safari requires local Safari remote automation setup and is usually not
     * configured through WebDriverManager the same way Chrome/Firefox are.</p>
     */
    private static WebDriver createSafariDriver() {
        throw new UnsupportedOperationException(
                "Safari is not implemented in this assessment scope. Use ui.browser=chrome."
        );
    }

    private static boolean isHeadless() {
        return Boolean.parseBoolean(PropertyReader.getOrDefault("ui.headless", "false"));
    }
}