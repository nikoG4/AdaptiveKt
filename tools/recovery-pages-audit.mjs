import { createRequire } from "node:module";
import fs from "node:fs/promises";
import path from "node:path";
import process from "node:process";

const require = createRequire(new URL("../examples/ecommerce-demo/package.json", import.meta.url));
const { chromium } = require("@playwright/test");

const baseUrl = process.env.ADAPTIVEKT_AUDIT_BASE_URL ?? "http://localhost:8080";
const outputDir = process.env.ADAPTIVEKT_AUDIT_OUTPUT_DIR ?? "artifacts/screenshots/recovery-validation";
const reportPath = process.env.ADAPTIVEKT_AUDIT_REPORT ?? "docs/internal/PAGES_REAL_BROWSER_FAILURE_AUDIT.md";

const routes = [
  { id: "root", path: "/" },
  { id: "docs", path: "/docs/" },
  { id: "components", path: "/components/" },
  { id: "demo", path: "/demo/" },
  { id: "admin-demo", path: "/demo/app/" },
  { id: "ecommerce-home", path: "/examples/ecommerce/" },
  { id: "ecommerce-shop", path: "/examples/ecommerce/#/shop" },
  { id: "ecommerce-cart", path: "/examples/ecommerce/#/cart" },
  { id: "ecommerce-checkout", path: "/examples/ecommerce/#/checkout" },
  { id: "ecommerce-login", path: "/examples/ecommerce/#/login" },
  { id: "ai-workspace-home", path: "/examples/ai-workspace/" },
  { id: "ai-workspace-chats", path: "/examples/ai-workspace/#/chats" },
  { id: "ai-workspace-prompts", path: "/examples/ai-workspace/#/prompts" },
  { id: "ai-workspace-settings", path: "/examples/ai-workspace/#/settings" },
];

const viewports = [
  { id: "mobile", width: 390, height: 844 },
  { id: "tablet", width: 768, height: 1024 },
  { id: "desktop", width: 1280, height: 800 },
  { id: "large", width: 1440, height: 900 },
];

const fatalPatterns = [
  /^Exception$/i,
  /WebAssembly\.Exception/i,
  /MissingResourceException/i,
  /Failed to fetch/i,
  /MIME type/i,
  /document\.body/i,
  /404.*(\.wasm|\.js|composeResources)/i,
  /net::ERR_/i,
];

function isBenignConsole(message) {
  return /WEBGL_debug_renderer_info/i.test(message);
}

function isFatal(message) {
  return fatalPatterns.some((pattern) => pattern.test(message));
}

function sanitizeName(input) {
  return input.replace(/[^a-z0-9_-]+/gi, "-").replace(/^-|-$/g, "").toLowerCase();
}

await fs.mkdir(outputDir, { recursive: true });
await fs.mkdir(path.dirname(reportPath), { recursive: true });

const browser = await chromium.launch();
const results = [];

