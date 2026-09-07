package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Invoice
import com.example.model.InvoiceStatus
import com.example.ui.components.ComptoirBadge
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun InvoiceDetailScreen(
    invoice: Invoice,
    onBack: () -> Unit,
    onProceedToPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = invoice.id,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
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

        Spacer(modifier = Modifier.height(16.dp))

        // Total Summary Card (Flat Surface)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgSurface),
            border = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Total facture",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatCurrency(invoice.total),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComptoirTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${invoice.itemCount} articles",
                    fontSize = 12.sp,
                    color = ComptoirTextTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Articles
        Text(
            text = "ARTICLES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = ComptoirTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Items Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                invoice.items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = item.product.icon,
                                fontSize = 22.sp
                            )

                            Column {
                                Text(
                                    text = item.product.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ComptoirTextPrimary
                                )
                                Text(
                                    text = "${formatCurrency(item.unitPrice)} × ${item.quantity}",
                                    fontSize = 12.sp,
                                    color = ComptoirTextSecondary
                                )
                            }
                        }

                        Text(
                            text = formatCurrency(item.total),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ComptoirTextPrimary
                        )
                    }

                    if (index < invoice.items.size - 1) {
                        HorizontalDivider(color = ComptoirBorderLight, thickness = 1.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ComptoirTextPrimary,
                    containerColor = ComptoirBgSurface
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Retour", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            if (invoice.status == InvoiceStatus.EN_COURS) {
                Button(
                    onClick = onProceedToPayment,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("pay_invoice_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ComptoirBgDark,
                        contentColor = ComptoirBgPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Encaisser", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
