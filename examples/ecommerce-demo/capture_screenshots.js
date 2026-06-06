const { chromium } = require('@playwright/test');
const fs = require('fs');
const path = require('path');

const BASE_URL = 'http://127.0.0.1:4173';
const resultsDir = path.join(__dirname, 'test-results', 'visual-after');

async function waitForCompose(page) {
  await page.waitForSelector('canvas', { timeout: 30000 });
  await page.waitForTimeout(2500);
}

async function assertNoHorizontalOverflow(page, name) {
  const metrics = await page.evaluate(() => ({
    innerWidth: window.innerWidth,
    scrollWidth: document.documentElement.scrollWidth,
    canvasCount: document.querySelectorAll('canvas').length,
  }));
  if (metrics.canvasCount < 1) {
    throw new Error(`${name}: no Compose canvas found`);
  }
  if (metrics.scrollWidth > metrics.innerWidth + 2) {
    throw new Error(`${name}: horizontal overflow ${metrics.scrollWidth} > ${metrics.innerWidth}`);
  }
}

async function capture(page, route, name) {
  console.log(`Capture ${name}: ${route}`);
  await page.goto(`${BASE_URL}${route}`);
  await waitForCompose(page);
  await assertNoHorizontalOverflow(page, name);
  await page.screenshot({ path: path.join(resultsDir, `${name}.png`), fullPage: true });
}

(async () => {
  if (!fs.existsSync(resultsDir)) {
    fs.mkdirSync(resultsDir, { recursive: true });
  }

  const browser = await chromium.launch();
  const mobileViewport = { width: 390, height: 844 };
  const desktopViewport = { width: 1440, height: 900 };

  try {
    const mobileLight = await browser.newPage({ viewport: mobileViewport, colorScheme: 'light' });
    await capture(mobileLight, '/', 'mobile-home-light');
    await capture(mobileLight, '/#/shop', 'mobile-products-light');
    await capture(mobileLight, '/#/product/p1', 'mobile-detail-light');
    await capture(mobileLight, '/#/cart', 'mobile-cart-light');
    await capture(mobileLight, '/#/checkout', 'mobile-checkout-light');
    await mobileLight.screenshot({ path: path.join(resultsDir, 'navigation-resize-mobile.png'), fullPage: true });
    await mobileLight.close();

    const mobileDark = await browser.newPage({ viewport: mobileViewport, colorScheme: 'dark' });
    await capture(mobileDark, '/', 'mobile-home-dark');
    await mobileDark.close();

    const desktopLight = await browser.newPage({ viewport: desktopViewport, colorScheme: 'light' });
    await capture(desktopLight, '/', 'desktop-home-light');
    await capture(desktopLight, '/#/shop', 'desktop-products-light');
    await desktopLight.screenshot({ path: path.join(resultsDir, 'navigation-resize-desktop.png'), fullPage: true });
    await desktopLight.close();

    const desktopDark = await browser.newPage({ viewport: desktopViewport, colorScheme: 'dark' });
    await capture(desktopDark, '/', 'desktop-home-dark');
    await desktopDark.close();
  } finally {
    await browser.close();
  }
})();
