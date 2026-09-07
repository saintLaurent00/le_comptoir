package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MovementType
import com.example.model.StockMovement
import com.example.model.StockMovementFilter
import com.example.ui.components.FilterPill
import com.example.ui.theme.*

@Composable
fun StockMovementScreen(
    movements: List<StockMovement>,
    selectedFilter: StockMovementFilter,
    onFilterChange: (StockMovementFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredMovements = when (selectedFilter) {
        StockMovementFilter.ALL -> movements
        StockMovementFilter.IN -> movements.filter { it.type == MovementType.ENTREE }
        StockMovementFilter.OUT -> movements.filter { it.type == MovementType.SORTIE }
        StockMovementFilter.LOSS -> movements.filter { it.type == MovementType.PERTE }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Mouvements",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )
        Text(
            text = "Historique des entrées et sorties",
            fontSize = 13.sp,
            color = ComptoirTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
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
            StockMovementFilter.values().forEach { filter ->
                FilterPill(
                    label = filter.label,
                    isSelected = filter == selectedFilter,
                    onClick = { onFilterChange(filter) }
                )
            }
        }

        // List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredMovements, key = { it.id }) { movement ->
                val isPositive = movement.quantityChange > 0
                val icon = when (movement.type) {
                    MovementType.ENTREE -> Icons.Default.ArrowDownward
                    MovementType.SORTIE -> Icons.Default.ArrowUpward
                    MovementType.PERTE -> Icons.Default.Inbox
                }
                val iconColor = when (movement.type) {
                    MovementType.ENTREE -> ComptoirAccentTeal
                    MovementType.SORTIE -> ComptoirAccentRed
                    MovementType.PERTE -> ComptoirAccentGold
                }

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
                                    tint = iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = movement.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ComptoirTextPrimary
                                )
                                Text(
                                    text = movement.subtitle,
                                    fontSize = 12.sp,
                                    color = ComptoirTextSecondary
                                )
                            }
                        }

                        Text(
                            text = if (isPositive) "+${movement.quantityChange}" else "${movement.quantityChange}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = iconColor
                        )
                    }
                }
            }
        }
    }
}
