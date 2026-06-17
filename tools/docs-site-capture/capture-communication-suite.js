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
  { name: 'chat-detail', hash: '#/chat/conversation/team-alpha' },
  { name: 'chat-search', hash: '#/chat/search' },
  { name: 'contacts', hash: '#/contacts' },
  { name: 'calls', hash: '#/calls' },
  { name: 'settings', hash: '#/settings' }
];

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const context = await browser.newContext();
  const page = await context.newPage();

  const savedFiles = [];

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
        savedFiles.push(screenshotPath);
      }
    }
  }

  console.log('Generating contact sheets...');
  
  async function createContactSheet(files, outputPath, columns, thumbWidth = 250) {
    let html = '<html><body style="background: #222; margin: 0; padding: 20px; display: grid; gap: 20px; grid-template-columns: repeat(' + columns + ', 1fr);">';
    for (const f of files) {
      const src = 'file://' + path.resolve(f).replace(/\\/g, '/');
      html += `<div style="text-align:center; color: white; font-family: sans-serif; font-size: 12px;"><img src="${src}" style="width: ${thumbWidth}px; border: 1px solid #555; margin-bottom: 8px;"/><br/>${path.basename(f)}</div>`;
    }
    html += '</body></html>';
    const tempPath = path.join(outputDir, 'temp-contact-sheet.html');
    fs.writeFileSync(tempPath, html);
    const sheetPage = await context.newPage();
    const rows = Math.ceil(files.length / columns);
    await sheetPage.setViewportSize({ width: columns * (thumbWidth + 40), height: rows * (thumbWidth * 2) });
    await sheetPage.goto('file://' + path.resolve(tempPath).replace(/\\/g, '/'), { waitUntil: 'networkidle' });
    await sheetPage.waitForTimeout(2000); // Wait for images
    await sheetPage.screenshot({ path: outputPath, fullPage: true });
    await sheetPage.close();
    fs.unlinkSync(tempPath);
  }

  const lightFiles = savedFiles.filter(f => f.includes('-light.png'));
  const darkFiles = savedFiles.filter(f => f.includes('-dark.png'));

  await createContactSheet(lightFiles, path.join(outputDir, 'contact-sheet-communication-suite-light.png'), 5, 250);
  await createContactSheet(darkFiles, path.join(outputDir, 'contact-sheet-communication-suite-dark.png'), 5, 250);
  await createContactSheet(savedFiles, path.join(outputDir, 'contact-sheet-communication-suite-all.png'), 10, 200);

  await browser.close();
  console.log(`Screenshots and contact sheets saved to ${outputDir}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
