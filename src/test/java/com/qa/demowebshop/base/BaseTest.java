package com.qa.demowebshop.base;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.microsoft.playwright.Page;
import com.qa.demowebshop.factory.PlaywrightFactory;
import com.qa.demowebshop.pages.HomePage;
import com.qa.demowebshop.pages.LoginPage;

public class BaseTest {
	PlaywrightFactory pf;
	Page page;
	protected Properties prop;
	protected HomePage homepage;
	protected LoginPage loginpage;
	
	@Parameters({"browser"})
	@BeforeTest
	public void setup(String browserName) {
		pf = new PlaywrightFactory();
		prop = pf.init_prop();

		
	    // If browserName is not provided from XML, use the one from config.properties
	    if (browserName != null && !browserName.isEmpty()) {
	        prop.setProperty("browser", browserName);
	        System.out.println("Browser name from XML file: " + browserName);
	    } else {
	        browserName = prop.getProperty("browser"); // Default from config.properties
	        System.out.println("No browser name provided from XML. Using default from config.properties: " + browserName);
	    }
	    
		page = pf.initBrowser(prop);
		homepage = new HomePage(page);
	}
	
	@AfterTest
	public void teardown() {
		
		if (page != null) {
			page.context().browser().close();
        }
	}
}
