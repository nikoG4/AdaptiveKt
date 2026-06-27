const { chromium } = require('playwright');
(async () => {
    const browser = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader'] });
    const context = await browser.newContext({ viewport: { width: 390, height: 844 } });
    const page = await context.newPage();
    page.on('console', msg => console.log('CONSOLE:', msg.text()));
    page.on('pageerror', err => {
        console.log('PAGE ERROR NAME:', err.name);
        console.log('PAGE ERROR MESSAGE:', err.message);
        console.log('PAGE ERROR STACK:', err.stack);
    });
    await page.goto('http://localhost:8080/docs/#getting-started', { waitUntil: 'networkidle' });
    await page.waitForTimeout(10000);
    await browser.close();
})();














