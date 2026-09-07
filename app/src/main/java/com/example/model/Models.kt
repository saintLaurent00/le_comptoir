package com.example.model

enum class Screen(val title: String) {
    LOGIN("Connexion"),
    DASHBOARD("Tableau de bord"),
    INVOICES("Factures"),
    NEW_INVOICE("Nouvelle facture"),
    INVOICE_DETAIL("Détail facture"),
    PAYMENT("Encaissement"),
    MIXED_PAYMENT("Paiement Mixte"),
    STOCK("Stock"),
    STOCK_MOVEMENT("Mouvements"),
    PRODUCTS("Produits"),
    CASH_REGISTER("Caisse"),
    CASH_CLOSE("Clôture"),
    MORE("Menu"),
    REPORTS("Rapports"),
    PROFILE("Profil")
}

enum class BottomTab {
    DASHBOARD,
    INVOICES,
    STOCK,
    CASH_REGISTER,
    MORE
}

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val icon: String,
    val category: String,
    val stockQuantity: Int,
    val minThreshold: Int
)

data class InvoiceItem(
    val product: Product,
    val quantity: Int,
    val unitPrice: Int
) {
    val total: Int get() = quantity * unitPrice
}

enum class InvoiceStatus(val label: String) {
    EN_COURS("En cours"),
    PAYEE("Payée"),
    ANNULEE("Annulée")
}

data class Invoice(
    val id: String,
    val time: String,
    val client: String,
    val items: List<InvoiceItem>,
    val status: InvoiceStatus,
    val paymentMethod: String? = null,
    val cashAmount: Int = 0,
    val mobileMoneyAmount: Int = 0,
    val cardAmount: Int = 0
) {
    val total: Int get() = items.sumOf { it.total }
    val itemCount: Int get() = items.sumOf { it.quantity }
}

enum class MovementType(val label: String) {
    ENTREE("Entrée"),
    SORTIE("Sortie"),
    PERTE("Perte")
}

data class StockMovement(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: MovementType,
    val quantityChange: Int,
    val time: String
)

data class CashState(
    val openingTime: String = "09:00",
    val cashier: String = "Admin",
    val floatAmount: Int = 50_000,
    val currentTotal: Int = 450_000,
    val cashAmount: Int = 280_000,
    val mobileMoneyAmount: Int = 170_000,
    val transactionCount: Int = 24,
    val isClosed: Boolean = false,
    val countedAmount: Int? = null,
    val discrepancy: Int? = null,
    val justification: String = ""
)

enum class InvoicesFilter(val label: String) {
    ALL("Toutes"),
    PENDING("En cours"),
    PAID("Payées"),
    TODAY("Aujourd'hui")
}

enum class StockMovementFilter(val label: String) {
    ALL("Tout"),
    IN("Entrées"),
    OUT("Sorties"),
    LOSS("Pertes")
}

enum class ReportPeriod(val label: String) {
    DAY("Jour"),
    WEEK("Semaine"),
    MONTH("Mois"),
    YEAR("Année")
}
