package com.mycompany.client;

// Selenide
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

// Java Collections
import java.util.Map;
import java.util.HashMap;

// Static methods
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Condition.appears;
import static com.codeborne.selenide.Condition.disappears;
import static com.codeborne.selenide.Screenshots.takeScreenShot;
import static com.codeborne.selenide.WebDriverRunner.isJBrowser;
import static com.codeborne.selenide.WebDriverRunner.isHeadless;

public class SportsPage {


    private static final String PAGE_ID = "sports-page";
    private static final String BUTTONS = "div.customTintedToolStrip div.sc-button";
    private static final String BACK = "#navigationBar > div.sc-button:nth-child(1)";
    private static final int WAIT = 500;


    public SportsPage() {

        // Use the page ID to ensure loading
        $(byId(PAGE_ID)).waitUntil(appears, WAIT);
    }

    public Dialog selectSport(String sport) {

        Dialog dialog = null;

        SelenideElement element = getButtonElement(sport);

        if(element != null && element.isDisplayed()) {
            element.click(); 
            dialog = new Dialog();
        }
        
        return dialog;
    }

    public void backNavigationAction() {

        SelenideElement element = $(BACK);

        if(element.isDisplayed()) {
            element.click();
            // if(isJBrowser() == false)
            //     element.waitUntil(disappears, WAIT);
        }

    }

    public boolean isDisplayed() {
        return $(byId(PAGE_ID)).isDisplayed();
    }

    //  P R I V A T E   ====================================================================

    private SelenideElement getButtonElement(String sport) {

        SelenideElement element = $(byId(PAGE_ID + "-" + sport)).waitUntil(appears, WAIT);
        
        if(element != null && element.isDisplayed() == false)
            element = null;

        return element;
    }

}