for (const viewport of viewports) {
  const context = await browser.newContext({
    viewport: { width: viewport.width, height: viewport.height },
  });

  for (const route of routes) {
    const page = await context.newPage();
    const consoleMessages = [];
    const pageErrors = [];
    const failedRequests = [];
    const responseErrors = [];

    page.on("console", (msg) => {
      const text = `${msg.type()}: ${msg.text()}`;
      if (!isBenignConsole(text)) {
        consoleMessages.push(text);
      }
    });
    page.on("pageerror", (error) => {
      pageErrors.push(error.message);
    });
    page.on("requestfailed", (request) => {
      failedRequests.push(`${request.method()} ${request.url()} :: ${request.failure()?.errorText ?? "failed"}`);
    });
    page.on("response", (response) => {
      const status = response.status();
      if (status >= 400) {
        responseErrors.push(`${status} ${response.url()}`);
      }
    });

    const url = `${baseUrl}${route.path}`;
    let navigationError = null;
    try {
      await page.goto(url, { waitUntil: "domcontentloaded", timeout: 30000 });
      await page.waitForTimeout(3500);
    } catch (error) {
      navigationError = error.message;
    }

    const screenshotName = `${viewport.id}-${sanitizeName(route.id)}.png`;
    const screenshotPath = path.join(outputDir, screenshotName);
    try {
      await page.screenshot({ path: screenshotPath, fullPage: false });
    } catch (error) {
      pageErrors.push(`screenshot failed: ${error.message}`);
    }

    const pageState = await page.evaluate(() => {
      const scripts = Array.from(document.scripts).map((script) => script.src).filter(Boolean);
      const resourceLinks = Array.from(document.querySelectorAll("link[href]"))
        .map((link) => link.getAttribute("href"))
        .filter(Boolean);
      const bodyText = document.body?.innerText ?? "";
      const bodyRect = document.body?.getBoundingClientRect();
      const canvasRects = Array.from(document.querySelectorAll("canvas")).map((canvas) => {
        const rect = canvas.getBoundingClientRect();
        return {
          x: rect.x,
          y: rect.y,
          width: rect.width,
          height: rect.height,
          top: rect.top,
          right: rect.right,
          bottom: rect.bottom,
          left: rect.left,
          visible:
            rect.width > 0 &&
            rect.height > 0 &&
            rect.bottom > 0 &&
            rect.right > 0 &&
            rect.top < window.innerHeight &&
            rect.left < window.innerWidth,
        };
      });
      return {
        title: document.title,
        bodyTextLength: bodyText.trim().length,
        bodyTextPreview: bodyText.trim().replace(/\s+/g, " ").slice(0, 180),
        canvasCount: document.querySelectorAll("canvas").length,
        visibleCanvasCount: canvasRects.filter((rect) => rect.visible).length,
        canvasRects,
        bodyChildCount: document.body?.childElementCount ?? 0,
        bodyWidth: bodyRect?.width ?? 0,
        bodyHeight: bodyRect?.height ?? 0,
        scripts,
        resourceLinks,
      };
    }).catch((error) => ({
      title: "",
      bodyTextLength: 0,
      bodyTextPreview: "",
      canvasCount: 0,
      visibleCanvasCount: 0,
      canvasRects: [],
      bodyChildCount: 0,
      bodyWidth: 0,
      bodyHeight: 0,
      scripts: [],
      resourceLinks: [],
      evaluateError: error.message,
    }));

    const allErrors = [
      ...(navigationError ? [navigationError] : []),
      ...consoleMessages,
      ...pageErrors,
      ...failedRequests,
      ...responseErrors,
      ...(pageState.evaluateError ? [pageState.evaluateError] : []),
    ];
    const fatalErrors = allErrors.filter(isFatal);
    const pageRuntimeErrors = pageErrors.filter((error) => !isBenignConsole(error));
    const visiblyMounted = pageState.visibleCanvasCount > 0 || pageState.bodyTextLength > 20;
    const passed = fatalErrors.length === 0 && pageRuntimeErrors.length === 0 && visiblyMounted;

    results.push({
      route: route.id,
      url,
      viewport,
      screenshotPath: screenshotPath.replaceAll("\\", "/"),
      passed,
      visiblyMounted,
      navigationError,
      consoleMessages,
      pageErrors,
      failedRequests,
      responseErrors,
      fatalErrors,
      pageRuntimeErrors,
      pageState,
    });

    await page.close();
  }

  await context.close();
}

await browser.close();

const summaryRows = results.map((result) => {
  const status = result.passed ? "PASS" : "FAIL";
  const fatal = result.fatalErrors.length ? result.fatalErrors.join("<br>") : "-";
  return `| ${status} | ${result.viewport.id} ${result.viewport.width}x${result.viewport.height} | ${result.route} | ${result.screenshotPath} | ${result.pageState.visibleCanvasCount}/${result.pageState.canvasCount} | ${result.pageState.bodyTextLength} | ${fatal} |`;
});

const resourceNotes = results.map((result) => {
  const scripts = result.pageState.scripts.length
    ? result.pageState.scripts.map((script) => `  - ${script}`).join("\n")
    : "  - none detected";
  const errors = [
    ...result.failedRequests,
    ...result.responseErrors,
    ...result.pageErrors,
    ...(result.navigationError ? [result.navigationError] : []),
  ];
  return [
    `### ${result.viewport.id} / ${result.route}`,
    `URL: ${result.url}`,
    `Screenshot: ${result.screenshotPath}`,
    `Canvas count: ${result.pageState.canvasCount}`,
    `Visible canvas count: ${result.pageState.visibleCanvasCount}`,
    `Body text length: ${result.pageState.bodyTextLength}`,
    `Body preview: ${result.pageState.bodyTextPreview || "-"}`,
    "Scripts:",
    scripts,
    "Errors:",
    errors.length ? errors.map((error) => `  - ${error}`).join("\n") : "  - none",
  ].join("\n");
});

const report = [
  "# Pages Real Browser Failure Audit",
  "",
  `Generated against ${baseUrl}.`,
  "",
  "| Status | Viewport | Route | Screenshot | Visible/total canvas | Text length | Fatal errors |",
  "|---|---:|---|---|---:|---:|---|",
  ...summaryRows,
  "",
  "## Resource And Error Details",
  "",
  ...resourceNotes,
  "",
].join("\n");

await fs.writeFile(reportPath, report, "utf8");
await fs.writeFile(path.join(outputDir, "audit-results.json"), JSON.stringify(results, null, 2), "utf8");

const failures = results.filter((result) => !result.passed);
console.log(`Audited ${results.length} route/viewport combinations.`);
console.log(`Passed: ${results.length - failures.length}`);
console.log(`Failed: ${failures.length}`);
console.log(`Report: ${reportPath}`);
if (failures.length > 0) {
  console.log("Failures:");
  for (const failure of failures) {
    console.log(`- ${failure.viewport.id} ${failure.route}: ${failure.fatalErrors[0] ?? "not visibly mounted"}`);
  }
  process.exitCode = 1;
}
