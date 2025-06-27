package com.lightning.androidfrontend.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lightning.androidfrontend.data.model.OrderStatus
import com.lightning.androidfrontend.data.model.UserOrder
import com.lightning.androidfrontend.ui.screens.home.HomeScreenViewModel
import com.lightning.androidfrontend.ui.screens.home.HomeScreens
import com.lightning.androidfrontend.utils.priceText


@Composable
fun OrderCard(
    navController:NavController,
    homeScreenViewModel:HomeScreenViewModel,
    order: UserOrder,
    orderId: String,
    orderStatus: String,
    storeName: String,
    orderType: String,
    createdAt: String,
    totalPrice: Double
){
    var status = ""
    if(orderStatus == com.lightning.androidfrontend.data.model.OrderStatus.DELEVERED.label)
        status="تم"
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth().clickable {
                navController.navigate(route = HomeScreens.ORDER_DETAILS.name) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(route = HomeScreens.ORDER_DETAILS.name) {
                        saveState = true
                    }
                    homeScreenViewModel.setSelectedOrderDetails(order)

                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F9F9),

        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top row: Order ID + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "الطلب #$orderId",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Store Name
            Text(
                text = storeName,
                fontSize = 15.sp,
                color = Color(0xFF555555),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = status,
                fontSize = 14.sp,
                color = if (status == "تم") Color(0xFF4CAF50) else Color(0xFFFF9800),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Order Type + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = orderType,
                    fontSize = 14.sp,
                    color = Color(0xFF777777)
                )
                Text(
                    text = createdAt,
                    fontSize = 14.sp,
                    color = Color(0xFF999999)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total Price
            Text(
                text = "الاجمالي : ${priceText(totalPrice)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )
        }
    }


}
