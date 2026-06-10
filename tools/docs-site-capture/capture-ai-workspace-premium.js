const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/ai-workspace-premium-refactor';

const routes = [
  { name: 'dashboard', path: '/examples/ai-workspace/' },
  { name: 'chats', path: '/examples/ai-workspace/#/chats' },
  { name: 'chat-detail', path: '/examples/ai-workspace/#/chats/c1', viewports: ['desktop'], themes: ['light', 'dark'] },
  { name: 'prompts', path: '/examples/ai-workspace/#/prompts' },
  { name: 'prompt-detail', path: '/examples/ai-workspace/#/prompts/p1', viewports: ['desktop'], themes: ['light', 'dark'] },
  { name: 'agents', path: '/examples/ai-workspace/#/assistants' },
  { name: 'settings', path: '/examples/ai-workspace/#/settings' }
];

const viewports = [
  { name: 'mobile', width: 390, height: 844 },
  { name: 'tablet', width: 768, height: 1024 },
  { name: 'desktop', width: 1280, height: 800 },
  { name: 'large', width: 1440, height: 900 }
];

const themes = [
  { name: 'light', colorScheme: 'light' },
  { name: 'dark', colorScheme: 'dark' }
];

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });

  const browser = await chromium.launch();
  const results = [];

  for (const theme of themes) {
    for (const viewport of viewports) {
      const context = await browser.newContext({
        viewport: { width: viewport.width, height: viewport.height },
        colorScheme: theme.colorScheme
      });

      for (const route of routes) {
        if (route.viewports && !route.viewports.includes(viewport.name)) {
          continue;
        }
        if (route.themes && !route.themes.includes(theme.name)) {
          continue;
        }

        const page = await context.newPage();
        const consoleMessages = [];
        const requestFailures = [];

        page.on('console', message => {
          if (message.type() === 'error') {
            consoleMessages.push(message.text());
          }
        });
        page.on('requestfailed', request => {
          requestFailures.push(`${request.method()} ${request.url()} ${request.failure()?.errorText || ''}`.trim());
        });

        const url = `${baseUrl}${route.path}`;
        const filename = `${viewport.name}-${route.name}-${theme.name}.png`;
        const filePath = path.join(outputDir, filename);

        console.log(`Capturing ${url} (${viewport.width}x${viewport.height}, ${theme.name}) -> ${filename}`);

        try {
          const response = await page.goto(url, { waitUntil: 'networkidle' });
          if (!response || !response.ok()) {
            throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
          }

          await page.waitForSelector('#webApp canvas', { timeout: 30000 });
          await page.waitForTimeout(1600);

          const canvasBox = await page.locator('#webApp canvas').boundingBox();
          if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
            throw new Error('Compose canvas is missing or too small');
          }

          const overflow = await page.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
          await page.screenshot({ path: filePath, fullPage: true });

          results.push({
            route: route.name,
            viewport: viewport.name,
            theme: theme.name,
            size: `${viewport.width}x${viewport.height}`,
            file: filename,
            success: true,
            consoleErrors: consoleMessages.length,
            networkFailures: requestFailures.length,
            horizontalOverflow: overflow
          });
        } catch (error) {
          console.error(`Failed: ${error.message}`);
          results.push({
            route: route.name,
            viewport: viewport.name,
            theme: theme.name,
            size: `${viewport.width}x${viewport.height}`,
            file: filename,
            success: false,
            error: error.message,
            consoleErrors: consoleMessages.length,
            networkFailures: requestFailures.length,
            horizontalOverflow: false
          });
        } finally {
          await page.close();
        }
      }

      await context.close();
    }
  }

  await browser.close();
  writeReport(results);

  const failed = results.some(result => !result.success || result.consoleErrors > 0 || result.networkFailures > 0 || result.horizontalOverflow);
  if (failed) {
    process.exit(1);
  }
}

function writeReport(results) {
  let report = '# AI Workspace premium refactor capture report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Route | Viewport | Theme | Size | File | Console errors | Network failures | Horizontal overflow | Result |\n';
  report += '|---|---|---|---|---|---:|---:|---|---|\n';

  for (const result of results) {
    const status = result.success ? 'OK' : `FAILED: ${result.error}`;
    const link = result.success ? `[${result.file}](${result.file})` : '-';
    report += `| ${result.route} | ${result.viewport} | ${result.theme} | ${result.size} | ${link} | ${result.consoleErrors} | ${result.networkFailures} | ${result.horizontalOverflow ? 'yes' : 'no'} | ${status} |\n`;
  }

  report += '\n## Gallery\n\n';
  for (const result of results.filter(item => item.success)) {
    report += `### ${result.viewport} ${result.route} ${result.theme}\n\n`;
    report += `<img src="${result.file}" width="100%" style="border: 1px solid #ccc;" />\n\n`;
  }

  fs.writeFileSync(path.join(outputDir, 'capture-report.md'), report);
  console.log(`Report generated at ${path.join(outputDir, 'capture-report.md')}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
