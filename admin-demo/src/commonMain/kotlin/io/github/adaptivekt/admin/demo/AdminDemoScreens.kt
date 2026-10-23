package io.github.adaptivekt.admin.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.admin.demo.ui.DemoCard
import io.github.adaptivekt.admin.demo.ui.DemoPanel
import io.github.adaptivekt.admin.demo.ui.DemoRemoteAvatar
import io.github.adaptivekt.admin.demo.ui.DemoRemoteThumbnail
import io.github.adaptivekt.admin.demo.ui.DemoStatusText
import io.github.adaptivekt.admin.demo.ui.DemoText
import io.github.adaptivekt.admin.demo.ui.DemoToggleChip
import io.github.adaptivekt.admin.demo.ui.Emphasis
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveDialog
import io.github.adaptivekt.components.AdaptiveSearchField
import io.github.adaptivekt.components.AdaptiveSectionHeader
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.components.icons.AdaptiveIcons
import io.github.adaptivekt.core.AdaptiveTokens
import io.github.adaptivekt.core.rememberAdaptiveInfo
import io.github.adaptivekt.data.AdaptiveActionPriority
import io.github.adaptivekt.data.AdaptiveDataAction
import io.github.adaptivekt.data.AdaptiveDataColumn
import io.github.adaptivekt.data.AdaptiveDataContent
import io.github.adaptivekt.data.AdaptiveDataMobileRole
import io.github.adaptivekt.data.AdaptiveDataState
import io.github.adaptivekt.forms.AdaptiveFormColumns
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.forms.FieldSpan
import io.github.adaptivekt.forms.LabelPosition
import io.github.adaptivekt.forms.AdaptiveValidationMessage
import io.github.adaptivekt.forms.AdaptiveValidationMessageType
import io.github.adaptivekt.layout.AdaptiveGrid

