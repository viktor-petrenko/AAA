package com.viktor.aaalife.setup.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By ADD_BACKPACK_BUTTON = By.id("add-to-cart-sauce-labs-backpack");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilLoaded() {
        waitForUrlContains("/inventory.html");
        visible(PAGE_TITLE);
    }

    public InventoryPage addBackpackToCart() {
        click(ADD_BACKPACK_BUTTON);
        visible(CART_BADGE);
        return this;
    }

    public String cartBadgeText() {
        return textOf(CART_BADGE);
    }

    public CartPage openCart() {
        click(CART_LINK);

        CartPage cartPage = new CartPage(driver);
        cartPage.waitUntilLoaded();

        return cartPage;
    }
}