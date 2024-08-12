package testCases;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

/*
 * Data is valid - login success - Test pass - logout
 * Data is valid - login failed - Test fail
 * Data is invalid - login success - Test fail - logout
 * Data is invalid - login failed - Test pass
 * 
 */
public class TC003_LoginDataDrivenTest extends BaseClass {

	@Test(dataProvider = "LoginData", dataProviderClass = utlities.dataProvider.class) //getting data provider from different class
	public void LoginDataDrivenTest(String userName, String password, String exp_Results) {
		try {
			HomePage hp = new HomePage(driver);
			hp.clickOnMyAccount();
			logger.info("Clicked On My Account");
			hp.clickOnLogin();
			logger.info("Clicked On Login");
			
			LoginPage lp = new LoginPage(driver);
			lp.enterEmail(userName);
			logger.info("Email entered - "+userName);
			lp.enterPassword(password);
			logger.info("Password Entered - "+password);
			lp.clickOnLogin();
			logger.info("Clicked On Login button");
			
			MyAccountPage accPage = new MyAccountPage(driver);
			boolean targetPage = accPage.isAccountPageExists();
			
			if(exp_Results.equalsIgnoreCase("Valid")) {
				if(targetPage == true) {
					logger.info("Testcase pass for data set - "+"["+userName+"]"+"["+password+"]");
					accPage.clickOnLogout();
					Assert.assertTrue(true);
					
						}
				else {
					logger.info("Testcase failed for data set - "+"["+userName+"]"+"["+password+"]");
					Assert.assertTrue(false);
				}
			}
			
			else if(exp_Results.equalsIgnoreCase("Invalid")){
				if(targetPage == false) {
					logger.info("Testcase pass for data set - "+"["+userName+"]"+"["+password+"]");
					Assert.assertTrue(true);
				}
				else {
					accPage.clickOnLogout();
					logger.info("Testcase failed for data set - "+"["+userName+"]"+"["+password+"]");
					Assert.assertTrue(false);
				}
			}

		}
		catch(Exception e) {
			logger.info("Test failed");
			Assert.fail();
		}
	}
}
