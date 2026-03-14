package utility;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitHelper {
	
	private final WebDriver driver;
    private final Duration timeout;

    public WaitHelper(WebDriver driver, long seconds) {
        this.driver = driver;
        this.timeout = Duration.ofSeconds(seconds);
    }

    public WebElement waitForVisible(By locator) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForText(By locator, String text) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.textToBe(locator, text));
    }

    
    /** Wait until element is invisible */
    public boolean waitForInvisible(By locator) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Wait until attribute equals some value */
    public boolean waitForAttribute(By locator, String attr, String value) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.attributeToBe(locator, attr, value));
    }

    /** Wait until number of elements > 0 */
    public List<WebElement> waitForAllVisible(By locator) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /** Wait until page title contains */
    public boolean waitForTitleContains(String text) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.titleContains(text));
    }
    
    public void clickFirstMember() {
        By locator = By.xpath("//input[@onclick='addMemberWithPackage(this)']");
        List<WebElement> members = new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        if (members.isEmpty()) return;

        WebElement label = driver.findElement(By.xpath("(//input[@onclick='addMemberWithPackage(this)']/following-sibling::label)[1]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", label);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", label);
        
        

    }
    
    public void info(String msg) {
    	
    	
    	System.out.println(msg);
    }
	
	
	

}
