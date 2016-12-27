package com.mycompany.client;

// Selenide
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

// Static methods
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Condition.appears;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappears;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Screenshots.takeScreenShot;

public class Dialog {

    private static final String ID = "dialog";
    private static final String TITLE = "div.windowHeaderLabel";
    private static final String YES = "div.dialogButtonsContainer > div.actionButton:nth-Child(1)";
    private static final String NO  = "div.dialogButtonsContainer > div.actionButton:nth-Child(2)";
    private static final String OUTSIDE = "div.modalMask";
    private static final int WAIT = 1000;


    SelenideElement dialog;

    public Dialog() {
        dialog = $(byId(ID)).waitUntil(appears, WAIT);
   }

    public String getTitle() {
        SelenideElement element = $(TITLE);
        return (element != null && element.isDisplayed()) ? element.text() :"";
    }

    public void selectYes() {
        SelenideElement element = dialog.$(YES);

        if(element.isDisplayed()) {
            element.click();
            dialog.waitUntil(disappears, WAIT);
        }

    }

    public void selectNo() {
        SelenideElement element = dialog.$(NO);

        if(element.isDisplayed()) {
            element.click();
            dialog.waitUntil(disappear, WAIT);
        }
    }

    public void selectOutside() {
        SelenideElement element = $(OUTSIDE);
        if(element.isDisplayed()) {
            element.click();
            dialog.waitUntil(disappear, WAIT);
        }

    }

    public boolean isDisplayed() {
        if(dialog == null)
            return false;

        return dialog.isDisplayed();
    }

}