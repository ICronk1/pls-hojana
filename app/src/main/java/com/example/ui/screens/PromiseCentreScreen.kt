package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.KeyGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground

@Composable
fun PromiseCentreScreen(
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isMadeToday by remember { mutableStateOf(appPreferences.isPromiseMadeToday()) }
    var currentStreak by remember { mutableIntStateOf(appPreferences.getCurrentStreak()) }
    var bestStreak by remember { mutableIntStateOf(appPreferences.getBestStreak()) }
    var showCelebration by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_btn")
    val buttonGlowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    fun onPromiseButtonClick() {
        val (newStreak, alreadyMade) = appPreferences.recordPromisePress()
        isMadeToday = true
        currentStreak = newStreak
        bestStreak = appPreferences.getBestStreak()
        showCelebration = true

        if (alreadyMade) {
            Toast.makeText(context, "Commitment already affirmed for today.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Day $newStreak confirmed and recorded.", Toast.LENGTH_LONG).show()
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        val isWideScreen = maxWidth >= 640.dp
        FloatingHeartsCanvas(particleCount = if (isWideScreen) 16 else 12, primaryColor = RomanticPink)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 960.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(SurfacePinkLight)
                    .border(1.dp, SurfaceCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "💍", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Promise Centre",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = AppConfig.MARRIAGE_PROMISES_HEADER,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
            )

            // DAILY STATUS BANNER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CrispWhite
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isMadeToday) SurfaceCardBorder else RomanticPink.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isMadeToday) Icons.Default.CheckCircle else Icons.Default.NotificationsActive,
                        contentDescription = "Status",
                        tint = RomanticPink,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isMadeToday) "Daily Vow Confirmed ✓" else "Daily Commitment Action",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isMadeToday)
                                "You've reaffirmed our mutual promises for today."
                            else
                                AppConfig.DAILY_REMINDER_NOTIFICATION_TEXT,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // STREAK COUNTER CARDS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Current Streak", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$currentStreak Days",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = RomanticPink
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "All-Time Best", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$bestStreak Days",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            // ACTION BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { onPromiseButtonClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMadeToday) SurfacePinkLight else RomanticPink,
                        contentColor = if (isMadeToday) RomanticPink else CrispWhite
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(54.dp)
                        .scale(if (!isMadeToday) buttonGlowScale else 1f)
                        .testTag("daily_promise_action_btn"),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isMadeToday) SurfaceCardBorder else RomanticPink
                    )
                ) {
                    Text(
                        text = if (isMadeToday) "Promise Confirmed For Today ✓" else "Affirm Today's Commitment",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isMadeToday) RomanticPink else CrispWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // THE 5 MARRIAGE PROMISES
            Text(
                text = "The 5 Foundational Vows",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            )

            if (isWideScreen) {
                val chunkedVows = AppConfig.PROMISES_LIST.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    chunkedVows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowItems.forEach { item ->
                                Box(modifier = Modifier.weight(1f)) {
                                    PromiseCardItem(item = item)
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppConfig.PROMISES_LIST.forEach { item ->
                        PromiseCardItem(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun PromiseCardItem(item: AppConfig.PromiseItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CrispWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SurfacePinkLight)
                    .border(1.dp, SurfaceCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.iconEmoji, fontSize = 17.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.promiseText,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Preview(name = "Phone - Promise Centre", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Promise Centre", showBackground = true, device = Devices.TABLET)
@Composable
fun PromiseCentrePreview() {
    MyApplicationTheme {
        PromiseCentreScreen(
            appPreferences = AppPreferences(LocalContext.current)
        )
    }
}
