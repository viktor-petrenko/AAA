package com.viktor.aaalife.tests.ui;

import com.viktor.aaalife.setup.ui.base.UiBaseTest;
import com.viktor.aaalife.setup.ui.listeners.UiFailureListener;
import com.viktor.aaalife.setup.ui.pages.CartPage;
import com.viktor.aaalife.setup.ui.pages.InventoryPage;
import com.viktor.aaalife.setup.ui.pages.LoginPage;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UiFailureListener.class)
public class SauceDemoSmokeTest extends UiBaseTest {

    @Test(groups = {"ui", "smoke"})
    public void sauceDemo_loginAndAddBackpackToCart_cartContainsSelectedItem() {
        InventoryPage inventoryPage = new LoginPage(driver())
                .open()
                .loginAsStandardUser();

        inventoryPage.addBackpackToCart();

        assertThat(inventoryPage.cartBadgeText())
                .as("Cart badge should show one selected item")
                .isEqualTo("1");

        CartPage cartPage = inventoryPage.openCart();

        assertThat(cartPage.itemNames())
                .as("Cart should contain selected product")
                .contains("Sauce Labs Backpack");
    }
}