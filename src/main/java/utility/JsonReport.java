package utility;

import java.util.LinkedHashMap;
import java.util.Map;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReport {
	
	
	 private static final String REPORT_FILE = "test-result.json";

	  
	    public static void write(String testName, String status, String bookingId, String message) {
	        try {
	            Map<String, Object> entry = new LinkedHashMap<>();
	            entry.put("testName", testName);
	            entry.put("status", status);
	            entry.put("booking_id", bookingId != null ? bookingId : "N/A");
	            entry.put("timestamp", System.currentTimeMillis());
	            entry.put("message", message != null ? message : "");

	            ObjectMapper mapper = new ObjectMapper();
	            // This will overwrite the file each time
	            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(REPORT_FILE), entry);

	            System.out.println("JSON report written: " + REPORT_FILE);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
	
	

}
