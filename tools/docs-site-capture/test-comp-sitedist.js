const { chromium } = require('playwright');
(async () => {
    const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
    const context = await browser.newContext({ viewport: { width: 1280, height: 800 } });
    const page = await context.newPage();
    page.on('console', msg => {
        if (msg.type() === 'error') console.log('ERROR:', msg.text());
        else console.log('LOG:', msg.text());
    });
    await page.goto('http://localhost:8080/components/#adaptive-theme', { waitUntil: 'networkidle' });
    await page.waitForTimeout(5000);
    await page.screenshot({ path: 'test-components-sitedist.png' });
    await browser.close();
})();





