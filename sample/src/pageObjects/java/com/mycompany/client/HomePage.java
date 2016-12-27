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
import static com.codeborne.selenide.WebDriverRunner.isJBrowser;

public class HomePage {

    private static final String PAGE_ID = "home-page";
    private static final int WAIT = 500;


    public HomePage() {
        $(byId(PAGE_ID)).waitUntil(appears, WAIT);
    }

    public SportsPage selectColor(String color) {

        SportsPage sportsPage = null;
        SelenideElement element = getButtonElement(color);

        if(element != null) {
            element.click(); 
            sportsPage = new SportsPage();   
        }

        return sportsPage;
    }

    public String getBackgroundColor(String color) {

        // JBrowser does not support returning attributes; Return input
        if(isJBrowser())
            return color;

        SelenideElement element = getButtonElement(color);
        
        return ( element != null ) ? getValueForStyle(element, "background-color") : "";
    }

    private SelenideElement getButtonElement(String color) {

        SelenideElement element = $(byId(PAGE_ID + "-" + color)).waitUntil(appears, WAIT);
        
        if(element != null && element.isDisplayed() == false)
            element = null;

        return element;
    }

    private String getValueForStyle(final SelenideElement element, final String property) {

        String style = element.attr("style");

        if(style == null || style.trim().length() == 0)
            return "";

        int indexOfProperty = style.indexOf(property);
        if(indexOfProperty == -1)
            return "";

        int indexOfSemicolon = style.indexOf(";", indexOfProperty);
        int indexOfValue = indexOfProperty + property.length() + 2;

        return style.substring(indexOfValue, indexOfSemicolon);

    }

    public boolean isDisplayed() {
        return $(byId(PAGE_ID)).isDisplayed();
    }

}