package utlities;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;
	
	String reportName;
	public void onStart(ITestContext testContext) {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		reportName = "Test-Report-"+timeStamp+".html";
		sparkReporter = new ExtentSparkReporter(".\\reports\\"+reportName);
		
		sparkReporter.config().setDocumentTitle("Opencart Automation Report");
		sparkReporter.config().setReportName("Opencart Functional Testing");
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Enviroment","QA");
		
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		List<String> includeGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if(!includeGroups.isEmpty()) {
			extent.setSystemInfo("Groups",includeGroups.toString());
		}
	}
	

	public void onTestSuccess(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups()); //to display groups in report
		test.log(Status.PASS,result.getName()+" got successfully executed");
	}
	
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups()); //to display groups in report
		
		test.log(Status.FAIL,result.getName()+" got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
		
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());
			System.out.println(imgPath);
			test.addScreenCaptureFromPath(imgPath);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP,result.getName()+" got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());  
	}
	
	public void onFinish(ITestContext context) {
		extent.flush();
		
		/*String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+reportName;
		File extentReport = new File(pathOfExtentReport);
		try {
			Desktop.getDesktop().browse(extentReport.toURL());
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		
		// Email sender
		try {
			URL url = new URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+reportName);
			
			//create the email massage
			ImageHtmlEmail email = new ImageHtmlEmail();
			email.setDataSourceResolver(new DataSourceUrlResolver(url));
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			String senderEmail = "souviksantra999@gmail.com";
			String senderPassword = "<password>";
			email.setAuthenticator(new DefaultAuthenticator(senderEmail, senderPassword));
			email.setSSLOnConnect(true);
			
			email.setFrom("souviksantra999@gmail.com");
			email.setSubject("Test Result - "+new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
			email.setMsg("Please find attached report");
			email.addTo("souviksantra1999@gmail.com");
			email.attach(url,"Extents Report.html","Please check report...");
			email.send(); //send the email
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
