package com.qa.demowebshop.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.demowebshop.base.BaseTest;
import com.qa.demowebshop.constants.Appconstants;

public class TC02_LoginPageTest extends BaseTest{
	
	@Test(priority=1)
	public void LoginPageNavigationTest() {
		loginpage=homepage.navigateToLoginpage();
		String actualLoginPageTitle = loginpage.getLoginpageTitle();
		System.out.println("Actual Login Page Title : "+actualLoginPageTitle+ "Expected login Page Title"+Appconstants.LOGIN_PAGE_TITLE);
		Assert.assertEquals(actualLoginPageTitle, Appconstants.LOGIN_PAGE_TITLE);
	}
	
	@Test(priority=2)
	public void forgotPWDlinkExistTest() {
		Assert.assertTrue(loginpage.isForgotlinkExist());
		
	}
	
	@Test(priority = 3)
	public void appLoginTest() {
		homepage.navigateToLoginpage();
		Assert.assertTrue(loginpage.doLogin(prop.getProperty("username").trim(), prop.getProperty("password").trim()));
		
		
	}
}