const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const baseUrl = process.argv[2] || 'http://localhost:8080';
const outputDir = process.argv[3] || 'build/web-visual-captures';

const screens = [
    'dashboard',
    'employees',
    'products',
    'invoices',
    'invoices-empty',
    'invoices-loading',
    'invoices-error',
    'settings',
    'components',
    'components-buttons',
    'components-badges',
    'components-avatars',
    'components-cards',
    'components-dropdowns',
    'components-fields',
    'components-selects',
    'components-selects-open'
];

const viewports = [
    { name: 'compact', width: 420, height: 900 },
    { name: 'medium', width: 720, height: 900 },
    { name: 'large', width: 1440, height: 900 }
];

async function capture() {
    if (!fs.existsSync(outputDir)) {
        fs.mkdirSync(outputDir, { recursive: true });
    }

    const browser = await chromium.launch();
    const results = [];

    for (const viewport of viewports) {
        const context = await browser.newContext({
            viewport: { width: viewport.width, height: viewport.height }
        });

        for (const screen of screens) {
            const page = await context.newPage();
            const url = `${baseUrl}/?screen=${screen}`;
            const filename = `${screen}-${viewport.name}-${viewport.width}x${viewport.height}.png`;
            const filePath = path.join(outputDir, filename);

            console.log(`Capturing ${screen} at ${viewport.name} (${viewport.width}x${viewport.height})...`);

            try {
                await page.goto(url, { waitUntil: 'networkidle' });
                // Wait for canvas to be present
                await page.waitForSelector('#webApp canvas', { timeout: 30000 });
                // Small stabilization delay
                await page.waitForTimeout(2000);
                await page.screenshot({ path: filePath });
                results.push({ screen, viewport: viewport.name, size: `${viewport.width}x${viewport.height}`, file: filename, success: true });
            } catch (e) {
                console.error(`Failed to capture ${screen} at ${viewport.name}: ${e.message}`);
                results.push({ screen, viewport: viewport.name, size: `${viewport.width}x${viewport.height}`, success: false, error: e.message });
            }
            await page.close();
        }
        await context.close();
    }

    await browser.close();
    generateReport(results);
}

function generateReport(results) {
    let report = '# Web Visual Capture Report\n\n';
    report += `Generated on: ${new Date().toISOString()}\n\n`;
    report += '| Screen | Viewport | Size | Result |\n';
    report += '| --- | --- | --- | --- |\n';

    results.forEach(res => {
        const status = res.success ? 'OK' : `FAILED ${res.error}`;
        const link = res.success ? `[${res.file}](${res.file})` : '-';
        report += `| ${res.screen} | ${res.viewport} | ${res.size} | ${status} ${link} |\n`;
    });

    report += '\n## Gallery\n\n';
    screens.forEach(screen => {
        report += `### ${screen}\n\n`;
        report += '<div style="display: flex; flex-wrap: wrap; gap: 10px;">\n';
        viewports.forEach(vp => {
            const res = results.find(r => r.screen === screen && r.viewport === vp.name);
            if (res && res.success) {
                report += `  <div style="flex: 1; min-width: 300px;">\n`;
                report += `    <p>${vp.name} (${vp.size})</p>\n`;
                report += `    <img src="${res.file}" width="100%" style="border: 1px solid #ccc;" />\n`;
                report += `  </div>\n`;
            }
        });
        report += '</div>\n\n';
    });

    fs.writeFileSync(path.join(outputDir, 'web-visual-capture-report.md'), report);
    console.log(`Report generated at ${path.join(outputDir, 'web-visual-capture-report.md')}`);

    if (results.some(res => !res.success)) {
        process.exit(1);
    }
}

capture().catch(err => {
    console.error(err);
    process.exit(1);
});
