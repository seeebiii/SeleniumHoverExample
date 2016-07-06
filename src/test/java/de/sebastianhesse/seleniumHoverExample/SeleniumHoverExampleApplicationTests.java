package de.sebastianhesse.seleniumHoverExample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeleniumHoverExampleApplication.class)
@WebAppConfiguration
public class SeleniumHoverExampleApplicationTests {

    public static final By POPUP_LINK_SELECTOR = By.cssSelector(".popupLink");
    public static final By POPUP_SELECTOR = By.id("popup");
    public static final By OTHER_SELECTOR = By.id("other");

    private WebDriver driver;


    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
        driver.navigate().to("http://localhost:8080/index.html");
    }


    @After
    public void tearDown() {
        driver.close();
    }


    /**
     * Solution A: Test a popup which opens directly after you hover over a link.
     */
    @Test
    public void testPopupWithoutWaiting() {
        // first find the link which opens the popup
        WebElement popupLink = driver.findElement(POPUP_LINK_SELECTOR);
        assertTrue("popup link shoud be displayed", popupLink.isDisplayed());

        // then use the Actions class from Selenium to perform custom "mouse" actions
        Actions builder = new Actions(driver);

        // move "mouse" to popup link which will open the popup and
        // then move to the popup in order to avoid automatic closing of it
        builder.moveToElement(popupLink)
                .moveToElement(driver.findElement(POPUP_SELECTOR))
                .build()
                .perform();

        // make sure that the popup is really visible
        WebElement popup = waiter(1).until(ExpectedConditions.visibilityOfElementLocated(POPUP_SELECTOR));
        assertTrue("popup should be visible", popup.isDisplayed());

        // now do whatever you want on your popup...
    }


    /**
     * Solution B: Test a popup which needs some time to open. This is the safe way to test any popup.
     */
    @Test
    public void testWaitForPopupToBeOpen() {
        // first find the link which opens the popup
        WebElement popupLink = driver.findElement(POPUP_LINK_SELECTOR);
        assertTrue("popup link should be displayed", popupLink.isDisplayed());

        // then use the Actions class from Selenium to perform custom "mouse" actions
        Actions builder = new Actions(driver);

        // move "mouse" to the popup link which will open the popup
        builder.moveToElement(popupLink)
                .build()
                .perform();

        // wait for popup to be open and make sure it is visible
        WebElement popup = waiter(1).until(ExpectedConditions.visibilityOfElementLocated(POPUP_SELECTOR));
        assertTrue("popup should be visible", popup.isDisplayed());

        // move "mouse" to popup in order to avoid that it's closing automatically
        builder.moveToElement(driver.findElement(POPUP_SELECTOR))
                .build()
                .perform();

        // if you want, you can test that the popup won't close automatically:
        // assertThatPopupStaysVisible(popupSelector, 3);

        // now do whatever you want on your popup...


        // if necessary, then properly close popup by moving "mouse" away from the popup
        builder.moveToElement(driver.findElement(OTHER_SELECTOR)).build().perform();
        boolean isPopupInvisible = waiter(1).until(ExpectedConditions.invisibilityOfElementLocated(POPUP_SELECTOR));
        assertTrue("popup should not be visible anymore.", isPopupInvisible);
    }


    /**
     * Asserts that the popup won't close within the given time.
     *
     * @param popupSelector selector to identify popup
     * @param seconds       time in seconds to wait for popup to disappear
     */
    private void assertThatPopupStaysVisible(By popupSelector, long seconds) {
        try {
            waiter(seconds).until(ExpectedConditions.invisibilityOfElementLocated(popupSelector));
        } catch (TimeoutException e) {
            WebElement popup = driver.findElement(popupSelector);
            assertTrue("popup should still be visible to user", popup.isDisplayed());
        }
    }


    private WebDriverWait waiter(long seconds) {
        return new WebDriverWait(driver, seconds);
    }

}
