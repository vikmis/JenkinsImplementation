package com.practice.tests;

import org.testng.Assert;
import org.testng.annotations.*;

/**
 * LoginTests — Smoke + Regression group
 * No real browser/app. Uses System.getProperty() to read Jenkins parameters.
 * Demonstrates: @BeforeClass, @AfterClass, @Test groups, assertions, failures.
 */
public class LoginTests {

    private String env;
    private String browser;
    private static int serverAttempt = 0;

    @BeforeClass(alwaysRun=true)
    public void setup() {
        // Reading values injected by Jenkins pipeline via -Denv=xxx -Dbrowser=xxx
    	System.out.println("ENV PROP: " + System.getProperty("env"));
        System.out.println("BROWSER PROP: " + System.getProperty("browser"));
        System.out.println("SUITE PROP: " + System.getProperty("suite"));
        env     = System.getProperty("env", "dev");
        browser = System.getProperty("browser", "chrome");

        System.out.println("========================================");
        System.out.println("[SETUP] LoginTests starting...");
        System.out.println("[SETUP] Environment : " + env);
        System.out.println("[SETUP] Browser     : " + browser);
        System.out.println("========================================");
    }

    @Test(groups = {"smoke", "regression"}, priority = 1)
    public void testValidLogin() {
        System.out.println("[TEST] testValidLogin running on env=" + env);
        System.out.println("[TEST] Simulating login with valid credentials...");
        // Dummy assertion — always passes
        Assert.assertEquals("LOGIN_SUCCESS", "LOGIN_SUCCESS",
                "Valid login should return success status");
        System.out.println("[TEST] testValidLogin PASSED ✓");
    }

    @Test(groups = {"smoke", "regression"}, priority = 2)
    public void testInvalidLogin() {
        System.out.println("[TEST] testInvalidLogin running on env=" + env);
        System.out.println("[TEST] Simulating login with wrong password...");
        String result = "LOGIN_FAILURE";
        Assert.assertNotEquals(result, "LOGIN_SUCCESS",
                "Invalid login should NOT succeed");
        System.out.println("[TEST] testInvalidLogin PASSED ✓");
    }

    @Test(groups = {"regression"}, priority = 3)
    public void testEmptyCredentials() {
        System.out.println("[TEST] testEmptyCredentials running on env=" + env);
        System.out.println("[TEST] Simulating empty username and password...");
        String errorMessage = "Username and password are required";
        Assert.assertTrue(errorMessage.contains("required"),
                "Should show validation message");
        System.out.println("[TEST] testEmptyCredentials PASSED ✓");
    }

    @Test(groups = {"regression"}, priority = 4)
    public void testPasswordMinLength() {
        System.out.println("[TEST] testPasswordMinLength running on env=" + env);
        System.out.println("[TEST] Checking password minimum length validation...");
        int minLength = 8;
        String testPassword = "abc";
        Assert.assertTrue(testPassword.length() < minLength,
                "Short password should be rejected");
        System.out.println("[TEST] testPasswordMinLength PASSED ✓");
    }
    @Test(groups = {"smoke"}, priority = 5)
    public void testServerConnection() {
        serverAttempt++;
        System.out.println("[TEST] testServerConnection | Attempt #" + serverAttempt);

        if (serverAttempt < 3) {
            System.out.println("[TEST] Server is DOWN on attempt #" + serverAttempt + " — failing!");
            Assert.fail("Server is down! Attempt #" + serverAttempt + " failed.");
        }

        System.out.println("[TEST] Server is UP on attempt #" + serverAttempt + " ✓");
        Assert.assertTrue(true);
    }

    @AfterClass
    public void teardown() {
        System.out.println("========================================");
        System.out.println("[TEARDOWN] LoginTests completed");
        System.out.println("[TEARDOWN] Closing browser: " + browser);
        System.out.println("========================================");
    }
}
