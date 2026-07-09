const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const crypto = require('crypto');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/home-code-comparison';

let baseUrlHost = 'localhost';
let baseUrlOrigin = baseUrl;
try {
  const parsed = new URL(baseUrl);
  baseUrlHost = parsed.host;
  baseUrlOrigin = parsed.origin;
} catch {
  // keep defaults
}

const viewports = [
  { name: '390x844', width: 390, height: 844 },
  { name: '600x900', width: 600, height: 900 },
  { name: '768x1024', width: 768, height: 1024 },
  { name: '839x900', width: 839, height: 900 },
  { name: '840x900', width: 840, height: 900 },
  { name: '959x900', width: 959, height: 900 },
  { name: '960x900', width: 960, height: 900 },
  { name: '1024x900', width: 1024, height: 900 },
  { name: '1440x1000', width: 1440, height: 1000 },
  { name: '1920x1080', width: 1920, height: 1080 }
];

const themes = [
  { name: 'light', query: '' },
  { name: 'dark', query: '?theme=dark' }
];

const ignoredRequests = [
    { url: "https://github.com/nikoG4/AdaptiveKt/hovercards/citation/sidebar_partial?tree_name=main", reason: "optional external github hovercard" },
    { url: "https://github.com/nikoG4/AdaptiveKt", reason: "optional external github repo resource" },
    { url: "favicon.ico", reason: "optional external favicon" },
    { url: "github.com", reason: "optional external github resource" }
];

function isOwnResource(url) {
    try {
        const u = new URL(url);
        return u.host === baseUrlHost || u.origin === baseUrlOrigin;
    } catch {
        return false;
    }
}

function classifyResourceError(url) {
    if (isOwnResource(url)) return { kind: 'own', ignored: false, reason: null };
    const match = ignoredRequests.find(ir => url.includes(ir.url));
    if (match) return { kind: 'optional-external', ignored: true, reason: match.reason };
    return { kind: 'unknown-external', ignored: false, reason: null };
}

function countMeaningfulLines(code) {
  let inBlock = false;
  let count = 0;
  for (let line of code.split('\n')) {
      const trimmed = line.trim();
      if (inBlock) {
          if (trimmed.includes('*/')) {
              inBlock = false;
              const after = trimmed.substring(trimmed.lastIndexOf('*/') + 2).trim();
              if (after.length > 0 && !after.startsWith('/*') && !after.startsWith('//')) { count++; }
          }
      } else {
          if (trimmed.startsWith('/*')) {
              if (!trimmed.includes('*/')) { inBlock = true; }
              else {
                  const after = trimmed.substring(trimmed.lastIndexOf('*/') + 2).trim();
                  if (after.length > 0 && !after.startsWith('/*') && !after.startsWith('//')) { count++; }
              }
          } else if (trimmed.length > 0 && !trimmed.startsWith('import ') && !trimmed.startsWith('package ') && !trimmed.startsWith('//')) {
              count++;
          }
      }
  }
  return count;
}

function getKotlinMetrics() {
  const snippetsFile = path.resolve(__dirname, '../../docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/HomeCodeComparisonSnippets.kt');
  const content = fs.readFileSync(snippetsFile, 'utf8');
  const adaptiveMatch = content.match(/internal val AdaptiveDataViewComparisonCode = """([\s\S]*?)"""/);
  const plainMatch = content.match(/internal val PlainComposeDataViewComparisonCode = """([\s\S]*?)"""/);

  if (!adaptiveMatch || !plainMatch) throw new Error("Could not parse Kotlin snippets");

  const adaptiveLines = countMeaningfulLines(adaptiveMatch[1]);
  const composeLines = countMeaningfulLines(plainMatch[1]);
  const savedLines = Math.max(0, composeLines - adaptiveLines);
  const reductionPercent = composeLines > 0 ? Math.floor((savedLines * 100) / composeLines) : 0;

  return { adaptiveLines, composeLines, savedLines, reductionPercent };
}

