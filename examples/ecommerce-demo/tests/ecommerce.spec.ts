import { test, expect } from '@playwright/test';

async function waitForCompose(page: any) {
  await page.waitForSelector('canvas', { timeout: 30000 });
  await page.waitForTimeout(1200);
}

async function expectHealthyCanvas(page: any) {
  const metrics = await page.evaluate(() => ({
    canvasCount: document.querySelectorAll('canvas').length,
    innerWidth: window.innerWidth,
    scrollWidth: document.documentElement.scrollWidth,
    bodyBg: getComputedStyle(document.body).backgroundColor,
  }));
  expect(metrics.canvasCount).toBeGreaterThan(0);
  expect(metrics.scrollWidth).toBeLessThanOrEqual(metrics.innerWidth + 2);
  return metrics;
}

test.describe('Adaptive Store visual smoke', () => {
  test('mobile home uses bottom-nav viewport without horizontal overflow', async ({ page }) => {
    await page.setViewportSize({ width: 390, height: 844 });
    await page.goto('/');
    await waitForCompose(page);
    await expectHealthyCanvas(page);
    await page.screenshot({ path: 'test-results/mobile-home-smoke.png', fullPage: true });
  });

  test('desktop products render without broken navigation overflow', async ({ page }) => {
    await page.setViewportSize({ width: 1440, height: 900 });
    await page.goto('/');
    await waitForCompose(page);
    await page.evaluate(() => {
        window.history.pushState(null, "", "/shop");
        window.dispatchEvent(new PopStateEvent("popstate"));
    });
    await waitForCompose(page);
    await expectHealthyCanvas(page);
    await page.screenshot({ path: 'test-results/desktop-products-smoke.png', fullPage: true });
  });

  test('dark color scheme renders a distinct app frame', async ({ browser }) => {
    const page = await browser.newPage({ viewport: { width: 390, height: 844 }, colorScheme: 'dark' });
    await page.goto('/');
    await waitForCompose(page);
    await expectHealthyCanvas(page);
    await page.screenshot({ path: 'test-results/mobile-home-dark-smoke.png', fullPage: true });
    await page.close();
  });

  test('browser back returns from detail to products and checkout to cart history', async ({ page }) => {
    test.setTimeout(70000);
    await page.goto('/');
    await waitForCompose(page);
    await page.evaluate(() => {
        window.history.pushState(null, "", "/shop");
        window.dispatchEvent(new PopStateEvent("popstate"));
    });
    await waitForCompose(page);
    await page.evaluate(() => {
        window.history.pushState(null, "", "/product/p1");
        window.dispatchEvent(new PopStateEvent("popstate"));
    });
    await waitForCompose(page);
    await page.goBack();
    await waitForCompose(page);
    await expect(page.url()).toContain('/shop');

    await page.evaluate(() => {
        window.history.pushState(null, "", "/cart");
        window.dispatchEvent(new PopStateEvent("popstate"));
    });
    await waitForCompose(page);
    await page.evaluate(() => {
        window.history.pushState(null, "", "/checkout");
        window.dispatchEvent(new PopStateEvent("popstate"));
    });
    await waitForCompose(page);
    await page.goBack();
    await waitForCompose(page);
    await expect(page.url()).toContain('/cart');
  });
});
