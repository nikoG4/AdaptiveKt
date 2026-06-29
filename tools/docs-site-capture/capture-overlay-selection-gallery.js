const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/overlay-selection-gallery';

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

const states = [
  {
    name: 'selectable-text',
    hash: 'adaptive-selection-area',
    mode: 'normal'
  },
  {
    name: 'dialog-centered',
    hash: 'adaptive-accordion-dialog-centered-open',
    baseHash: 'adaptive-accordion-dialog',
    mode: 'dialog'
  },
  {
    name: 'dialog-long',
    hash: 'adaptive-accordion-dialog-long-open',
    baseHash: 'adaptive-accordion-dialog',
    mode: 'dialog'
  },
  {
    name: 'dialog-destructive',
    hash: 'adaptive-accordion-dialog-destructive-open',
    baseHash: 'adaptive-accordion-dialog',
    mode: 'dialog'
  }
];

function routeFor(theme, hash) {
  return `${baseUrl}/components/${theme.query}#${hash}`;
}

function countByteDiff(before, after) {
  const length = Math.min(before.length, after.length);
  let diff = Math.abs(before.length - after.length);
  for (let i = 0; i < length; i += 1) {
    if (before[i] !== after[i]) {
      diff += 1;
    }
  }
  return diff;
}

async function waitForCanvas(page) {
  await page.waitForSelector('#webApp canvas', { timeout: 30000 });
  await page.waitForTimeout(1500);

  const canvasBox = await page.locator('#webApp canvas').boundingBox();
  if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
    throw new Error('Compose canvas is missing or too small');
  }
}

async function captureState(page, viewport, state) {
  if (state.mode !== 'dialog') {
    return { opened: true, layoutShift: false };
  }

  const baseUrlForState = routeFor(currentTheme, state.baseHash || 'adaptive-accordion-dialog');
  await page.goto(baseUrlForState, { waitUntil: 'networkidle' });
  await waitForCanvas(page);
  const baseHeight = await page.evaluate(() => document.documentElement.scrollHeight);
  const before = await page.screenshot({ fullPage: true });
  await page.goto(routeFor(currentTheme, state.hash), { waitUntil: 'networkidle' });
  await waitForCanvas(page);
  const afterHeight = await page.evaluate(() => document.documentElement.scrollHeight);
  const after = await page.screenshot({ fullPage: true });
  const maxDiff = countByteDiff(before, after);
  const opened = maxDiff > 8000;
  return {
    opened,
    layoutShift: Math.abs(afterHeight - baseHeight) > 2,
    screenshotByteDiff: maxDiff
  };
}

let currentTheme = themes[0];

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
  const results = [];

  for (const theme of themes) {
    currentTheme = theme;
    for (const viewport of viewports) {
      for (const state of states) {
        const context = await browser.newContext({
          viewport: { width: viewport.width, height: viewport.height },
          colorScheme: theme.name
        });
        const page = await context.newPage();
        const consoleMessages = [];
        const requestFailures = [];

        page.on('console', message => {
          if (message.type() === 'error' && !message.text().includes('WebGL') && !message.text().includes('GL Driver Message')) {
            consoleMessages.push(message.text());
          }
        });
        page.on('requestfailed', request => {
          requestFailures.push(`${request.method()} ${request.url()} ${request.failure()?.errorText || ''}`.trim());
        });

        const url = routeFor(theme, state.hash);
        const filename = `${state.name}-${viewport.name}-${theme.name}.png`;
        const filePath = path.join(outputDir, filename);

        console.log(`Capturing ${url} (${viewport.width}x${viewport.height}, ${theme.name}, ${state.name}) -> ${filename}`);

        try {
          const response = await page.goto(url, { waitUntil: 'networkidle' });
          if (!response || !response.ok()) {
            throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
          }

          await waitForCanvas(page);
          const overlayResult = await captureState(page, viewport, state);

          await page.screenshot({ path: filePath, fullPage: true });
          let screenshotSize = fs.statSync(filePath).size;
          if (screenshotSize < 20000) {
            await page.waitForTimeout(2000);
            await page.screenshot({ path: filePath, fullPage: true });
            screenshotSize = fs.statSync(filePath).size;
          }
          if (screenshotSize < 20000) {
            throw new Error(`Screenshot appears blank or incomplete (${screenshotSize} bytes)`);
          }

          const overflow = await page.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
          if (state.mode === 'dialog' && !overlayResult.opened) {
            throw new Error(`Dialog did not visibly open after candidate clicks; max diff ${overlayResult.screenshotByteDiff || 0}`);
          }

          results.push({
            state: state.name,
            viewport: viewport.name,
            theme: theme.name,
            size: `${viewport.width}x${viewport.height}`,
            file: filename,
            success: true,
            consoleErrors: consoleMessages.length,
            networkFailures: requestFailures.length,
            horizontalOverflow: overflow,
            dialogOpened: state.mode === 'dialog' ? overlayResult.opened : 'n/a',
            layoutShift: state.mode === 'dialog' ? overlayResult.layoutShift : false
          });
        } catch (error) {
          console.error(`Failed: ${error.message}`);
          results.push({
            state: state.name,
            viewport: viewport.name,
            theme: theme.name,
            size: `${viewport.width}x${viewport.height}`,
            file: filename,
            success: false,
            error: error.message,
            consoleErrors: consoleMessages.length,
            networkFailures: requestFailures.length,
            horizontalOverflow: false,
            dialogOpened: false,
            layoutShift: false
          });
        } finally {
          await page.close();
          await context.close();
        }
      }
    }
  }

  await browser.close();
  writeReport(results);

  const failed = results.some(result =>
    !result.success ||
    result.consoleErrors > 0 ||
    result.networkFailures > 0 ||
    result.horizontalOverflow ||
    result.layoutShift
  );
  if (failed) {
    process.exit(1);
  }
}

function writeReport(results) {
  let report = '# Overlay and selection capture report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| State | Viewport | Theme | Size | File | Console errors | Network failures | Horizontal overflow | Dialog opened | Layout shift | Result |\n';
  report += '|---|---|---|---|---|---:|---:|---|---|---|---|\n';

  for (const result of results) {
    const status = result.success ? 'OK' : `FAILED: ${result.error}`;
    const link = result.success ? `[${result.file}](${result.file})` : '-';
    report += `| ${result.state} | ${result.viewport} | ${result.theme} | ${result.size} | ${link} | ${result.consoleErrors} | ${result.networkFailures} | ${result.horizontalOverflow ? 'yes' : 'no'} | ${result.dialogOpened} | ${result.layoutShift ? 'yes' : 'no'} | ${status} |\n`;
  }

  fs.writeFileSync(path.join(outputDir, 'capture-report.md'), report);
  console.log(`Report generated at ${path.join(outputDir, 'capture-report.md')}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});





