const { chromium } = require('playwright');
(async () => {
    const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
    const context = await browser.newContext({ viewport: { width: 390, height: 844 } });
    const page = await context.newPage();
    page.on('console', msg => {
        if (msg.type() === 'error') console.log('ERROR:', msg.text());
        else console.log('LOG:', msg.text());
    });
    await page.goto('http://localhost:8080/docs/#getting-started', { waitUntil: 'networkidle' });
    await page.waitForTimeout(10000);
    await page.screenshot({ path: 'test-docs-compact.png' });
    await browser.close();
})();