@Composable
internal fun DashboardScreen() {
    val metrics = AdminDemoData.dashboardMetrics
    val adaptiveInfo = rememberAdaptiveInfo()
    val span = when {
        adaptiveInfo.isCompact -> 12
        adaptiveInfo.isMedium || adaptiveInfo.isExpanded -> 6
        else -> 3
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveSectionHeader(title = "Dashboard")
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        DemoText(
            text = "Operational snapshot for teams, catalog, invoices, and revenue.",
            emphasis = Emphasis.Subtle,
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        AdaptiveGrid(columns = 12, horizontalGap = AdaptiveTokens.Spacing.Large, verticalGap = AdaptiveTokens.Spacing.Large) {
            metrics.forEach { metric ->
                item(span = span) {
                    DemoCard(
                        title = metric.title,
                        value = metric.value,
                        subtitle = metric.subtitle,
                        indicator = when (metric.title) {
                            "Monthly revenue" -> "+12%"
                            "Pending invoices" -> "Needs review"
                            else -> "Stable"
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
        AdaptiveGrid(columns = 12, horizontalGap = AdaptiveTokens.Spacing.Large, verticalGap = AdaptiveTokens.Spacing.Large) {
            item(span = if (adaptiveInfo.isCompact) 12 else 7) {
                DemoPanel(
                    title = "Recent invoices",
                    subtitle = "Latest billing activity across customers.",
                ) {
                    AdminDemoData.invoices.take(3).forEach { invoice ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AdaptiveTokens.Spacing.Small),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                DemoText(text = invoice.customer, emphasis = Emphasis.Strong)
                                DemoText(text = invoice.issueDate, emphasis = Emphasis.Subtle)
                            }
                            Column {
                                DemoText(text = invoice.amount, emphasis = Emphasis.Strong)
                                DemoStatusText(text = invoice.status)
                            }
                        }
                    }
                }
            }
            item(span = if (adaptiveInfo.isCompact) 12 else 5) {
                DemoPanel(
                    title = "Activity summary",
                    subtitle = "Workspace health at a glance.",
                ) {
                    listOf(
                        "Catalog sync" to "Completed",
                        "Payroll export" to "Pending",
                        "Security review" to "Active",
                    ).forEach { (label, status) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AdaptiveTokens.Spacing.Small),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            DemoText(text = label)
                            DemoStatusText(text = status)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun EmployeesScreen() {
    // This is a fully interactive local demo; changes reset on refresh.
    val employees = remember {
        mutableStateListOf<Employee>().apply { addAll(AdminDemoData.employees) }
    }
    var searchText by remember { mutableStateOf("") }
    var selectedEmployeeId by remember { mutableStateOf<String?>(employees.firstOrNull()?.id) }
    var showEditor by remember { mutableStateOf(false) }
    var editingId by remember { mutableStateOf<String?>(null) }
    var draftName by remember { mutableStateOf("") }
    var draftRole by remember { mutableStateOf("") }
    var draftDepartment by remember { mutableStateOf("") }
    var draftEmail by remember { mutableStateOf("") }

    val openEditor: (Employee?) -> Unit = { employee ->
        editingId = employee?.id
        draftName = employee?.name.orEmpty()
        draftRole = employee?.role.orEmpty()
        draftDepartment = employee?.department.orEmpty()
        draftEmail = employee?.email.orEmpty()
        showEditor = true
    }

    val visibleEmployees = if (searchText.isBlank()) employees.toList() else employees.filter {
        it.name.contains(searchText, ignoreCase = true) ||
            it.email.contains(searchText, ignoreCase = true) ||
            it.department.contains(searchText, ignoreCase = true) ||
            it.role.contains(searchText, ignoreCase = true)
    }
    val employeeActions = listOf(
        AdaptiveDataAction<Employee>(
            id = "view",
            label = "View",
            priority = AdaptiveActionPriority.Primary,
            onClick = { selectedEmployeeId = it.id },
        ),
        AdaptiveDataAction<Employee>(
            id = "edit",
            label = "Edit",
            priority = AdaptiveActionPriority.Secondary,
            onClick = { openEditor(it) },
        ),
        AdaptiveDataAction<Employee>(
            id = "archive",
            label = "Archive",
            priority = AdaptiveActionPriority.Overflow,
            destructive = true,
            onClick = { employee ->
                val index = employees.indexOfFirst { it.id == employee.id }
                if (index >= 0) employees[index] = employee.copy(status = "Archived")
                selectedEmployeeId = employee.id
            },
        ),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveSectionHeader(title = "Employees")
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
        DemoText(
            text = "${employees.size} team members · ${employees.count { it.status == "Active" }} active · Local demo data",
            emphasis = Emphasis.Subtle,
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))

        io.github.adaptivekt.data.AdaptiveDataView(
            state = AdaptiveDataContent(visibleEmployees),
            columns = listOf(
                AdaptiveDataColumn(
                    id = "avatar",
                    header = "",
                    weight = 0.5f,
                    mobileRole = AdaptiveDataMobileRole.Media,
                    mobilePriority = 0,
                ) { DemoRemoteAvatar(name = it.name, avatarUrl = it.avatarUrl, size = 36.dp) },
                AdaptiveDataColumn(
                    id = "name",
                    header = "Name",
                    weight = 2f,
                    mobileRole = AdaptiveDataMobileRole.Title,
                    mobilePriority = 1,
                ) { DemoText(text = it.name, emphasis = Emphasis.Strong) },
                AdaptiveDataColumn(
                    id = "role",
                    header = "Role",
                    weight = 1.4f,
                    mobileRole = AdaptiveDataMobileRole.Subtitle,
                ) { DemoText(text = it.role) },
                AdaptiveDataColumn(
                    id = "department",
                    header = "Department",
                    weight = 1.4f,
                ) { DemoText(text = it.department) },
                AdaptiveDataColumn(
                    id = "status",
                    header = "Status",
                    weight = 1f,
                    mobileRole = AdaptiveDataMobileRole.Status,
                ) { DemoStatusText(text = it.status) },
                AdaptiveDataColumn(
                    id = "email",
                    header = "Email",
                    weight = 2.4f,
                ) { DemoText(text = it.email) },
            ),
            filterSlot = {
                AdaptiveSearchField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = "Search by name, role, department or email",
                    onClear = { searchText = "" },
                )
            },
            actions = {
                AdaptiveButton(
                    text = "New Employee",
                    leadingIcon = { AdaptiveIcons.Plus(size = 16.dp, tint = Color.White) },
                    onClick = { openEditor(null) },
                )
            },
            rowActions = employeeActions,
            onItemClick = { selectedEmployeeId = it.id },
        )

        selectedEmployeeId?.let { employeeId ->
            val employee = employees.firstOrNull { it.id == employeeId }
            if (employee != null) {
                Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
                DemoPanel(title = "Employee profile", subtitle = "Selected record · Changes stay in this browser session") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    ) {
                        DemoRemoteAvatar(name = employee.name, avatarUrl = employee.avatarUrl, size = 48.dp)
                        Column {
                            DemoText(text = employee.name, emphasis = Emphasis.Strong)
                            Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.XSmall))
                            DemoStatusText(text = employee.status)
                        }
                    }
                    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
                    val compact = rememberAdaptiveInfo().isCompact
                    AdaptiveGrid(
                        columns = 12,
                        horizontalGap = AdaptiveTokens.Spacing.Medium,
                        verticalGap = AdaptiveTokens.Spacing.Medium,
                    ) {
                        item(span = if (compact) 12 else 4) {
                            Column {
                                DemoText(text = "ROLE", emphasis = Emphasis.Subtle)
                                DemoText(text = employee.role)
                            }
                        }
                        item(span = if (compact) 12 else 4) {
                            Column {
                                DemoText(text = "DEPARTMENT", emphasis = Emphasis.Subtle)
                                DemoText(text = employee.department)
                            }
                        }
                        item(span = if (compact) 12 else 4) {
                            Column {
                                DemoText(text = "EMAIL", emphasis = Emphasis.Subtle)
                                DemoText(text = employee.email)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Large))
                    AdaptiveButton(
                        text = "Edit profile",
                        variant = AdaptiveButtonVariant.Secondary,
                        onClick = { openEditor(employee) },
                    )
                }
            }
        }
    }

    if (showEditor) {
        val valid = draftName.isNotBlank() && draftRole.isNotBlank() &&
            draftDepartment.isNotBlank() && draftEmail.contains("@") &&
            draftEmail.substringAfter("@").contains(".")
        AdaptiveDialog(
            title = if (editingId == null) "Add employee" else "Edit employee",
            onDismissRequest = { showEditor = false },
            dismissButton = {
                AdaptiveButton(
                    text = "Cancel",
                    variant = AdaptiveButtonVariant.Secondary,
                    onClick = { showEditor = false },
                )
            },
            confirmButton = {
                AdaptiveButton(
                    text = if (editingId == null) "Add employee" else "Save changes",
                    enabled = valid,
                    onClick = {
                        if (valid) {
                            val id = editingId
                            val original = employees.firstOrNull { it.id == id }
                            val updated = Employee(
                                id = id ?: "demo-${employees.size + 1}",
                                avatarColor = original?.avatarColor ?: Color(0xFFE8F1FF),
                                name = draftName.trim(),
                                role = draftRole.trim(),
                                department = draftDepartment.trim(),
                                status = original?.status ?: "Active",
                                email = draftEmail.trim(),
                                avatarUrl = original?.avatarUrl,
                            )
                            if (original == null) {
                                employees.add(updated)
                            } else {
                                val index = employees.indexOfFirst { it.id == original.id }
                                if (index >= 0) employees[index] = updated
                            }
                            selectedEmployeeId = updated.id
                            searchText = ""
                            showEditor = false
                        }
                    },
                )
            },
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Medium)) {
                DemoText(text = "Try creating or updating a record. No changes are sent to a server.", emphasis = Emphasis.Subtle, maxLines = 2)
                AdaptiveTextField(
                    value = draftName, onValueChange = { draftName = it },
                    label = "Full name", placeholder = "e.g. Alex Morgan",
                )
                AdaptiveTextField(
                    value = draftRole, onValueChange = { draftRole = it },
                    label = "Role", placeholder = "e.g. Product Designer",
                )
                AdaptiveTextField(
                    value = draftDepartment, onValueChange = { draftDepartment = it },
                    label = "Department", placeholder = "e.g. Design",
                )
                AdaptiveTextField(
                    value = draftEmail, onValueChange = { draftEmail = it },
                    label = "Email", placeholder = "name@example.com",
                )
            }
        }
    }
}
@Composable
internal fun ProductsScreen() {
    val productActions = listOf(
        AdaptiveDataAction<Product>(
            id = "view",
            label = "View",
            priority = AdaptiveActionPriority.Primary,
            onClick = { },
        ),
        AdaptiveDataAction<Product>(
            id = "adjust",
            label = "Adjust",
            priority = AdaptiveActionPriority.Secondary,
            onClick = { },
        ),
        AdaptiveDataAction<Product>(
            id = "disable",
            label = "Disable",
            priority = AdaptiveActionPriority.Overflow,
            destructive = true,
            onClick = { },
        ),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveSectionHeader(title = "Products")
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        DemoText(
            text = "Catalog health, inventory status, and product pricing.",
            emphasis = Emphasis.Subtle,
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))

        io.github.adaptivekt.data.AdaptiveDataView(
            state = AdaptiveDataContent(AdminDemoData.products),
            columns = listOf(
                AdaptiveDataColumn(
                    id = "thumbnail",
                    header = "",
                    weight = 0.5f,
                    mobileRole = AdaptiveDataMobileRole.Media,
                    mobilePriority = 0,
                ) { DemoRemoteThumbnail(label = it.name, imageUrl = it.thumbnailUrl, fallbackTone = it.thumbnailColor) },
                AdaptiveDataColumn(
                    id = "name",
                    header = "Name",
                    weight = 2f,
                    mobileRole = AdaptiveDataMobileRole.Title,
                    mobilePriority = 1,
                ) { DemoText(text = it.name, emphasis = Emphasis.Strong) },
                AdaptiveDataColumn(
                    id = "sku",
                    header = "SKU",
                    weight = 1f,
                    mobileRole = AdaptiveDataMobileRole.Subtitle,
                ) { DemoText(text = it.sku) },
                AdaptiveDataColumn(
                    id = "stock",
                    header = "Stock",
                    weight = 1f,
                ) { DemoText(text = it.stock.toString()) },
                AdaptiveDataColumn(
                    id = "price",
                    header = "Price",
                    weight = 1f,
                ) { DemoText(text = it.price, emphasis = Emphasis.Strong) },
                AdaptiveDataColumn(
                    id = "status",
                    header = "Status",
                    weight = 1f,
                    mobileRole = AdaptiveDataMobileRole.Status,
                ) { DemoStatusText(text = it.status) },
            ),
            filterSlot = {
                DemoText(text = "Product catalog preview", emphasis = Emphasis.Subtle)
            },
            actions = {
                AdaptiveButton(
                    text = "Add product",
                    leadingIcon = { AdaptiveIcons.Plus(size = 16.dp, tint = Color.White) },
                    onClick = { /* no-op demo action */ },
                )
            },
            rowActions = productActions,
            onItemClick = { /* item clicked */ },
        )
    }
}

