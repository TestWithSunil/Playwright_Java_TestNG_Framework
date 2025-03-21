package com.qa.demowebshop.factory;

import com.microsoft.playwright.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class PlaywrightFactory {
    private static ThreadLocal<Playwright> threadLocalPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Browser> threadLocalBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> threadLocalBrowserContext = new ThreadLocal<>();
    private static ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();
    private Properties prop;

    public Page initBrowser(Properties prop) {
        String browserName = prop.getProperty("browser", "chrome").trim();
        boolean isHeadless = Boolean.parseBoolean(prop.getProperty("headless", "true").trim()); // Default: true
        String url = prop.getProperty("url").trim();

        System.out.println("Browser: " + browserName + " | Headless Mode: " + isHeadless);

        try {
            Playwright playwright = Playwright.create();
            threadLocalPlaywright.set(playwright);

            Browser browser;
            switch (browserName.toLowerCase()) {
            case "chrome":
            	browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            	        .setChannel("chrome")
            	        .setHeadless(isHeadless)
            	        .setArgs(isHeadless ? List.of("--headless=new") : List.of("--start-maximized"))); 


                break;

                case "edge":
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                            .setChannel("msedge")
                            .setHeadless(isHeadless));
                    break;

                case "firefox":
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions()
                            .setHeadless(isHeadless));
                    break;

                case "safari":
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions()
                            .setHeadless(isHeadless));
                    break;

                case "chromium":
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                            .setHeadless(isHeadless));
                    break;

                default:
                    throw new IllegalArgumentException("Invalid browser name. Supported browsers: chrome, edge, firefox, safari, chromium.");
            }

            threadLocalBrowser.set(browser);
            BrowserContext browserContext = browser.newContext();
            threadLocalBrowserContext.set(browserContext);
            Page page = browserContext.newPage();
            threadLocalPage.set(page);

            page.navigate(url);
            return page;

        } catch (Exception e) {
            System.err.println("Exception occurred during browser initialization: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Page getPage() {
        return threadLocalPage.get();
    }

    public void cleanup() {
        try {
            if (threadLocalBrowser.get() != null) {
                threadLocalBrowser.get().close();
            }
            if (threadLocalPlaywright.get() != null) {
                threadLocalPlaywright.get().close();
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Properties init_prop() {
        try (FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties")) {
            prop = new Properties();
            prop.load(ip);
        } catch (IOException e) {
            System.err.println("Failed to load properties file: " + e.getMessage());
            e.printStackTrace();
        }
        return prop;
    }

    public static void takeScreenshot(String screenshotPath) {
        try {
            getPage().screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(screenshotPath))
                    .setFullPage(true));
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
