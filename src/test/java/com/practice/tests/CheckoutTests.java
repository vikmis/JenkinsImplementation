package com.practice.tests;

import org.testng.Assert;
import org.testng.annotations.*;

/**
 * CheckoutTests — Regression group
 * Contains one INTENTIONAL FAILING test to demonstrate:
 *   - Jenkins build marking as UNSTABLE (not FAILURE) using allowUnstableBuilds
 *   - post{} block behavior on test failures
 *   - TestNG report showing failed tests
 *
 * To control the failure via Jenkins parameter:
 *   -DforceFailure=true  → testPaymentProcessing will FAIL
 *   -DforceFailure=false → testPaymentProcessing will PASS (default)
 */
public class CheckoutTests {

    private String env;
    private boolean forceFailure;

    @BeforeClass(alwaysRun=true)
    public void setup() {
        env          = System.getProperty("env", "dev");
        forceFailure = Boolean.parseBoolean(System.getProperty("forceFailure", "false"));

        System.out.println("========================================");
        System.out.println("[SETUP] CheckoutTests starting...");
        System.out.println("[SETUP] Environment  : " + env);
        System.out.println("[SETUP] Force Failure: " + forceFailure);
        System.out.println("========================================");
    }

    @Test(groups = {"regression"}, priority = 1)
    public void testAddToCart() {
        System.out.println("[TEST] testAddToCart | env=" + env);
        System.out.println("[TEST] Simulating adding item to cart...");
        int cartCount = 1;
        Assert.assertEquals(cartCount, 1, "Cart should have 1 item");
        System.out.println("[TEST] testAddToCart PASSED ✓");
    }

    @Test(groups = {"regression"}, priority = 2)
    public void testApplyCoupon() {
        System.out.println("[TEST] testApplyCoupon | env=" + env);
        System.out.println("[TEST] Simulating coupon code SAVE10...");
        double originalPrice = 1000.0;
        double discountedPrice = 900.0;
        Assert.assertTrue(discountedPrice < originalPrice,
                "Discounted price should be less than original");
        System.out.println("[TEST] testApplyCoupon PASSED ✓");
    }

    @Test(groups = {"regression"}, priority = 3)
    public void testPaymentProcessing() {
        System.out.println("[TEST] testPaymentProcessing | env=" + env);
        System.out.println("[TEST] Simulating payment gateway call...");

        if (forceFailure) {
            System.out.println("[TEST] forceFailure=true → INTENTIONALLY FAILING this test!");
            Assert.fail("Payment gateway timeout — simulated failure for Jenkins demo");
        }

        System.out.println("[TEST] Payment processed successfully (simulated)");
        Assert.assertTrue(true, "Payment should succeed");
        System.out.println("[TEST] testPaymentProcessing PASSED ✓");
    }

    @Test(groups = {"regression"}, priority = 4)
    public void testOrderConfirmation() {
        System.out.println("[TEST] testOrderConfirmation | env=" + env);
        System.out.println("[TEST] Simulating order confirmation email trigger...");
        String orderId = "ORD-20250001";
        Assert.assertNotNull(orderId, "Order ID should not be null");
        Assert.assertTrue(orderId.startsWith("ORD-"), "Order ID format should match");
        System.out.println("[TEST] testOrderConfirmation PASSED ✓");
    }

    @AfterClass
    public void teardown() {
        System.out.println("[TEARDOWN] CheckoutTests completed");
    }
}
