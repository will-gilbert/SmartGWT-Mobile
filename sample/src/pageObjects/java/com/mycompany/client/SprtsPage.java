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
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Condition.appears;
import static com.codeborne.selenide.Condition.disappears;

public class SportsPage {


    private static final String PAGE_ID = "sports-page";
    private static final String BUTTONS = "div.customTintedToolStrip div.sc-button";
    private static final String BACK = "div.sc-navigationbar > div.backButton";
    private static final int WAIT = 500;


    private final Map<String,SelenideElement> sports = new HashMap<>();

    public SportsPage() {

        // Use the page ID to ensure loading
        $(byId(PAGE_ID)).waitUntil(appears, WAIT);

        // Find all of the "Sports" buttons
        ElementsCollection elements = $$(BUTTONS);

        for (int i = 0; i < elements.size(); i++) {
            SelenideElement element = elements.get(i);
            if(element != null && element.isDisplayed())
                sports.put(element.text(), element);
       }

    }

    public Dialog selectSport(String sport) {

        Dialog dialog = null;

        SelenideElement element = getButtonElement(sport);

        if(element != null) {
            element.click(); 
            dialog = new Dialog();
        }
        
        return dialog;
    }

    public void backNavigationAction() {
        SelenideElement element = $(BACK);
        if(element.isDisplayed()) {
            element.click();
            element.waitUntil(disappears, WAIT);
        }

    }


    private SelenideElement getButtonElement(String name) {

        SelenideElement element = sports.get(name);
        
        if(element != null && element.isDisplayed() == false)
            element = null;

        return element;
    }

    public boolean isDisplayed() {
        return $(byId(PAGE_ID)).isDisplayed();
    }

}