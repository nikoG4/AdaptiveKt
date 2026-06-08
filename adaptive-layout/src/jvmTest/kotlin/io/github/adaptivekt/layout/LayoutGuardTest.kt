package io.github.adaptivekt.layout

import org.junit.Test
import java.io.File
import kotlin.test.assertTrue
import kotlin.test.fail

class LayoutGuardTest {

    private val forbiddenPatterns = listOf(
        "BoxWithConstraints",
        "breakpointForWidth",
        "import androidx.compose.foundation.layout.BoxWithConstraints"
    )

    // Allowed LocalAdaptiveLayoutInfo.current usages (app-specific structural variants).
    // All paths use normalized separators '/'
    private val allowList = mapOf(
        "examples/ecommerce-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/ecommerce/ui/home/HomeScreen.kt" to listOf(
            "val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current"
        ),
        "examples/ecommerce-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/ecommerce/ui/products/ProductScreens.kt" to listOf(
            "val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current"
        ),
        "examples/ecommerce-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/ecommerce/ui/cart/CartScreens.kt" to listOf(
            "val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current"
        ),
        "examples/ecommerce-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/ecommerce/ui/components/AppShell.kt" to listOf(
            "val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current"
        ),
        "examples/ecommerce-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/ecommerce/ui/account/WishlistScreen.kt" to listOf(
            "val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current"
        )
    )

    @Test
    fun verifyEcommerceDemoDoesNotUseForbiddenLayoutPatterns() {
        val rootDir = File(System.getProperty("user.dir")).parentFile ?: return
        val ecommerceDemoDir = File(rootDir, "examples/ecommerce-demo/src")
        
        if (!ecommerceDemoDir.exists()) {
            println("Skipping test: ecommerce-demo source directory not found at ${ecommerceDemoDir.absolutePath}")
            return
        }

        val kotlinFiles = ecommerceDemoDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
        assertTrue(kotlinFiles.isNotEmpty(), "Expected to find Kotlin files in ecommerce-demo")

        var errors = 0
        kotlinFiles.forEach { file ->
            val relativePath = file.toRelativeString(rootDir).replace('\\', '/')
            val content = file.readText()
            
            // 1. Check for strictly forbidden patterns
            forbiddenPatterns.forEach { pattern ->
                if (content.contains(pattern)) {
                    System.err.println("ERROR: Forbidden pattern '$pattern' found in ${file.absolutePath}")
                    errors++
                }
            }

            // 2. Check for layout measurement using max width/height
            if (content.contains("maxWidth") && !content.contains("Modifier.fillMaxWidth") && !content.contains("Modifier.widthIn(max =")) {
                if (!relativePath.contains("AppShell.kt")) { // ignore legit AppShell uses if any
                    // Note: This is a loose check, we mostly want to prevent manual calc
                    // e.g. "if (maxWidth > 600.dp)"
                    if (content.matches(Regex(".*maxWidth\\s*[><=]+.*".toRegex().pattern))) {
                        System.err.println("ERROR: Potential manual adaptive measurement using 'maxWidth' found in ${file.absolutePath}")
                        errors++
                    }
                }
            }

            // 3. Check for LocalAdaptiveLayoutInfo.current against allowlist
            if (content.contains("LocalAdaptiveLayoutInfo.current")) {
                val allowedInFile = allowList[relativePath]
                if (allowedInFile == null) {
                    System.err.println("ERROR: LocalAdaptiveLayoutInfo.current used in unauthorized file: $relativePath")
                    errors++
                }
            }
        }

        if (errors > 0) {
            fail("Found $errors layout guard violations in ecommerce-demo. Please use standard Adaptive primitives (AdaptivePage, AdaptiveTwoPane, AdaptiveGrid) instead of manual measurement.")
        }
    }
}
