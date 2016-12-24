this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

import com.mycompany.client.HomePage
import com.mycompany.client.HomePageImpl

// Selenide
import com.codeborne.selenide.Selenide

import cucumber.api.PendingException


// Shutdown the browser and reopen in order run a clean test


Given(~/a running 'SmartGWT-mobile Sample' web application/) { ->
	Selenide.close();
	Selenide.open("index.html");
    homePage = new HomePageImpl();
    assert homePage != null
}

When(~/the color (.+) is clicked/) { color ->
    sportsPage = homePage.selectColor(color)
}

Then(~/the application will return to the 'Home' page/) { ->
    // assert homePage.isDisplayed()
}
