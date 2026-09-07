package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Invoice
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun PaymentScreen(
    invoice: Invoice,
    onBack: () -> Unit,
    onMixedPaymentClick: () -> Unit,
    onConfirmPayment: (method: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMethod by remember { mutableStateOf("Espèces") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Encaissement",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )
        Text(
            text = "Facture ${invoice.id}",
            fontSize = 13.sp,
            color = ComptoirTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        // Amount Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgSurface),
            border = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Montant à encaisser",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatCurrency(invoice.total),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComptoirTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Moyen de paiement
        Text(
            text = "MOYEN DE PAIEMENT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = ComptoirTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        PaymentMethodOption(
            title = "Espèces",
            icon = Icons.Default.Money,
            iconTint = ComptoirAccentTeal,
            isSelected = selectedMethod == "Espèces",
            onClick = { selectedMethod = "Espèces" }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PaymentMethodOption(
            title = "Mobile Money",
            icon = Icons.Default.PhoneAndroid,
            iconTint = if (selectedMethod == "Mobile Money") ComptoirAccentTeal else ComptoirTextSecondary,
            isSelected = selectedMethod == "Mobile Money",
            onClick = { selectedMethod = "Mobile Money" }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PaymentMethodOption(
            title = "Carte Bancaire",
            icon = Icons.Default.CreditCard,
            iconTint = if (selectedMethod == "Carte Bancaire") ComptoirAccentTeal else ComptoirTextSecondary,
            isSelected = selectedMethod == "Carte Bancaire",
            onClick = { selectedMethod = "Carte Bancaire" }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Actions
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
                    containerColor = ComptoirBgSurface,
                    contentColor = ComptoirTextPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
            ) {
                Text(text = "Retour", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = onMixedPaymentClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("mixed_payment_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = ComptoirBgSurface,
                    contentColor = ComptoirTextPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
            ) {
                Icon(
                    imageVector = Icons.Default.CallSplit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Mixte", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onConfirmPayment(selectedMethod) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("confirm_payment_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ComptoirAccentTeal,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Valider l'encaissement",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PaymentMethodOption(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ComptoirBgSurface else ComptoirBgCard
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isSelected) ComptoirTextPrimary else ComptoirBorderLight
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ComptoirBgSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = ComptoirTextPrimary,
                modifier = Modifier.weight(1f)
            )

            // Radio Indicator
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (isSelected) ComptoirTextPrimary else ComptoirBorderMedium,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(ComptoirTextPrimary)
                    )
                }
            }
        }
    }
}
