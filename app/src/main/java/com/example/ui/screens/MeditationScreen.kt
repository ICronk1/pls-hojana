package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.theme.BlushPink
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WhiteBackground
import kotlinx.coroutines.delay

@Composable
fun MeditationScreen(
    modifier: Modifier = Modifier
) {
    var selectedSession by remember { mutableStateOf(AppConfig.MEDITATION_SESSIONS.first()) }
    var isRunning by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableIntStateOf(selectedSession.durationMinutes * 60) }

    // Breathing phase: 0..3s Inhale, 4..7s Hold, 8..11s Exhale (12s cycle)
    var breathCycleSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedSession) {
        isRunning = false
        secondsRemaining = selectedSession.durationMinutes * 60
        breathCycleSeconds = 0
    }

    LaunchedEffect(isRunning, secondsRemaining) {
        if (isRunning && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
            breathCycleSeconds = (breathCycleSeconds + 1) % 12
        } else if (secondsRemaining == 0) {
            isRunning = false
        }
    }

    val (breathPhase, targetScale) = when {
        !isRunning -> Pair("Ready", 1.0f)
        breathCycleSeconds in 0..3 -> Pair("Inhale Deeply...", 1.25f)
        breathCycleSeconds in 4..7 -> Pair("Hold Breath...", 1.25f)
        else -> Pair("Exhale Slowly...", 0.90f)
    }

    val breathScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(
            durationMillis = if (breathCycleSeconds in 4..7) 400 else 3600,
            easing = LinearEasing
        ),
        label = "breathScale"
    )

    val infinitePulse = rememberInfiniteTransition(label = "ambientPulse")
    val ambientPulse by infinitePulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambientScale"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        val isWideScreen = maxWidth >= 720.dp
        FloatingHeartsCanvas(particleCount = if (isWideScreen) 16 else 10, primaryColor = RomanticPink)

        if (isWideScreen) {
            // WIDE SCREEN / TABLET 2-COLUMN STRUCTURE (Zero letterboxing)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Left Column: Breathing Circle Animation & Controls
                Card(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Meditation & Focus",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = AppConfig.MEDITATION_ABOUT_TEXT,
                            fontSize = 13.sp,
                            color = SubtleGrayText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                        )

                        // Breathing Circle
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .scale(ambientPulse),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(230.dp)
                                    .scale(breathScale)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                RomanticPink.copy(alpha = 0.25f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            Box(
                                modifier = Modifier
                                    .size(175.dp)
                                    .scale(breathScale)
                                    .clip(CircleShape)
                                    .background(SurfacePinkLight)
                                    .border(1.5.dp, RomanticPink.copy(alpha = 0.4f), CircleShape)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                val minutes = secondsRemaining / 60
                                val secs = secondsRemaining % 60
                                Text(
                                    text = String.format("%02d:%02d", minutes, secs),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isRunning) breathPhase else "Standby",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RomanticPink
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Controls
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    isRunning = false
                                    secondsRemaining = selectedSession.durationMinutes * 60
                                    breathCycleSeconds = 0
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(SurfacePinkLight)
                                    .border(1.dp, BorderSubtle, CircleShape)
                                    .testTag("meditation_reset_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    tint = RomanticPink
                                )
                            }

                            Button(
                                onClick = { isRunning = !isRunning },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RomanticPink,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(140.dp)
                                    .testTag("meditation_toggle_btn")
                            ) {
                                Icon(
                                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isRunning) "Pause" else "Start",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isRunning) "Pause" else "Start",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Right Column: Session Details & Interval Picker
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Active Session Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfacePinkLight),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedSession.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(BlushPink)
                                            .border(1.dp, RomanticPink.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${selectedSession.durationMinutes} min",
                                            fontSize = 11.sp,
                                            color = RomanticPink,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = selectedSession.description,
                                    fontSize = 12.sp,
                                    color = SubtleGrayText,
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Protocol: ${selectedSession.instructions}",
                                    fontSize = 12.sp,
                                    color = RomanticPink,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Choose Focus Interval",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        AppConfig.MEDITATION_SESSIONS.forEach { session ->
                            val isSelected = session.id == selectedSession.id
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { selectedSession = session }
                                    .testTag("session_item_${session.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) BlushPink else CrispWhite
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) RomanticPink else SurfaceCardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = session.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${session.durationMinutes} minutes • ${session.category}",
                                            fontSize = 12.sp,
                                            color = SubtleGrayText
                                        )
                                    }
                                    if (isSelected) {
                                        Text(text = "✦", fontSize = 16.sp, color = RomanticPink)
                                    }
                                }
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
                Text(
                    text = "Meditation & Focus Sanctuary",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = AppConfig.MEDITATION_ABOUT_TEXT,
                    fontSize = 13.sp,
                    color = SubtleGrayText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // BREATHING CIRCLE ANIMATION
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .scale(ambientPulse),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(230.dp)
                            .scale(breathScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        RomanticPink.copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(175.dp)
                            .scale(breathScale)
                            .clip(CircleShape)
                            .background(SurfacePinkLight)
                            .border(1.5.dp, RomanticPink.copy(alpha = 0.4f), CircleShape)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val minutes = secondsRemaining / 60
                        val secs = secondsRemaining % 60
                        Text(
                            text = String.format("%02d:%02d", minutes, secs),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isRunning) breathPhase else "Standby",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RomanticPink
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // TIMER CONTROLS
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            isRunning = false
                            secondsRemaining = selectedSession.durationMinutes * 60
                            breathCycleSeconds = 0
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(SurfacePinkLight)
                            .border(1.dp, BorderSubtle, CircleShape)
                            .testTag("meditation_reset_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = RomanticPink
                        )
                    }

                    Button(
                        onClick = { isRunning = !isRunning },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RomanticPink,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .width(140.dp)
                            .testTag("meditation_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pause" else "Start",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isRunning) "Pause" else "Start",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ACTIVE SESSION DETAILS
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedSession.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BlushPink)
                                    .border(1.dp, RomanticPink.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${selectedSession.durationMinutes} min",
                                    fontSize = 11.sp,
                                    color = RomanticPink,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = selectedSession.description,
                            fontSize = 13.sp,
                            color = SubtleGrayText,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Protocol: ${selectedSession.instructions}",
                            fontSize = 12.sp,
                            color = RomanticPink,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // SESSIONS SELECTOR
                Text(
                    text = "Choose Session Interval",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppConfig.MEDITATION_SESSIONS.forEach { session ->
                        val isSelected = session.id == selectedSession.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSession = session }
                                .testTag("session_item_${session.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) BlushPink else CrispWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) RomanticPink else SurfaceCardBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = session.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${session.durationMinutes} minutes • ${session.category}",
                                        fontSize = 12.sp,
                                        color = SubtleGrayText
                                    )
                                }
                                if (isSelected) {
                                    Text(text = "✦", fontSize = 16.sp, color = RomanticPink)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Phone - Meditation", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Meditation", showBackground = true, device = Devices.TABLET)
@Composable
fun MeditationScreenPreview() {
    MyApplicationTheme {
        MeditationScreen()
    }
}
