package com.lightning.androidfrontend.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.data.model.Item
import com.lightning.androidfrontend.data.model.MenuItemSize
import com.lightning.androidfrontend.utils.priceText


@Composable
fun ProductScreen(

) {
    var selectedSize by remember { mutableStateOf("العادي") }
    var addPepsi by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(1) }

    val priceMap = mapOf("العادي" to 19.0, "وسط" to 22.0, "كبير" to 25.0)
    val total = (priceMap[selectedSize] ?: 0.0) * quantity + if (addPepsi) 5.0 else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(16.dp)
    ) {
        // صورة المنتج
        Image(
            painter = painterResource(id = R.drawable.item_a), // ضع صورتك هنا
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // اسم المنتج والتقييم
        Text("كودو حريقة", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
            Text("4.9", modifier = Modifier.padding(start = 4.dp), fontSize = 16.sp)
        }

        Spacer(Modifier.height(8.dp))

        // الوصف
        Text("قطعتين برجر دجاج، هالابينو، مايونيز، جبنة، بطاطس، ومشروب.", fontSize = 14.sp, color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        // اختيار الحجم
        Text("الأحجام", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        listOf("العادي", "وسط", "كبير").forEach { size ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedSize == size,
                        onClick = { selectedSize = size }
                    )
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedSize == size,
                    onClick = { selectedSize = size }
                )
                Text(
                    "$size - ${priceMap[size]} ر.س",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // الإضافات
        Text("المشروبات", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = addPepsi, onCheckedChange = { addPepsi = it })
            Text("بيبسي - 5.00 ر.س")
        }

        Spacer(Modifier.height(16.dp))

        // تحديد الكمية
        Text("الكمية", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                Icon(Icons.Default.Star, contentDescription = "تقليل")
            }
            Text("$quantity", fontSize = 18.sp)
            IconButton(onClick = { if (quantity < 5) quantity++ }) {
                Icon(Icons.Default.Add, contentDescription = "زيادة")
            }
        }

        Spacer(Modifier.height(24.dp))

        // زر الإضافة إلى السلة
        Button(
            onClick = { /* تنفيذ الإضافة */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
        ) {
            Text("أضف إلى السلة - ${"%.2f".format(total)} ر.س", fontSize = 18.sp)
        }
    }
}
