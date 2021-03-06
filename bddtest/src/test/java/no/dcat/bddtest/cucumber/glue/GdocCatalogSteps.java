package no.dcat.bddtest.cucumber.glue;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

/**
 * Created by dask on 16.12.2016.
 */
public class GdocCatalogSteps extends CommonPage {

    //  private final WebDriver driver = WebDriverFactory.createWebDriver();

    private static Logger logger = LoggerFactory.getLogger(GdocCatalogSteps.class);


    public static String PORTAL_URL = "http://localhost:8080"; // = "http://fdk-por-fellesdatakatalog-ut1.ose-npc.brreg.no/"; //"http://portal-fdk.tt1.brreg.no";


    public static String ADMIN_URL = "http://localhost:8082"; // = "http://fdk-adm-fellesdatakatalog-ut1.ose-npc.brreg.no/"; //"http://admin-fdk.tt1.brreg.no";


    public static String GDOC_CONVERT_URL = "http://localhost:8084"; //  = "http://192.168.99.100:8084/convert";


    private static String elasticsearch_host = "http://localhost"; // = "http://elasticsearch-fellesdatakatalog-ut1.ose-npc.brreg.no/";


    private static int elasticsearch_port = 9200;

    @Before
    public void setup() {
        setupDriver();
    }

    @After
    public void shutdown() {
        stopDriver();
    }

    @Given("^I open the admin portal$")
    public void userOpensAdminPortal() throws Throwable {

        driver.get(ADMIN_URL);
        // log inn
        WebElement user = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));
        if (user != null && password != null) {
            user.sendKeys("test_admin");
            password.sendKeys("password");
            WebElement submit = driver.findElement(By.name("submit"));
            submit.submit();
        }
        sleep(2000);

    }

    void registerCatalog(String descriptionInput, String urlInput, String orgnrInput) {
        WebElement description = driver.findElement(By.id("inputDescription"));
        description.sendKeys(descriptionInput);
        WebElement url = driver.findElement(By.id("inputUrl"));
        url.sendKeys(urlInput);
        WebElement orgnumber = driver.findElement(By.id("inputOrgnumber"));
        orgnumber.sendKeys(orgnrInput);

        WebElement saveButton = driver.findElement(By.xpath("/html/body/div/div[3]/div[1]/button"));
        saveButton.click();
    }

    @Given("^I select harvest \"([^\"]*)\" catalog$")
    public void doHarvestGdoc(String query) throws Throwable {
        // find gdoc import
        WebElement row;
        boolean catalogExists = driver.findElements(By.xpath("//tr[td[contains(text(),"+query+")]]")).size() > 0;

        if (!catalogExists) {
            if ("gdoc".equals(query)) {
                registerCatalog("Innføringsteamets gdoc data", "http://gdoc:8080/versions/latest", "91919191");
            } else if ("difi".equals(query)) {
                registerCatalog("difi katalog", "http://data.norge.no/api/dcat2/991825827/data.jsonld", "991825827");
            }
            sleep(1000);
        }

        row = driver.findElement(By.xpath("//tr[td[contains(text(),'gdoc')]]"));
        WebElement harvest = row.findElement(By.xpath("td/a[contains(@href,'admin/harvestDcatSource?')]"));
        logger.info("harvest-url " + harvest.getAttribute("href"));

        harvest.click();

        sleep(2000); // to allow for harvest time
    }

    @Then("^the following dataset detail pages shall exist:$")
    public void norwegianProperties(DataTable datasets) throws Throwable {
        try {

            for (List<String> dataset : datasets.raw()) {
                String dsId = dataset.get(0);
                logger.info("Test dataset {}",dsId);

                openPage("detail?id="+ dsId);

                assertTrue("Detail page has title ", driver.getTitle() != null);
            }
        } finally {
            driver.close();
        }
    }

    @When("^brukeren åpner portalen$")
    public void brukerÅpnerPortalen() throws Throwable {
        driver.get(PORTAL_URL);

    }



}
