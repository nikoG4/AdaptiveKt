const { chromium } = require('playwright');
const path = require('path');
const fs = require('fs');

(async () => {
    const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
    const context = await browser.newContext({ viewport: { width: 1280, height: 800 } });
    const page = await context.newPage();
    const logPath = path.join(__dirname, 'output-errors.log');
    
    page.on('console', msg => {
        fs.appendFileSync(logPath, 'CONSOLE: ' + msg.text() + '\n');
    });
    page.on('pageerror', err => {
        fs.appendFileSync(logPath, 'PAGE ERROR STACK: ' + err.stack + '\n');
    });
    page.on('requestfailed', request => {
        fs.appendFileSync(logPath, 'REQ FAILED: ' + request.url() + ' ' + request.failure().errorText + '\n');
    });
    page.on('response', response => {
        fs.appendFileSync(logPath, 'REQ OK: ' + response.url() + ' ' + response.status() + '\n');
    });

    await page.goto('http://localhost:8080/components/#adaptive-theme', { waitUntil: 'networkidle' });
    await page.waitForTimeout(5000);
    await browser.close();
})();








