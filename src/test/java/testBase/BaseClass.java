package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {

	public WebDriver driver;
	public Logger logger;
	
	@BeforeClass(groups = {"Regression","Sanity","Master","DDT_Test"})
	@Parameters(value = {"os","browser"})
	public void setup(String os, String br) throws IOException {
		logger = LogManager.getLogger(this.getClass());
		
		logger.info("Test Automation setup started... ");
		String enviroment = getPropertiesValue("execution_env");
		
		if(enviroment.equalsIgnoreCase("remote")) {
			DesiredCapabilities desiredCap = new DesiredCapabilities();
			
			//OS
			if(os.equalsIgnoreCase("windows")) {
				desiredCap.setPlatform(Platform.WIN11);
			}
			else if(os.equalsIgnoreCase("mac")) {
				desiredCap.setPlatform(Platform.MAC);
			}
			else {
				System.out.println("No matching os");
				return;
			}
			
			//Browser
			switch(br.toLowerCase()) {
			case "chrome": desiredCap.setBrowserName("chrome");
						   break;
			case "edge" : desiredCap.setBrowserName("MicrosoftEdge"); 
						   break;
			case "firefox" : desiredCap.setBrowserName("firefox"); 
			   				break;
			default: System.out.println("No Matching browser");
					 return;
			
			}
			
			driver = new RemoteWebDriver(new URL("http://192.168.43.64:4444"),desiredCap);
		}
		
		if(enviroment.equalsIgnoreCase("local")) {
			switch(br.toLowerCase()) { 
			case  "chrome" : driver = new ChromeDriver();
							 break;
							 
			case  "firefox" : driver = new FirefoxDriver();
			 				  break;
			 				  
			case  "edge" : driver = new EdgeDriver();
			 				break;
			
			default : logger.warn("Invalid browser provided - "+br);
					  return;
		
			}
		}
		
		logger.info("Test Execution started on - "+br);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		String url = getPropertiesValue("appUrl");
		driver.get(url);
		logger.info("Url : <"+url+"> loaded sucessfully");
		driver.manage().window().maximize();
		logger.info("Test Automation setup done... ");
	}
	
	@AfterClass(groups = {"Regression","Sanity","Master","DDT_Test"})
	public void tearDown() {
		driver.quit();
		logger.info("Driver closed... ");
	}
	
	public String randomString() {
		String generatedString = RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
	
	public String randomNumber() {
		String generatedNumber = RandomStringUtils.randomNumeric(5);
		return generatedNumber;
	}
	
	public String randomAlphanumeric() {
		String generatedString = RandomStringUtils.randomAlphabetic(5);
		String generatedNumber = RandomStringUtils.randomNumeric(5);
		return generatedString+"#"+generatedNumber;
	}
	
	public String generateRandomTelNumber() {
		String generatedNumber = RandomStringUtils.randomNumeric(9);
		return 9+""+generatedNumber;
	}
	
	public String getPropertiesValue(String element) {
		//Loading config.properties file
		String value="";
		try{
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir").concat("\\src\\test\\resources\\config.properties"));
			Properties prop = new Properties();
			prop.load(fis);
			logger.info("Properties file loaded");
			value = prop.getProperty(element);
		}
		catch (Exception e) {
			logger.info("Unable to read data "+e.getMessage());
			Assert.fail();
		}
		return value;
		
	}

	public String captureScreen(String tname) {
		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		TakesScreenshot takesScreenShot = (TakesScreenshot) driver;
		File sourceFile  = takesScreenShot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath = System.getProperty("user.dir")+"\\screenshots\\"+tname+"_"+timeStamp+".png";
		File targetFile = new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
		return targetFilePath;
	}
}
