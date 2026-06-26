const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const baseUrl = (process.argv[2] || 'http://localhost:8080').replace(/\/$/, '');
const outputDir = process.argv[3] || 'artifacts/screenshots/home-code-comparison';

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

function countMeaningfulLines(code) {
  let inBlock = false;
  let count = 0;
  for (let line of code.split('\n')) {
      const trimmed = line.trim();
      if (inBlock) {
          if (trimmed.includes('*/')) {
              inBlock = false;
              const after = trimmed.substring(trimmed.lastIndexOf('*/') + 2).trim();
              if (after.length > 0 && !after.startsWith('/*') && !after.startsWith('//')) {
                  count++;
              }
          }
      } else {
          if (trimmed.startsWith('/*')) {
              if (!trimmed.includes('*/')) {
                  inBlock = true;
              } else {
                  const after = trimmed.substring(trimmed.lastIndexOf('*/') + 2).trim();
                  if (after.length > 0 && !after.startsWith('/*') && !after.startsWith('//')) {
                      count++;
                  }
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

async function capture() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch();
  const results = [];
  
  let globalConsoleErrors = 0;
  let globalPageErrors = 0;
  let globalFailedRequests = 0;
  let globalOverflowFailures = 0;

  const metrics = getKotlinMetrics();

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

      page.on('console', message => {
        if (message.type() === 'error') {
          consoleMessages++;
        }
      });
      page.on('requestfailed', request => {
        if (!request.url().includes('favicon.ico') && !request.url().includes('github.com')) {
          console.error(`Failed request: ${request.url()} - ${request.failure()?.errorText}`);
          requestFailures++;
        }
      });
      page.on('pageerror', error => {
        pageErrors++;
      });

      const url = `${baseUrl}/${theme.query}`;
      console.log(`Testing ${url} (${viewport.name}, ${theme.name})`);

      try {
        const response = await page.goto(url, { waitUntil: 'networkidle' });
        if (!response || !response.ok()) {
          throw new Error(`HTTP status ${response ? response.status() : 'unknown'}`);
        }

        await page.waitForSelector('#webApp canvas', { timeout: 30000 });
        await page.waitForTimeout(3000); // Allow Compose to settle

        // Check overflow
        const overflow = await page.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
        if (overflow) {
            globalOverflowFailures++;
        }

        // Scroll to section
        const sectionLoc = page.locator('text=Code Comparison').first();
        if (await sectionLoc.isVisible()) {
            await sectionLoc.scrollIntoViewIfNeeded();
            await page.waitForTimeout(1000);
        }

        // Keyboard interaction fallback since Canvas doesn't expose DOM semantics by default
        await page.locator('#webApp canvas').click({ position: { x: 10, y: 10 } }); // Focus canvas
        await page.waitForTimeout(500);

        const isSideBySide = viewport.width >= 960;

        // Base state
        const baseFile = `${viewport.name}-${theme.name}-base.png`;
        await page.screenshot({ path: path.join(outputDir, baseFile), fullPage: true });
        results.push({ viewport: viewport.name, theme: theme.name, state: 'base', file: baseFile });

        if (!isSideBySide) {
            // Tabbed layout
            // Focus order: Get Started(1), GitHub(2), Play(3), AdaptiveKt Tab(4), Plain Compose Tab(5)
            for (let i = 0; i < 5; i++) {
                await page.keyboard.press('Tab');
                await page.waitForTimeout(200);
            }
            await page.keyboard.press('Enter'); // Select Plain Compose Tab
            await page.waitForTimeout(500);
            
            const file = `${viewport.name}-${theme.name}-plain-compose.png`;
            await page.screenshot({ path: path.join(outputDir, file), fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'plain-compose', file });

            // Now press Tab 2 more times to reach the Expand button
            // Methodology Toggle(6), Expand Button(7)
            await page.keyboard.press('Tab');
            await page.waitForTimeout(200);
            await page.keyboard.press('Tab');
            await page.waitForTimeout(200);
            await page.keyboard.press('Enter'); // Expand
            await page.waitForTimeout(500);

            const fileExp = `${viewport.name}-${theme.name}-expanded.png`;
            await page.screenshot({ path: path.join(outputDir, fileExp), fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'expanded', file: fileExp });

        } else {
            // SideBySide layout
            // Focus order: Get Started(1), GitHub(2), Play(3), Methodology Toggle(4), Expand Adaptive(5), Expand Compose(6)
            for (let i = 0; i < 5; i++) {
                await page.keyboard.press('Tab');
                await page.waitForTimeout(200);
            }
            await page.keyboard.press('Enter'); // Expand AdaptiveKt
            await page.waitForTimeout(500);
            
            const fileExpAd = `${viewport.name}-${theme.name}-expanded-adaptive.png`;
            await page.screenshot({ path: path.join(outputDir, fileExpAd), fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'expanded-adaptive', file: fileExpAd });
            
            await page.keyboard.press('Tab');
            await page.waitForTimeout(200);
            await page.keyboard.press('Enter'); // Expand Plain Compose
            await page.waitForTimeout(500);
            
            const fileExpCo = `${viewport.name}-${theme.name}-expanded-compose.png`;
            await page.screenshot({ path: path.join(outputDir, fileExpCo), fullPage: true });
            results.push({ viewport: viewport.name, theme: theme.name, state: 'expanded-compose', file: fileExpCo });
        }

        globalConsoleErrors += consoleMessages;
        globalPageErrors += pageErrors;
        globalFailedRequests += requestFailures;
        
      } catch (error) {
        console.error(`Failed: ${error.message}`);
      } finally {
        await page.close();
        await context.close();
      }
    }
  }

  await browser.close();

  // Generate Contact Sheet if montage is available
  try {
    console.log("Generating contact sheet...");
    execSync(`magick montage -geometry 400x+10+10 "${outputDir}/*.png" "${outputDir}/contact-sheet.png"`, { stdio: 'ignore' });
  } catch (e) {
    console.log("ImageMagick montage not available or failed. Skipping contact sheet.");
  }

  // Generate Manifest
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
    pageErrors: globalPageErrors,
    failedRequests: globalFailedRequests,
    horizontalOverflowFailures: globalOverflowFailures
  };

  fs.writeFileSync(path.join(outputDir, 'manifest.json'), JSON.stringify(manifest, null, 2));

  // Generate validation.log
  fs.writeFileSync(path.join(outputDir, 'validation.log'), `Validation finished at ${new Date().toISOString()}\nErrors: ${globalConsoleErrors}\nPage Errors: ${globalPageErrors}\nNetwork Failures: ${globalFailedRequests}\nOverflows: ${globalOverflowFailures}`);

  if (globalConsoleErrors > 0 || globalPageErrors > 0 || globalFailedRequests > 0 || globalOverflowFailures > 0) {
      console.error("Validation failed due to errors or overflow");
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
