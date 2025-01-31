package com.qa.demowebshop.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
	Page page;

	// 1. Page constructor
	public LoginPage(Page page) {
		this.page=page;
	}
	
	// 2. String locators
	private String mail= "//input[@id='input-email']"; 
	private String pwd= "//input[@id='input-password']"; 
	private String loginBtn= "//input[@value='Login']";
	private String forgotpwdlink= "//div[@class='form-group']//a[normalize-space()='Forgotten Password']";
	private String logoutlink="//a[@class='list-group-item'][normalize-space()='Logout']";
	
	// 3. Page actions/Method
	public String getLoginpageTitle() {
		String pagetitle = page.title();
		return pagetitle;
	}
	
	public boolean isForgotlinkExist() {
		return page.isVisible(forgotpwdlink);
		
	}
	
	public boolean doLogin(String appUserName,String appPassword) {
		System.out.println("AppUserName :" + appUserName + "  AppPassword :"+appPassword);
		page.fill(mail, appUserName);
		page.fill(pwd, appPassword);
		page.click(loginBtn);
		
		if (page.isVisible(logoutlink)) {
			System.out.println("Logged in successfully...");
			return true;
		} 
		return false;
	}
	
}
