package io.github.adaptivekt.admin.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.core.AdaptiveColorSchemes
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.navigation.AdaptiveNavItem
import io.github.adaptivekt.navigation.AdaptiveNavigationScaffold
import io.github.adaptivekt.admin.demo.ui.AdminDemoTopBar

@Composable
public fun AdminDemoApp(
    initialScreen: AdminDemoScreen = AdminDemoScreen.Dashboard,
    initialAccountMenuOpen: Boolean = false,
    initialDarkTheme: Boolean = false,
) {
    var darkTheme by remember { mutableStateOf(initialDarkTheme) }
    AdaptiveTheme(
        colorScheme = if (darkTheme) AdaptiveColorSchemes.defaultDark() else AdaptiveColorSchemes.defaultLight(),
    ) {
        AdminDemoThemedApp(
            initialScreen = initialScreen,
            initialAccountMenuOpen = initialAccountMenuOpen,
            darkTheme = darkTheme,
            onThemeToggle = { darkTheme = !darkTheme },
        )
    }
}

@Composable
private fun AdminDemoThemedApp(
    initialScreen: AdminDemoScreen,
    initialAccountMenuOpen: Boolean,
    darkTheme: Boolean,
    onThemeToggle: () -> Unit,
) {
    var selectedItemId by remember {
        mutableStateOf(
            when {
                initialScreen.id.startsWith("components") -> "components"
                initialScreen.id.startsWith("invoices") -> "invoices"
                else -> initialScreen.id
            },
        )
    }

    val navItems = listOf(
        AdaptiveNavItem(id = "dashboard", label = "Dashboard"),
        AdaptiveNavItem(id = "employees", label = "Employees"),
        AdaptiveNavItem(id = "products", label = "Products"),
        AdaptiveNavItem(id = "invoices", label = "Invoices"),
        AdaptiveNavItem(id = "settings", label = "Settings"),
        AdaptiveNavItem(id = "components", label = "UI Kit"),
    )

    AdaptiveNavigationScaffold(
        navItems = navItems,
        selectedItemId = selectedItemId,
        onItemSelected = { selectedItemId = it },
        topBar = {
            AdminDemoTopBar(
                initialAccountMenuOpen = initialAccountMenuOpen,
                darkTheme = darkTheme,
                onThemeToggle = onThemeToggle,
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AdaptiveTheme.colors.background)
                .padding(padding),
        ) {
            AdaptiveContainer {
                when (selectedItemId) {
                    "dashboard" -> DashboardScreen()
                    "employees" -> EmployeesScreen()
                    "products" -> ProductsScreen()
                    "invoices" -> InvoicesScreen(initialState = initialScreen.invoiceState)
                    "settings" -> SettingsScreen()
                    "components" -> ComponentsShowcaseScreen(
                        focusSection = initialScreen.componentsShowcaseSection,
                        initialSelectExpanded = initialScreen == AdminDemoScreen.ComponentsSelectsOpen,
                        initialMultiSelectExpanded = initialScreen == AdminDemoScreen.ComponentsMultiSelectsOpen,
                    )
                    else -> DashboardScreen()
                }
            }
        }
    }
}

private val AdminDemoScreen.componentsShowcaseSection: ComponentsShowcaseSection?
    get() = when (this) {
        AdminDemoScreen.ComponentsButtons -> ComponentsShowcaseSection.Buttons
        AdminDemoScreen.ComponentsBadges -> ComponentsShowcaseSection.Badges
        AdminDemoScreen.ComponentsAvatars -> ComponentsShowcaseSection.Avatars
        AdminDemoScreen.ComponentsCards -> ComponentsShowcaseSection.Cards
        AdminDemoScreen.ComponentsDropdowns -> ComponentsShowcaseSection.Dropdowns
        AdminDemoScreen.ComponentsFields -> ComponentsShowcaseSection.Fields
        AdminDemoScreen.ComponentsSelects -> ComponentsShowcaseSection.Selects
        AdminDemoScreen.ComponentsSelectsOpen -> ComponentsShowcaseSection.Selects
        AdminDemoScreen.ComponentsMultiSelects -> ComponentsShowcaseSection.MultiSelects
        AdminDemoScreen.ComponentsMultiSelectsOpen -> ComponentsShowcaseSection.MultiSelects
        else -> null
    }

private val AdminDemoScreen.invoiceState: InvoiceDemoState
    get() = when (this) {
        AdminDemoScreen.InvoicesEmpty -> InvoiceDemoState.Empty
        AdminDemoScreen.InvoicesLoading -> InvoiceDemoState.Loading
        AdminDemoScreen.InvoicesError -> InvoiceDemoState.Error
        else -> InvoiceDemoState.Content
    }
