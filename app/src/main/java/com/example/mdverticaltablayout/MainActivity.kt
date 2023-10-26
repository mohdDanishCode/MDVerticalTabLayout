package com.example.mdverticaltablayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.danish.mdverticaltablayout.model.CategoryItem
import com.danish.mdverticaltablayout.ui.MDVerticalTabLayout
import com.example.mdverticaltablayout.ui.theme.MDVerticalTabLayoutTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MDVerticalTabLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val categoryItemList: SnapshotStateList<CategoryItem> = remember {
        mutableStateListOf(
            CategoryItem(id = UUID.randomUUID().toString(), name = "Kurkure Namkeen - Masala Munch, 70 g", image = "https://www.bigbasket.com/media/uploads/p/xxl/102761_17-kurkure-namkeen-masala-munch.jpg"),
            CategoryItem(id = UUID.randomUUID().toString(), name = "Kurkure Namkeen - Masala Munch, 70 g", image = "https://www.bigbasket.com/media/uploads/p/xxl/102761_17-kurkure-namkeen-masala-munch.jpg"),
            CategoryItem(id = UUID.randomUUID().toString(), name = "Kurkure Namkeen - Masala Munch, 70 g", image = "https://www.bigbasket.com/media/uploads/p/xxl/102761_17-kurkure-namkeen-masala-munch.jpg"),
            CategoryItem(id = UUID.randomUUID().toString(), name = "Kurkure Namkeen - Masala Munch, 70 g", image = "https://www.bigbasket.com/media/uploads/p/xxl/102761_17-kurkure-namkeen-masala-munch.jpg"),
            CategoryItem(id = UUID.randomUUID().toString(), name = "Kurkure Namkeen - Masala Munch, 70 g", image = "https://www.bigbasket.com/media/uploads/p/xxl/102761_17-kurkure-namkeen-masala-munch.jpg"),
            CategoryItem(id = UUID.randomUUID().toString(), name = "Kurkure Namkeen - Masala Munch, 70 g", image = "https://www.bigbasket.com/media/uploads/p/xxl/102761_17-kurkure-namkeen-masala-munch.jpg"),
            CategoryItem(id = UUID.randomUUID().toString(), name = "Kurkure Namkeen - Masala Munch, 70 g", image = "https://www.bigbasket.com/media/uploads/p/xxl/102761_17-kurkure-namkeen-masala-munch.jpg"),

        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MDVerticalTabLayout(
            list = categoryItemList,
            indicatorColor = Color(0xFF2C8335),
            gradientFirstColor = Color(0xFFE9FFEB),
            gradientSecondColor = Color(0xFF47C253),
        ) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MDVerticalTabLayoutTheme {
        Greeting()
    }
}
