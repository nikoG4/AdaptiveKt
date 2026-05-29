package io.github.adaptivekt.admin.demo

public enum class AdminDemoScreen(public val id: String) {
    Dashboard("dashboard"),
    Employees("employees"),
    Products("products"),
    Invoices("invoices"),
    InvoicesEmpty("invoices-empty"),
    InvoicesLoading("invoices-loading"),
    InvoicesError("invoices-error"),
    Settings("settings"),
    Components("components"),
    ComponentsButtons("components-buttons"),
    ComponentsBadges("components-badges"),
    ComponentsAvatars("components-avatars"),
    ComponentsCards("components-cards"),
    ComponentsDropdowns("components-dropdowns"),
    ComponentsFields("components-fields"),
    ComponentsSelects("components-selects"),
    ComponentsSelectsOpen("components-selects-open");

    public companion object {
        public fun fromId(id: String?): AdminDemoScreen = when (id?.lowercase()) {
            Dashboard.id -> Dashboard
            Employees.id -> Employees
            Products.id -> Products
            Invoices.id -> Invoices
            InvoicesEmpty.id -> InvoicesEmpty
            InvoicesLoading.id -> InvoicesLoading
            InvoicesError.id -> InvoicesError
            Settings.id -> Settings
            Components.id -> Components
            ComponentsButtons.id -> ComponentsButtons
            ComponentsBadges.id -> ComponentsBadges
            ComponentsAvatars.id -> ComponentsAvatars
            ComponentsCards.id -> ComponentsCards
            ComponentsDropdowns.id -> ComponentsDropdowns
            ComponentsFields.id -> ComponentsFields
            ComponentsSelects.id -> ComponentsSelects
            ComponentsSelectsOpen.id -> ComponentsSelectsOpen
            else -> Dashboard
        }
    }
}
