package com.mycompany.client;


// Selenide
import com.codeborne.selenide.Selenide;


// JUnit 4.x testing
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import static com.codeborne.selenide.WebDriverRunner.isJBrowser;
import static com.codeborne.selenide.Screenshots.takeScreenShot;

// @Ignore
public class PageObjectsTest {

    HomePage homePage;    

    @Before
    public void instanceHomePage()  {
        Selenide.open("index.html");
        homePage = new HomePage();
    }


    @Test
    public void homePageColors() {

        // 'JBrowserDriver' does not return attribute strings; Skip these tests
        if( isJBrowser() )
            return;

        assertEquals("blue", homePage.getBackgroundColor("blue"));
        assertEquals("red", homePage.getBackgroundColor("red"));
        assertEquals("yellow", homePage.getBackgroundColor("yellow"));
        assertEquals("green", homePage.getBackgroundColor("green"));
        assertEquals("gray", homePage.getBackgroundColor("gray"));
        assertEquals("white", homePage.getBackgroundColor("white"));
        assertEquals("black", homePage.getBackgroundColor("black"));
        assertEquals("pink", homePage.getBackgroundColor("pink"));
        assertEquals("brown", homePage.getBackgroundColor("brown"));
    }

    @Test
    public void selectBlueBaseball() {
        SportsPage sportsPage =  homePage.selectColor("blue");
        assertNotNull(sportsPage);

        Dialog dialog = sportsPage.selectSport("Baseball");
        assertEquals("Do you like Baseball?", dialog.getTitle());

        dialog.selectYes();

        if(isJBrowser() == false)
            assertFalse(dialog.isDisplayed());

        sportsPage.backNavigationAction();
        takeScreenShot("BackToHome");
    }

    @Test
    public void selectRedFootball() {
        SportsPage sportsPage =  homePage.selectColor("red");
        assertNotNull(sportsPage);

        Dialog dialog = sportsPage.selectSport("Football");
        assertEquals("Do you like Football?", dialog.getTitle());

        dialog.selectNo();
        if(isJBrowser() == false)
            assertFalse(dialog.isDisplayed());
        
        sportsPage.backNavigationAction();

    }

}
