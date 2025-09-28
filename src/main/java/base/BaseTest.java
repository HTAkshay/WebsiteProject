package base;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import utility.ConfigData;

public class BaseTest {

	protected WebDriver driver;
	 protected static  Logger logger ;


    public void setUp() throws MalformedURLException {
        WebDriverManager.chromedriver().setup();
      // ChromeOptions options = new ChromeOptions();
     //  options.addArguments("--headless=new","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage");
       
        logger = LoggerFactory.getLogger(BaseTest.class);
        
        
//        driver = new RemoteWebDriver(
//                new URL("http://localhost:4444/wd/hub"), // Selenium Grid inside Docker
//                options
//        );
       
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    public void tearDown() throws InterruptedException {
        if (driver != null) driver.quit();
        Thread.sleep(2000);
    }
	
	
	
	
}
