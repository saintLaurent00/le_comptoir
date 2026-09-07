package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CashState
import com.example.model.Invoice
import com.example.model.InvoiceStatus
import com.example.ui.components.ComptoirBadge
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    cashState: CashState,
    invoices: List<Invoice>,
    onSelectInvoice: (Invoice) -> Unit,
    onNavigateToStock: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingInvoicesCount = invoices.count { it.status == InvoiceStatus.EN_COURS }
    val paidInvoicesCount = invoices.count { it.status == InvoiceStatus.PAYEE }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Welcome Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Bonjour, Admin 👋",
                    fontSize = 14.sp,
                    color = ComptoirTextSecondary
                )
                Text(
                    text = "Tableau de bord",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComptoirTextPrimary
                )
            }

            ComptoirBadge(
                text = "En ligne",
                backgroundColor = ComptoirAccentTealLight,
                textColor = ComptoirAccentTeal,
                showDot = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // KPI Hero Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CA du jour",
                        fontSize = 12.sp,
                        color = ComptoirTextTertiary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = ComptoirAccentTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "+12%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ComptoirAccentTeal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Big Formatted Amount (e.g. 450 000 F)
                val annotatedString = buildAnnotatedString {
                    append("450 ")
                    withStyle(style = SpanStyle(color = ComptoirAccentGold)) {
                        append("000 F")
                    }
                }
                Text(
                    text = annotatedString,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    color = ComptoirBgPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MetaStatItem(count = "${invoices.size}", label = "factures")
                    MetaStatItem(count = "$paidInvoicesCount", label = "encaissées")
                    MetaStatItem(count = "3", label = "alertes")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid-2 Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                label = "FACTURES EN COURS",
                value = "$pendingInvoicesCount",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "ENCAISSÉ DU JOUR",
                value = "184 000 F",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Activité récente
        Text(
            text = "ACTIVITÉ RÉCENTE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = ComptoirTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Item 1: Invoice #F2026-0142
                val inv1 = invoices.find { it.id == "#F2026-0142" } ?: invoices.firstOrNull()
                ActivityRow(
                    icon = Icons.Default.Receipt,
                    iconTint = ComptoirAccentGold,
                    title = inv1?.id ?: "Facture #F2026-0142",
                    subtitle = "Encaissement en cours",
                    trailingText = inv1?.let { formatCurrency(it.total) } ?: "35 000 F",
                    trailingColor = ComptoirAccentGold,
                    onClick = { inv1?.let(onSelectInvoice) }
                )

                HorizontalDivider(color = ComptoirBorderLight, thickness = 1.dp)

                // Item 2: Invoice #F2026-0140 (paid)
                val inv2 = invoices.find { it.id == "#F2026-0141" } ?: invoices.getOrNull(1)
                ActivityRow(
                    icon = Icons.Default.CheckCircle,
                    iconTint = ComptoirAccentTeal,
                    title = inv2?.id ?: "Facture #F2026-0140",
                    subtitle = "Payée par Mobile Money",
                    trailingText = inv2?.let { formatCurrency(it.total) } ?: "12 500 F",
                    trailingColor = ComptoirAccentTeal,
                    onClick = { inv2?.let(onSelectInvoice) }
                )

                HorizontalDivider(color = ComptoirBorderLight, thickness = 1.dp)

                // Item 3: Stock alert
                ActivityRow(
                    icon = Icons.Default.Inventory2,
                    iconTint = ComptoirAccentRed,
                    title = "Stock bas",
                    subtitle = "Drapeau 65cl — 4 restants",
                    trailingBadge = {
                        ComptoirBadge(
                            text = "Alerte",
                            backgroundColor = ComptoirAccentRedLight,
                            textColor = ComptoirAccentRed
                        )
                    },
                    onClick = onNavigateToStock
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MetaStatItem(count: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = count,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirBgPrimary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = ComptoirTextTertiary
        )
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = ComptoirTextPrimary
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                color = ComptoirTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

@Composable
private fun ActivityRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    trailingText: String? = null,
    trailingColor: Color = ComptoirTextPrimary,
    trailingBadge: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ComptoirBgSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = ComptoirTextSecondary
                )
            }
        }

        if (trailingBadge != null) {
            trailingBadge()
        } else if (trailingText != null) {
            Text(
                text = trailingText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = trailingColor
            )
        }
    }
}
