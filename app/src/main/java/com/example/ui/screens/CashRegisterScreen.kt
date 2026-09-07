package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CashState
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun CashRegisterScreen(
    cashState: CashState,
    onCloseCashClick: () -> Unit,
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
        Text(
            text = "Caisse",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )
        Text(
            text = "Ouverture: ${cashState.openingTime} · ${cashState.cashier}",
            fontSize = 13.sp,
            color = ComptoirTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

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
                Text(
                    text = "Solde actuel",
                    fontSize = 11.sp,
                    color = ComptoirTextTertiary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatCurrency(cashState.currentTotal),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    color = ComptoirBgPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
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
                        text = "+12% vs hier",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ComptoirAccentTeal
                    )
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
                label = "FOND DE CAISSE",
                value = "${cashState.floatAmount / 1000}K F",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "TRANSACTIONS",
                value = "${cashState.transactionCount}",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                label = "ESPÈCES",
                value = "${cashState.cashAmount / 1000}K F",
                valueColor = ComptoirAccentTeal,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "MOBILE MONEY",
                value = "${cashState.mobileMoneyAmount / 1000}K F",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Clôturer Button
        Button(
            onClick = onCloseCashClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("close_cash_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ComptoirAccentRedLight,
                contentColor = ComptoirAccentRed
            )
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Clôturer la caisse",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
