const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/route-validation';

const benignConsoleErrorPatterns = [
  { pattern: 'failed to attach a stencil buffer', reason: 'Chromium/SwiftShader stencil buffer diagnostic in headless CI (Rendering will be skipped)' },
];

function isBenignConsoleError(text) {
  return benignConsoleErrorPatterns.find(bp => text.includes(bp.pattern));
}

const routes = [
  '#adaptive-theme', '#adaptive-button', '#adaptive-icon-button', '#adaptive-badge',
  '#adaptive-chip', '#adaptive-avatar', '#adaptive-thumbnail', '#adaptive-card',
  '#adaptive-selectionarea', '#adaptive-carousel', '#adaptive-text-field',
  '#adaptive-search-field', '#adaptive-select', '#adaptive-multi-select',
  '#adaptive-tabs', '#adaptive-dialog', '#adaptive-accordion-dialog', '#adaptive-navigation-scaffold',
  '#adaptive-data-view', '#adaptive-form-layout', '#adaptive-empty-state',
  '#adaptive-loading-state', '#adaptive-error-state'
];

async function validate() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const context = await browser.newContext();
  const page = await context.newPage();
  const results = [];

  for (const hash of routes) {
    const consoleErrors = [];
    const ignoredConsoleErrors = [];
    const requestFailures = [];

    // Attach listeners
    const consoleHandler = message => {
      if (message.type() === 'error') {
        const text = message.text();
        const benign = isBenignConsoleError(text);
        if (benign) {
          ignoredConsoleErrors.push({ text, reason: benign.reason });
        } else {
          consoleErrors.push(text);
        }
      }
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
      await page.waitForTimeout(1000); // Wait for compose rendering

      const canvasBox = await page.locator('#webApp canvas').boundingBox();
      if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
        throw new Error('Compose canvas is missing or too small');
      }

      // Check if hash routed correctly. In Compose Web, we can't easily query semantic DOM nodes unless they have testTags,
      // but if the page didn't throw an exception, it's mostly working.
      if (!page.url().includes(hash)) {
        throw new Error(`URL did not navigate to expected hash ${hash}`);
      }
      
      if (consoleErrors.length > 0) {
        throw new Error(`Console errors detected: ${consoleErrors[0]}`);
      }

      results.push({ hash, success: true, consoleErrors: 0, ignoredConsoleErrors: ignoredConsoleErrors.length, networkFailures: requestFailures.length });
    } catch (error) {
      console.error(`Failed: ${error.message}`);
      results.push({ hash, success: false, error: error.message, consoleErrors: consoleErrors.length, ignoredConsoleErrors: ignoredConsoleErrors.length, networkFailures: requestFailures.length });
    } finally {
      page.off('console', consoleHandler);
      page.off('requestfailed', requestHandler);
    }
  }

  await browser.close();
  
  let report = '# Component Routes Validation Report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Route | Console errors | Ignored benign | Network failures | Result |\n';
  report += '|---|---:|---:|---:|---|\n';

  let failed = false;
  for (const result of results) {
    const status = result.success ? 'OK' : `FAILED: ${result.error}`;
    report += `| ${result.hash} | ${result.consoleErrors} | ${result.ignoredConsoleErrors} | ${result.networkFailures} | ${status} |\n`;
    if (!result.success || result.consoleErrors > 0 || result.networkFailures > 0) failed = true;
  }

  report += '\n## Ignored benign console error patterns\n\n';
  for (const bp of benignConsoleErrorPatterns) {
    report += `- \`${bp.pattern}\` — ${bp.reason}\n`;
  }

  fs.writeFileSync(path.join(outputDir, 'route-validation-report.md'), report);
  console.log(`Report generated at ${path.join(outputDir, 'route-validation-report.md')}`);

  if (failed) {
    process.exit(1);
  }
}

validate().catch(error => {
  console.error(error);
  process.exit(1);
});
