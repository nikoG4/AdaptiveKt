const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8090').replace(/\/$/, '');
const outputDir = process.argv[3] || 'build/docs-site-web-captures';

const pages = [
  { name: 'home', path: '/' },
  { name: 'components', path: '/components/' },
  { name: 'docs', path: '/docs/' },
  { name: 'demo', path: '/demo/' }
];

const darkPages = [
  { name: 'home-dark', path: '/?theme=dark' },
  { name: 'components-dark', path: '/components/?theme=dark' }
];

const viewports = [
  { name: 'compact', width: 420, height: 900 },
  { name: 'medium', width: 720, height: 900 },
  { name: 'large', width: 1440, height: 900 }
];

async function captureDocsSite() {
  fs.mkdirSync(outputDir, { recursive: true });

  const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
  const results = [];

  for (const viewport of viewports) {
    const context = await browser.newContext({
      viewport: { width: viewport.width, height: viewport.height }
    });

    const pagesForViewport = [
      ...pages,
      ...(viewport.name === 'compact' || viewport.name === 'large' ? darkPages : [])
    ];

    for (const sitePage of pagesForViewport) {
      const page = await context.newPage();
      const url = `${baseUrl}${sitePage.path}`;
      const filename = `${sitePage.name}-${viewport.name}-${viewport.width}x${viewport.height}.png`;
      const filePath = path.join(outputDir, filename);

      console.log(`Capturing ${url} at ${viewport.name} (${viewport.width}x${viewport.height})...`);

      try {
        await page.goto(url, { waitUntil: 'networkidle' });
        await page.waitForSelector('#webApp canvas', { timeout: 30000 });
        await page.waitForTimeout(1500);
        const canvasBox = await page.locator('#webApp canvas').boundingBox();
        if (!canvasBox || canvasBox.width < 50 || canvasBox.height < 50) {
          throw new Error('Compose canvas is missing or too small');
        }
        await page.screenshot({ path: filePath, fullPage: true });
        results.push({ page: sitePage.name, viewport: viewport.name, size: `${viewport.width}x${viewport.height}`, file: filename, success: true });
      } catch (error) {
        console.error(`Failed to capture ${sitePage.name} at ${viewport.name}: ${error.message}`);
        results.push({ page: sitePage.name, viewport: viewport.name, size: `${viewport.width}x${viewport.height}`, success: false, error: error.message });
      } finally {
        await page.close();
      }
    }

    await context.close();
  }

  const context = await browser.newContext({ viewport: { width: 1440, height: 900 } });
  const page = await context.newPage();
  const smokeUrl = `${baseUrl}/demo/app/`;
  const smokeFile = 'admin-demo-link-smoke-large-1440x900.png';

  console.log(`Checking admin demo target ${smokeUrl}...`);
  try {
    const response = await page.goto(smokeUrl, { waitUntil: 'networkidle' });
    if (!response || !response.ok()) {
      throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
    }
    await page.waitForSelector('#webApp canvas', { timeout: 30000 });
    await page.waitForTimeout(1500);
    await page.screenshot({ path: path.join(outputDir, smokeFile), fullPage: true });
    results.push({ page: 'admin-demo-link-smoke', viewport: 'large', size: '1440x900', file: smokeFile, success: true });
  } catch (error) {
    console.error(`Admin demo target failed: ${error.message}`);
    results.push({ page: 'admin-demo-link-smoke', viewport: 'large', size: '1440x900', success: false, error: error.message });
  } finally {
    await page.close();
    await context.close();
  }

  const darkSmokeContext = await browser.newContext({ viewport: { width: 1440, height: 900 } });
  const darkSmokePage = await darkSmokeContext.newPage();
  const darkSmokeUrl = `${baseUrl}/demo/app/?theme=dark`;
  const darkSmokeFile = 'admin-demo-dark-link-smoke-large-1440x900.png';

  console.log(`Checking dark admin demo target ${darkSmokeUrl}...`);
  try {
    const response = await darkSmokePage.goto(darkSmokeUrl, { waitUntil: 'networkidle' });
    if (!response || !response.ok()) {
      throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
    }
    await darkSmokePage.waitForSelector('#webApp canvas', { timeout: 30000 });
    await darkSmokePage.waitForTimeout(1500);
    await darkSmokePage.screenshot({ path: path.join(outputDir, darkSmokeFile), fullPage: true });
    results.push({ page: 'admin-demo-dark-link-smoke', viewport: 'large', size: '1440x900', file: darkSmokeFile, success: true });
  } catch (error) {
    console.error(`Dark admin demo target failed: ${error.message}`);
    results.push({ page: 'admin-demo-dark-link-smoke', viewport: 'large', size: '1440x900', success: false, error: error.message });
  } finally {
    await darkSmokePage.close();
    await darkSmokeContext.close();
    await browser.close();
  }

  generateReport(results);

  if (results.some(result => !result.success)) {
    process.exit(1);
  }
}

function generateReport(results) {
  let report = '# Docs Site Web Capture Report\n\n';
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += '| Page | Viewport | Size | Result |\n';
  report += '| --- | --- | --- | --- |\n';

  for (const result of results) {
    const status = result.success ? 'OK' : `FAILED ${result.error}`;
    const link = result.success ? `[${result.file}](${result.file})` : '-';
    report += `| ${result.page} | ${result.viewport} | ${result.size} | ${status} ${link} |\n`;
  }

  report += '\n## Gallery\n\n';
  for (const result of results.filter(item => item.success)) {
    report += `### ${result.page} - ${result.viewport}\n\n`;
    report += `<img src="${result.file}" width="100%" style="border: 1px solid #ccc;" />\n\n`;
  }

  const reportPath = path.join(outputDir, 'docs-site-web-capture-report.md');
  fs.writeFileSync(reportPath, report);
  console.log(`Report generated at ${reportPath}`);
}

captureDocsSite().catch(error => {
  console.error(error);
  process.exit(1);
});





