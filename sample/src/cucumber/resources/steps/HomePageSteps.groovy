this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

import com.mycompany.client.HomePage

// Selenide
import com.codeborne.selenide.Selenide
import cucumber.api.PendingException

import static com.codeborne.selenide.WebDriverRunner.isJBrowser;


Given(~/a running 'SmartGWT-mobile Sample' web application/) { ->
	Selenide.open("index.html");
    homePage = new HomePage();
    assert homePage != null
}

When(~/the color (.+) is clicked/) { color ->
    sportsPage = homePage.selectColor(color)
}

Then(~/the application will return to the 'Home' page/) { ->
    
	if(isJBrowser() == false)
    	assert homePage.isDisplayed()
}
