package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigData {
	
	private final Properties props = new Properties();

    public String get(String key) throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\Akshay Sunil Mankar\\git\\WebsiteProject\\src\\main\\resources\\config.properties");
            props.load(fis);
      
        return props.getProperty(key);
	
	

}
}
