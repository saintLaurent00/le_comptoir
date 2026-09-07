package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    onOptionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(ComptoirBgDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AD",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ComptoirBgPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Admin",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )

        Text(
            text = "admin@lecomptoir.com",
            fontSize = 13.sp,
            color = ComptoirTextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Options List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileOptionItem(
                title = "Informations personnelles",
                icon = Icons.Default.Person,
                onClick = { onOptionClick("Informations personnelles") }
            )

            ProfileOptionItem(
                title = "Notifications",
                icon = Icons.Default.Notifications,
                onClick = { onOptionClick("Notifications") }
            )

            ProfileOptionItem(
                title = "Modèle de ticket",
                icon = Icons.Default.Receipt,
                onClick = { onOptionClick("Modèle de ticket") }
            )

            ProfileOptionItem(
                title = "Sécurité",
                icon = Icons.Default.Security,
                onClick = { onOptionClick("Sécurité") }
            )

            ProfileOptionItem(
                title = "Sauvegarde",
                icon = Icons.Default.Storage,
                onClick = { onOptionClick("Sauvegarde") }
            )

            ProfileOptionItem(
                title = "À propos",
                icon = Icons.Default.Info,
                trailingText = "v1.0.0",
                onClick = { onOptionClick("Version 1.0.0 — Le Comptoir") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileOptionItem(
    title: String,
    icon: ImageVector,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ComptoirTextSecondary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = ComptoirTextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (trailingText != null) {
                Text(
                    text = trailingText,
                    fontSize = 12.sp,
                    color = ComptoirTextTertiary
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = ComptoirTextTertiary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
