package com.qa.demowebshop.pages;

import com.microsoft.playwright.Page;

public class HomePage{
	Page page;
	
//	1.Page constructor
	public HomePage(Page page) {
		this.page=page;
	}
	
//	2. String locators
	private String seacrh= "//input[@placeholder='Search']"; 
	private String seacrhIcon= "//i[@class='fa fa-search']"; 
	private String seacrhPageHeader= "div[id='content'] h1"; 
	private String loginLink= "//a[normalize-space()='Login']"; 
	private String LoginDropdownToggle="//span[@class='caret']";
	
	public String getHomePageTitle() {
		return page.title();
		
	}
	
	public String getHomePageURL() {
		return page.url();
		
	}
	
	public String dosearch(String productName) {
		page.fill(seacrh, productName);
		page.click(seacrhIcon);
		String PageHeader = page.textContent(seacrhPageHeader);
		System.out.println("productName");
		return PageHeader;
	}
	
	public LoginPage navigateToLoginpage() {
		page.click(LoginDropdownToggle);
		page.click(loginLink);
		return new LoginPage(page);
	}

}
