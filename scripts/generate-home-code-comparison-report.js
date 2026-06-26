const fs = require('fs');
const path = require('path');

const manifestPath = path.join(__dirname, '../artifacts/screenshots/home-code-comparison/manifest.json');
const reportDir = path.join(__dirname, '../artifacts/reports');
const reportPath = path.join(reportDir, 'home-code-comparison.md');

if (!fs.existsSync(manifestPath)) {
    console.error(`Manifest not found at ${manifestPath}`);
    process.exit(1);
}

const manifest = JSON.parse(fs.readFileSync(manifestPath, 'utf8'));

const markdown = `# Home Code Comparison Visual Report

Generated on: ${manifest.generatedAt}
Git SHA: ${manifest.gitSha}

## Metrics Validation

- **AdaptiveKt Lines:** ${manifest.adaptiveLines}
- **Plain Compose Lines:** ${manifest.composeLines}
- **Lines Saved:** ${manifest.savedLines}
- **Reduction:** ${manifest.reductionPercent}%

## Accessibility and Semantics in Wasm Canvas

Playwright semantic text locators (e.g., \`text=Less boilerplate, more focus\`) are **NOT** natively available in the DOM for Compose Wasm because the UI is rendered entirely inside a \`<canvas>\` element and the accessibility tree is not exposed as HTML DOM elements by default in this configuration.

**Working Locators:**
- \`#webApp canvas\` (for initial focus)

**Failed Locators:**
- Any text-based semantic locator (e.g., \`text=Code Comparison\`)

**Fallback Strategy:**
Due to the lack of semantic DOM, interaction tests fall back to keyboard navigation (using \`Tab\` and \`Enter\`) combined with visual snapshot hashing to verify state changes (e.g., ensuring collapsed state hash != expanded state hash). 

## Visual Contact Sheet

![Contact Sheet](../screenshots/home-code-comparison/contact-sheet.png)

## Screenshots

| Viewport | Theme | State | File |
|----------|-------|-------|------|
${manifest.screenshots.map(s => `| ${s.viewport} | ${s.theme} | ${s.state} | [image](../screenshots/home-code-comparison/${s.file}) |`).join('\n')}

## Stability

- **Console Errors:** ${manifest.consoleErrors}
- **Page Errors:** ${manifest.pageErrors}
- **Failed Requests:** ${manifest.failedRequests}
- **Horizontal Overflows:** ${manifest.horizontalOverflowFailures}
`;

fs.mkdirSync(reportDir, { recursive: true });
fs.writeFileSync(reportPath, markdown);
console.log(`Report generated at ${reportPath}`);
