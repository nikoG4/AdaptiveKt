const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/route-validation';

// Canonical route ids from SiteComponentsPage.kt. Compose/Wasm renders labels
// into a canvas, so Playwright cannot use getByText() to verify a heading.
// The app publishes the actual resolved component id/title after composition.
// A stale/unknown route falls back to the first component and fails this check.
const routes = [
  ['#adaptive-theme', 'AdaptiveTheme'],
  ['#adaptive-button', 'AdaptiveButton'],
  ['#adaptive-icon-button', 'AdaptiveIconButton'],
  ['#adaptive-badge', 'AdaptiveBadge'],
  ['#adaptive-chip', 'AdaptiveChip'],
  ['#adaptive-avatar', 'AdaptiveAvatar'],
  ['#adaptive-thumbnail', 'AdaptiveThumbnail'],
  ['#adaptive-card-surface', 'AdaptiveCard and AdaptiveSurface'],
  ['#adaptive-selection-area', 'AdaptiveSelectionArea'],
  ['#adaptive-carousel', 'AdaptiveCarousel'],
  ['#adaptive-text-field', 'AdaptiveTextField'],
  ['#adaptive-search-field', 'AdaptiveSearchField'],
  ['#adaptive-select', 'AdaptiveSelect'],
  ['#adaptive-multi-select', 'AdaptiveMultiSelect'],
  ['#adaptive-tabs', 'AdaptiveTabs'],
  ['#adaptive-accordion-dialog', 'AdaptiveAccordion and AdaptiveDialog'],
  ['#adaptive-navigation-scaffold', 'AdaptiveNavigationScaffold'],
  ['#adaptive-data-view', 'AdaptiveDataView'],
  ['#adaptive-form-layout', 'AdaptiveFormLayout'],
  ['#adaptive-empty-state', 'AdaptiveEmptyState'],
  ['#adaptive-loading-state', 'AdaptiveLoadingState'],
  ['#adaptive-error-state', 'AdaptiveErrorState']
];

async function assertResolvedComponentRoute(page, hash, expectedTitle) {
  const expectedId = hash.slice(1);
  const expectedPageTitle = `${expectedTitle} | AdaptiveKt Components`;
  try {
    await page.waitForFunction(
      ([id, title]) => document.documentElement.getAttribute('data-adaptive-component-route') === id &&
        document.title === title,
      [expectedId, expectedPageTitle],
      { timeout: 15000 }
    );
  } catch (error) {
    const actual = await page.evaluate(() => ({
      id: document.documentElement.getAttribute('data-adaptive-component-route'),
      title: document.title
    }));
    throw new Error(
      `Route ${hash} did not resolve to ${expectedTitle} (actual id=${actual.id}, title=${actual.title})`
    );
  }
}

async function validate() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const context = await browser.newContext();
  const page = await context.newPage();
  const results = [];

  for (const [hash, expectedText] of routes) {
    const consoleMessages = [];
    const requestFailures = [];

    const consoleHandler = message => {
      if (message.type() === 'error') consoleMessages.push(message.text());
    };
    const requestHandler = request => {
      requestFailures.push(`${request.method()} ${request.url()} ${request.failure()?.errorText || ''}`.trim());
    };
    page.on('console', consoleHandler);
    page.on('requestfailed', requestHandler);

    const url = `${baseUrl}/components/${hash}`;
    console.log(`Validating ${url}...`);

    try {
      const response = await page.goto(url, { waitUntil: 'networkidle' });
      if (response !== null && !response.ok()) {
        throw new Error(`HTTP status ${response.status()}`);
      }

      await page.waitForSelector('#webApp canvas', { timeout: 30000 });
      await page.waitForTimeout(1000);

      const canvasBox = await page.locator('#webApp canvas').boundingBox();
      if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
        throw new Error('Compose canvas is missing or too small');
      }

      if (!page.url().includes(hash)) {
        throw new Error(`URL did not navigate to expected hash ${hash}`);
      }

      await assertResolvedComponentRoute(page, hash, expectedText);

      if (consoleMessages.length > 0) {
        throw new Error(`Console errors detected: ${consoleMessages[0]}`);
      }

      results.push({ hash, expectedText, success: true, consoleErrors: 0, networkFailures: requestFailures.length });
    } catch (error) {
      console.error(`Failed: ${error.message}`);
      results.push({
        hash,
        expectedText,
        success: false,
        error: error.message,
        consoleErrors: consoleMessages.length,
        networkFailures: requestFailures.length
      });
    } finally {
      page.off('console', consoleHandler);
      page.off('requestfailed', requestHandler);
    }
  }

  await browser.close();

  let report = '# Component Routes Validation Report\n\n';
  report += 'The resolved component id and title are published after Compose canvas composition. ';
  report += 'This test does not rely on DOM text nodes and is not an OCR/visual assertion.\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Route | Expected heading | Console errors | Network failures | Result |\n';
  report += '|---|---|---:|---:|---|\n';

  let failed = false;
  for (const result of results) {
    const status = result.success ? 'OK' : `FAILED: ${result.error}`;
    report += `| ${result.hash} | ${result.expectedText} | ${result.consoleErrors} | ${result.networkFailures} | ${status} |\n`;
    if (!result.success || result.consoleErrors > 0 || result.networkFailures > 0) failed = true;
  }

  fs.writeFileSync(path.join(outputDir, 'route-validation-report.md'), report);
  console.log(`Report generated at ${path.join(outputDir, 'route-validation-report.md')}`);

  if (failed) process.exit(1);
}

validate().catch(error => {
  console.error(error);
  process.exit(1);
});
