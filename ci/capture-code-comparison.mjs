import { chromium } from 'playwright';
import fs from 'fs';
import path from 'path';

const PORT = 8080;
const URL = `http://localhost:${PORT}/#code-comparison`;
const OUTPUT_DIR = path.join(process.cwd(), 'docs-site/build/screenshots/code-comparison');

async function ensureDir(dir) {
    if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
    }
}

async function capture() {
    console.log(`Starting code comparison capture at ${URL}`);
    await ensureDir(OUTPUT_DIR);

    const browser = await chromium.launch({ headless: true });
    
    // Desktop captures (1200x800)
    const desktopContext = await browser.newContext({
        viewport: { width: 1200, height: 800 }
    });
    const desktopPage = await desktopContext.newPage();
    
    await desktopPage.goto(URL, { waitUntil: 'networkidle' });
    
    // Wait for the comparison section to be visible
    await desktopPage.waitForSelector('#code-comparison', { state: 'visible' });
    
    // Click AdaptiveKt tab if not already selected
    const adaptiveTab = desktopPage.locator('text=AdaptiveKt').first();
    if (await adaptiveTab.isVisible()) {
        await adaptiveTab.click();
        await desktopPage.waitForTimeout(500); // wait for animation
    }
    await desktopPage.screenshot({ path: path.join(OUTPUT_DIR, 'adaptivekt-desktop.webp'), type: 'webp' });
    
    // Click Plain Compose tab
    const plainComposeTab = desktopPage.locator('text=Plain Compose').first();
    if (await plainComposeTab.isVisible()) {
        await plainComposeTab.click();
        await desktopPage.waitForTimeout(500); // wait for animation
    }
    await desktopPage.screenshot({ path: path.join(OUTPUT_DIR, 'plain-compose-desktop.webp'), type: 'webp' });
    
    await desktopContext.close();

    // Mobile captures (390x844)
    const mobileContext = await browser.newContext({
        viewport: { width: 390, height: 844 },
        isMobile: true,
        hasTouch: true
    });
    const mobilePage = await mobileContext.newPage();
    
    await mobilePage.goto(URL, { waitUntil: 'networkidle' });
    
    // Wait for the comparison section to be visible
    await mobilePage.waitForSelector('#code-comparison', { state: 'visible' });
    
    // In mobile, it might be a dropdown or tabs. Assuming tabs for now.
    const mobileAdaptiveTab = mobilePage.locator('text=AdaptiveKt').first();
    if (await mobileAdaptiveTab.isVisible()) {
        await mobileAdaptiveTab.click();
        await mobilePage.waitForTimeout(500); // wait for animation
    }
    await mobilePage.screenshot({ path: path.join(OUTPUT_DIR, 'adaptivekt-mobile.webp'), type: 'webp' });
    
    const mobilePlainComposeTab = mobilePage.locator('text=Plain Compose').first();
    if (await mobilePlainComposeTab.isVisible()) {
        await mobilePlainComposeTab.click();
        await mobilePage.waitForTimeout(500); // wait for animation
    }
    await mobilePage.screenshot({ path: path.join(OUTPUT_DIR, 'plain-compose-mobile.webp'), type: 'webp' });
    
    await mobileContext.close();
    await browser.close();

    // Generate Manifest
    const manifest = {
        generatedAt: new Date().toISOString(),
        screenshots: {
            desktop: {
                adaptiveKt: 'adaptivekt-desktop.webp',
                plainCompose: 'plain-compose-desktop.webp'
            },
            mobile: {
                adaptiveKt: 'adaptivekt-mobile.webp',
                plainCompose: 'plain-compose-mobile.webp'
            }
        },
        metadata: {
            viewportDesktop: '1200x800',
            viewportMobile: '390x844'
        }
    };

    fs.writeFileSync(
        path.join(OUTPUT_DIR, 'home-code-comparison-manifest.json'),
        JSON.stringify(manifest, null, 2)
    );
    console.log(`Manifest and screenshots saved to ${OUTPUT_DIR}`);
}

capture().catch(err => {
    console.error('Capture failed', err);
    process.exit(1);
});
