package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Invoice
import com.example.model.InvoiceStatus
import com.example.model.InvoicesFilter
import com.example.ui.components.ComptoirBadge
import com.example.ui.components.FilterPill
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun InvoicesScreen(
    invoices: List<Invoice>,
    selectedFilter: InvoicesFilter,
    onFilterChange: (InvoicesFilter) -> Unit,
    onNewInvoiceClick: () -> Unit,
    onSelectInvoice: (Invoice) -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingCount = invoices.count { it.status == InvoiceStatus.EN_COURS }
    val paidCount = invoices.count { it.status == InvoiceStatus.PAYEE }

    val filteredInvoices = when (selectedFilter) {
        InvoicesFilter.ALL -> invoices
        InvoicesFilter.PENDING -> invoices.filter { it.status == InvoiceStatus.EN_COURS }
        InvoicesFilter.PAID -> invoices.filter { it.status == InvoiceStatus.PAYEE }
        InvoicesFilter.TODAY -> invoices
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Title & New Invoice Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Factures",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ComptoirTextPrimary
            )

            Button(
                onClick = onNewInvoiceClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ComptoirBgDark,
                    contentColor = ComptoirBgPrimary
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                modifier = Modifier.testTag("new_invoice_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nouvelle",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Text(
            text = "$pendingCount en cours · $paidCount clôturées",
            fontSize = 13.sp,
            color = ComptoirTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        // Filter Pills
        val pillsScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(pillsScroll)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InvoicesFilter.values().forEach { filter ->
                FilterPill(
                    label = filter.label,
                    isSelected = filter == selectedFilter,
                    onClick = { onFilterChange(filter) }
                )
            }
        }

        // Invoice Cards List
        if (filteredInvoices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aucune facture trouvée",
                    color = ComptoirTextTertiary,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredInvoices, key = { it.id }) { invoice ->
                    InvoiceCard(
                        invoice = invoice,
                        onClick = { onSelectInvoice(invoice) }
                    )
                }
            }
        }
    }
}

@Composable
fun InvoiceCard(
    invoice: Invoice,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("invoice_card_${invoice.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = invoice.id,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ComptoirTextPrimary
                    )
                    Text(
                        text = invoice.time,
                        fontSize = 12.sp,
                        color = ComptoirTextSecondary
                    )
                }

                if (invoice.status == InvoiceStatus.EN_COURS) {
                    ComptoirBadge(
                        text = "En cours",
                        backgroundColor = ComptoirAccentGoldLight,
                        textColor = ComptoirAccentGold
                    )
                } else {
                    ComptoirBadge(
                        text = "Payée",
                        backgroundColor = ComptoirAccentTealLight,
                        textColor = ComptoirAccentTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Client: ${invoice.client}",
                    fontSize = 12.sp,
                    color = ComptoirTextSecondary
                )
                Text(
                    text = formatCurrency(invoice.total),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (invoice.status == InvoiceStatus.EN_COURS) ComptoirAccentGold else ComptoirAccentTeal
                )
            }
        }
    }
}
