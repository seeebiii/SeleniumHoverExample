# SeleniumHoverExample
Example project to demonstrate how you test popups and hovers with Selenium

## Getting Started
1. Start a local server by running ```mvn spring-boot:run```
2. You can open ```http://localhost:8080/index.html``` to test the popup by yourself
3. Run the tests within ```SeleniumHoverExampleApplicationTests```, e.g. run them in your IDE

## How to simply test a popup
```
// In the following a selector is something like By.id("identifier")
 
// use the Actions class from Selenium to perform custom "mouse" actions
Actions builder = new Actions(driver);
 
// move "mouse" to popup link which will open the popup and
// then move to the popup in order to avoid automatic closing of it
builder.moveToElement(driver.findElement(POPUP_LINK_SELECTOR))
       .moveToElement(driver.findElement(POPUP_SELECTOR))
       .build()
       .perform();
```

Please take a look at another example in ```SeleniumHoverExampleApplicationTests```.
