package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InvoiceItem
import com.example.model.Product
import com.example.ui.components.ComptoirBadge
import com.example.ui.components.FilterPill
import com.example.ui.components.formatCurrency
import com.example.ui.theme.*

@Composable
fun NewInvoiceScreen(
    products: List<Product>,
    draftItems: List<InvoiceItem>,
    onAddToCart: (Product) -> Unit,
    onDecrementFromCart: (Product) -> Unit,
    onValidateInvoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tout") }

    val categories = listOf("Tout", "Bières", "Vins", "Cocktails", "Plats")

    val filteredProducts = products.filter { product ->
        val matchesCategory = if (selectedCategory == "Tout") true else product.category.contains(selectedCategory, ignoreCase = true)
        val matchesSearch = product.name.contains(searchQuery, ignoreCase = true) || product.category.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    val totalAmount = draftItems.sumOf { it.total }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nouvelle facture",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComptoirTextPrimary
                )

                ComptoirBadge(
                    text = "Ouverte",
                    backgroundColor = ComptoirAccentTealLight,
                    textColor = ComptoirAccentTeal
                )
            }

            Text(
                text = "Ajoutez des produits à la facture",
                fontSize = 13.sp,
                color = ComptoirTextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Rechercher un produit...",
                        color = ComptoirTextTertiary,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = ComptoirTextTertiary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("product_search_input"),
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

            Spacer(modifier = Modifier.height(12.dp))

            // Category Pills
            val pillsScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(pillsScroll)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val iconPrefix = when (cat) {
                        "Bières" -> "🍺 "
                        "Vins" -> "🍷 "
                        "Cocktails" -> "🍹 "
                        "Plats" -> "🍗 "
                        else -> ""
                    }
                    FilterPill(
                        label = "$iconPrefix$cat",
                        isSelected = cat == selectedCategory,
                        onClick = { selectedCategory = cat }
                    )
                }
            }

            // Products List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    val currentItem = draftItems.find { it.product.id == product.id }
                    val qty = currentItem?.quantity ?: 0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = product.icon,
                                    fontSize = 24.sp
                                )
                                Column {
                                    Text(
                                        text = product.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ComptoirTextPrimary
                                    )
                                    Text(
                                        text = product.category,
                                        fontSize = 12.sp,
                                        color = ComptoirTextSecondary
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = formatCurrency(product.price),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ComptoirTextPrimary
                                )

                                // Stepper
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (qty > 0) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ComptoirBgSurface)
                                                .border(1.dp, ComptoirBorderLight, RoundedCornerShape(8.dp))
                                                .clickable { onDecrementFromCart(product) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Diminuer",
                                                tint = ComptoirTextPrimary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }

                                        Text(
                                            text = "$qty",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ComptoirTextPrimary,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ComptoirBgSurface)
                                            .border(1.dp, ComptoirBorderLight, RoundedCornerShape(8.dp))
                                            .clickable { onAddToCart(product) }
                                            .testTag("add_product_${product.id}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Ajouter",
                                            tint = ComptoirTextPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sticky Bottom Total Bar
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total facture",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ComptoirTextSecondary
                    )
                    Text(
                        text = formatCurrency(totalAmount),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComptoirTextPrimary
                    )
                }

                Button(
                    onClick = onValidateInvoice,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ComptoirBgDark,
                        contentColor = ComptoirBgPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                    modifier = Modifier.testTag("validate_invoice_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Valider",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
