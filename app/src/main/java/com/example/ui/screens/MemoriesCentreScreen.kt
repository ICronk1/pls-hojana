package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.config.AppConfig
import com.example.ui.components.FloatingHeartsCanvas
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
fun MemoriesCentreScreen(
    modifier: Modifier = Modifier
) {
    var selectedPhotoIndex by remember { mutableStateOf<Int?>(null) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        val screenWidth = maxWidth
        val columns = when {
            screenWidth >= 1100.dp -> 4
            screenWidth >= 768.dp -> 3
            screenWidth >= 520.dp -> 2
            else -> 2
        }

        // Ambient romantic gentle hearts in background
        FloatingHeartsCanvas(particleCount = 12, primaryColor = RomanticPink)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ALBUM HEADER LANE
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(SurfacePinkLight)
                        .border(1.5.dp, RomanticPink.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📸", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Our Photo Album",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "A physical keepsake lane • Overlapping polaroid snapshots of us",
                    fontSize = 13.sp,
                    color = SubtleGrayText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Realistic tactile Album Lane Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 680.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = CrispWhite,
                    shadowElevation = 6.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎞️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Album Lane",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${AppConfig.ALBUM_PHOTOS.size} Original Snapshots",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfacePinkLight)
                                .border(1.dp, RomanticPink.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Tap photo to open 🔍",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RomanticPink
                            )
                        }
                    }
                }
            }

            // ALBUM LANE OVERLAPPING PHOTO GRID
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 1200.dp)
                    .testTag("album_photo_grid"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 40.dp),
                horizontalArrangement = Arrangement.spacedBy((-10).dp), // Organic overlap
                verticalArrangement = Arrangement.spacedBy((-12).dp)    // Organic vertical overlap
            ) {
                itemsIndexed(AppConfig.ALBUM_PHOTOS) { index, photo ->
                    AlbumPolaroidCard(
                        photo = photo,
                        index = index,
                        onClick = { selectedPhotoIndex = index }
                    )
                }
            }
        }

        // FULLSCREEN HEAVYWEIGHT ALBUM OPENED MODAL
        selectedPhotoIndex?.let { idx ->
            val photo = AppConfig.ALBUM_PHOTOS.getOrNull(idx)
            if (photo != null) {
                HeavyAlbumViewerDialog(
                    photo = photo,
                    currentIndex = idx,
                    totalCount = AppConfig.ALBUM_PHOTOS.size,
                    onDismiss = { selectedPhotoIndex = null },
                    onPrev = {
                        if (idx > 0) selectedPhotoIndex = idx - 1
                    },
                    onNext = {
                        if (idx < AppConfig.ALBUM_PHOTOS.size - 1) selectedPhotoIndex = idx + 1
                    }
                )
            }
        }
    }
}

/**
 * Individual tactile Polaroid Album card that features:
 * - Pure white photographic album border
 * - Subtle randomized tilt (-4° to +4°) for authentic scrapbook album feel
 * - Deep multi-layered drop shadow for tangible weight
 * - Smooth physical pop-out feedback when tapped
 */
@Composable
private fun AlbumPolaroidCard(
    photo: AppConfig.AlbumPhoto,
    index: Int,
    onClick: () -> Unit
) {
    // Subtle deterministic rotation per card index (-3.5f to +3.5f)
    val angle = remember(index) {
        val angles = listOf(-3.5f, 2.8f, -2.0f, 3.2f, -1.5f, 2.2f, -3.0f, 1.8f)
        angles[index % angles.size]
    }

    var isPressed by remember { mutableStateOf(false) }
    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) 18f else 6f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "albumCardElevation"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 1.05f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "albumCardScale"
    )

    Box(
        modifier = Modifier
            .padding(10.dp)
            .rotate(angle)
            .shadow(
                elevation = animatedElevation.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color(0x66000000),
                spotColor = Color(0x40000000)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White) // Pure white photographic background
            .border(1.dp, Color(0xFFECECEC), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            }
            .testTag("polaroid_card_${photo.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Photo Area with inner frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.92f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF6F6F6))
                    .border(0.5.dp, Color(0xFFE2E2E2), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.directUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = photo.caption,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = RomanticPink,
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SurfacePinkLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📷", fontSize = 28.sp)
                        }
                    }
                )

                // High-gloss corner sticker effect
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Handwritten caption space typical of white photo album
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${photo.id}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RomanticPink
                )
                Text(
                    text = "❤️",
                    fontSize = 10.sp
                )
            }
        }
    }
}

/**
 * Heavyweight physical album viewer dialog that:
 * - Pops open with realistic physics spring & deep backdrop dim
 * - Heavyweight photographic album card texture with pure white margins
 * - Full resolution image viewport with touch-to-fullscreen
 * - Navigation between snapshots (#1 of 49)
 */
@Composable
private fun HeavyAlbumViewerDialog(
    photo: AppConfig.AlbumPhoto,
    currentIndex: Int,
    totalCount: Int,
    onDismiss: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.82f)) // Deep darkened backdrop for weight
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Physical Thick Album Canvas Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 560.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* absorb clicks */ }
                    .shadow(
                        elevation = 28.dp,
                        shape = RoundedCornerShape(22.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    ),
                shape = RoundedCornerShape(22.dp),
                color = Color.White, // Pure photographic white
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE8E8E8))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar with close button & count
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfacePinkLight)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Snapshot ${currentIndex + 1} / $totalCount",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RomanticPink
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF2F2F2))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Main Heavy Photographic Frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.88f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF0F0F0))
                            .border(1.5.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photo.directUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = photo.caption,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(
                                            color = RomanticPink,
                                            strokeWidth = 3.dp
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Opening Memory...",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "❤️ Photo memory is loading...", color = TextSecondary)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom Navigation Bar in Album (Previous / Next)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (currentIndex > 0) SurfacePinkLight else Color(0xFFF5F5F5),
                            modifier = Modifier
                                .clickable(enabled = currentIndex > 0) { onPrev() }
                        ) {
                            Text(
                                text = "◀ Previous",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (currentIndex > 0) RomanticPink else Color.LightGray,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📸", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Cherished Moment",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (currentIndex < totalCount - 1) SurfacePinkLight else Color(0xFFF5F5F5),
                            modifier = Modifier
                                .clickable(enabled = currentIndex < totalCount - 1) { onNext() }
                        ) {
                            Text(
                                text = "Next ▶",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (currentIndex < totalCount - 1) RomanticPink else Color.LightGray,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Phone - Memories Album", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Memories Album", showBackground = true, device = Devices.TABLET)
@Composable
fun MemoriesCentrePreview() {
    MyApplicationTheme {
        MemoriesCentreScreen()
    }
}
