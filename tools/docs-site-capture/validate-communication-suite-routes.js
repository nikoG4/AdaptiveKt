const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080/examples/communication-suite').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/route-validation/communication-suite';

const routes = [
  '/', '#/chat', '#/chat/inbox', '#/chat/conversation/team-alpha', '#/chat/conversation/support-desk',
  '#/chat/search', '#/mail', '#/mail/inbox', '#/mail/thread/product-launch', '#/mail/thread/security-review',
  '#/mail/compose', '#/settings'
];

function canonicalHash(route) {
  if (route === '/' || route === '#/chat') return '#/chat/inbox';
  if (route === '#/mail') return '#/mail/inbox';
  return route;
}

async function validate() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const context = await browser.newContext();
  const page = await context.newPage();
  const results = [];

  for (const hash of routes) {
    const expectedHash = canonicalHash(hash);
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

    const url = `${baseUrl}${hash}`;
    console.log(`Validating ${url}...`);

    try {
      const response = await page.goto(url, { waitUntil: 'networkidle' });
      if (response !== null && !response.ok()) {
        throw new Error(`HTTP status ${response.status()}`);
      }

      await page.waitForSelector('canvas', { timeout: 30000 });
      await page.waitForTimeout(1000);

      const currentHash = await page.evaluate(() => window.location.hash);
      if (currentHash !== expectedHash) {
        throw new Error(`URL mismatch. Expected hash ${expectedHash} but got ${currentHash}`);
      }

      const bridgeRoute = await page.evaluate(() => window.__adaptiveKtCommunicationRoute);
      if (bridgeRoute !== undefined && bridgeRoute !== expectedHash) {
        throw new Error(`Bridge mismatch. Expected hash ${expectedHash} but got ${bridgeRoute}`);
      }

      const hasOverflow = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
      if (hasOverflow) throw new Error('Horizontal overflow detected');

      const canvasBox = await page.locator('canvas').boundingBox();
      if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
        throw new Error('Compose canvas is missing or too small');
      }

      if (consoleMessages.length > 0) {
        throw new Error(`Console errors detected: ${consoleMessages[0]}`);
      }

      results.push({ hash, expectedHash, success: true, consoleErrors: 0, networkFailures: requestFailures.length });
    } catch (error) {
      console.error(`Failed: ${error.message}`);
      results.push({ hash, expectedHash, success: false, error: error.message, consoleErrors: consoleMessages.length, networkFailures: requestFailures.length });
    } finally {
      page.off('console', consoleHandler);
      page.off('requestfailed', requestHandler);
    }
  }

  await browser.close();

  let report = '# Communication Suite Routes Validation Report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Requested route | Canonical route | Console errors | Network failures | Result |\n';
  report += '|---|---|---:|---:|---|\n';

  let failed = false;
  for (const result of results) {
    const status = result.success ? 'OK' : `FAILED: ${result.error}`;
    report += `| ${result.hash} | ${result.expectedHash} | ${result.consoleErrors} | ${result.networkFailures} | ${status} |\n`;
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
