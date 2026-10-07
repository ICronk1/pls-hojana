package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.data.AppPreferences
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.components.PasswordModal
import com.example.ui.theme.BlushPink
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.KeyGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SoftWhiteBackground
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.concurrent.TimeUnit

@Composable
fun SecretCentreScreen(
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isVaultUnlocked by remember { mutableStateOf(false) }
    var showPasswordModal by remember { mutableStateOf(!isVaultUnlocked) }
    var unlockedSecretIds by remember { mutableStateOf(appPreferences.getUnlockedSecretIds()) }
    // Start with null so no note automatically pops open or displays initially
    var selectedSecretToRead by remember { mutableStateOf<AppConfig.SecretItem?>(null) }
    var cooldownRemainingMillis by remember { mutableLongStateOf(appPreferences.getSecretCooldownRemainingMillis()) }
    var lockedDialogItem by remember { mutableStateOf<AppConfig.SecretItem?>(null) }

    fun formatRemainingTime(millis: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        return when {
            days > 0 -> "$days days, $hours hrs"
            hours > 0 -> "$hours hrs, $minutes mins"
            else -> "$minutes mins"
        }
    }

    fun handleSecretClick(item: AppConfig.SecretItem) {
        val isUnlocked = unlockedSecretIds.contains(item.id)
        if (isUnlocked) {
            // Already unlocked before, can be re-read freely
            selectedSecretToRead = item
            return
        }

        // It is currently locked. Check weekly cooldown.
        val remaining = appPreferences.getSecretCooldownRemainingMillis()
        if (remaining > 0L) {
            // Cooldown is active: user cannot unlock a new secret yet this week
            cooldownRemainingMillis = remaining
            lockedDialogItem = item
        } else {
            // Cooldown is expired (or first unlock ever): unlock this secret now and start 7-day cooldown
            appPreferences.unlockSecret(item.id)
            unlockedSecretIds = appPreferences.getUnlockedSecretIds()
            cooldownRemainingMillis = appPreferences.getSecretCooldownRemainingMillis()
            selectedSecretToRead = item
            Toast.makeText(context, "Secret unlocked for this week! 🔒✨", Toast.LENGTH_SHORT).show()
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SoftWhiteBackground)
    ) {
        val isWideScreen = maxWidth >= 720.dp
        FloatingHeartsCanvas(particleCount = if (isWideScreen) 16 else 10, primaryColor = RomanticPink)

        if (showPasswordModal && !isVaultUnlocked) {
            PasswordModal(
                title = "Confidential Vault Access",
                subtitle = "Enter the 4-digit passcode to unlock encrypted notes",
                correctPin = AppConfig.SECRET_CENTRE_PASSWORD,
                accentColor = RomanticPink,
                onDismiss = { /* keep open */ },
                onSuccess = {
                    isVaultUnlocked = true
                    showPasswordModal = false
                }
            )
        }

        if (isWideScreen) {
            // WIDE SCREEN / TABLET MASTER-DETAIL LAYOUT (Zero letterboxing)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left Column: Vault Header & Notes List
                Card(
                    modifier = Modifier
                        .width(360.dp)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(SurfacePinkLight)
                                    .border(1.dp, BorderSubtle, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isVaultUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                                    contentDescription = "Vault",
                                    tint = RomanticPink,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Secret Centre",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "1 Secret / Week • ${unlockedSecretIds.size}/${AppConfig.SECRETS_LIST.size} Unlocked",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (cooldownRemainingMillis > 0L) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SurfacePinkLight)
                                    .border(1.dp, RomanticPink.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "⏳ Next secret unlock in: ${formatRemainingTime(cooldownRemainingMillis)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RomanticPink
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Archived Notes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        AppConfig.SECRETS_LIST.forEach { item ->
                            val isSelected = selectedSecretToRead?.id == item.id
                            val isUnlocked = unlockedSecretIds.contains(item.id)

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { handleSecretClick(item) }
                                    .testTag("secret_card_${item.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) SurfacePinkLight else CrispWhite
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) RomanticPink else BorderSubtle
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.dateHint,
                                            fontSize = 11.sp,
                                            color = SubtleGrayText,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = if (isUnlocked) item.title else "🔒 Encrypted Secret #${item.id}",
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) RomanticPink else if (isUnlocked) TextPrimary else SubtleGrayText
                                        )
                                    }

                                    Icon(
                                        imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (isUnlocked) RomanticPink else SubtleGrayText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Right Column: Reader View (fills remaining width)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    selectedSecretToRead?.let { secret ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(28.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfacePinkLight)
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = secret.dateHint,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = RomanticPink
                                    )
                                }
                                Text(text = "✉️ Personal Reflection", fontSize = 13.sp, color = TextSecondary)
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = secret.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = secret.secretContent,
                                fontSize = 16.sp,
                                color = TextPrimary,
                                lineHeight = 26.sp
                            )
                        }
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "🔒", fontSize = 42.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Select an unlocked secret from the list to read.",
                                    fontSize = 15.sp,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Only 1 secret can be unlocked each week.",
                                    fontSize = 13.sp,
                                    color = SubtleGrayText,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // PHONE SINGLE-COLUMN LAYOUT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SurfacePinkLight)
                        .border(1.dp, BorderSubtle, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isVaultUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                        contentDescription = "Vault",
                        tint = RomanticPink,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Secret Centre",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Personal written reflections. 1 secret can be unlocked each week.",
                    fontSize = 13.sp,
                    color = SubtleGrayText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                if (cooldownRemainingMillis > 0L) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfacePinkLight)
                            .border(1.dp, RomanticPink.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "⏳ Next secret unlock available in: ${formatRemainingTime(cooldownRemainingMillis)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RomanticPink,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = "Archived Notes (${unlockedSecretIds.size}/${AppConfig.SECRETS_LIST.size} Unlocked)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppConfig.SECRETS_LIST.forEach { item ->
                        val isUnlocked = unlockedSecretIds.contains(item.id)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { handleSecretClick(item) }
                                .testTag("secret_card_${item.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUnlocked) CrispWhite else Color(0xFFFAFAFA)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isUnlocked) RomanticPink.copy(alpha = 0.6f) else SurfaceCardBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.dateHint,
                                        fontSize = 11.sp,
                                        color = SubtleGrayText,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = if (isUnlocked) item.title else "🔒 Encrypted Secret #${item.id}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isUnlocked) TextPrimary else SubtleGrayText
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isUnlocked) BlushPink else Color(0xFFEEEEEE))
                                        .border(1.dp, BorderSubtle, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (isUnlocked) RomanticPink else Color.Gray,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // POPUP MODAL TO READ SECRET ON PHONE (Only when an unlocked secret is clicked)
            if (!isWideScreen) {
                selectedSecretToRead?.let { secret ->
                    androidx.compose.ui.window.Dialog(onDismissRequest = { selectedSecretToRead = null }) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CrispWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "✉️", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = secret.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = secret.secretContent,
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    lineHeight = 22.sp,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = { selectedSecretToRead = null },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = RomanticPink,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Close Note", fontWeight = FontWeight.SemiBold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        // LOCKED COOLDOWN DIALOG
        lockedDialogItem?.let { lockedItem ->
            androidx.compose.ui.window.Dialog(onDismissRequest = { lockedDialogItem = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RomanticPink.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "⏳", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Secret Locked",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Only 1 secret can be unlocked each week! Please wait for the cooldown timer to expire.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfacePinkLight)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Next unlock in: ${formatRemainingTime(cooldownRemainingMillis)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RomanticPink
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { lockedDialogItem = null },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RomanticPink,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Got it", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Phone - Secret Centre", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Secret Centre", showBackground = true, device = Devices.TABLET)
@Composable
fun SecretCentrePreview() {
    MyApplicationTheme {
        SecretCentreScreen(
            appPreferences = AppPreferences(LocalContext.current)
        )
    }
}
