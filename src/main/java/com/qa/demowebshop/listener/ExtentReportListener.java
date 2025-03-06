package com.qa.demowebshop.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import static com.qa.demowebshop.factory.PlaywrightFactory.takeScreenshot;

public class ExtentReportListener implements ITestListener {

    private static final String BASE_FOLDER = "./Automation_Report/";
    private static String reportFolderPath;
    private static ExtentReports extent = init();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentReports extentReports;

    private static ExtentReports init() {
        // Generate timestamp-based report folder
        String timeStamp = new SimpleDateFormat("dd-MMM-yyyy_hh-mm-a").format(new Date());
        reportFolderPath = BASE_FOLDER + "Report_" + timeStamp + "/";

        Path reportPath = Paths.get(reportFolderPath);
        Path screenshotPath = Paths.get(reportFolderPath + "Screenshots/");

        try {
            // Create report and screenshot folders if they don't exist
            Files.createDirectories(reportPath);
            Files.createDirectories(screenshotPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create Extent report file inside the report folder
        String reportFilePath = reportFolderPath + "Extent_Report.html";
        extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportFilePath);
        reporter.config().setReportName("Open Cart Automation Test Results");
        extentReports.attachReporter(reporter);

        // System info
        extentReports.setSystemInfo("System", "Windows");
        extentReports.setSystemInfo("Author", "Sunilkumar");
        extentReports.setSystemInfo("Build#", "1.1");
        extentReports.setSystemInfo("Team", "Playwright Automation");
        extentReports.setSystemInfo("Customer Name", "CSG");

        return extentReports;
    }
    

    @Override
    public synchronized void onStart(ITestContext context) {
        System.out.println("Test Suite started!");
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println("Test Suite is ending!");
        extent.flush();
        test.remove();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String qualifiedName = result.getMethod().getQualifiedName();
        int last = qualifiedName.lastIndexOf(".");
        int mid = qualifiedName.substring(0, last).lastIndexOf(".");
        String className = qualifiedName.substring(mid + 1, last);

        System.out.println(methodName + " started!");
        ExtentTest extentTest = extent.createTest(methodName, result.getMethod().getDescription());

        extentTest.assignCategory(result.getTestContext().getSuite().getName());
        extentTest.assignCategory(className);
        test.set(extentTest);
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    public synchronized void onTestSuccess(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " passed!");
        String screenshotPath = saveScreenshot(result.getMethod().getMethodName());
        test.get().pass("Test passed", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    public synchronized void onTestFailure(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " failed!");
        String screenshotPath = saveScreenshot(result.getMethod().getMethodName());
        test.get().fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    public synchronized void onTestSkipped(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " skipped!");
        String screenshotPath = saveScreenshot(result.getMethod().getMethodName());
        test.get().skip(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }
    
    /**
     * Sorts the report folders in descending order (latest first).
     */
    private static void sortReportFolders() {
        File baseDir = new File(BASE_FOLDER);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return;
        }

        File[] reportDirs = baseDir.listFiles(File::isDirectory);
        if (reportDirs != null) {
            Arrays.sort(reportDirs, Comparator.comparing(File::getName).reversed());
            System.out.println("Reports sorted in descending order:");
            for (File report : reportDirs) {
                System.out.println(report.getName());
            }
        }
    }

    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName());
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    private String saveScreenshot(String testName) {
        String screenshotRelativePath = "Screenshots/" + testName + "_" 
            + new SimpleDateFormat("hh-mm-ss-a").format(new Date()) + ".png";
        String screenshotFullPath = reportFolderPath + screenshotRelativePath;

        takeScreenshot(screenshotFullPath);  // Call Playwright screenshot method with full path
        return screenshotRelativePath; // Return relative path for Extent Report
    }
}
