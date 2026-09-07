package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MoreScreen(
    onNavigateToReports: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToPersonnel: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
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
            text = "Menu",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MenuOptionCard(
                title = "Rapports",
                subtitle = "Analyses et statistiques",
                icon = Icons.Default.BarChart,
                iconTint = ComptoirTextSecondary,
                onClick = onNavigateToReports,
                testTag = "menu_reports"
            )

            MenuOptionCard(
                title = "Produits",
                subtitle = "Gérer le catalogue",
                icon = Icons.Default.Layers,
                iconTint = ComptoirAccentGold,
                onClick = onNavigateToProducts,
                testTag = "menu_products"
            )

            MenuOptionCard(
                title = "Stock",
                subtitle = "Inventaire et alertes",
                icon = Icons.Default.Inventory2,
                iconTint = ComptoirAccentTeal,
                onClick = onNavigateToStock,
                testTag = "menu_stock"
            )

            MenuOptionCard(
                title = "Personnel",
                subtitle = "Serveurs, caissiers",
                icon = Icons.Default.Group,
                iconTint = ComptoirTextSecondary,
                onClick = onNavigateToPersonnel,
                testTag = "menu_personnel"
            )

            MenuOptionCard(
                title = "Paramètres",
                subtitle = "Profil et préférences",
                icon = Icons.Default.Settings,
                iconTint = ComptoirTextSecondary,
                onClick = onNavigateToProfile,
                testTag = "menu_settings"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Logout Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onLogout)
                    .testTag("menu_logout"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirAccentRedLight))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = ComptoirAccentRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Se déconnecter",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ComptoirAccentRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MenuOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ComptoirBgCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ComptoirBorderLight))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = ComptoirTextSecondary
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = ComptoirTextTertiary,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
