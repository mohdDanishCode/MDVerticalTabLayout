package com.danish.mdverticaltablayout.ui

import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danish.mdverticaltablayout.model.CategoryItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MDVerticalTabLayout(
    modifier: Modifier = Modifier,
    bottomPaddingDp: Dp = 0.dp,
    list: SnapshotStateList<CategoryItem>,
    gradientFirstColor: Color,
    gradientSecondColor: Color,
    indicatorColor: Color,
    indicatorHeightType: IndicatorHeightType = IndicatorHeightType.FIXED,
    indicatorHeight: Dp = 58.dp,
    textStyle: TextStyle = TextStyle.Default,
    onItemClick: (CategoryItem) -> Unit,
) {
    val context = LocalContext.current
    val globalOffsetYPosition = rememberSaveable {
        mutableIntStateOf(0)
    }
    val globalHeight = rememberSaveable {
        mutableIntStateOf(0)
    }

    val selectedPosition = rememberSaveable {
        mutableIntStateOf(0)
    }

    val movementThreshHold = remember {
        100
    }

    val offsetYPosition = rememberSaveable {
        mutableIntStateOf(0)
    }

    val heightOfItem = rememberSaveable {
        mutableIntStateOf(0)
    }
    val listState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val map = remember {
        hashMapOf<Int, Int>()
    }

    val durationMillis = rememberSaveable {
        mutableStateOf(0)
    }

    // Define the animation spec
    val animationSpec by remember {
        derivedStateOf {
            tween<Offset>(
                durationMillis = durationMillis.value,
                easing = FastOutSlowInEasing,
            )
        }
    }

    val animationSpecForScroll by remember {
        derivedStateOf {
            tween<Float>(
                durationMillis = 600,
                easing = FastOutSlowInEasing,
            )
        }
    }

    // Create an animated offset
    val animatedOffset by animateOffsetAsState(
        targetValue = Offset(x = 0F, y = offsetYPosition.value.toFloat()),
        animationSpec = animationSpec,
        label = "",
    )

    val indicatorHeight by remember {
        derivedStateOf {
            when (indicatorHeightType) {
                IndicatorHeightType.FIXED -> {
                    indicatorHeight
                }
                IndicatorHeightType.FILL -> {
                    getDp(context, heightOfItem.intValue)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
            .background(Color.White)
            .fillMaxHeight()
            .width(78.dp)
            .onGloballyPositioned {
                globalOffsetYPosition.value = it.positionInRoot().y.toInt()
                globalHeight.value = it.size.height
            },
        content = { paddingValues ->
            Surface(modifier = Modifier.fillMaxHeight().padding(paddingValues), elevation = 10.dp) {
                Box(
                    contentAlignment = Alignment.TopEnd,
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(listState)
                            .width(78.dp),
                    ) {
                        list.forEachIndexed { position, item ->
                            map[position] = 0
                            var height: Int = 0

                            ItemSubCategory(
                                item = item,
                                onChecked = selectedPosition.value == position,
                                gradientFirstColor = gradientFirstColor,
                                gradientSecondColor = gradientSecondColor,
                                textStyle = textStyle,
                                modifier = Modifier
                                    .onGloballyPositioned {
                                        map[position] = it.positionInRoot().y.toInt()
                                        height = it.size.height
                                        if (selectedPosition.value == position) {
                                            height = it.size.height
                                            offsetYPosition.value =
                                                it.positionInRoot().y.toInt() - globalOffsetYPosition.value
                                            heightOfItem.value = height
                                        }
                                    }
                                    .fillMaxWidth(),
                            ) {
                                durationMillis.value = 600
                                offsetYPosition.intValue = map[position] ?: (0 - globalOffsetYPosition.intValue)
                                selectedPosition.intValue = position
                                heightOfItem.intValue = height
                                coroutineScope.launch {
                                    if (offsetYPosition.intValue <= height + movementThreshHold) {
                                        listState.animateScrollTo(animationSpec = animationSpecForScroll, value = listState.value - height / 2 - movementThreshHold)
                                    } else if (offsetYPosition.intValue >= globalHeight.intValue - height / 2 - movementThreshHold) {
                                        listState.animateScrollTo(animationSpec = animationSpecForScroll, value = (listState.value + height / 2 + movementThreshHold))
                                    }
                                    delay(600)
                                    durationMillis.value = 0
                                }
                                onItemClick(item)
                            }
                            if (position == list.size - 1) {
                                Spacer(
                                    modifier = Modifier
                                        .padding(bottom = bottomPaddingDp)
                                        .fillMaxWidth()
                                        .background(Color.White),
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White),
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .offset(
                                x = getDp(context, animatedOffset.x.toInt()),
                                y = getDp(context, animatedOffset.y.toInt()),
                            )
                            .padding(top = 24.dp)
                            .width(3.dp)
                            .height(indicatorHeight)
                            .background(
                                color = indicatorColor,
                                shape = RoundedCornerShape(2.dp),
                            ),

                    )
                }
            }
        },
    )
}

fun getDp(context: Context, offset: Int): Dp {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val dpValue = offset / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dpValue.dp
}

enum class IndicatorHeightType {
    FILL,
    FIXED,
}
