package com.qa.demowebshop.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.demowebshop.ExcelUtils.ExcelReader;
import com.qa.demowebshop.base.BaseTest;
import com.qa.demowebshop.constants.Appconstants;

public class TC01_HomePageTest extends BaseTest{

	ExcelReader excelReader = new ExcelReader("./src/test/resources/testData/testdata.xlsx", "HomePage");
	
	@Test
	public void getHomePageTittleTest() {

		  String expectedHomePagetitle = excelReader.getCellData("DWS_01", "HomePage_Title");
		  System.out.println("Fetched Search Data for DWS_01: " + expectedHomePagetitle);
		  
		String actualtitle = homepage.getHomePageTitle();
		
		System.out.println("HomePage title : "+actualtitle);
		Assert.assertEquals(actualtitle, expectedHomePagetitle);
		
	}
	
	@Test
	public void getHomePageURLTest() {
		String actualURL = homepage.getHomePageURL();
		System.out.println("Page URL : "+actualURL);
		String expectedHomePageURL = excelReader.getCellData("DWS_01", "HomePage_URL");
		Assert.assertEquals(actualURL, expectedHomePageURL);
		
		}
	
	@Test
	public void searchTest() {
		String actualSearchHeader = homepage.dosearch("Macbook");
		System.out.println(actualSearchHeader);
		String expectedSearchitem = excelReader.getCellData("DWS_01", "Search");
		Assert.assertEquals(actualSearchHeader, expectedSearchitem);
		
	}
}
