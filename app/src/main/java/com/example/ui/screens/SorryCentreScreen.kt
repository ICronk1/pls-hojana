package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import com.example.config.AppConfig
import com.example.data.AppPreferences
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.components.PasswordModal
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground

@Composable
fun SorryCentreScreen(
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier
) {
    var isVaultUnlocked by remember { mutableStateOf(false) }
    var showPasswordModal by remember { mutableStateOf(!isVaultUnlocked) }
    var selectedApologyToRead by remember {
        mutableStateOf<AppConfig.ApologyItem?>(AppConfig.APOLOGIES_LIST.firstOrNull())
    }
    var phoneDialogApology by remember { mutableStateOf<AppConfig.ApologyItem?>(null) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        val isWideScreen = maxWidth >= 720.dp
        FloatingHeartsCanvas(particleCount = if (isWideScreen) 16 else 10, primaryColor = RomanticPink)

        if (showPasswordModal && !isVaultUnlocked) {
            PasswordModal(
                title = "Reflection Vault",
                subtitle = "Enter 4-digit code to access reconciliation notes",
                correctPin = AppConfig.SORRY_CENTRE_PASSWORD,
                accentColor = RomanticPink,
                onDismiss = { /* keep open */ },
                onSuccess = {
                    isVaultUnlocked = true
                    showPasswordModal = false
                }
            )
        }

        if (isWideScreen) {
            // WIDE SCREEN / TABLET MASTER-DETAIL LAYOUT (No letterboxing)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left Column: Notes List (360dp)
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
                                    .border(1.dp, SurfaceCardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🕊️", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Sorry Centre",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${AppConfig.APOLOGIES_LIST.size} Personal Reflections",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Reconciliation Notes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        AppConfig.APOLOGIES_LIST.forEach { item ->
                            val isSelected = selectedApologyToRead?.number == item.number

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        appPreferences.unlockApology(item.number)
                                        selectedApologyToRead = item
                                    }
                                    .testTag("sorry_card_${item.number}"),
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
                                            text = "Note #${item.number}",
                                            fontSize = 11.sp,
                                            color = SubtleGrayText,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = item.title,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) RomanticPink else TextPrimary
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = RomanticPink,
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
                    selectedApologyToRead?.let { apology ->
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
                                        text = "Reflection #${apology.number}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = RomanticPink
                                    )
                                }
                                Text(
                                    text = "🕊️ Accountability & Love",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = apology.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = apology.message,
                                fontSize = 16.sp,
                                color = TextPrimary,
                                lineHeight = 26.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // SINCERE COMMITMENT CARD
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = SurfacePinkLight),
                                border = androidx.compose.foundation.BorderStroke(1.dp, RomanticPink.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "💖", fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "My Sincere Commitment",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RomanticPink
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = apology.sincereCommitment,
                                        fontSize = 14.sp,
                                        color = TextPrimary,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Select a note from the left to read.",
                                fontSize = 15.sp,
                                color = TextSecondary
                            )
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
                        .border(1.dp, SurfaceCardBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🕊️", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Sorry Centre",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Sincere personal accountability, reflection, and commitments.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                Text(
                    text = "Reconciliation Notes (${AppConfig.APOLOGIES_LIST.size} Available)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppConfig.APOLOGIES_LIST.forEach { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    appPreferences.unlockApology(item.number)
                                    phoneDialogApology = item
                                }
                                .testTag("sorry_card_${item.number}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CrispWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
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
                                        text = "Note #${item.number}",
                                        fontSize = 11.sp,
                                        color = SubtleGrayText,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = item.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(SurfacePinkLight)
                                        .border(1.dp, BorderSubtle, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = RomanticPink,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // POPUP MODAL TO READ APOLOGY ON PHONE
            phoneDialogApology?.let { apology ->
                Dialog(onDismissRequest = { phoneDialogApology = null }) {
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
                            Text(text = "🕊️", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = apology.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = apology.message,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 22.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            // Commitment in modal
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SurfacePinkLight),
                                border = androidx.compose.foundation.BorderStroke(1.dp, RomanticPink.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "Commitment:",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RomanticPink
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = apology.sincereCommitment,
                                        fontSize = 13.sp,
                                        color = TextPrimary,
                                        lineHeight = 19.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = { phoneDialogApology = null },
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
}

@Preview(name = "Phone - Sorry Centre", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Sorry Centre", showBackground = true, device = Devices.TABLET)
@Composable
fun SorryCentrePreview() {
    MyApplicationTheme {
        SorryCentreScreen(
            appPreferences = AppPreferences(LocalContext.current)
        )
    }
}
