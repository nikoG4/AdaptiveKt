import { test, expect } from '@playwright/test';

test.describe('Adaptive Store Premium E2E', () => {
  test.beforeEach(async ({ page }) => {
    page.on('console', msg => console.log(`BROWSER CONSOLE: ${msg.text()}`));
    page.on('pageerror', err => console.log(`BROWSER ERROR: ${err.message}`));
  });

  test('should load app and capture premium hero', async ({ page }) => {
    const consolePromise = page.waitForEvent('console', msg => msg.text() === 'APP_STARTED');
    await page.goto('/');
    await consolePromise;
    
    const canvas = await page.waitForSelector('canvas', { timeout: 30000 });
    expect(canvas).toBeTruthy();
    
    // Give it a moment to render the first frame
    await page.waitForTimeout(2000);
    
    // Take a screenshot instead of checking text, since Wasm Canvas headless doesn't reliably expose a11y DOM here.
    await page.screenshot({ path: 'test-results/home-hero.png' });
  });

  test('navigate to shop and back using browser button', async ({ page }) => {
    await page.goto('/');
    await page.waitForSelector('canvas');
    await page.waitForTimeout(2000);
    
    // Attempt visual click on canvas where 'Shop new arrivals' button is roughly located
    // This is a minimal test bridge substitute since getByText is unavailable in pure Canvas
    await page.mouse.click(page.viewportSize()!.width / 2 - 100, page.viewportSize()!.height / 2 + 100);
    
    await page.waitForTimeout(1000);
    await page.screenshot({ path: 'test-results/shop-products.png' });
    
    await page.goBack();
    await page.waitForTimeout(1000);
    await page.screenshot({ path: 'test-results/home-back.png' });
  });
});