function getFileHash(filePath) {
    if (!fs.existsSync(filePath)) return null;
    const fileBuffer = fs.readFileSync(filePath);
    const hashSum = crypto.createHash('sha256');
    hashSum.update(fileBuffer);
    return hashSum.digest('hex');
}

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch({
    args: ['--use-gl=angle', '--use-angle=swiftshader']
  });
  const results = [];
  const consoleErrorDetails = [];
  const pageErrorDetails = [];
  const failedRequestDetails = [];
  const httpErrorDetails = [];
  const ignoredHttpErrorDetails = [];
  const ignoredFailedRequestDetails = [];
  const ignoredConsoleResourceErrors = [];

  let globalConsoleErrors = 0;
  let globalPageErrors = 0;
  let globalFailedRequests = 0;
  let globalOverflowFailures = 0;
  let globalIgnoredConsoleErrors = 0;
  let globalHttpErrors = 0;
  let globalIgnoredHttpErrors = 0;
  let globalIgnoredFailedRequests = 0;

  const metrics = getKotlinMetrics();

  const benignConsoleErrorPatterns = [
    { pattern: 'WebGL', reason: 'Chromium WebGL GPU driver message' },
    { pattern: 'GL Driver Message', reason: 'Chromium GL driver diagnostic' },
    { pattern: 'GPU memory manager', reason: 'Chromium GPU memory manager diagnostic' },
    { pattern: 'swiftshader', reason: 'SwiftShader software renderer diagnostic' },
    { pattern: 'Failed to download hardware adapter', reason: 'Chromium hardware adapter probe in CI headless' },
    { pattern: 'ResizeObserver loop', reason: 'Benign ResizeObserver loop warning from Compose Wasm layout' },
    { pattern: 'ResizeObserver Loop', reason: 'Benign ResizeObserver loop warning from Compose Wasm layout' },
    { pattern: 'downloadable font', reason: 'Chromium font loading diagnostic in headless' },
    { pattern: 'Font', reason: 'Chromium font loading diagnostic in headless' },
    { pattern: 'gfx', reason: 'Chromium graphics backend diagnostic' },
    { pattern: 'GpuChannelHost', reason: 'Chromium GPU channel host diagnostic in headless' },
    { pattern: 'gl_initialize', reason: 'Chromium GL initialization diagnostic in headless' },
    { pattern: 'Failed to create GLES2', reason: 'Chromium GLES2 context creation failure in SwiftShader headless' },
    { pattern: 'vulkan', reason: 'Chromium Vulkan backend diagnostic in headless' },
    { pattern: 'Vulkan', reason: 'Chromium Vulkan backend diagnostic in headless' },
    { pattern: 'Dawn', reason: 'Chromium Dawn WebGPU backend diagnostic in headless' },
    { pattern: 'Skia', reason: 'Skia renderer diagnostic in headless' },
    { pattern: 'Out of process', reason: 'Chromium out-of-process rasterization diagnostic' },
  ];

  function isBenignConsoleError(text) {
    return benignConsoleErrorPatterns.find(bp => text.includes(bp.pattern));
  }

  for (const theme of themes) {
    for (const viewport of viewports) {
      const context = await browser.newContext({
        viewport: { width: viewport.width, height: viewport.height },
        colorScheme: theme.name
      });
      const page = await context.newPage();
      let consoleMessages = 0;
      let requestFailures = 0;
      let pageErrors = 0;
      let currentState = 'load';

      page.on('console', message => {
        const text = message.text();
        const type = message.type();
        if (type !== 'error') return;
        const benign = isBenignConsoleError(text);
        if (benign) {
          globalIgnoredConsoleErrors++;
          return;
        }
        const location = message.location() || {};
        const locUrl = location.url || '';
        const isFailedResource = /Failed to load resource/.test(text);
        if (isFailedResource && locUrl) {
          const cls = classifyResourceError(locUrl);
          if (cls.ignored) {
            globalIgnoredConsoleErrors++;
            ignoredConsoleResourceErrors.push({
              viewport: viewport.name,
              theme: theme.name,
              state: currentState,
              url: locUrl,
              text: text,
              reason: cls.reason
            });
            console.log(`[ignored console.error] (${viewport.name}/${theme.name}/${currentState}): ${text} @ ${locUrl} [${cls.reason}]`);
            return;
          }
        }
        consoleMessages++;
        consoleErrorDetails.push({
          viewport: viewport.name,
          theme: theme.name,
          state: currentState,
          type: type,
          text: text,
          url: locUrl || undefined,
          lineNumber: location.lineNumber,
          columnNumber: location.columnNumber
        });
        console.error(`[console.error] (${viewport.name}/${theme.name}/${currentState}): ${text}${locUrl ? ` @ ${locUrl}` : ''}`);
      });
      page.on('response', response => {
        const status = response.status();
        if (status < 400) return;
        const url = response.url();
        const request = response.request();
        const cls = classifyResourceError(url);
        const detail = {
          viewport: viewport.name,
          theme: theme.name,
          state: currentState,
          url: url,
          status: status,
          statusText: response.statusText(),
          resourceType: request.resourceType(),
          method: request.method()
        };
        if (cls.ignored) {
          globalIgnoredHttpErrors++;
          detail.reason = cls.reason;
          ignoredHttpErrorDetails.push(detail);
          console.log(`[ignored http ${status}] (${viewport.name}/${theme.name}/${currentState}) ${url} [${detail.method} ${detail.resourceType}] ${detail.statusText} [${cls.reason}]`);
        } else {
          globalHttpErrors++;
          httpErrorDetails.push(detail);
          console.error(`[http ${status}] (${viewport.name}/${theme.name}/${currentState}) ${url} [${detail.method} ${detail.resourceType}] ${detail.statusText}`);
        }
      });
      page.on('requestfailed', request => {
        const url = request.url();
        const errorText = request.failure()?.errorText || 'unknown';
        const cls = classifyResourceError(url);
        if (cls.ignored) {
          globalIgnoredFailedRequests++;
          ignoredFailedRequestDetails.push({
            viewport: viewport.name,
            theme: theme.name,
            state: currentState,
            url: url,
            error: errorText,
            reason: cls.reason
          });
          console.log(`[ignored failed request] (${viewport.name}/${theme.name}/${currentState}): ${url} - ${errorText} [${cls.reason}]`);
          return;
        }
        console.error(`Failed request: ${url} - ${errorText} [${request.resourceType()}] (${viewport.name}/${theme.name}/${currentState})`);
        requestFailures++;
        failedRequestDetails.push({
          viewport: viewport.name,
          theme: theme.name,
          state: currentState,
          url: url,
          error: errorText,
          resourceType: request.resourceType()
        });
      });
      page.on('pageerror', error => {
        pageErrors++;
        pageErrorDetails.push({
          viewport: viewport.name,
          theme: theme.name,
          state: currentState,
          message: error.message,
          stack: error.stack ? error.stack.split('\n').slice(0, 3).join('\n') : undefined
        });
        console.error(`[pageerror] (${viewport.name}/${theme.name}/${currentState}): ${error.message}`);
      });

      const url = `${baseUrl}/${theme.query}`;
      console.log(`Testing ${url} (${viewport.name}, ${theme.name})`);

      try {
        currentState = 'load';
        const response = await page.goto(url, { waitUntil: 'networkidle' });
        if (!response || !response.ok()) {
          throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
        }

        await page.waitForSelector('#webApp canvas', { timeout: 30000 });
        await page.waitForTimeout(3000); // Allow Compose to settle

        const overflow = await page.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
        if (overflow) {
            globalOverflowFailures++;
        }

        const sectionLoc = page.locator('text=Less boilerplate, more focus').first();
        if (await sectionLoc.isVisible({ timeout: 1000 })) {
            await sectionLoc.scrollIntoViewIfNeeded();
            await page.waitForTimeout(1000);
        } else {
            console.log("Semantic text locator failed. Wasm Canvas accessibility DOM is not exposed. Using keyboard fallback exclusively.");
        }

        // We must focus the canvas first for keyboard navigation to work
        await page.locator('#webApp canvas').click({ position: { x: 10, y: 10 } });
        await page.waitForTimeout(500);

        const isSideBySide = viewport.width >= 960;

        currentState = 'base';
        const baseFile = `${viewport.name}-${theme.name}-base.png`;
        const basePath = path.join(outputDir, baseFile);
        await page.screenshot({ path: basePath, fullPage: true });
        results.push({ viewport: viewport.name, theme: theme.name, state: 'base', file: baseFile });
        const baseHash = getFileHash(basePath);

        if (!isSideBySide) {
            // Tabbed layout
            currentState = 'plain-compose';
            for (let i = 0; i < 5; i++) {
                await page.keyboard.press('Tab');
                await page.waitForTimeout(200);
            }
            await page.keyboard.press('Enter'); // Select Plain Compose Tab
            await page.waitForTimeout(1000);

            const file = `${viewport.name}-${theme.name}-plain-compose.png`;
            const filePath = path.join(outputDir, file);
            await page.screenshot({ path: filePath, fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'plain-compose', file });
            const composeHash = getFileHash(filePath);

            if (composeHash === baseHash) {
                throw new Error("Interaction failed: Plain Compose panel screenshot identical to AdaptiveKt base screenshot.");
            }

            // Expand
            currentState = 'expanded';
            await page.keyboard.press('Tab');
            await page.waitForTimeout(200);
            await page.keyboard.press('Tab');
            await page.waitForTimeout(200);
            await page.keyboard.press('Enter');
            await page.waitForTimeout(1000);

            const fileExp = `${viewport.name}-${theme.name}-expanded.png`;
            const fileExpPath = path.join(outputDir, fileExp);
            await page.screenshot({ path: fileExpPath, fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'expanded', file: fileExp });
            const expHash = getFileHash(fileExpPath);

            if (expHash === composeHash) {
                throw new Error("Interaction failed: Expanded screenshot identical to collapsed screenshot.");
            }

            // Methodology
            // Let's reset page and use methodology toggle
            currentState = 'methodology';
            await page.goto(url, { waitUntil: 'networkidle' });
            await page.waitForSelector('#webApp canvas', { timeout: 30000 });
            await page.waitForTimeout(3000);
            await page.locator('#webApp canvas').click({ position: { x: 10, y: 10 } });
            for (let i = 0; i < 6; i++) {
                await page.keyboard.press('Tab');
                await page.waitForTimeout(200);
            }
            await page.keyboard.press('Enter'); // Methodology Toggle
            await page.waitForTimeout(1000);

            const fileMeth = `${viewport.name}-${theme.name}-methodology.png`;
            const methPath = path.join(outputDir, fileMeth);
            await page.screenshot({ path: methPath, fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'methodology', file: fileMeth });
            const methHash = getFileHash(methPath);
            if (methHash === baseHash) {
                throw new Error("Interaction failed: Methodology expanded screenshot identical to base.");
            }

        } else {
            // SideBySide layout
            currentState = 'expanded-adaptive';
            for (let i = 0; i < 5; i++) {
                await page.keyboard.press('Tab');
                await page.waitForTimeout(200);
            }
            await page.keyboard.press('Enter'); // Expand AdaptiveKt
            await page.waitForTimeout(1000);

            const fileExpAd = `${viewport.name}-${theme.name}-expanded-adaptive.png`;
            const adPath = path.join(outputDir, fileExpAd);
            await page.screenshot({ path: adPath, fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'expanded-adaptive', file: fileExpAd });
            const adHash = getFileHash(adPath);
            if (adHash === baseHash) {
                throw new Error("Interaction failed: Expanded AdaptiveKt screenshot identical to base.");
            }

            currentState = 'expanded-compose';
            await page.keyboard.press('Tab');
            await page.waitForTimeout(200);
            await page.keyboard.press('Enter'); // Expand Plain Compose
            await page.waitForTimeout(1000);

            const fileExpCo = `${viewport.name}-${theme.name}-expanded-compose.png`;
            const coPath = path.join(outputDir, fileExpCo);
            await page.screenshot({ path: coPath, fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'expanded-compose', file: fileExpCo });
            const coHash = getFileHash(coPath);
            if (coHash === adHash) {
                throw new Error("Interaction failed: Expanded Plain Compose screenshot identical to Expanded AdaptiveKt.");
            }
        }

        globalConsoleErrors += consoleMessages;
        globalPageErrors += pageErrors;
        globalFailedRequests += requestFailures;

      } catch (error) {
        console.error(`Failed: ${error.message}`);
        globalPageErrors++;
      } finally {
        await page.close();
        await context.close();
      }
    }
  }

  await browser.close();

  try {
    console.log("Generating contact sheet...");
    execSync(`magick montage -geometry 400x+10+10 "${outputDir}/*.png" "${outputDir}/contact-sheet.png"`, { stdio: 'ignore' });
  } catch (e) {
    console.log("ImageMagick montage not available or failed. Skipping contact sheet.");
  }

  const manifest = {
    gitSha: getGitSha(),
    generatedAt: new Date().toISOString(),
    adaptiveLines: metrics.adaptiveLines,
    composeLines: metrics.composeLines,
    savedLines: metrics.savedLines,
    reductionPercent: metrics.reductionPercent,
    dataBreakpointDp: 840,
    sectionLayoutBreakpointDp: 960,
    screenshots: results,
    consoleErrors: globalConsoleErrors,
    consoleErrorDetails: consoleErrorDetails,
    ignoredConsoleErrors: globalIgnoredConsoleErrors,
    ignoredConsoleResourceErrors: ignoredConsoleResourceErrors,
    benignConsoleErrorPatterns: benignConsoleErrorPatterns.map(bp => ({ pattern: bp.pattern, reason: bp.reason })),
    pageErrors: globalPageErrors,
    pageErrorDetails: pageErrorDetails,
    failedRequests: globalFailedRequests,
    failedRequestDetails: failedRequestDetails,
    httpErrors: globalHttpErrors,
    httpErrorDetails: httpErrorDetails,
    ignoredHttpErrors: globalIgnoredHttpErrors,
    ignoredHttpErrorDetails: ignoredHttpErrorDetails,
    ignoredFailedRequests: globalIgnoredFailedRequests,
    ignoredFailedRequestDetails: ignoredFailedRequestDetails,
    horizontalOverflowFailures: globalOverflowFailures,
    ignoredRequests: ignoredRequests,
    resourceClassification: {
      ownOrigin: baseUrlOrigin,
      ownHost: baseUrlHost,
      note: 'Own-origin resources (localhost / site-dist assets) are never ignored. Only specific external patterns are ignored.'
    }
  };

  fs.writeFileSync(path.join(outputDir, 'manifest.json'), JSON.stringify(manifest, null, 2));

  const validationLines = [
    `Validation finished at ${new Date().toISOString()}`,
    `Errors: ${globalConsoleErrors}`,
    `Ignored benign console errors: ${globalIgnoredConsoleErrors}`,
    `Page Errors: ${globalPageErrors}`,
    `Network Failures: ${globalFailedRequests}`,
    `HTTP Errors: ${globalHttpErrors}`,
    `Ignored HTTP Errors: ${globalIgnoredHttpErrors}`,
    `Ignored Failed Requests: ${globalIgnoredFailedRequests}`,
    `Overflows: ${globalOverflowFailures}`,
    ''
  ];

  if (consoleErrorDetails.length > 0) {
    validationLines.push('--- Console errors (unclassified) ---');
    for (const e of consoleErrorDetails) {
      const loc = e.url ? ` @ ${e.url}` : '';
      const pos = (e.lineNumber || e.columnNumber) ? `:${e.lineNumber || 0}:${e.columnNumber || 0}` : '';
      validationLines.push(`[${e.viewport}/${e.theme}/${e.state}] (${e.type}): ${e.text}${loc}${pos}`);
    }
    validationLines.push('');
  }

  if (httpErrorDetails.length > 0) {
    validationLines.push('--- HTTP errors (own / unknown-external, failing) ---');
    for (const e of httpErrorDetails) {
      validationLines.push(`[${e.viewport}/${e.theme}/${e.state}] ${e.method} ${e.url} -> ${e.status} ${e.statusText} [${e.resourceType}]`);
    }
    validationLines.push('');
  }

  if (pageErrorDetails.length > 0) {
    validationLines.push('--- Page errors ---');
    for (const e of pageErrorDetails) {
      validationLines.push(`[${e.viewport}/${e.theme}/${e.state}] ${e.message}`);
      if (e.stack) validationLines.push(`  ${e.stack}`);
    }
    validationLines.push('');
  }

  if (failedRequestDetails.length > 0) {
    validationLines.push('--- Failed requests ---');
    for (const e of failedRequestDetails) {
      validationLines.push(`[${e.viewport}/${e.theme}/${e.state}] ${e.url} - ${e.error} [${e.resourceType}]`);
    }
    validationLines.push('');
  }

  if (ignoredHttpErrorDetails.length > 0) {
    validationLines.push(`--- Ignored HTTP errors (optional external, ${ignoredHttpErrorDetails.length} total) ---`);
    for (const e of ignoredHttpErrorDetails) {
      validationLines.push(`[${e.viewport}/${e.theme}/${e.state}] ${e.method} ${e.url} -> ${e.status} ${e.statusText} [${e.resourceType}] (${e.reason})`);
    }
    validationLines.push('');
  }

  if (ignoredFailedRequestDetails.length > 0) {
    validationLines.push(`--- Ignored failed requests (optional external, ${ignoredFailedRequestDetails.length} total) ---`);
    for (const e of ignoredFailedRequestDetails) {
      validationLines.push(`[${e.viewport}/${e.theme}/${e.state}] ${e.url} - ${e.error} (${e.reason})`);
    }
    validationLines.push('');
  }

  if (ignoredConsoleResourceErrors.length > 0) {
    validationLines.push(`--- Ignored console resource errors (optional external, ${ignoredConsoleResourceErrors.length} total) ---`);
    for (const e of ignoredConsoleResourceErrors) {
      validationLines.push(`[${e.viewport}/${e.theme}/${e.state}] ${e.text} @ ${e.url} (${e.reason})`);
    }
    validationLines.push('');
  }

  if (globalIgnoredConsoleErrors > 0) {
    validationLines.push(`--- Ignored benign console errors (${globalIgnoredConsoleErrors} total) ---`);
    validationLines.push(`Patterns: ${benignConsoleErrorPatterns.map(bp => bp.pattern).join(', ')}`);
    validationLines.push('');
  }

  fs.writeFileSync(path.join(outputDir, 'validation.log'), validationLines.join('\n'));

  if (globalConsoleErrors > 0 || globalPageErrors > 0 || globalFailedRequests > 0 || globalHttpErrors > 0 || globalOverflowFailures > 0) {
      console.error("Validation failed due to errors or overflow");
      if (httpErrorDetails.length > 0) {
        console.error(`HTTP errors (${httpErrorDetails.length}):`);
        for (const e of httpErrorDetails) {
          console.error(`  [${e.viewport}/${e.theme}/${e.state}] ${e.method} ${e.url} -> ${e.status} ${e.statusText} [${e.resourceType}]`);
        }
      }
      if (consoleErrorDetails.length > 0) {
        console.error(`Unclassified console errors (${consoleErrorDetails.length}):`);
        for (const e of consoleErrorDetails) {
          const loc = e.url ? ` @ ${e.url}` : '';
          console.error(`  [${e.viewport}/${e.theme}/${e.state}] ${e.text}${loc}`);
        }
      }
      if (failedRequestDetails.length > 0) {
        console.error(`Failed requests (${failedRequestDetails.length}):`);
        for (const e of failedRequestDetails) {
          console.error(`  [${e.viewport}/${e.theme}/${e.state}] ${e.url} - ${e.error} [${e.resourceType}]`);
        }
      }
      process.exit(1);
  }
}

function getGitSha() {
    try {
        return execSync('git rev-parse HEAD').toString().trim();
    } catch {
        return "unknown";
    }
}

capture().catch(error => {
  console.error(error);
  process.exit(1);
});
