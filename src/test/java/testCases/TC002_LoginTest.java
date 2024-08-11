package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC002_LoginTest extends BaseClass{

	@Test
	public void loginTest() {
		logger.info("*** TC002_Login Test ***");
		try {
			
			HomePage hp = new HomePage(driver);
			hp.clickOnMyAccount();
			logger.info("Clicked On My Account");
			hp.clickOnLogin();
			logger.info("Clicked On Login");
			
			LoginPage lp = new LoginPage(driver);
			lp.enterEmail(getPropertiesValue("username"));
			logger.info("Email entered");
			lp.enterPassword(getPropertiesValue("password"));
			logger.info("Password Entered");
			lp.clickOnLogin();
			logger.info("Clicked On Login button");
			
			MyAccountPage accPage = new MyAccountPage(driver);
			boolean isMyAccountPagePresent = accPage.isAccountPageExists();
			Assert.assertTrue(isMyAccountPagePresent, "My Account Page is not present");
			logger.info("My Account page is displaying");
			logger.info("Test case executed");
		}
		catch(Exception e) {
			logger.error("Test Case broken - "+e.getMessage());
			Assert.fail();
		}
	}
}
