package io.github.adaptivekt.admin.demo

import androidx.compose.ui.graphics.Color

public data class Employee(
    val id: String,
    val avatarColor: Color,
    val name: String,
    val role: String,
    val department: String,
    val status: String,
    val email: String,
)

public data class Product(
    val id: String,
    val thumbnailColor: Color,
    val name: String,
    val sku: String,
    val stock: Int,
    val price: String,
    val status: String,
)

public data class Invoice(
    val id: String,
    val customer: String,
    val amount: String,
    val status: String,
    val issueDate: String,
)

public object AdminDemoData {
    public val employees: List<Employee> = listOf(
        Employee("e1", Color(0xFFE8F1FF), "Alicia Romero", "Product Manager", "Strategy", "Active", "alicia@adaptivekt.io"),
        Employee("e2", Color(0xFFEFF6FF), "David Chen", "Software Engineer", "Platform", "Active", "david@adaptivekt.io"),
        Employee("e3", Color(0xFFFFF7ED), "Marta Silva", "QA Lead", "Quality", "On leave", "marta@adaptivekt.io"),
        Employee("e4", Color(0xFFECFDF5), "Noah Kim", "UX Designer", "Design", "Active", "noah@adaptivekt.io"),
    )

    public val products: List<Product> = listOf(
        Product("p1", Color(0xFFE8F1FF), "Adaptive Panel", "APT-210", 84, "$129", "In stock"),
        Product("p2", Color(0xFFFFF7ED), "Responsive Grid Kit", "RGK-430", 34, "$249", "Low stock"),
        Product("p3", Color(0xFFECFDF5), "Form Builder", "FB-105", 16, "$99", "In stock"),
        Product("p4", Color(0xFFFEF2F2), "Data Table Pro", "DTP-670", 0, "$179", "Out of stock"),
    )

    public val invoices: List<Invoice> = listOf(
        Invoice("i1", "Acme Corp", "$12,500", "Paid", "2026-05-03"),
        Invoice("i2", "Beta Ltd.", "$4,200", "Pending", "2026-05-08"),
        Invoice("i3", "Delta Group", "$7,980", "Overdue", "2026-04-27"),
        Invoice("i4", "Lambda LLC", "$1,600", "Draft", "2026-05-12"),
    )

    public val dashboardMetrics: List<DashboardMetric> = listOf(
        DashboardMetric("Total employees", "124", "Team size across departments"),
        DashboardMetric("Active products", "42", "Live catalog items"),
        DashboardMetric("Pending invoices", "8", "Awaiting payment"),
        DashboardMetric("Monthly revenue", "$72,300", "Current month total"),
    )
}

public data class DashboardMetric(
    val title: String,
    val value: String,
    val subtitle: String,
)
