package com.qa.demowebshop.base;

import java.util.Properties;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
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
    public void setup(@Optional("") String browserName) {  
        System.out.println("Initializing Playwright Factory...");
        pf = new PlaywrightFactory();
        prop = pf.init_prop();

        // Fallback mechanism: Use browser from config.properties if not provided in XML
        if (browserName == null || browserName.isEmpty()) {
            browserName = prop.getProperty("browser", "chrome"); // Default to "chrome" if missing
            System.out.println("No browser name provided from XML. Using default from config.properties: " + browserName);
        } else {
            System.out.println("Browser name from XML file: " + browserName);
        }

        prop.setProperty("browser", browserName); // Set the browser value in properties
        page = pf.initBrowser(prop);

        // Null Check to prevent NPE when creating HomePage
        if (page == null) {
            throw new IllegalStateException("Browser page failed to initialize. Check Playwright setup.");
        }

        System.out.println("Browser initialized successfully. Creating HomePage instance...");
        homepage = new HomePage(page);
    }

    @AfterTest
    public void teardown() {
        System.out.println("Closing the browser...");
        if (pf != null) {
            pf.cleanup(); // Ensures Playwright and BrowserContext are properly closed
        }
    }
}
