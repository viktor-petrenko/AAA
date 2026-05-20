package com.viktor.aaalife.setup.ui.pages;

import com.viktor.aaalife.setup.config.PropertyReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(PropertyReader.get("ui.base.url"));
        visible(USERNAME_INPUT);
        return this;
    }

    public InventoryPage login(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.waitUntilLoaded();

        return inventoryPage;
    }

    public InventoryPage loginAsStandardUser() {
        return login(
                PropertyReader.get("sauce.username"),
                PropertyReader.get("sauce.password")
        );
    }
}