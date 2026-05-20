package com.viktor.aaalife.setup.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By CART_ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilLoaded() {
        waitForUrlContains("/cart.html");
        visible(PAGE_TITLE);
    }

    public List<String> itemNames() {
        return driver.findElements(CART_ITEM_NAMES)
                .stream()
                .map(element -> element.getText().trim())
                .toList();
    }
}