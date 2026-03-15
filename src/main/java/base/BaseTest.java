package base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.LogManager;

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
	// protected static  Logger logger ;
	// protected static Logger logger = LoggerFactory.getLogger(BaseTest.class);


    public void setUp() throws MalformedURLException {
        WebDriverManager.chromedriver().setup();
       ChromeOptions options = new ChromeOptions();
       options.addArguments("--headless=new","--window-size=1920,1080","--no-sandbox","--disable-dev-shm-usage","--disable-notifications","--disable-infobars","--remote-allow-origins=*");
       
       // logger = LoggerFactory.getLogger(BaseTest.class);
       
//    		   --headless=new"
        
        
//        driver = new RemoteWebDriver(
//                new URL("http://localhost:4444/wd/hub"), // Selenium Grid inside Docker
//                options
//        );
       
        driver = new ChromeDriver(options);
        //logger.info("Launching Chrome Browser");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    public void tearDown() throws InterruptedException {
        if (driver != null) driver.quit();
        //logger.info("Closing Browser");
        Thread.sleep(2000);
    }
	
	
	
	
}
