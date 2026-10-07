package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RomanticFooter(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val isTablet = maxWidth >= 600.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isTablet) 24.dp else 16.dp, vertical = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CrispWhite)
                .border(1.dp, SurfaceCardBorder, RoundedCornerShape(20.dp))
                .padding(if (isTablet) 24.dp else 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isTablet) {
                // Tablet layout: 3 actions side by side in a Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Action 1: Call
                    FooterActionCard(
                        modifier = Modifier.weight(1f),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call",
                                tint = RomanticPink,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        title = "Direct Line",
                        subtitle = AppConfig.ContactConfig.MY_PHONE_NUMBER,
                        containerColor = SurfacePinkLight,
                        testTag = "footer_call_action",
                        onClick = {
                            val phone = AppConfig.ContactConfig.MY_PHONE_NUMBER
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Calling $phone", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    // Action 2: Instagram
                    FooterActionCard(
                        modifier = Modifier.weight(1f),
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Message",
                                tint = RomanticPink,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        title = "Instagram Priority",
                        subtitle = "${AppConfig.ContactConfig.INSTAGRAM_HANDLE} ↗",
                        containerColor = SurfacePinkLight,
                        testTag = "footer_instagram_action",
                        onClick = {
                            val url = AppConfig.ContactConfig.INSTAGRAM_URL
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Opening Instagram...", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    // Action 3: Rest
                    FooterActionCard(
                        modifier = Modifier.weight(1f),
                        icon = {
                            Text(text = "☕", fontSize = 18.sp)
                        },
                        title = "Silent Rest Line",
                        subtitle = "Call & say nothing ❤️",
                        containerColor = CrispWhite,
                        testTag = "footer_rest_action",
                        onClick = {
                            val phone = AppConfig.ContactConfig.MY_PHONE_NUMBER
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // ignored
                            }
                        }
                    )
                }
            } else {
                // Phone layout: Stacked vertically
                // Line 1: Phone call
                FooterActionCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = RomanticPink,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    title = "Direct Line",
                    subtitle = AppConfig.ContactConfig.MY_PHONE_NUMBER,
                    containerColor = SurfacePinkLight,
                    testTag = "footer_call_action",
                    onClick = {
                        val phone = AppConfig.ContactConfig.MY_PHONE_NUMBER
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Calling $phone", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                // Line 2: Instagram
                FooterActionCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Message",
                            tint = RomanticPink,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    title = "Instagram Priority Direct",
                    subtitle = "${AppConfig.ContactConfig.INSTAGRAM_HANDLE} ↗",
                    containerColor = SurfacePinkLight,
                    testTag = "footer_instagram_action",
                    onClick = {
                        val url = AppConfig.ContactConfig.INSTAGRAM_URL
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Opening Instagram...", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                // Line 3: Quiet rest mode
                FooterActionCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Text(text = "☕", fontSize = 18.sp)
                    },
                    title = "Silent Rest Mode",
                    subtitle = AppConfig.ContactConfig.REST_TEXT,
                    containerColor = CrispWhite,
                    testTag = "footer_rest_action",
                    onClick = {
                        val phone = AppConfig.ContactConfig.MY_PHONE_NUMBER
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // ignored
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FooterActionCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    containerColor: androidx.compose.ui.graphics.Color,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}

@Preview(name = "Phone - Romantic Footer", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Romantic Footer", showBackground = true, device = Devices.TABLET)
@Composable
fun RomanticFooterPreview() {
    MyApplicationTheme {
        RomanticFooter()
    }
}
