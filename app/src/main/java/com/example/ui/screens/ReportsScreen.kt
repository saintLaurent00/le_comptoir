package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReportPeriod
import com.example.ui.components.FilterPill
import com.example.ui.theme.*

@Composable
fun ReportsScreen(
    selectedPeriod: ReportPeriod,
    onPeriodChange: (ReportPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val daysData = listOf(
        Pair("L", 0.35f),
        Pair("M", 0.55f),
        Pair("M", 0.40f),
        Pair("J", 0.75f),
        Pair("V", 0.65f),
        Pair("S", 0.90f),
        Pair("D", 0.80f)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Rapports",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )
        Text(
            text = "Analyse des performances",
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
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReportPeriod.values().forEach { period ->
                FilterPill(
                    label = period.label,
                    isSelected = period == selectedPeriod,
                    onClick = { onPeriodChange(period) }
                )
            }
        }

        // Bar Chart Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Chiffre d'affaires (7j)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Chart Bars
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    daysData.forEachIndexed { index, pair ->
                        val isLast = index == daysData.size - 1
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(pair.second)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (isLast) ComptoirBgDark else ComptoirBgDark.copy(alpha = 0.15f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Day Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    daysData.forEach { pair ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pair.first,
                                fontSize = 11.sp,
                                color = ComptoirTextTertiary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
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
                label = "TOTAL SEMAINE",
                value = "2.8M F",
                valueColor = ComptoirAccentGold,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "MOYENNE/JOUR",
                value = "400K F",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Top Produits
        Text(
            text = "TOP PRODUITS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = ComptoirTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TopProductCard(rank = "1", name = "Drapeau 65cl", salesCount = "142 vendus", amount = "213K F")
            TopProductCard(rank = "2", name = "Braise de poulet", salesCount = "38 vendus", amount = "190K F")
            TopProductCard(rank = "3", name = "Cocktail Cave", salesCount = "24 vendus", amount = "192K F")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TopProductCard(
    rank: String,
    name: String,
    salesCount: String,
    amount: String
) {
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = rank,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComptoirAccentGold,
                    modifier = Modifier.width(20.dp)
                )

                Column {
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ComptoirTextPrimary
                    )
                    Text(
                        text = salesCount,
                        fontSize = 12.sp,
                        color = ComptoirTextSecondary
                    )
                }
            }

            Text(
                text = amount,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ComptoirTextPrimary
            )
        }
    }
}
