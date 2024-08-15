package testCases;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.RegisterPage;
import testBase.BaseClass;

public class TC001_AccountRegistration extends BaseClass{

	
	@Test(groups = {"Regression","Master"})
	public void verify_Account_Registration() {
		logger.info("*** TC001_AccountRegistration ***");
		try {
			HomePage hp = new HomePage(driver);
			hp.clickOnMyAccount();
			logger.info("Clicked On My Account");
			hp.clickOnRegister();
			logger.info("Clicked On Register");
			RegisterPage regPage = new RegisterPage(driver);
			regPage.setFirstName(randomString().toUpperCase());
			logger.info("First Name entered");
			regPage.setLastName(randomString().toUpperCase());
			logger.info("Last Name entered");
			regPage.setEmail(randomString().toUpperCase()+"_"+randomNumber()+"@gmail.com");
			logger.info("Email entered");
			regPage.setTelePhone(generateRandomTelNumber());
			logger.info("Telephone entered");
			String password=randomAlphanumeric();
			regPage.setPassword(password);
			logger.info("Password entered");
			regPage.setConfirmPassword(password);
			logger.info("Confirmed Password entered");
			regPage.setPrivacyPolicy();
			logger.info("Policy seted");
			regPage.clickContinue();
			logger.info("Clicked on Continue button");
			String confirmMassage = regPage.getConfirmationMsg();
			if(confirmMassage.equals("Your Account Has Been Created!")) {
				Assert.assertTrue(true);
			}
			else {
				logger.error("Test case failed");
				logger.debug("Debug log ...");
				Assert.assertTrue(false);
			}
		}
		catch(Exception e) {
			logger.error("Test Case broken - "+e.getMessage());
			Assert.fail();
		}
	}

	
}
