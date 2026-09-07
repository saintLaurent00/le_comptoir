package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.ui.components.ComptoirBadge
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun ProductsScreen(
    products: List<Product>,
    onAddProductClick: () -> Unit,
    onRestockProduct: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Produits",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ComptoirTextPrimary
            )

            Button(
                onClick = onAddProductClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ComptoirBgDark,
                    contentColor = ComptoirBgPrimary
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                modifier = Modifier.testTag("add_new_product_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter un produit",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Rechercher...", color = ComptoirTextTertiary, fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = ComptoirTextTertiary,
                    modifier = Modifier.size(18.dp)
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ComptoirBgSurface,
                unfocusedContainerColor = ComptoirBgSurface,
                focusedBorderColor = ComptoirTextPrimary,
                unfocusedBorderColor = ComptoirBorderLight
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Product Cards
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredProducts, key = { it.id }) { product ->
                val isCritique = product.stockQuantity <= product.minThreshold / 3
                val isBas = product.stockQuantity <= product.minThreshold && !isCritique

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRestockProduct(product.id) },
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
                            Text(text = product.icon, fontSize = 24.sp)

                            Column {
                                Text(
                                    text = product.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ComptoirTextPrimary
                                )
                                Text(
                                    text = "${product.category} · Stock: ${product.stockQuantity}",
                                    fontSize = 12.sp,
                                    color = ComptoirTextSecondary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = formatCurrency(product.price),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ComptoirTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            when {
                                isCritique -> {
                                    ComptoirBadge(
                                        text = "Critique",
                                        backgroundColor = ComptoirAccentRedLight,
                                        textColor = ComptoirAccentRed
                                    )
                                }
                                isBas -> {
                                    ComptoirBadge(
                                        text = "Bas",
                                        backgroundColor = ComptoirAccentGoldLight,
                                        textColor = ComptoirAccentGold
                                    )
                                }
                                else -> {
                                    ComptoirBadge(
                                        text = "En stock",
                                        backgroundColor = ComptoirAccentTealLight,
                                        textColor = ComptoirAccentTeal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
