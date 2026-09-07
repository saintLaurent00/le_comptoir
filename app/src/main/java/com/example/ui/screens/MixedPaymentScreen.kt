package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Invoice
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun MixedPaymentScreen(
    invoice: Invoice,
    onBack: () -> Unit,
    onConfirmMixedPayment: (cash: Int, mobileMoney: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val total = invoice.total
    var cashText by remember { mutableStateOf((total / 2).toString()) }
    var mobileMoneyText by remember { mutableStateOf((total - total / 2).toString()) }

    val cashAmount = cashText.toIntOrNull() ?: 0
    val mobileAmount = mobileMoneyText.toIntOrNull() ?: 0
    val remaining = total - (cashAmount + mobileAmount)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Paiement Mixte",
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

        // Remaining / Target Card
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
                    text = if (remaining == 0) "Montant total couvert" else "Reste à répartir",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (remaining == 0) formatCurrency(total) else formatCurrency(remaining),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (remaining == 0) ComptoirAccentTeal else ComptoirTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Espèces Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Money,
                        contentDescription = null,
                        tint = ComptoirAccentTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Espèces",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ComptoirTextPrimary
                    )
                }

                OutlinedTextField(
                    value = cashText,
                    onValueChange = { cashText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        color = ComptoirTextPrimary
                    ),
                    modifier = Modifier
                        .width(130.dp)
                        .testTag("mixed_cash_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ComptoirBgSurface,
                        unfocusedContainerColor = ComptoirBgSurface,
                        focusedBorderColor = ComptoirTextPrimary,
                        unfocusedBorderColor = ComptoirBorderLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Mobile Money Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = ComptoirTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Mobile Money",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ComptoirTextPrimary
                    )
                }

                OutlinedTextField(
                    value = mobileMoneyText,
                    onValueChange = { mobileMoneyText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        color = ComptoirTextPrimary
                    ),
                    modifier = Modifier
                        .width(130.dp)
                        .testTag("mixed_mobile_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ComptoirBgSurface,
                        unfocusedContainerColor = ComptoirBgSurface,
                        focusedBorderColor = ComptoirTextPrimary,
                        unfocusedBorderColor = ComptoirBorderLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = ComptoirBgSurface,
                    contentColor = ComptoirTextPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
            ) {
                Text(text = "Annuler", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = { onConfirmMixedPayment(cashAmount, mobileAmount) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("confirm_mixed_payment_button"),
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
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Confirmer",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
