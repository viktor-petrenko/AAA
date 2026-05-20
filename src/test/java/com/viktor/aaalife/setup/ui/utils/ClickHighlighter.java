package com.viktor.aaalife.setup.ui.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class ClickHighlighter {

    private ClickHighlighter() {
    }

    public static void markClick(WebDriver driver, WebElement element) {
        String script = """
                const element = arguments[0];
                const rect = element.getBoundingClientRect();

                const marker = document.createElement('div');
                marker.setAttribute('data-test-click-marker', 'true');

                marker.style.position = 'fixed';
                marker.style.left = (rect.left + rect.width / 2 - 10) + 'px';
                marker.style.top = (rect.top + rect.height / 2 - 10) + 'px';
                marker.style.width = '20px';
                marker.style.height = '20px';
                marker.style.border = '3px solid red';
                marker.style.borderRadius = '50%';
                marker.style.background = 'rgba(255, 0, 0, 0.25)';
                marker.style.zIndex = '999999';
                marker.style.pointerEvents = 'none';

                document.body.appendChild(marker);
                """;

        ((JavascriptExecutor) driver).executeScript(script, element);
    }
}