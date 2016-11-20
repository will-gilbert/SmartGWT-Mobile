

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features="src/cucumber/resources/sample.feature",
    glue={"src/cucumber/resources/steps/"}
)
public class GlueTest {}
