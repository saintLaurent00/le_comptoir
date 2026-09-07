package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ComptoirViewModel : ViewModel() {

    // --- Navigation & Backstack ---
    private val _currentScreen = MutableStateFlow(Screen.LOGIN)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val backStack = mutableListOf(Screen.LOGIN)

    private val _selectedTab = MutableStateFlow(BottomTab.DASHBOARD)
    val selectedTab: StateFlow<BottomTab> = _selectedTab.asStateFlow()

    fun navigateTo(screen: Screen) {
        if (screen == Screen.LOGIN) {
            backStack.clear()
            backStack.add(Screen.LOGIN)
            _currentScreen.value = Screen.LOGIN
            return
        }

        // Sync tab if it's one of main screens
        when (screen) {
            Screen.DASHBOARD -> _selectedTab.value = BottomTab.DASHBOARD
            Screen.INVOICES, Screen.NEW_INVOICE, Screen.INVOICE_DETAIL, Screen.PAYMENT, Screen.MIXED_PAYMENT ->
                _selectedTab.value = BottomTab.INVOICES
            Screen.STOCK, Screen.STOCK_MOVEMENT, Screen.PRODUCTS ->
                _selectedTab.value = BottomTab.STOCK
            Screen.CASH_REGISTER, Screen.CASH_CLOSE ->
                _selectedTab.value = BottomTab.CASH_REGISTER
            Screen.MORE, Screen.REPORTS, Screen.PROFILE ->
                _selectedTab.value = BottomTab.MORE
            else -> {}
        }

        if (backStack.lastOrNull() != screen) {
            backStack.add(screen)
        }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
            val previous = backStack.last()
            _currentScreen.value = previous
            return true
        }
        return false
    }

    fun switchTab(tab: BottomTab) {
        _selectedTab.value = tab
        when (tab) {
            BottomTab.DASHBOARD -> navigateTo(Screen.DASHBOARD)
            BottomTab.INVOICES -> navigateTo(Screen.INVOICES)
            BottomTab.STOCK -> navigateTo(Screen.STOCK)
            BottomTab.CASH_REGISTER -> navigateTo(Screen.CASH_REGISTER)
            BottomTab.MORE -> navigateTo(Screen.MORE)
        }
    }

    // --- Toast Notifications ---
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun showToast(message: String) {
        viewModelScope.launch {
            _toastMessage.value = message
            delay(2200)
            if (_toastMessage.value == message) {
                _toastMessage.value = null
            }
        }
    }

    // --- Products Catalogue ---
    private val _products = MutableStateFlow(
        listOf(
            Product("1", "Drapeau 65cl", 1500, "🍺", "Bières", stockQuantity = 4, minThreshold = 24),
            Product("2", "Castel 65cl", 1500, "🍺", "Bières", stockQuantity = 36, minThreshold = 20),
            Product("3", "33 Export", 1500, "🍺", "Bières", stockQuantity = 42, minThreshold = 20),
            Product("4", "Castel Wine Rouge", 4500, "🍷", "Vins", stockQuantity = 2, minThreshold = 12),
            Product("5", "Whisky Local", 3000, "🥃", "Spiritueux", stockQuantity = 18, minThreshold = 10),
            Product("6", "Cocktail Cave", 8000, "🍹", "Cocktails", stockQuantity = 32, minThreshold = 15),
            Product("7", "Poulet entier", 4000, "🍗", "Plats", stockQuantity = 8, minThreshold = 15),
            Product("8", "Braise de poulet", 5000, "🍗", "Plats", stockQuantity = 8, minThreshold = 10),
            Product("9", "Alloco poisson", 3500, "🐟", "Plats", stockQuantity = 14, minThreshold = 8),
            Product("10", "Kédjenou", 6000, "🍲", "Plats", stockQuantity = 10, minThreshold = 6)
        )
    )
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun restockProduct(productId: String, amount: Int = 24) {
        val updated = _products.value.map { prod ->
            if (prod.id == productId) {
                val newQty = prod.stockQuantity + amount
                prod.copy(stockQuantity = newQty)
            } else prod
        }
        _products.value = updated

        val product = updated.find { it.id == productId }
        if (product != null) {
            val nowTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            addStockMovement(
                StockMovement(
                    id = UUID.randomUUID().toString(),
                    title = "Réappro ${product.name}",
                    subtitle = "Aujourd'hui · $nowTime",
                    type = MovementType.ENTREE,
                    quantityChange = amount,
                    time = nowTime
                )
            )
            showToast("✅ +$amount ${product.name} réapprovisionné")
        }
    }

    // --- Invoices ---
    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())
    val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    private val _selectedInvoice = MutableStateFlow<Invoice?>(null)
    val selectedInvoice: StateFlow<Invoice?> = _selectedInvoice.asStateFlow()

    private val _invoiceFilter = MutableStateFlow(InvoicesFilter.ALL)
    val invoiceFilter: StateFlow<InvoicesFilter> = _invoiceFilter.asStateFlow()

    fun setInvoiceFilter(filter: InvoicesFilter) {
        _invoiceFilter.value = filter
    }

    fun selectInvoice(invoice: Invoice) {
        _selectedInvoice.value = invoice
        navigateTo(Screen.INVOICE_DETAIL)
    }

    // --- Draft Invoice (New Invoice Screen) ---
    private val _draftItems = MutableStateFlow<List<InvoiceItem>>(emptyList())
    val draftItems: StateFlow<List<InvoiceItem>> = _draftItems.asStateFlow()

    fun addToDraft(product: Product) {
        val current = _draftItems.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(InvoiceItem(product = product, quantity = 1, unitPrice = product.price))
        }
        _draftItems.value = current
        showToast("✅ ${product.name} ajouté")
    }

    fun decrementDraftItem(product: Product) {
        val current = _draftItems.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            if (existing.quantity > 1) {
                current[existingIndex] = existing.copy(quantity = existing.quantity - 1)
            } else {
                current.removeAt(existingIndex)
            }
            _draftItems.value = current
        }
    }

    fun clearDraft() {
        _draftItems.value = emptyList()
    }

    fun finalizeNewInvoice(clientName: String = "Comptoir") {
        val items = _draftItems.value
        if (items.isEmpty()) {
            showToast("⚠️ La facture est vide")
            return
        }

        val invoiceNumber = "F2026-%04d".format(_invoices.value.size + 143)
        val nowTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val newInvoice = Invoice(
            id = "#$invoiceNumber",
            time = "$nowTime · ${items.sumOf { it.quantity }} articles",
            client = clientName,
            items = items,
            status = InvoiceStatus.EN_COURS
        )

        _invoices.value = listOf(newInvoice) + _invoices.value

        // Deduct from product stock & add movements
        val currentProducts = _products.value.toMutableList()
        items.forEach { item ->
            val idx = currentProducts.indexOfFirst { it.id == item.product.id }
            if (idx >= 0) {
                val p = currentProducts[idx]
                val newQty = (p.stockQuantity - item.quantity).coerceAtLeast(0)
                currentProducts[idx] = p.copy(stockQuantity = newQty)
            }
            addStockMovement(
                StockMovement(
                    id = UUID.randomUUID().toString(),
                    title = "Vente #${invoiceNumber}",
                    subtitle = "Aujourd'hui · $nowTime",
                    type = MovementType.SORTIE,
                    quantityChange = -item.quantity,
                    time = nowTime
                )
            )
        }
        _products.value = currentProducts

        clearDraft()
        showToast("✅ Facture créée")
        navigateTo(Screen.INVOICES)
    }

    // --- Payments ---
    fun payInvoice(
        invoiceId: String,
        method: String,
        cash: Int = 0,
        mobileMoney: Int = 0,
        card: Int = 0
    ) {
        val list = _invoices.value.map { inv ->
            if (inv.id == invoiceId) {
                inv.copy(
                    status = InvoiceStatus.PAYEE,
                    paymentMethod = method,
                    cashAmount = cash,
                    mobileMoneyAmount = mobileMoney,
                    cardAmount = card
                )
            } else inv
        }
        _invoices.value = list

        val paidInvoice = list.find { it.id == invoiceId }
        _selectedInvoice.value = paidInvoice

        // Update Cash Session
        if (paidInvoice != null) {
            val total = paidInvoice.total
            val current = _cashState.value
            _cashState.value = current.copy(
                currentTotal = current.currentTotal + total,
                cashAmount = current.cashAmount + (if (method == "Espèces") total else cash),
                mobileMoneyAmount = current.mobileMoneyAmount + (if (method == "Mobile Money") total else mobileMoney),
                transactionCount = current.transactionCount + 1
            )
        }

        showToast("✅ Paiement validé")
        navigateTo(Screen.INVOICES)
    }

    // --- Stock Movements ---
    private val _movements = MutableStateFlow<List<StockMovement>>(emptyList())
    val movements: StateFlow<List<StockMovement>> = _movements.asStateFlow()

    private val _movementFilter = MutableStateFlow(StockMovementFilter.ALL)
    val movementFilter: StateFlow<StockMovementFilter> = _movementFilter.asStateFlow()

    fun setMovementFilter(filter: StockMovementFilter) {
        _movementFilter.value = filter
    }

    fun addStockMovement(movement: StockMovement) {
        _movements.value = listOf(movement) + _movements.value
    }

    // --- Cash Register ---
    private val _cashState = MutableStateFlow(CashState())
    val cashState: StateFlow<CashState> = _cashState.asStateFlow()

    fun closeCash(countedAmount: Int, justification: String) {
        val current = _cashState.value
        val expected = current.currentTotal
        val diff = countedAmount - expected
        _cashState.value = current.copy(
            isClosed = true,
            countedAmount = countedAmount,
            discrepancy = diff,
            justification = justification
        )
        showToast("✅ Caisse clôturée")
        navigateTo(Screen.DASHBOARD)
    }

    // --- Reports Period ---
    private val _reportPeriod = MutableStateFlow(ReportPeriod.DAY)
    val reportPeriod: StateFlow<ReportPeriod> = _reportPeriod.asStateFlow()

    fun setReportPeriod(period: ReportPeriod) {
        _reportPeriod.value = period
    }

    // --- Search & Category Filtering ---
    private val _productSearchQuery = MutableStateFlow("")
    val productSearchQuery: StateFlow<String> = _productSearchQuery.asStateFlow()

    fun setProductSearchQuery(query: String) {
        _productSearchQuery.value = query
    }

    private val _selectedCategory = MutableStateFlow("Tout")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    init {
        // Initialize default initial data matching the HTML mockup exactly
        val drapeau = _products.value.first { it.id == "1" }
        val braise = _products.value.first { it.id == "8" }
        val alloco = _products.value.first { it.id == "9" }
        val cocktail = _products.value.first { it.id == "6" }
        val vin = _products.value.first { it.id == "4" }
        val export = _products.value.first { it.id == "3" }
        val kedjenou = _products.value.first { it.id == "10" }
        val castel = _products.value.first { it.id == "2" }

        val initialInvoices = listOf(
            Invoice(
                id = "#F2026-0142",
                time = "12:45 · 4 articles",
                client = "Comptoir",
                items = listOf(
                    InvoiceItem(drapeau, 2, 1500),
                    InvoiceItem(braise, 1, 5000),
                    InvoiceItem(alloco, 1, 3500),
                    InvoiceItem(cocktail, 2, 8000)
                ),
                status = InvoiceStatus.EN_COURS
            ),
            Invoice(
                id = "#F2026-0141",
                time = "11:20 · 2 articles",
                client = "Groupe VIP",
                items = listOf(
                    InvoiceItem(vin, 2, 4500),
                    InvoiceItem(alloco, 1, 3500)
                ),
                status = InvoiceStatus.PAYEE,
                paymentMethod = "Mobile Money",
                mobileMoneyAmount = 12500
            ),
            Invoice(
                id = "#F2026-0140",
                time = "10:05 · 6 articles",
                client = "Table 4",
                items = listOf(
                    InvoiceItem(cocktail, 4, 8000),
                    InvoiceItem(braise, 2, 5000),
                    InvoiceItem(export, 2, 1500)
                ),
                status = InvoiceStatus.EN_COURS
            ),
            Invoice(
                id = "#F2026-0139",
                time = "09:30 · 3 articles",
                client = "Comptoir",
                items = listOf(
                    InvoiceItem(drapeau, 6, 1500),
                    InvoiceItem(kedjenou, 1, 6000),
                    InvoiceItem(castel, 2, 1500)
                ),
                status = InvoiceStatus.PAYEE,
                paymentMethod = "Espèces",
                cashAmount = 18000
            )
        )
        _invoices.value = initialInvoices

        _movements.value = listOf(
            StockMovement("m1", "Réappro Drapeau 65cl", "Aujourd'hui · 14:23", MovementType.ENTREE, 48, "14:23"),
            StockMovement("m2", "Vente #F2026-0142", "Aujourd'hui · 13:10", MovementType.SORTIE, -6, "13:10"),
            StockMovement("m3", "Perte — Bouteille cassée", "Hier · 22:45", MovementType.PERTE, -1, "22:45"),
            StockMovement("m4", "Livraison fournisseur", "Hier · 10:00", MovementType.ENTREE, 24, "10:00")
        )
    }
}
