package com.qa.demowebshop.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.demowebshop.base.BaseTest;
import com.qa.demowebshop.constants.Appconstants;

public class HomePageTest extends BaseTest{

	

	
	@Test
	public void getHomePageTittleTest() {
		String actualtitle = homepage.getHomePageTitle();
		
		System.out.println("HomePage title : "+actualtitle);
		Assert.assertEquals(actualtitle, Appconstants.HOME_PAGE_TITLE);
		
	}
	
	@Test
	public void getHomePageURLTest() {
		String actualURL = homepage.getHomePageURL();
		System.out.println("Page URL : "+actualURL);
		Assert.assertEquals(actualURL, prop.getProperty("url"));
		
		}
	
	@Test
	public void searchTest() {
		String actualSearchHeader = homepage.dosearch("Macbook");
		System.out.println(actualSearchHeader);
		Assert.assertEquals(actualSearchHeader, "Search - Macbook");
		
	}
}
