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
  { name: 'chat-inbox', hash: '#/chat/inbox', expectedArea: 'Chat' },
  { name: 'chat-detail-direct', hash: '#/chat/conversation/sarah-chen', expectedArea: 'Chat' },
  { name: 'chat-detail-group', hash: '#/chat/conversation/team-alpha', expectedArea: 'Chat' },
  { name: 'chat-search', hash: '#/chat/search', expectedArea: 'Chat' },
  { name: 'contacts-list', hash: '#/contacts', expectedArea: 'Contacts' },
  { name: 'contacts-favorites', hash: '#/contacts/favorites', expectedArea: 'Contacts' },
  { name: 'contacts-detail', hash: '#/contacts/sarah-chen', expectedArea: 'Contacts' },
  { name: 'calls-recent', hash: '#/calls', expectedArea: 'Calls' },
  { name: 'calls-missed', hash: '#/calls/missed', expectedArea: 'Calls' },
  { name: 'calls-incoming', hash: '#/calls/incoming/call_3', expectedArea: 'Calls' },
  { name: 'calls-active', hash: '#/calls/active/call_2', expectedArea: 'Calls' },
  { name: 'settings-home', hash: '#/settings', expectedArea: 'Settings' },
  { name: 'settings-profile', hash: '#/settings/profile', expectedArea: 'Settings' },
  { name: 'settings-appearance', hash: '#/settings/appearance', expectedArea: 'Settings' },
  { name: 'settings-notifications', hash: '#/settings/notifications', expectedArea: 'Settings' },
  { name: 'settings-privacy', hash: '#/settings/privacy', expectedArea: 'Settings' },
  { name: 'settings-data', hash: '#/settings/data', expectedArea: 'Settings' },
  { name: 'settings-developer', hash: '#/settings/developer', expectedArea: 'Settings' },
  { name: 'settings-help', hash: '#/settings/help', expectedArea: 'Settings' },
  { name: 'demo-empty', hash: '#/demo/empty-chats', expectedArea: 'Chat' }
];

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
  const savedFiles = [];
  for (const theme of themes) {
    for (const viewport of viewports) {
      for (const screen of screens) {
        const captureContext = await browser.newContext();
        const page = await captureContext.newPage();
        await page.setViewportSize({ width: viewport.width, height: viewport.height });

        const url = `${baseUrl}/?theme=${theme}${screen.hash}`;
        console.log(`Capturing ${screen.name} on ${viewport.name} in ${theme} mode...`);

        // Load page with query param for theme
        await page.goto(url, { waitUntil: 'networkidle' });

        await page.waitForSelector('canvas', { timeout: 30000 });
        await page.waitForTimeout(2000); // Give time for animations and rendering

        const bridge = await page.evaluate(() => window.__adaptiveKtCommunicationValidation);
        if (!bridge) {
            console.warn(`Validation bridge missing for ${screen.name}`);
        } else {
            if (bridge.route !== screen.hash) console.warn(`Hash mismatch: expected ${screen.hash}, got ${bridge.route}`);
            if (bridge.activeArea !== screen.expectedArea) console.warn(`Area mismatch: expected ${screen.expectedArea}, got ${bridge.activeArea}`);
        }

        const canvasBox = await page.locator('canvas').boundingBox();
        if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
            console.error(`Visually blank canvas detected for ${screen.name}`);
        }

        const screenshotPath = path.join(outputDir, `${screen.name}-${viewport.name}-${theme}.png`);
        await page.screenshot({ path: screenshotPath });
        savedFiles.push(screenshotPath);

        await captureContext.close();
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
    const sheetContext = await browser.newContext();
    const sheetPage = await sheetContext.newPage();
    const rows = Math.ceil(files.length / columns);
    await sheetPage.setViewportSize({ width: columns * (thumbWidth + 40), height: rows * (thumbWidth * 2) });
    await sheetPage.goto('file://' + path.resolve(tempPath).replace(/\\/g, '/'), { waitUntil: 'networkidle' });
    await sheetPage.waitForTimeout(2000); // Wait for images
    await sheetPage.screenshot({ path: outputPath, fullPage: true });
    await sheetPage.close();
    await sheetContext.close();
    fs.unlinkSync(tempPath);
  }

  const lightFiles = savedFiles.filter(f => f.includes('-light.png'));
  const darkFiles = savedFiles.filter(f => f.includes('-dark.png'));
  const chatContactFiles = savedFiles.filter(f => path.basename(f).startsWith('chat-') || path.basename(f).startsWith('contacts-'));
  const callSettingsFiles = savedFiles.filter(f => path.basename(f).startsWith('calls-') || path.basename(f).startsWith('settings-'));
  const mobileDesktopFiles = savedFiles.filter(f => f.includes('-mobile-') || f.includes('-desktop-'));

  await createContactSheet(
    lightFiles,
    path.join(outputDir, 'contact-sheet-adaptive-chat-light.png'),
    5,
    250
  );

  await createContactSheet(
    darkFiles,
    path.join(outputDir, 'contact-sheet-adaptive-chat-dark.png'),
    5,
    250
  );

  await createContactSheet(
    chatContactFiles,
    path.join(outputDir, 'contact-sheet-adaptive-chat-chat-contacts.png'),
    5,
    250
  );

  await createContactSheet(
    callSettingsFiles,
    path.join(outputDir, 'contact-sheet-adaptive-chat-calls-settings.png'),
    5,
    250
  );

  await createContactSheet(
    mobileDesktopFiles,
    path.join(outputDir, 'contact-sheet-adaptive-chat-mobile-desktop.png'),
    4,
    300
  );

  await browser.close();
  console.log(`Screenshots and contact sheets saved to ${outputDir}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});





