const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/component-gallery';

const route = { name: 'component-gallery', path: '/components/' };

const viewports = [
  { name: 'mobile', width: 390, height: 844 },
  { name: 'tablet', width: 768, height: 1024 },
  { name: 'desktop', width: 1280, height: 800 },
  { name: 'large', width: 1440, height: 900 }
];

const themes = [
  { name: 'light', query: '' },
  { name: 'dark', query: '?theme=dark' }
];

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const results = [];

  for (const theme of themes) {
    for (const viewport of viewports) {
      const context = await browser.newContext({
        viewport: { width: viewport.width, height: viewport.height },
        colorScheme: theme.name
      });
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

      const url = `${baseUrl}${route.path}${theme.query}`;
      const filename = `component-gallery-${viewport.name}-${theme.name}.png`;
      const filePath = path.join(outputDir, filename);

      console.log(`Capturing ${url} (${viewport.width}x${viewport.height}, ${theme.name}) -> ${filename}`);

      try {
        const response = await page.goto(url, { waitUntil: 'networkidle' });
        if (!response || !response.ok()) {
          throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
        }

        await page.waitForSelector('#webApp canvas', { timeout: 30000 });
        await page.waitForTimeout(3200);

        const canvasBox = await page.locator('#webApp canvas').boundingBox();
        if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
          throw new Error('Compose canvas is missing or too small');
        }

        const overflow = await page.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
        await page.screenshot({ path: filePath, fullPage: true });

        let screenshotSize = fs.statSync(filePath).size;
        if (screenshotSize < 20000) {
          await page.waitForTimeout(4000);
          await page.screenshot({ path: filePath, fullPage: true });
          screenshotSize = fs.statSync(filePath).size;
        }

        if (screenshotSize < 20000) {
          throw new Error(`Screenshot appears blank or incomplete (${screenshotSize} bytes)`);
        }

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
        await context.close();
      }
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
  let report = '# Component gallery capture report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Route | Viewport | Theme | Size | File | Console errors | Network failures | Horizontal overflow | Result |\n';
  report += '|---|---|---|---|---|---:|---:|---|---|\n';

  for (const result of results) {
    const status = result.success ? 'OK' : `FAILED: ${result.error}`;
    const link = result.success ? `[${result.file}](${result.file})` : '-';
    report += `| ${result.route} | ${result.viewport} | ${result.theme} | ${result.size} | ${link} | ${result.consoleErrors} | ${result.networkFailures} | ${result.horizontalOverflow ? 'yes' : 'no'} | ${status} |\n`;
  }

  fs.writeFileSync(path.join(outputDir, 'capture-report.md'), report);
  console.log(`Report generated at ${path.join(outputDir, 'capture-report.md')}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
