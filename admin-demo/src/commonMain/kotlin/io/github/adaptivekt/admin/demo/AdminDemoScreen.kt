package io.github.adaptivekt.admin.demo

public enum class AdminDemoScreen(public val id: String) {
    Dashboard("dashboard"),
    Employees("employees"),
    Products("products"),
    Invoices("invoices"),
    Settings("settings"),
    Components("components"),
    ComponentsButtons("components-buttons"),
    ComponentsBadges("components-badges"),
    ComponentsAvatars("components-avatars"),
    ComponentsCards("components-cards"),
    ComponentsDropdowns("components-dropdowns"),
    ComponentsFields("components-fields");

    public companion object {
        public fun fromId(id: String?): AdminDemoScreen = when (id?.lowercase()) {
            Dashboard.id -> Dashboard
            Employees.id -> Employees
            Products.id -> Products
            Invoices.id -> Invoices
            Settings.id -> Settings
            Components.id -> Components
            ComponentsButtons.id -> ComponentsButtons
            ComponentsBadges.id -> ComponentsBadges
            ComponentsAvatars.id -> ComponentsAvatars
            ComponentsCards.id -> ComponentsCards
            ComponentsDropdowns.id -> ComponentsDropdowns
            ComponentsFields.id -> ComponentsFields
            else -> Dashboard
        }
    }
}
