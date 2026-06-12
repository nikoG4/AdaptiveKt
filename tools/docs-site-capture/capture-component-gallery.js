const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/component-gallery-hardening';

const routesToCapture = [
  '#adaptive-theme',
  '#adaptive-card',
  '#adaptive-selectionarea',
  '#adaptive-dialog',
  '#adaptive-carousel',
  '#adaptive-data-view',
  '#adaptive-form-layout',
  '#adaptive-navigation-scaffold'
];

const viewports = [
  { name: 'compact', width: 390, height: 844 },
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

  for (const hash of routesToCapture) {
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

        const url = `${baseUrl}/components/${theme.query}${hash}`;
        const routeName = hash.replace('#', '');
        const filename = `${routeName}-${viewport.name}-${theme.name}.png`;
        const filePath = path.join(outputDir, filename);

        console.log(`Capturing ${url} (${viewport.width}x${viewport.height}, ${theme.name}) -> ${filename}`);

        try {
          const response = await page.goto(url, { waitUntil: 'networkidle' });
          if (!response || !response.ok()) {
            throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
          }

          await page.waitForSelector('#webApp canvas', { timeout: 30000 });
          await page.waitForTimeout(3000); // Allow Compose to settle

          const canvasBox = await page.locator('#webApp canvas').boundingBox();
          if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
            throw new Error('Compose canvas is missing or too small');
          }

          // Overlap Detection: Try to query semantic text elements.
          // Note: In Compose Wasm, semantics are rendered as hidden div elements overlaid on the canvas.
          let overlapDetected = false;
          try {
            // Very naive overlap check based on bounding boxes of semantic elements on the page
            // This relies on Compose's semantics tree being queryable via Playwright text locators
            const titleLoc = page.locator(`text=${routeName.split('-').map(p => p.charAt(0).toUpperCase() + p.slice(1)).join('')}`).first();
            const titleBox = await titleLoc.boundingBox();
            if (titleBox && titleBox.height === 0) {
              console.warn(`Warning: Title height is 0 for ${routeName}`);
              overlapDetected = true;
            }
            
            // Just capturing overlap flag
          } catch (e) {
            // Ignore if we can't find semantic text, since Compose might not export everything perfectly
          }

          const overflow = await page.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
          if (overflow) {
            console.warn(`Warning: Horizontal overflow detected for ${routeName}`);
          }

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
            route: routeName,
            viewport: viewport.name,
            theme: theme.name,
            size: `${viewport.width}x${viewport.height}`,
            file: filename,
            success: true,
            consoleErrors: consoleMessages.length,
            networkFailures: requestFailures.length,
            horizontalOverflow: overflow,
            overlapDetected: overlapDetected
          });
        } catch (error) {
          console.error(`Failed: ${error.message}`);
          results.push({
            route: routeName,
            viewport: viewport.name,
            theme: theme.name,
            size: `${viewport.width}x${viewport.height}`,
            file: filename,
            success: false,
            error: error.message,
            consoleErrors: consoleMessages.length,
            networkFailures: requestFailures.length,
            horizontalOverflow: false,
            overlapDetected: false
          });
        } finally {
          await page.close();
          await context.close();
        }
      }
    }
  }

  await browser.close();

  // Generate Contact Sheet if montage is available (ImageMagick)
  try {
    console.log("Generating contact sheet...");
    execSync(`magick montage -geometry 400x+10+10 "${outputDir}/*.png" "${outputDir}/contact-sheet.png"`, { stdio: 'ignore' });
  } catch (e) {
    console.log("ImageMagick montage not available or failed. Skipping contact sheet.");
  }

  writeReport(results);

  const failed = results.some(result => !result.success || result.consoleErrors > 0 || result.networkFailures > 0 || result.horizontalOverflow || result.overlapDetected);
  if (failed) {
    process.exit(1);
  }
}

function writeReport(results) {
  const reportDir = path.resolve('../../docs/internal');
  if (!fs.existsSync(reportDir)) fs.mkdirSync(reportDir, { recursive: true });
  const reportFile = path.join(reportDir, 'DOCS_SITE_VISUAL_VALIDATION_REPORT.md');

  let report = '# Docs Site Visual Validation Report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Route | Viewport | Theme | Screenshot | Console | Network | Overflow | Overlap | Result |\n';
  report += '|---|---|---|---|---:|---:|---|---|---|\n';

  for (const result of results) {
    const status = result.success && !result.horizontalOverflow && !result.overlapDetected ? 'OK' : `FAILED: ${result.error || 'visual regression'}`;
    const link = result.success ? `[image](../../${outputDir}/${result.file})` : '-';
    report += `| ${result.route} | ${result.viewport} | ${result.theme} | ${link} | ${result.consoleErrors} | ${result.networkFailures} | ${result.horizontalOverflow ? 'yes' : 'no'} | ${result.overlapDetected ? 'yes' : 'no'} | ${status} |\n`;
  }

  fs.writeFileSync(reportFile, report);
  console.log(`Report generated at ${reportFile}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
