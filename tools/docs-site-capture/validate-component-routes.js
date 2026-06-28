const { chromium } = require("playwright");
const fs = require("fs");
const path = require("path");

const baseUrl = (process.argv[2] || "http://localhost:8080").replace(/\/$/, "");
const outputDir = process.argv[3] || "artifacts/route-validation";

const componentRoutes = JSON.parse(fs.readFileSync(path.join(__dirname, "component-routes.json"), "utf8")).components;

function isKnownRunnerWarning(text) {
  return text.includes("GPU stall due to ReadPixels") ||
         text.includes("Automatic fallback to software WebGL has been deprecated");
}

async function validate() {
  fs.mkdirSync(outputDir, { recursive: true });
  const browser = await chromium.launch({ args: ["--use-gl=angle", "--use-angle=swiftshader"] });
  const context = await browser.newContext();
  const page = await context.newPage();
  const results = [];
  let failed = false;

  for (const componentId of componentRoutes) {
    const consoleErrors = [];
    const pageErrors = [];
    const requestFailures = [];

    // Attach listeners
    const consoleHandler = message => {
      if (message.type() === "error" || message.type() === "warning") {
        if (!isKnownRunnerWarning(message.text())) {
          consoleErrors.push(message.text());
        }
      }
    };
    const pageErrorHandler = error => {
      pageErrors.push(error.message);
    };
    const requestHandler = request => {
      requestFailures.push(`${request.method()} ${request.url()} ${request.failure()?.errorText || ""}`.trim());
    };
    
    page.on("console", consoleHandler);
    page.on("pageerror", pageErrorHandler);
    page.on("requestfailed", requestHandler);

    const url = `${baseUrl}/components/?capture=1#${componentId}`;
    console.log(`Validating ${url}...`);

    let result = {
      expectedComponentId: componentId,
      actualComponentId: null,
      url: url,
      httpStatus: 0,
      canvasFound: false,
      screenshotBytes: 0,
      consoleErrors: [],
      pageErrors: [],
      requestFailures: [],
      overflow: false,
      success: false
    };

    try {
      const response = await page.goto(url, { waitUntil: "networkidle" });
      if (response !== null) {
        result.httpStatus = response.status();
        if (!response.ok()) {
          throw new Error(`HTTP status ${response.status()}`);
        }
      }

      await page.waitForSelector("#webApp canvas", { timeout: 15000 });
      await page.waitForTimeout(1000); // Wait for compose rendering

      const canvasBox = await page.locator("#webApp canvas").boundingBox();
      if (!canvasBox || canvasBox.width < 100 || canvasBox.height < 100) {
        throw new Error("Compose canvas is missing or too small");
      }
      result.canvasFound = true;

      // Check deterministic state
      const validationEl = await page.$("#docs-validation-state");
      if (!validationEl) {
        throw new Error("Validation state element not found");
      }
      const actualComponentId = await validationEl.getAttribute("data-component");
      result.actualComponentId = actualComponentId;

      if (actualComponentId !== componentId) {
        throw new Error(`Component mismatch. Expected: ${componentId}, Actual: ${actualComponentId}`);
      }

      // Check overflow
      const bodyWidth = await page.evaluate(() => document.body.scrollWidth);
      const windowWidth = await page.evaluate(() => window.innerWidth);
      if (bodyWidth > windowWidth) {
        result.overflow = true;
        throw new Error(`Global overflow detected (bodyWidth ${bodyWidth} > windowWidth ${windowWidth})`);
      }

      if (consoleErrors.length > 0) {
        throw new Error(`Console errors detected: ${consoleErrors[0]}`);
      }
      if (pageErrors.length > 0) {
        throw new Error(`Page errors detected: ${pageErrors[0]}`);
      }

      result.success = true;
    } catch (error) {
      console.error(`Failed: ${error.message}`);
      result.error = error.message;
    } finally {
      result.consoleErrors = consoleErrors;
      result.pageErrors = pageErrors;
      result.requestFailures = requestFailures;
      
      // Take screenshot anyway
      try {
        const screenshotPath = path.join(outputDir, `${componentId}.png`);
        const buffer = await page.screenshot({ path: screenshotPath });
        result.screenshotBytes = buffer.length;
        if (buffer.length < 5000) {
          result.error = (result.error ? result.error + "; " : "") + "Screenshot is suspiciously small";
          result.success = false;
        }
      } catch (e) {
        // ignore screenshot errors if already failing
      }

      results.push(result);
      if (!result.success) failed = true;

      page.off("console", consoleHandler);
      page.off("pageerror", pageErrorHandler);
      page.off("requestfailed", requestHandler);
    }
  }

  await browser.close();
  
  fs.writeFileSync(path.join(outputDir, "route-validation.json"), JSON.stringify(results, null, 2));

  let report = "# Component Routes Validation Report\n\n";
  report += `Generated on: ${new Date().toISOString()}\n\n`;
  report += `Base URL: ${baseUrl}\n\n`;
  report += "| Route | Console errors | JS errors | Network failures | Result |\n";
  report += "|---|---:|---:|---:|---|\n";

  for (const result of results) {
    const status = result.success ? "OK" : `FAILED: ${result.error}`;
    report += `| ${result.expectedComponentId} | ${result.consoleErrors.length} | ${result.pageErrors.length} | ${result.requestFailures.length} | ${status} |\n`;
  }

  fs.writeFileSync(path.join(outputDir, "route-validation-report.md"), report);
  console.log(`Report generated at ${path.join(outputDir, "route-validation-report.md")}`);

  if (failed) {
    process.exit(1);
  }
}

validate().catch(error => {
  console.error(error);
  process.exit(1);
});

