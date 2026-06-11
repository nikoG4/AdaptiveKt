const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080/examples/communication-suite').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/communication-suite';

const viewports = [
  { name: 'mobile', width: 390, height: 844 },
  { name: 'tablet', width: 768, height: 1024 },
  { name: 'laptop', width: 1280, height: 800 },
  { name: 'desktop', width: 1440, height: 900 },
  { name: 'ultrawide', width: 1920, height: 1080 }
];

const themes = ['light', 'dark'];

const screens = [
  { name: 'chat-inbox', hash: '#/chat/inbox' },
  { name: 'chat-detail', hash: '#/chat/conversation/c_1' },
  { name: 'chat-search', hash: '#/chat/search' },
  { name: 'mail-inbox', hash: '#/mail/inbox' },
  { name: 'mail-reading', hash: '#/mail/thread/t_1' },
  { name: 'mail-compose', hash: '#/mail/compose' },
  { name: 'settings', hash: '#/settings' }
];

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const context = await browser.newContext();
  const page = await context.newPage();

  for (const theme of themes) {
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height });

      for (const screen of screens) {
        const url = `${baseUrl}/${screen.hash}`;
        console.log(`Capturing ${screen.name} on ${viewport.name} in ${theme} mode...`);

        // Set theme in localStorage if supported, or via query param (assuming theme=dark)
        // AdaptiveKt examples often use localStorage `adaptive_theme`
        await page.goto(url, { waitUntil: 'networkidle' });
        await page.evaluate((t) => {
            localStorage.setItem('adaptive_theme', t);
        }, theme);
        // Reload to apply theme
        await page.goto(url, { waitUntil: 'networkidle' });

        await page.waitForSelector('canvas', { timeout: 30000 });
        await page.waitForTimeout(2000); // Give time for animations and rendering

        const screenshotPath = path.join(outputDir, `${screen.name}-${viewport.name}-${theme}.png`);
        await page.screenshot({ path: screenshotPath });
      }
    }
  }

  await browser.close();
  console.log(`Screenshots saved to ${outputDir}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
