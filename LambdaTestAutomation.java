package com.lambdatest.testng101;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class LambdaTestAutomation {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    @Parameters({"browser", "platform", "browserVersion", "url", "username", "accessKey"})
    public void setup(
            @Optional("chrome") String browser,
            @Optional("Windows 10") String platform,
            @Optional("127.0") String browserVersion,
            @Optional("https://www.lambdatest.com/selenium-playground/") String url,
            @Optional("pggowthami.892") String username,
            @Optional("5ciZuDIZSy8Og3vxF27tm3xTbsUn17y9uk3I9FLyn1ic6SyfrS") String accessKey
    ) throws MalformedURLException {
        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setCapability("platformName", platform);
        browserOptions.setCapability("browserVersion", browserVersion);

        // LambdaTest Capabilities
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", username);
        ltOptions.put("accessKey", accessKey);
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("network", true);
        ltOptions.put("timezone", "Kolkata");
        ltOptions.put("build", "Lambdatest");
        ltOptions.put("project", "testng101");
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");

        browserOptions.setCapability("LT:Options", ltOptions);

        // LambdaTest Selenium Grid URL
        String GRID_URL = "https://" + "pggowthami.892" + ":" + "5ciZuDIZSy8Og3vxF27tm3xTbsUn17y9uk3I9FLyn1ic6SyfrS" + "@hub.lambdatest.com/wd/hub";
        driver = new RemoteWebDriver(new URL(GRID_URL), browserOptions);
        wait = new WebDriverWait(driver, 10);

        driver.get(url);
    }

    @Test(priority = 1, timeOut = 20000)
    public void testTitleValidation() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        String actualTitle = driver.getTitle();
        System.out.println("Actual Page Title: " + actualTitle);
        Assert.assertEquals(actualTitle, "LambdaTest", "Page title validation failed");
    }

    @Test(priority = 2, timeOut = 20000)
    public void testCheckboxValidation() {
        WebElement checkboxDemoLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Checkbox Demo']")));
        checkboxDemoLink.click();

        WebElement singleCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='isAgeSelected']")));
        singleCheckbox.click();
        Assert.assertTrue(singleCheckbox.isSelected(), "Checkbox should be selected");

        singleCheckbox.click();
        Assert.assertFalse(singleCheckbox.isSelected(), "Checkbox should be unselected");

        driver.navigate().back();
    }

    @Test(priority = 3, timeOut = 20000)
    public void testJavaScriptAlertValidation() {
        WebElement jsAlertLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Javascript Alerts']")));
        jsAlertLink.click();

        WebElement clickMeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-dark my-30 mx-10 hover:bg-lambda-900 hover:border-lambda-900']")));
        clickMeButton.click();

        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        System.out.println("Alert Message: " + alertText);
        Assert.assertEquals(alertText, "I am an alert box!", "Alert message validation failed");

        driver.switchTo().alert().accept();
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}