const crypto = require('crypto');
const fs = require('fs');
const path = require('path');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = path.resolve(process.argv[3] || 'artifacts/screenshots/component-gallery/beta-ui-kit');

const viewports = [
  { id: 'compact', width: 390, height: 844 },
  { id: 'desktop', width: 1280, height: 800 },
];
const themes = ['light', 'dark'];

const scenes = [
  { id: 'cards', screen: 'components-cards', styles: ['modern', 'classic'] },
  { id: 'dashboard', screen: 'dashboard', styles: ['modern', 'classic'] },
  { id: 'dialog', screen: 'components-dialogs-open', styles: ['modern', 'classic'] },
];

/**
 * CI can validate the complete screenshot matrix without installing a browser.
 * Exactly 24 scenes: 3 routes x 2 visual styles x 2 themes x 2 viewports.
 */
function verifyCaptureMatrix() {
  const ids = scenes.flatMap(scene =>
    scene.styles.flatMap(style =>
      themes.flatMap(theme =>
        viewports.map(viewport => [scene.id, style, theme, viewport.id].join('-')))));
  const requiredScenes = ['cards', 'dashboard', 'dialog'];
  if (ids.length !== 24 || new Set(ids).size !== 24 ||
      scenes.length !== requiredScenes.length ||
      requiredScenes.some(name => {
        const scene = scenes.find(item => item.id === name);
        return !scene || scene.styles.length !== 2 ||
          !scene.styles.includes('modern') || !scene.styles.includes('classic');
      })) {
    throw new Error('Invalid beta UI Kit screenshot matrix: ' + ids.length +
      ' captures, ' + new Set(ids).size + ' unique IDs');
  }
  return ids;
}

async function capture() {
  const expectedIds = verifyCaptureMatrix();
  fs.mkdirSync(outputDir, { recursive: true });
  const { chromium } = require('playwright');
  const browser = await chromium.launch();
  const results = [];

  try {
    for (const scene of scenes) {
      for (const style of scene.styles) {
        for (const theme of themes) {
          for (const viewport of viewports) {
            const context = await browser.newContext({
              viewport: { width: viewport.width, height: viewport.height },
              colorScheme: theme,
              deviceScaleFactor: 1,
            });
            const page = await context.newPage();
            const runtimeErrors = [];
            const requestFailures = [];
            page.on('pageerror', error => runtimeErrors.push(error.message));
            page.on('requestfailed', request =>
              requestFailures.push(request.url() + ': ' + request.failure()?.errorText));
            const id = [scene.id, style, theme, viewport.id].join('-');
            const url = baseUrl + '/demo/app/?screen=' + scene.screen +
              '&theme=' + theme + '&style=' + style;
            const filename = id + '.png';
            const imagePath = path.join(outputDir, filename);

            try {
              console.log('Capture:', id, url);
              const response = await page.goto(url, { waitUntil: 'networkidle', timeout: 45000 });
              if (!response || !response.ok()) throw new Error('Unexpected HTTP status');
              await page.locator('#webApp canvas').first().waitFor({ timeout: 45000 });
              await page.waitForTimeout(1400); // Includes animation settling and Compose first frame.
              const canvas = await page.locator('#webApp canvas').first().boundingBox();
              if (!canvas || canvas.width < 100 || canvas.height < 100) {
                throw new Error('Missing or undersized Compose canvas');
              }

              await page.screenshot({ path: imagePath });
              let size = fs.statSync(imagePath).size;
              if (size < 12000) {
                await page.waitForTimeout(1500);
                await page.screenshot({ path: imagePath });
                size = fs.statSync(imagePath).size;
              }
              if (size < 12000) throw new Error('Screenshot appears blank (' + size + ' bytes)');
              if (runtimeErrors.length) throw new Error('Browser runtime error: ' + runtimeErrors[0]);
              // Validate actual PNG dimensions rather than trusting file extension/size.
              const png = fs.readFileSync(imagePath);
              const pngSignature = Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]);
              if (!png.subarray(0, 8).equals(pngSignature)) {
                throw new Error('Invalid PNG signature');
              }
              const pngWidth = png.readUInt32BE(16);
              const pngHeight = png.readUInt32BE(20);
              if (pngWidth !== viewport.width || pngHeight !== viewport.height) {
                throw new Error('Unexpected PNG dimensions: ' + pngWidth + 'x' + pngHeight);
              }
              const sha256 = crypto.createHash('sha256').update(png).digest('hex');

              results.push({
                id, success: true, file: filename, bytes: size, screen: scene.screen,
                style, theme, viewport: viewport.id, sha256, width: pngWidth, height: pngHeight, requestFailures,
              });
            } catch (error) {
              console.error('Capture failed:', id, error.message);
              results.push({
                id, success: false, error: error.message, screen: scene.screen,
                style, theme, viewport: viewport.id, runtimeErrors, requestFailures,
              });
            } finally {
              await context.close();
            }
          }
        }
      }
    }
  } finally {
    await browser.close();
  }

  const manifest = {
    capturedAt: new Date().toISOString(),
    baseUrl,
    expectedTotal: expectedIds.length,
    total: results.length,
    passed: results.filter(x => x.success).length,
    failed: results.filter(x => !x.success).length,
    results,
  };
  // Matching PNG bytes across different routes/styles can indicate that a URL
  // parameter was ignored. Record them for human QA without assuming a false fail.
  const hashGroups = new Map();
  for (const result of results.filter(item => item.success)) {
    const ids = hashGroups.get(result.sha256) || [];
    ids.push(result.id);
    hashGroups.set(result.sha256, ids);
  }
  manifest.identicalImages = [...hashGroups.values()].filter(ids => ids.length > 1);
  fs.writeFileSync(path.join(outputDir, 'manifest.json'), JSON.stringify(manifest, null, 2));
  const markdown = [
    '# Modern/Classic real Compose screenshot evidence',
    '',
    'Generated: ' + manifest.capturedAt,
    '',
    '| Scene | Theme | Style | Viewport | Result |',
    '| --- | --- | --- | --- | --- |',
    ...results.map(x => '| ' + x.screen + ' | ' + x.theme + ' | ' + x.style +
      ' | ' + x.viewport + ' | ' +
      (x.success ? '[image](' + x.file + ')' : 'FAILED: ' + x.error.replace(/\|/g, '/')) + ' |'),
    '',
    'Screenshots require human visual review before a beta release.',
  ].join('\n');
  fs.writeFileSync(path.join(outputDir, 'README.md'), markdown);
  console.log('Beta UI Kit:', manifest.passed + '/' + manifest.total + ' captures passed');
  if (manifest.identicalImages.length) console.warn('Visually identical screenshot files:', manifest.identicalImages);
  if (manifest.failed || manifest.total !== expectedIds.length) process.exitCode = 1;
}

if (process.argv.includes('--verify-matrix')) {
  try {
    const ids = verifyCaptureMatrix();
    console.log('Beta UI Kit matrix valid:', ids.length, 'unique scenes');
  } catch (error) {
    console.error(error);
    process.exitCode = 1;
  }
} else {
  capture().catch(error => {
    console.error(error);
    process.exitCode = 1;
  });
}
