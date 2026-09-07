package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("admin") }
    var password by remember { mutableStateOf("password") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComptoirBgPrimary)
            .padding(horizontal = 24.dp)
            .padding(top = 40.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Icon Emblem
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(ComptoirBgDark),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🍻", fontSize = 38.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Le Comptoir",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ComptoirTextPrimary
        )

        Text(
            text = "Gestion de maquis & cave",
            fontSize = 14.sp,
            color = ComptoirTextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input Card
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
                    text = "Identifiant",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("admin@lecomptoir.com", color = ComptoirTextTertiary, fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("username_input"),
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

                Text(
                    text = "Mot de passe",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ComptoirTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••", color = ComptoirTextTertiary, fontSize = 14.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input"),
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
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Login Button
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("login_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ComptoirBgDark,
                contentColor = ComptoirBgPrimary
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Login,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Se connecter",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mot de passe oublié ?",
            fontSize = 13.sp,
            color = ComptoirTextTertiary,
            modifier = Modifier
                .clickable(onClick = onForgotPassword)
                .padding(8.dp)
        )
    }
}
