const { chromium } = require('playwright');
const path = require('path');
(async () => {
    const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
    const context = await browser.newContext({ viewport: { width: 1280, height: 800 } });
    const page = await context.newPage();
    const fs = require('fs');
    const logPath = path.join(__dirname, 'output-sitedist.log');
    page.on('console', msg => {
        const text = msg.type() + ': ' + msg.text() + '\n';
        fs.appendFileSync(logPath, text);
    });
    await page.goto('http://localhost:8080/components/#adaptive-theme', { waitUntil: 'networkidle' });
    await page.waitForTimeout(5000);
    await browser.close();
})();





