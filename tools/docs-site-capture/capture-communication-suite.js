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
  { name: 'chat-detail-direct', hash: '#/chat/conversation/c1' },
  { name: 'chat-detail-group', hash: '#/chat/conversation/c2' },
  { name: 'chat-search', hash: '#/chat/search' },
  { name: 'contacts-list', hash: '#/contacts' },
  { name: 'contacts-favorites', hash: '#/contacts/favorites' },
  { name: 'contacts-detail', hash: '#/contacts/u1' },
  { name: 'calls-recent', hash: '#/calls' },
  { name: 'calls-missed', hash: '#/calls/missed' },
  { name: 'calls-incoming', hash: '#/calls/incoming/u2' },
  { name: 'calls-active', hash: '#/calls/active/u3' },
  { name: 'settings-home', hash: '#/settings' },
  { name: 'settings-profile', hash: '#/settings/profile' },
  { name: 'settings-appearance', hash: '#/settings/appearance' },
  { name: 'settings-notifications', hash: '#/settings/notifications' },
  { name: 'settings-privacy', hash: '#/settings/privacy' },
  { name: 'settings-data', hash: '#/settings/data' },
  { name: 'settings-developer', hash: '#/settings/developer' },
  { name: 'settings-help', hash: '#/settings/help' },
  { name: 'demo-empty', hash: '#/demo/empty-chats' }
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
        const url = `${baseUrl}/?theme=${theme}${screen.hash}`;
        console.log(`Capturing ${screen.name} on ${viewport.name} in ${theme} mode...`);

        // Load page with query param for theme
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
  const chatContactFiles = savedFiles.filter(f => path.basename(f).startsWith('chat-') || path.basename(f).startsWith('contacts-'));
  const callSettingsFiles = savedFiles.filter(f => path.basename(f).startsWith('calls-') || path.basename(f).startsWith('settings-'));
  const mobileDesktopFiles = savedFiles.filter(f => f.includes('-mobile-') || f.includes('-desktop-'));

  await createContactSheet(lightFiles, path.join(outputDir, '1-all-screens-light.png'), 5, 250);
  await createContactSheet(darkFiles, path.join(outputDir, '2-all-screens-dark.png'), 5, 250);
  await createContactSheet(chatContactFiles, path.join(outputDir, '3-chat-contacts.png'), 5, 250);
  await createContactSheet(callSettingsFiles, path.join(outputDir, '4-calls-settings.png'), 5, 250);
  await createContactSheet(mobileDesktopFiles, path.join(outputDir, '5-mobile-desktop-compare.png'), 4, 300);

  await browser.close();
  console.log(`Screenshots and contact sheets saved to ${outputDir}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
