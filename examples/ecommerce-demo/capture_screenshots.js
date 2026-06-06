const { chromium } = require('@playwright/test');
const fs = require('fs');
const path = require('path');

(async () => {
  const browser = await chromium.launch();
  const mobileViewport = { width: 390, height: 844 };
  const desktopViewport = { width: 1440, height: 900 };

  const resultsDir = path.join(__dirname, 'test-results', 'visual-after');
  if (!fs.existsSync(resultsDir)) {
    fs.mkdirSync(resultsDir, { recursive: true });
  }

  try {
    const mPage = await browser.newPage({ viewport: mobileViewport });

    const capture = async (route, name) => {
      try {
        console.log(`Mobile: Navigating to ${route}...`);
        await mPage.goto(`http://127.0.0.1:4173${route}`);
        await mPage.waitForSelector('canvas', { timeout: 15000 });
        await mPage.waitForTimeout(4000);
        await mPage.screenshot({ path: path.join(resultsDir, `${name}.png`) });
        console.log(`Saved: ${name}.png`);
      } catch (e) {
         console.error(`Error capturing ${name}:`, e.message);
      }
    };

    await capture('/', 'mobile-home');
    await capture('/shop', 'mobile-products');
    await capture('/product/p1', 'mobile-detail');
    await capture('/cart', 'mobile-cart');
    await capture('/checkout', 'mobile-checkout');
    await capture('/login', 'mobile-login');
    await capture('/account', 'mobile-account');

    await mPage.close();

    const dPage = await browser.newPage({ viewport: desktopViewport });
    const captureDesktop = async (route, name) => {
        try {
          console.log(`Desktop: Navigating to ${route}...`);
          await dPage.goto(`http://127.0.0.1:4173${route}`);
          await dPage.waitForSelector('canvas', { timeout: 15000 });
          await dPage.waitForTimeout(4000);
          await dPage.screenshot({ path: path.join(resultsDir, `${name}.png`) });
          console.log(`Saved: ${name}.png`);
        } catch (e) {
           console.error(`Error capturing ${name}:`, e.message);
        }
      };
    await captureDesktop('/', 'desktop-home');
    await captureDesktop('/shop', 'desktop-products');
    await dPage.close();

  } catch (e) {
    console.error('Error during capture:', e);
  } finally {
    await browser.close();
  }
})();
