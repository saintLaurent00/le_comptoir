package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CashState
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun CashCloseScreen(
    cashState: CashState,
    onConfirmClose: (counted: Int, reason: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var countedText by remember { mutableStateOf("448500") }
    var reasonText by remember { mutableStateOf("Monnaie rendue incorrecte") }

    val expected = cashState.currentTotal
    val counted = countedText.toIntOrNull() ?: 0
    val discrepancy = counted - expected
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Clôture",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )
        Text(
            text = "Saisissez le montant réel compté",
            fontSize = 13.sp,
            color = ComptoirTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        // Expected Balance Card
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
                    text = "Solde attendu",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatCurrency(expected),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComptoirTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Counted Input
        OutlinedTextField(
            value = countedText,
            onValueChange = { countedText = it },
            placeholder = { Text("Montant compté", color = ComptoirTextTertiary) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Paid,
                    contentDescription = null,
                    tint = ComptoirAccentGold,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                Text(
                    text = "F",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComptoirTextTertiary,
                    modifier = Modifier.padding(end = 12.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("counted_amount_input"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ComptoirBgSurface,
                unfocusedContainerColor = ComptoirBgSurface,
                focusedBorderColor = ComptoirTextPrimary,
                unfocusedBorderColor = ComptoirBorderLight,
                focusedTextColor = ComptoirTextPrimary,
                unfocusedTextColor = ComptoirTextPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Discrepancy Card
        val hasDiscrepancy = discrepancy != 0
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(
                    if (hasDiscrepancy) ComptoirAccentRed else ComptoirAccentTeal
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Écart",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ComptoirTextSecondary
                        )
                        Text(
                            text = if (discrepancy > 0) "+${formatCurrency(discrepancy)}" else formatCurrency(discrepancy),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasDiscrepancy) ComptoirAccentRed else ComptoirAccentTeal
                        )
                    }

                    if (hasDiscrepancy) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = ComptoirAccentRed,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                if (hasDiscrepancy) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Justification requise",
                        fontSize = 11.sp,
                        color = ComptoirTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reason Text Area
        OutlinedTextField(
            value = reasonText,
            onValueChange = { reasonText = it },
            placeholder = { Text("Raison de l'écart...", color = ComptoirTextTertiary) },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("discrepancy_reason_input"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ComptoirBgSurface,
                unfocusedContainerColor = ComptoirBgSurface,
                focusedBorderColor = ComptoirTextPrimary,
                unfocusedBorderColor = ComptoirBorderLight,
                focusedTextColor = ComptoirTextPrimary,
                unfocusedTextColor = ComptoirTextPrimary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onConfirmClose(counted, reasonText) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("confirm_close_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ComptoirBgDark,
                contentColor = ComptoirBgPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Confirmer la clôture",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
