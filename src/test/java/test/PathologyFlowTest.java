package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import utility.ConfigData;
import utility.JsonReport;
import utility.WaitHelper;

public class PathologyFlowTest  extends BaseTest{
	
	

    private WaitHelper wait;
    private ConfigData config;
    String booking_id = null; 

    @Test()
     public void BookingFlow() throws Exception {
        // 1. setup
        setUp();                   
        wait = new WaitHelper(driver, 20);
        config = new ConfigData();

        try {
            // 2. open site
            driver.get(config.get("url"));
            wait.waitForTitleContains(""); // ensure page loaded
            Thread.sleep(2000);
            // 3. select location
            wait.waitForClickable(By.xpath("//label[text()='Your Location ']")).click();
            wait.waitForVisible(By.id("myInputLocation"))
                .sendKeys(config.get("city"));
            wait.waitForClickable(By.xpath("//p[text()='Gurgaon']")).click();

            // 4. search test/package
            wait.waitForVisible(By.cssSelector("ul li input.select2-search__field"))
                .sendKeys(config.get("testname"));

            List<WebElement> suggestions =
                    wait.waitForAllVisible(By.xpath("//span[@style='float:left;']"));

            for (WebElement testlist : suggestions) {
                if (testlist.getText().equalsIgnoreCase(config.get("testname"))) {
                    testlist.click();
                    break;
                }
            }

            wait.waitForClickable(By.cssSelector("div.button_icon_form")).click();

            // 5. details + book now
           WebElement details = wait.waitForClickable(By.xpath("//a[text()='Details']"));
        Actions act= new Actions(driver);
        		act.moveToElement(details).click().perform();
//            wait.waitForClickable(By.xpath("//a[text()='Details']")).click();
           
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,300);");
            WebElement bookNowBtn = wait.waitForClickable(By.xpath("//button[text()=' Book Now ']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", bookNowBtn);
            
            
            //lead form close
         WebElement leadform = wait.waitForClickable(By.xpath("(//button[@class='close'])[5]"));
         leadform.click();

            // 6. enter mobile & fetch OTP
            wait.waitForVisible(By.id("mobile_number")).sendKeys(config.get("mobile"));
            wait.waitForClickable(By.xpath("//button[text()='Continue']")).click();

            String otp = fetchOtpByPolling(config.get("mobile"));
            Assert.assertTrue(otp != null && otp.length() == 6, "OTP not fetched!");

            for (int i = 1; i <= 6; i++) {
                String dynamicId = "otc-" + i;
                WebElement otpField = wait.waitForClickable(By.id(dynamicId));
                otpField.sendKeys(String.valueOf(otp.charAt(i - 1)));
            }
            
            wait.clickFirstMember();
            Thread.sleep(3000);
            
/*	            // 7. select member (first)
        List<WebElement> membersList =
            driver.findElements(By.xpath("(//input[@onclick='addMemberWithPackage(this)'])"));
          //  wait.waitForAllVisible(By.xpath("(//input[@onclick='addMemberWithPackage(this)'])"));
            if (!membersList.isEmpty() && !membersList.get(0).isSelected()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", membersList.get(0));
            }
            
            */

            WebElement nextbtn = driver.findElement(By.id("next-to-address"));

            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", 
                nextbtn );
          //  ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,300);");
            wait.waitForClickable(By.id("next-to-address")).click();

            // 8. date & slot selection
            
            WebElement datePicker = wait.waitForClickable(By.id("dp2"));

            ((JavascriptExecutor)driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", datePicker);

            Thread.sleep(500);

            datePicker.click();
          
//            wait.waitForClickable(By.id("dp2")).click();
//            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500);");

            
            /*
            List<WebElement> allDates = wait.waitForAllVisible(
                    By.xpath("//div[@class='datepicker-days']//table//tbody//tr//td[not(contains(@class,'disabled'))]"));
         
            int enabledCount = 0;
            boolean slotFound = false;
            for (WebElement dateElement : allDates) {
                if (dateElement.isEnabled()) {
                    enabledCount++;
                    if (enabledCount == 2) { // choose 2nd enabled date
                        dateElement.click();
                        WebElement slotPanel = wait.waitForClickable(By.id("collection_time"));
                        Select select = new Select(slotPanel);
                        if (select.getOptions().size() > 1) {
                            select.selectByIndex(1);
                            slotFound = true;
                        }
                        break;
                    }
                }
            }
            Assert.assertTrue(slotFound, "No slot found on 2nd enabled date!");
            
            */
            
        
         //old loop logic without wait
            boolean slotFound = false;
 	       Set<String> triedDates = new HashSet<>(); // Track already clicked dates

 	       while (!slotFound) {
 	           // Fetch currently enabled dates
 	           List<WebElement> allDates = wait.waitForAllVisible(
 	               By.xpath("//div[@class='datepicker-days']//table[@class='table-condensed']//tbody//tr//td[not(contains(@class,'disabled'))]"));

 	           boolean newDateClicked = false;

 	           for (WebElement dateElement : allDates) {
 	               String dateText = dateElement.getText();
 	               if (triedDates.contains(dateText)) {
 	                   continue; // skip already tried dates
 	               }

 	               try {
 	                   dateElement.click(); // click date
 	                   triedDates.add(dateText);
 	                   newDateClicked = true;

 	                   // Wait for slots
 	                   WebElement slotPanel = wait.waitForClickable(By.id("collection_time"));
 	                   Select s = new Select(slotPanel);
 	                   List<WebElement> allSlots = s.getOptions();
 	                   System.out.println("the slot size is "+allSlots.size());

 	                   if (allSlots.size() > 1) {
 	                       s.selectByIndex(2); // pick second slot
 	                       slotFound = true;
 	                       break; // exit for loop
 	                   } else {
 	                       // No slots, need to reopen calendar for next iteration
 	                	  WebElement dateSelection = wait.waitForClickable(By.id("dp2"));
 	                       dateSelection.click();
 	                      ((JavascriptExecutor) driver).executeScript("scroll(0, 500);");
 	                      // js.executeScript("window.scrollBy(0,500);");
 	                       Thread.sleep(500); // small wait for calendar to load
 	                       break; // break inner loop to refetch dates
 	                   }
 	               } catch (Exception e) {
 	                   e.printStackTrace();
 	               }
 	           }

 	           if (!newDateClicked) {
 	               System.out.println("No available slot found in date range.");
 	               break;
 	           }
 	       }
 	      // loop end here
 	     
          
 	        
        
           
            Thread.sleep(3000);
            // 9. payment selection
            ((JavascriptExecutor) driver).executeScript("scroll(0, 900);");
            WebElement element7 = wait.waitForVisible(By.cssSelector("#pay_wrp_cash > div > label > span"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element7);

            WebElement element9 = wait.waitForVisible(
                    By.cssSelector("#collapse5 > div > div > div.payment_wrap2 > div > div > div > label"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element9);
            Thread.sleep(2000);
           // wait.waitForClickable(By.id("payment_out")).click();
           wait.waitForVisible(By.xpath("//a[@id='payment_out' and text()='Complete Order ']")).click();
               Thread.sleep(3000);
            // 10. cancel booking
              String bookingId = wait.waitForClickable(By.xpath("(//div[@class='booking_info_inner'])[1]//h3")).getText();
              System.out.println("the booking id is "+bookingId);
               
            WebElement fullCancel = wait.waitForVisible(By.xpath("(//button[@title='Cancel'])[1]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fullCancel);

            Select reason = new Select(wait.waitForVisible(By.id("cancel_reason_id")));
            reason.selectByIndex(7);

           wait.waitForClickable(By.xpath("//a[text()='Cancel Booking'][10]")).click();
            Thread.sleep(5000);
            System.out.println("Booking flow completed.");
            JsonReport.write("BookingFlow", "Pass",bookingId,"BookingFlow completed successfully");
           
        }catch(Exception e){
        	
    
			JsonReport.write("BookingFlow", "FAIL",booking_id,e.getMessage());
            Assert.fail("Booking flow failed", e); 
        	
        } finally {
            tearDown();
        }
    }

 
    
    
    private String fetchOtpByPolling(String mobileNumber) throws IOException {
    	 String otp = null;
    	    int retries = Integer.parseInt(config.get("otpretries"));
    	    long interval = Long.parseLong(config.get("otpinterval"));
    	    String awsdbUrl = config.get("awsdburl");
    	    String dbUser = config.get("dbuser");
    	    String dbPass = config.get("dbpassword");
    	    String query = "SELECT otp_code FROM user_otp WHERE mobile_no = ? AND status = 1 ORDER BY session_start_time DESC LIMIT 1";

    	    while (retries-- > 0) {
    	        try (Connection conn = DriverManager.getConnection(awsdbUrl, dbUser, dbPass);
    	             PreparedStatement stmt = conn.prepareStatement(query)) {

    	            stmt.setString(1, mobileNumber);
    	            try (ResultSet rs = stmt.executeQuery()) {
    	                if (rs.next()) {
    	                    otp = rs.getString("otp_code");
    	                    if (otp != null && otp.length() == 6) {
    	                       // logger.info("OTP fetched successfully for mobile {}", mobileNumber);
    	                        return otp;
    	                    }
    	                }
    	            }
    	          //  logger.info("OTP not found yet for mobile {}. Waiting {} ms before retry.", mobileNumber, interval);
    	            Thread.sleep(interval);
    	        } catch (Exception e) {
    	          //  logger.error("Error fetching OTP for mobile {}: {}", mobileNumber, e.getMessage(), e);
    	        }
    	    }
    	    throw new RuntimeException("OTP not found for mobile: " + mobileNumber);
    }
    
 
    
    
	

}
