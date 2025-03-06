package com.qa.demowebshop.factory;

import com.microsoft.playwright.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class PlaywrightFactory {
    private static ThreadLocal<Playwright> threadLocalPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Browser> threadLocalBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> threadLocalBrowserContext = new ThreadLocal<>();
    private static ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();
    private Properties prop;

    public Page initBrowser(Properties prop) {
        String browserName = prop.getProperty("browser").trim();
        System.out.println("Browser name is: " + browserName);

        try {
            Playwright playwright = Playwright.create();
            threadLocalPlaywright.set(playwright);

            Browser browser;
            switch (browserName.toLowerCase()) {
                case "chrome":
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                            .setChannel("chrome")
                            .setHeadless(false));
                    break;

                case "edge":
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                            .setChannel("msedge")
                            .setHeadless(false));
                    break;

                case "firefox":
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions()
                            .setHeadless(false));
                    break;

                case "safari":
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions()
                            .setHeadless(false));
                    break;

                case "chromium":
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                            .setHeadless(false));
                    break;

                default:
                    throw new IllegalArgumentException("Invalid browser name. Supported browsers: chrome, edge, firefox, safari, chromium.");
            }

            threadLocalBrowser.set(browser);
            BrowserContext browserContext = browser.newContext();
            threadLocalBrowserContext.set(browserContext);
            Page page = browserContext.newPage();
            threadLocalPage.set(page);

            page.navigate(prop.getProperty("url").trim());
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
        if (threadLocalBrowser.get() != null) {
            threadLocalBrowser.get().close();
        }
        if (threadLocalPlaywright.get() != null) {
            threadLocalPlaywright.get().close();
        }
    }

    public Properties init_prop() {
        try (FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties")) {
            prop = new Properties();
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
    
//    public static String takeScreenshot() {
//        try {
//            String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
//            getPage().screenshot(new Page.ScreenshotOptions()
//                    .setPath(Paths.get(path))
//                    .setFullPage(true));
//            return path;
//        } catch (Exception e) {
//            System.err.println("Failed to take screenshot: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
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
