package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.*
import com.example.ui.components.ComptoirBottomNav
import com.example.ui.components.ComptoirToast
import com.example.ui.components.ComptoirTopBar
import com.example.ui.screens.*
import com.example.ui.theme.ComptoirBgPrimary
import com.example.ui.theme.ComptoirBorderLight
import com.example.ui.theme.ComptoirTextPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ComptoirViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ComptoirApp()
            }
        }
    }
}

@Composable
fun ComptoirApp(viewModel: ComptoirViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    val products by viewModel.products.collectAsStateWithLifecycle()
    val invoices by viewModel.invoices.collectAsStateWithLifecycle()
    val selectedInvoice by viewModel.selectedInvoice.collectAsStateWithLifecycle()
    val invoiceFilter by viewModel.invoiceFilter.collectAsStateWithLifecycle()
    val draftItems by viewModel.draftItems.collectAsStateWithLifecycle()
    val movements by viewModel.movements.collectAsStateWithLifecycle()
    val movementFilter by viewModel.movementFilter.collectAsStateWithLifecycle()
    val cashState by viewModel.cashState.collectAsStateWithLifecycle()
    val reportPeriod by viewModel.reportPeriod.collectAsStateWithLifecycle()

    var showAddProductDialog by remember { mutableStateOf(false) }

    // Intercept back button
    BackHandler(enabled = currentScreen != Screen.LOGIN && currentScreen != Screen.DASHBOARD) {
        if (!viewModel.navigateBack()) {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ComptoirBgPrimary,
        topBar = {
            if (currentScreen != Screen.LOGIN) {
                ComptoirTopBar(
                    currentScreen = currentScreen,
                    onBackClick = { viewModel.navigateBack() },
                    onNotificationClick = { viewModel.showToast("🔔 Aucune nouvelle notification") },
                    onProfileClick = { viewModel.navigateTo(Screen.PROFILE) },
                    modifier = Modifier.statusBarsPadding()
                )
            }
        },
        bottomBar = {
            if (currentScreen != Screen.LOGIN) {
                ComptoirBottomNav(
                    selectedTab = selectedTab,
                    onTabSelected = { tab -> viewModel.switchTab(tab) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main content based on currentScreen
            AnimatedContent(
                targetState = currentScreen,
                label = "ScreenTransition",
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.LOGIN -> LoginScreen(
                        onLoginSuccess = {
                            viewModel.navigateTo(Screen.DASHBOARD)
                            viewModel.showToast("👋 Bienvenue sur Le Comptoir")
                        },
                        onForgotPassword = {
                            viewModel.showToast("🔑 Contactez l'administrateur système")
                        }
                    )
                    Screen.DASHBOARD -> DashboardScreen(
                        cashState = cashState,
                        invoices = invoices,
                        onSelectInvoice = { invoice -> viewModel.selectInvoice(invoice) },
                        onNavigateToStock = { viewModel.navigateTo(Screen.STOCK) }
                    )
                    Screen.INVOICES -> InvoicesScreen(
                        invoices = invoices,
                        selectedFilter = invoiceFilter,
                        onFilterChange = { filter -> viewModel.setInvoiceFilter(filter) },
                        onNewInvoiceClick = { viewModel.navigateTo(Screen.NEW_INVOICE) },
                        onSelectInvoice = { invoice -> viewModel.selectInvoice(invoice) }
                    )
                    Screen.NEW_INVOICE -> NewInvoiceScreen(
                        products = products,
                        draftItems = draftItems,
                        onAddToCart = { product -> viewModel.addToDraft(product) },
                        onDecrementFromCart = { product -> viewModel.decrementDraftItem(product) },
                        onValidateInvoice = { viewModel.finalizeNewInvoice() }
                    )
                    Screen.INVOICE_DETAIL -> {
                        val invoice = selectedInvoice ?: invoices.firstOrNull()
                        if (invoice != null) {
                            InvoiceDetailScreen(
                                invoice = invoice,
                                onBack = { viewModel.navigateBack() },
                                onProceedToPayment = { viewModel.navigateTo(Screen.PAYMENT) }
                            )
                        } else {
                            viewModel.navigateTo(Screen.INVOICES)
                        }
                    }
                    Screen.PAYMENT -> {
                        val invoice = selectedInvoice ?: invoices.firstOrNull()
                        if (invoice != null) {
                            PaymentScreen(
                                invoice = invoice,
                                onBack = { viewModel.navigateBack() },
                                onMixedPaymentClick = { viewModel.navigateTo(Screen.MIXED_PAYMENT) },
                                onConfirmPayment = { method ->
                                    viewModel.payInvoice(invoice.id, method)
                                }
                            )
                        } else {
                            viewModel.navigateTo(Screen.INVOICES)
                        }
                    }
                    Screen.MIXED_PAYMENT -> {
                        val invoice = selectedInvoice ?: invoices.firstOrNull()
                        if (invoice != null) {
                            MixedPaymentScreen(
                                invoice = invoice,
                                onBack = { viewModel.navigateBack() },
                                onConfirmMixedPayment = { cash, mobileMoney ->
                                    viewModel.payInvoice(
                                        invoiceId = invoice.id,
                                        method = "Mixte",
                                        cash = cash,
                                        mobileMoney = mobileMoney
                                    )
                                }
                            )
                        } else {
                            viewModel.navigateTo(Screen.INVOICES)
                        }
                    }
                    Screen.STOCK -> StockScreen(
                        products = products,
                        onRestock = { productId -> viewModel.restockProduct(productId) },
                        onViewMovements = { viewModel.navigateTo(Screen.STOCK_MOVEMENT) }
                    )
                    Screen.STOCK_MOVEMENT -> StockMovementScreen(
                        movements = movements,
                        selectedFilter = movementFilter,
                        onFilterChange = { filter -> viewModel.setMovementFilter(filter) }
                    )
                    Screen.PRODUCTS -> ProductsScreen(
                        products = products,
                        onAddProductClick = { showAddProductDialog = true },
                        onRestockProduct = { productId -> viewModel.restockProduct(productId) }
                    )
                    Screen.CASH_REGISTER -> CashRegisterScreen(
                        cashState = cashState,
                        onCloseCashClick = { viewModel.navigateTo(Screen.CASH_CLOSE) }
                    )
                    Screen.CASH_CLOSE -> CashCloseScreen(
                        cashState = cashState,
                        onConfirmClose = { counted, reason ->
                            viewModel.closeCash(counted, reason)
                        }
                    )
                    Screen.MORE -> MoreScreen(
                        onNavigateToReports = { viewModel.navigateTo(Screen.REPORTS) },
                        onNavigateToProducts = { viewModel.navigateTo(Screen.PRODUCTS) },
                        onNavigateToStock = { viewModel.navigateTo(Screen.STOCK) },
                        onNavigateToPersonnel = { viewModel.showToast("👥 3 serveurs en service") },
                        onNavigateToProfile = { viewModel.navigateTo(Screen.PROFILE) },
                        onLogout = { viewModel.navigateTo(Screen.LOGIN) }
                    )
                    Screen.REPORTS -> ReportsScreen(
                        selectedPeriod = reportPeriod,
                        onPeriodChange = { period -> viewModel.setReportPeriod(period) }
                    )
                    Screen.PROFILE -> ProfileScreen(
                        onOptionClick = { option -> viewModel.showToast("⚙️ $option") }
                    )
                }
            }

            // Floating Toast notification
            ComptoirToast(
                message = toastMessage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            )
        }
    }

    // Add Product Dialog
    if (showAddProductDialog) {
        var newName by remember { mutableStateOf("") }
        var newPrice by remember { mutableStateOf("") }
        var newCategory by remember { mutableStateOf("Bières") }

        AlertDialog(
            onDismissRequest = { showAddProductDialog = false },
            title = {
                Text(
                    text = "Ajouter un produit",
                    color = ComptoirTextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nom du produit") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPrice,
                        onValueChange = { newPrice = it },
                        label = { Text("Prix (F CFA)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val price = newPrice.toIntOrNull() ?: 1500
                        if (newName.isNotBlank()) {
                            viewModel.showToast("✅ Produit '$newName' créé")
                            showAddProductDialog = false
                        }
                    }
                ) {
                    Text("Ajouter", color = ComptoirTextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProductDialog = false }) {
                    Text("Annuler", color = ComptoirTextPrimary)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
