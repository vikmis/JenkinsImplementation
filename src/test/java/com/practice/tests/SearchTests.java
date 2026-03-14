package com.practice.tests;

import org.testng.Assert;
import org.testng.annotations.*;

/**
 * SearchTests — Regression group
 * Used in parallel execution demo in Jenkins pipeline.
 * Thread name is printed to show tests running in parallel.
 */
public class SearchTests {

    private String env;

    @BeforeClass
    public void setup() {
        env = System.getProperty("env", "dev");
        System.out.println("========================================");
        System.out.println("[SETUP] SearchTests starting...");
        System.out.println("[SETUP] Thread: " + Thread.currentThread().getName());
        System.out.println("[SETUP] Environment: " + env);
        System.out.println("========================================");
    }

    @Test(groups = {"regression"}, priority = 1)
    public void testBasicSearch() {
        System.out.println("[TEST] testBasicSearch | Thread: "
                + Thread.currentThread().getName());
        System.out.println("[TEST] Simulating search for keyword: 'laptop'");
        int resultCount = 42;
        Assert.assertTrue(resultCount > 0, "Search should return results");
        System.out.println("[TEST] testBasicSearch PASSED ✓");
    }

    @Test(groups = {"regression"}, priority = 2)
    public void testSearchWithFilters() {
        System.out.println("[TEST] testSearchWithFilters | Thread: "
                + Thread.currentThread().getName());
        System.out.println("[TEST] Simulating search with price filter...");
        double maxPrice = 50000.0;
        double foundPrice = 35000.0;
        Assert.assertTrue(foundPrice <= maxPrice,
                "Filtered results should respect price cap");
        System.out.println("[TEST] testSearchWithFilters PASSED ✓");
    }

    @Test(groups = {"regression"}, priority = 3)
    public void testEmptySearch() {
        System.out.println("[TEST] testEmptySearch | Thread: "
                + Thread.currentThread().getName());
        System.out.println("[TEST] Simulating empty search query...");
        String errorMsg = "Please enter a search term";
        Assert.assertFalse(errorMsg.isEmpty(), "Should show error for empty search");
        System.out.println("[TEST] testEmptySearch PASSED ✓");
    }

    @AfterClass
    public void teardown() {
        System.out.println("[TEARDOWN] SearchTests completed | Thread: "
                + Thread.currentThread().getName());
    }
}
