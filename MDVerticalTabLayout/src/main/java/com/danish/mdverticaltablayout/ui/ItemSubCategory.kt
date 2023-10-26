package com.danish.mdverticaltablayout.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.danish.mdverticaltablayout.model.CategoryItem

@Composable
fun ItemSubCategory(
    modifier: Modifier = Modifier,
    item: CategoryItem,
    onChecked: Boolean,
    gradientFirstColor: Color,
    gradientSecondColor: Color,
    textStyle: TextStyle,
    onClick: () -> Unit,
) {
    var offset by rememberSaveable { mutableStateOf(0f to 0f) }

    val animationSpec by remember {
        derivedStateOf {
            tween<Offset>(
                durationMillis = 600,
                easing = FastOutSlowInEasing,
            )
        }
    }

    val scaleSpec = tween<Float>(
        durationMillis = 600,
        easing = FastOutSlowInEasing,
    )

    val scale by animateFloatAsState(
        targetValue = if (onChecked) 1.5f else 1.0f,
        animationSpec = scaleSpec,
        label = "",
    )

    // Create an animated offset
    val animatedOffset by animateOffsetAsState(
        targetValue = Offset(x = offset.first, y = offset.second),
        animationSpec = animationSpec,
        label = "",
    )

    LaunchedEffect(key1 = onChecked) {
        offset = if (onChecked) 0f to -20f else 0f to 0f
    }

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    onClick()
                }
            }
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .size(74.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            val boxColors = if (onChecked) {
                listOf(
                    gradientFirstColor,
                    gradientSecondColor,
                )
            } else {
                listOf(
                    Color(0xFFF7F9F7),
                    Color(0xFFF7F9F7),
                )
            }
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = boxColors,
                        ),
                    ),

            )
            AsyncImage(
                modifier = Modifier
                    .size(44.dp)
                    .offset { IntOffset(animatedOffset.x.toInt(), animatedOffset.y.toInt()) }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                    )
                    .padding(bottom = 8.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 4.dp)
                .defaultMinSize(minHeight = 28.dp),
            text = item.name,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = textStyle,
            maxLines = 2,
        )
    }
}
