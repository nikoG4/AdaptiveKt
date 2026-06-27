const { chromium } = require('playwright');
const fs = require('fs');

(async () => {
  const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
  const page = await browser.newPage();
  
  await page.goto('http://localhost:8080', { waitUntil: 'networkidle' });
  await page.waitForSelector('#webApp canvas');
  await page.waitForTimeout(3000);
  
  await page.locator('#webApp canvas').click({ position: { x: 10, y: 10 } });
  
  // Tab 5 times
  for (let i = 0; i < 5; i++) {
      await page.keyboard.press('Tab');
      await page.waitForTimeout(200);
  }
  await page.screenshot({ path: 'artifacts/tab5.png' });
  
  await page.keyboard.press('Tab');
  await page.waitForTimeout(200);
  await page.screenshot({ path: 'artifacts/tab6.png' });

  await page.keyboard.press('Tab');
  await page.waitForTimeout(200);
  await page.screenshot({ path: 'artifacts/tab7.png' });

  await browser.close();
  process.exit(0);
})();





