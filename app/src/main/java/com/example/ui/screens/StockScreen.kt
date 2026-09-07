package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.ui.theme.*

@Composable
fun StockScreen(
    products: List<Product>,
    onRestock: (productId: String) -> Unit,
    onViewMovements: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val totalStock = products.sumOf { it.stockQuantity }
    val alertProducts = products.filter { it.stockQuantity <= it.minThreshold }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Stock",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )
        Text(
            text = "${alertProducts.size} produits sous le seuil d'alerte",
            fontSize = 13.sp,
            color = ComptoirTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        // Grid-2 Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                label = "EN STOCK",
                value = "$totalStock",
                valueColor = ComptoirAccentTeal,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "ALERTES",
                value = "${alertProducts.size}",
                valueColor = ComptoirAccentRed,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Alertes critiques
        Text(
            text = "⚠️ ALERTES CRITIQUES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = ComptoirTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Critical Alert Cards
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            alertProducts.forEach { product ->
                val isSevere = product.stockQuantity <= product.minThreshold / 3
                val accentColor = if (isSevere) ComptoirAccentRed else ComptoirAccentGold

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
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(accentColor)
                            )

                            Column {
                                Text(
                                    text = product.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ComptoirTextPrimary
                                )
                                Text(
                                    text = "${product.stockQuantity} restants · Min: ${product.minThreshold}",
                                    fontSize = 12.sp,
                                    color = accentColor
                                )
                            }
                        }

                        Button(
                            onClick = { onRestock(product.id) },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accentColor,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("restock_button_${product.id}")
                        ) {
                            Text(
                                text = "Réappro",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // View Movements Button
        OutlinedButton(
            onClick = onViewMovements,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("view_movements_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = ComptoirBgSurface,
                contentColor = ComptoirTextPrimary
            ),
            border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Voir les mouvements",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
