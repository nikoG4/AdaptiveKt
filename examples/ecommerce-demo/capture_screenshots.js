const { chromium } = require('@playwright/test');
const fs = require('fs');
const path = require('path');

(async () => {
  const browser = await chromium.launch();
  const page = await browser.newPage({ viewport: { width: 1440, height: 900 } });
  const resultsDir = path.join(__dirname, 'tests', 'screenshots');
  
  if (!fs.existsSync(resultsDir)) {
    fs.mkdirSync(resultsDir, { recursive: true });
  }

  try {
    console.log('Navigating to app...');
    await page.goto('http://127.0.0.1:4173');
    await page.waitForSelector('canvas', { timeout: 30000 });
    await page.waitForTimeout(5000);
    
    await page.screenshot({ path: path.join(resultsDir, 'desktop-home.png') });
    console.log('Saved: desktop-home.png');

    // Mobile view
    const mobilePage = await browser.newPage({ viewport: { width: 390, height: 844 } });
    await mobilePage.goto('http://127.0.0.1:4173');
    await mobilePage.waitForTimeout(5000);
    await mobilePage.screenshot({ path: path.join(resultsDir, 'mobile-home.png') });
    console.log('Saved: mobile-home.png');
    await mobilePage.close();

    // Navigation to Products
    await page.click('text=Shop new arrivals');
    await page.waitForTimeout(2000);
    await page.screenshot({ path: path.join(resultsDir, 'product-listing.png') });
    console.log('Saved: product-listing.png');

    // Navigation to Product Detail
    await page.click('text=NovaBook Pro 14');
    await page.waitForTimeout(2000);
    await page.screenshot({ path: path.join(resultsDir, 'product-detail.png') });
    console.log('Saved: product-detail.png');

    // Navigation to Cart
    await page.click('text=Add to Cart');
    await page.waitForTimeout(1000);
    await page.click('text=Cart');
    await page.waitForTimeout(2000);
    await page.screenshot({ path: path.join(resultsDir, 'cart.png') });
    console.log('Saved: cart.png');

    // Checkout
    await page.click('text=Checkout');
    await page.waitForTimeout(2000);
    await page.screenshot({ path: path.join(resultsDir, 'checkout.png') });
    console.log('Saved: checkout.png');

  } catch (e) {
    console.error('Error during capture:', e);
  } finally {
    await browser.close();
  }
})();
