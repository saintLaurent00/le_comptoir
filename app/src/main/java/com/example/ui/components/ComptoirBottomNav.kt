package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BottomTab
import com.example.ui.theme.*

private data class NavItemData(
    val tab: BottomTab,
    val label: String,
    val icon: ImageVector
)

@Composable
fun ComptoirBottomNav(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItemData(BottomTab.DASHBOARD, "Accueil", Icons.Default.PieChart),
        NavItemData(BottomTab.INVOICES, "Factures", Icons.Default.Receipt),
        NavItemData(BottomTab.STOCK, "Stock", Icons.Default.Inventory2),
        NavItemData(BottomTab.CASH_REGISTER, "Caisse", Icons.Default.PointOfSale),
        NavItemData(BottomTab.MORE, "Plus", Icons.Default.MoreHoriz)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ComptoirBgPrimary)
    ) {
        HorizontalDivider(color = ComptoirBorderLight, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.tab == selectedTab
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onTabSelected(item.tab) }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) ComptoirTextPrimary else ComptoirTextTertiary,
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = if (isSelected) (-2).dp else 0.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) ComptoirTextPrimary else ComptoirTextTertiary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    // Gold active dot
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) ComptoirAccentGold else ComptoirBgPrimary)
                    )
                }
            }
        }
    }
}
