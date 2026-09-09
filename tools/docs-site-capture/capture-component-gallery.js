const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/component-gallery-hardening';

// Keep these hashes aligned with the canonical ids in SiteComponentsPage.kt.
// Interactive states are captured explicitly for regressions that only appear while an overlay is open.
const routesToCapture = [
  { name: 'adaptive-theme', hash: '#adaptive-theme', expectedText: 'AdaptiveTheme' },
  { name: 'adaptive-card-surface', hash: '#adaptive-card-surface', expectedText: 'AdaptiveCard and AdaptiveSurface' },
  { name: 'adaptive-selection-area', hash: '#adaptive-selection-area', expectedText: 'AdaptiveSelectionArea' },
  { name: 'adaptive-accordion-dialog-centered-open', hash: '#adaptive-accordion-dialog-centered-open', expectedText: 'Confirm action' },
  { name: 'adaptive-select-open', hash: '#adaptive-select', expectedText: 'AdaptiveSelect', openBelowLabel: 'Status' },
  { name: 'adaptive-multi-select-open', hash: '#adaptive-multi-select', expectedText: 'AdaptiveMultiSelect', openBelowLabel: 'Assignees' },
  { name: 'adaptive-carousel', hash: '#adaptive-carousel', expectedText: 'AdaptiveCarousel' },
  { name: 'adaptive-data-view', hash: '#adaptive-data-view', expectedText: 'AdaptiveDataView' },
  { name: 'adaptive-form-layout', hash: '#adaptive-form-layout', expectedText: 'AdaptiveFormLayout' },
  { name: 'adaptive-navigation-scaffold', hash: '#adaptive-navigation-scaffold', expectedText: 'AdaptiveNavigationScaffold' }
];

const viewports = [
  { name: 'compact', width: 390, height: 844 },
  { name: 'tablet', width: 768, height: 1024 },
  { name: 'desktop', width: 1280, height: 800 },
  // Wide enough that AdaptiveDataView's content pane reaches table mode even with docs navigation + TOC visible.
  { name: 'large', width: 1920, height: 1080 }
];

const themes = [
  { name: 'light', query: '' },
  { name: 'dark', query: '?theme=dark' }
];

function countByteDiff(before, after) {
  const length = Math.min(before.length, after.length);
  let diff = Math.abs(before.length - after.length);
  for (let i = 0; i < length; i += 1) {
    if (before[i] !== after[i]) diff += 1;
  }
  return diff;
}

async function visibleTextBox(page, text) {
  const candidates = page.getByText(text, { exact: true });
  const count = await candidates.count();
  for (let i = 0; i < count; i += 1) {
    const box = await candidates.nth(i).boundingBox();
    if (box && box.width > 0 && box.height > 0) return box;
  }
  return null;
}

async function assertRenderedRoute(page, route) {
  const box = await visibleTextBox(page, route.expectedText);
  if (!box) {
    throw new Error(`Expected rendered text not found for ${route.hash}: ${route.expectedText}`);
  }
}

async function openAnchoredControl(page, label) {
  const labelBox = await visibleTextBox(page, label);
  if (!labelBox) {
    throw new Error(`Could not locate control label: ${label}`);
  }

  const before = await page.screenshot({ fullPage: true });
  // Labels are immediately above AdaptiveSelectTriggerFrame in the docs examples.
  await page.mouse.click(labelBox.x + Math.max(24, labelBox.width / 2), labelBox.y + labelBox.height + 30);
  await page.waitForTimeout(700);
  const after = await page.screenshot({ fullPage: true });
  const diff = countByteDiff(before, after);

  if (diff < 5000) {
    throw new Error(`Anchored control under ${label} did not visibly open (screenshot diff ${diff})`);
  }

  return diff;
}

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const results = [];

  for (const route of routesToCapture) {
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
          if (message.type() === 'error') consoleMessages.push(message.text());
        });
        page.on('requestfailed', request => {
          requestFailures.push(`${request.method()} ${request.url()} ${request.failure()?.errorText || ''}`.trim());
        });

        const url = `${baseUrl}/components/${theme.query}${route.hash}`;
        const filename = `${route.name}-${viewport.name}-${theme.name}.png`;
        const filePath = path.join(outputDir, filename);

        console.log(`Capturing ${url} (${viewport.width}x${viewport.height}, ${theme.name}) -> ${filename}`);

        try {
          const response = await page.goto(url, { waitUntil: 'networkidle' });
          if (!response || !response.ok()) {
            throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
          }

          await page.waitForSelector('#webApp canvas', { timeout: 30000 });
          await page.waitForTimeout(2500);

          const canvasBox = await page.locator('#webApp canvas').boundingBox();
          if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
            throw new Error('Compose canvas is missing or too small');
          }

          await assertRenderedRoute(page, route);

          let interactionDiff = 0;
          if (route.openBelowLabel) {
            interactionDiff = await openAnchoredControl(page, route.openBelowLabel);
          }

          const overflow = await page.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
          if (overflow) console.warn(`Warning: Horizontal overflow detected for ${route.name}`);

          await page.screenshot({ path: filePath, fullPage: true });

          let screenshotSize = fs.statSync(filePath).size;
          if (screenshotSize < 20000) {
            await page.waitForTimeout(3000);
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
            horizontalOverflow: overflow,
            interactionDiff
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
            horizontalOverflow: false,
            interactionDiff: 0
          });
        } finally {
          await page.close();
          await context.close();
        }
      }
    }
  }

  await browser.close();

  try {
    console.log('Generating contact sheet...');
    execSync(`magick montage -geometry 400x+10+10 "${outputDir}/*.png" "${outputDir}/contact-sheet.png"`, { stdio: 'ignore' });
  } catch (e) {
    console.log('ImageMagick montage not available or failed. Skipping contact sheet.');
  }

  writeReport(results);

  const failed = results.some(result =>
    !result.success ||
    result.consoleErrors > 0 ||
    result.networkFailures > 0 ||
    result.horizontalOverflow
  );
  if (failed) process.exit(1);
}

function writeReport(results) {
  const reportDir = path.resolve('../../docs/internal');
  if (!fs.existsSync(reportDir)) fs.mkdirSync(reportDir, { recursive: true });
  const reportFile = path.join(reportDir, 'DOCS_SITE_VISUAL_VALIDATION_REPORT.md');

  let report = '# Docs Site Visual Validation Report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Route | Viewport | Theme | Screenshot | Console | Network | Overflow | Interaction diff | Result |\n';
  report += '|---|---|---|---|---:|---:|---|---:|---|\n';

  for (const result of results) {
    const status = result.success && !result.horizontalOverflow ? 'OK' : `FAILED: ${result.error || 'visual regression'}`;
    const link = result.success ? `[image](../../${outputDir}/${result.file})` : '-';
    report += `| ${result.route} | ${result.viewport} | ${result.theme} | ${link} | ${result.consoleErrors} | ${result.networkFailures} | ${result.horizontalOverflow ? 'yes' : 'no'} | ${result.interactionDiff || 0} | ${status} |\n`;
  }

  fs.writeFileSync(reportFile, report);
  console.log(`Report generated at ${reportFile}`);
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
