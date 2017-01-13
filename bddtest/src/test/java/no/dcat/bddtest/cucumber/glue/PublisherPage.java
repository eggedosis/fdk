package no.dcat.bddtest.cucumber.glue;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/**
 * Cucumber glue class for the publisher feature.
 */
public class PublisherPage extends CommonPage {
    private static final String LOCAL_PATH_TO_IE_DRIVER = "src/main/resources/IEDriverServer.exe";
    private final String page = "publisher";

    @Before
    public void setup() {
        setupDriver();
    }

    @After
    public void shutdown() {
        stopDriver();
    }

    @Given("^I open the Publisher page in the browser\\.$")
    public void i_open_the_Publisher_page_in_the_browser() throws Throwable {
        openPage(page);
    }

    @Then("^the following Publisher and dataset aggregation shall exist:$")
    public void shallHave(DataTable publisherAggrs) throws Throwable {
        WebElement element = null;
        try {
            List<List<String>> publisherAggrsRaw = publisherAggrs.raw();

            for (List<String> publisherAggr : publisherAggrsRaw) {
                String publisherExp = publisherAggr.get(0);
                String countExp = publisherAggr.get(1);

                assertTrue(String.format("The page shall have an element with id %s", publisherExp), driver.findElement(By.id(publisherExp)).isEnabled());

                WebElement publisherElement = driver.findElement(By.id(publisherExp));

                WebElement publisherName = publisherElement.findElement(By.name("publisher"));
                String publisherNameStr = publisherName.getAttribute("innerHTML");

                assertTrue(String.format("The page shall have an element with text %s", publisherExp), publisherExp.equals(publisherNameStr));

                WebElement publisherCount = publisherElement.findElement(By.className("badge"));
                String count = publisherCount.getAttribute("innerHTML");

                assertTrue(String.format("The element %s shall have %s datasets, had %s.", publisherExp, count, count), countExp.equals(count));
            }
        } finally {
            driver.close();
        }
    }

    protected String getEnv(String env) {
        String value = System.getenv(env);

        if (StringUtils.isEmpty(value)) {
            throw new RuntimeException(String.format("Environment %s variable is not defines.", env));
        }

        return value;
    }

    protected int getEnvInt(String env) {
        return Integer.valueOf(getEnv(env));
    }
}