internal enum class InvoiceDemoState {
    Content,
    Empty,
    Loading,
    Error,
}

private fun InvoiceDemoState.toAdaptiveDataState(): AdaptiveDataState<Invoice> = when (this) {
    InvoiceDemoState.Content -> AdaptiveDataContent(AdminDemoData.invoices)
    InvoiceDemoState.Empty -> io.github.adaptivekt.data.AdaptiveDataEmpty(
        title = "No invoices found",
        description = "Your invoice feed is currently empty.",
    )
    InvoiceDemoState.Loading -> io.github.adaptivekt.data.AdaptiveDataLoading
    InvoiceDemoState.Error -> io.github.adaptivekt.data.AdaptiveDataError(
        title = "Unable to load invoices",
        description = "There was a problem fetching invoice details.",
    )
}

@Composable
internal fun InvoicesScreen(initialState: InvoiceDemoState = InvoiceDemoState.Content) {
    var invoiceState by remember(initialState) { mutableStateOf(initialState.toAdaptiveDataState()) }
    val options = listOf("Content", "Empty", "Loading", "Error")
    val invoiceActions = listOf(
        AdaptiveDataAction<Invoice>(
            id = "open",
            label = "Open",
            priority = AdaptiveActionPriority.Primary,
            onClick = { },
        ),
        AdaptiveDataAction<Invoice>(
            id = "send",
            label = "Send",
            priority = AdaptiveActionPriority.Secondary,
            onClick = { },
        ),
        AdaptiveDataAction<Invoice>(
            id = "void",
            label = "Void",
            priority = AdaptiveActionPriority.Overflow,
            destructive = true,
            onClick = { },
        ),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveSectionHeader(title = "Invoices")
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        DemoText(
            text = "Billing states with content, empty, loading, and error defaults.",
            emphasis = Emphasis.Subtle,
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))
        Row(horizontalArrangement = Arrangement.spacedBy(AdaptiveTokens.Spacing.Small)) {
            options.forEach { label ->
                DemoToggleChip(
                    text = label,
                    selected = when (label) {
                        "Content" -> invoiceState is AdaptiveDataContent
                        "Empty" -> invoiceState is io.github.adaptivekt.data.AdaptiveDataEmpty
                        "Loading" -> invoiceState is io.github.adaptivekt.data.AdaptiveDataLoading
                        "Error" -> invoiceState is io.github.adaptivekt.data.AdaptiveDataError
                        else -> false
                    },
                    onClick = {
                        invoiceState = when (label) {
                            "Content" -> InvoiceDemoState.Content.toAdaptiveDataState()
                            "Empty" -> InvoiceDemoState.Empty.toAdaptiveDataState()
                            "Loading" -> InvoiceDemoState.Loading.toAdaptiveDataState()
                            "Error" -> InvoiceDemoState.Error.toAdaptiveDataState()
                            else -> InvoiceDemoState.Content.toAdaptiveDataState()
                        }
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))

        io.github.adaptivekt.data.AdaptiveDataView(
            state = invoiceState,
            columns = listOf(
                AdaptiveDataColumn(
                    id = "customer",
                    header = "Customer",
                    weight = 2f,
                ) { DemoText(text = it.customer, emphasis = Emphasis.Strong) },
                AdaptiveDataColumn(
                    id = "amount",
                    header = "Amount",
                    weight = 1f,
                ) { DemoText(text = it.amount, emphasis = Emphasis.Strong) },
                AdaptiveDataColumn(
                    id = "status",
                    header = "Status",
                    weight = 1f,
                ) { DemoStatusText(text = it.status) },
                AdaptiveDataColumn(
                    id = "date",
                    header = "Issue date",
                    weight = 1f,
                ) { DemoText(text = it.issueDate) },
            ),
            filterSlot = {
                DemoText(text = "Invoice status preview", emphasis = Emphasis.Subtle)
            },
            actions = {
                AdaptiveButton(
                    text = "Refresh",
                    trailingIcon = { AdaptiveIcons.Check(size = 16.dp, tint = Color.White) },
                    onClick = { invoiceState = AdaptiveDataContent(AdminDemoData.invoices) },
                )
            },
            rowActions = invoiceActions,
            onItemClick = { /* optional invoice selection */ },
        )
    }
}

@Composable
internal fun SettingsScreen() {
    var companyName by remember { mutableStateOf("AdaptiveKt Inc.") }
    var taxId by remember { mutableStateOf("123-456-789") }
    var companyEmail by remember { mutableStateOf("admin@adaptivekt.io") }
    var currency by remember { mutableStateOf("USD") }
    var language by remember { mutableStateOf("English") }

    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveSectionHeader(title = "Settings")
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Small))
        DemoText(
            text = "Company profile and workspace preferences.",
            emphasis = Emphasis.Subtle,
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(AdaptiveTokens.Spacing.Medium))

        AdaptiveFormLayout(
            columns = AdaptiveFormColumns(compact = 1, medium = 2, expanded = 2, large = 2),
            labelPosition = LabelPosition.Top,
        ) {
            section(title = "Company") {
                field(label = "Company name", span = FieldSpan.Full, required = true) {
                    AdaptiveTextField(value = companyName, onValueChange = { companyName = it }, placeholder = "Company name")
                }
                field(label = "Tax ID", span = FieldSpan.Half, validationMessage = AdaptiveValidationMessage("Tax ID is required", type = AdaptiveValidationMessageType.Warning)) {
                    AdaptiveTextField(value = taxId, onValueChange = { taxId = it }, placeholder = "Tax ID")
                }
                field(label = "Email", span = FieldSpan.Half, required = true) {
                    AdaptiveTextField(value = companyEmail, onValueChange = { companyEmail = it }, placeholder = "Email address")
                }
            }

            section(title = "Preferences") {
                field(label = "Default currency", span = FieldSpan.Half) {
                    AdaptiveTextField(value = currency, onValueChange = { currency = it }, placeholder = "Currency")
                }
                field(label = "Language", span = FieldSpan.Half) {
                    AdaptiveTextField(value = language, onValueChange = { language = it }, placeholder = "Language")
                }
            }

            actions {
                primary {
                    AdaptiveButton(
                        text = "Save changes",
                        leadingIcon = { AdaptiveIcons.Check(size = 16.dp, tint = Color.White) },
                        onClick = { /* no-op save */ },
                    )
                }
                secondary {
                    AdaptiveButton(text = "Reset", variant = AdaptiveButtonVariant.Secondary, onClick = {
                        companyName = "AdaptiveKt Inc."
                        taxId = "123-456-789"
                        companyEmail = "admin@adaptivekt.io"
                        currency = "USD"
                        language = "English"
                    })
                }
            }
        }
    }
}
