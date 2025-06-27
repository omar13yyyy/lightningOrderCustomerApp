package com.lightning.androidfrontend.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.lightning.androidfrontend.data.model.StoresData
import com.lightning.androidfrontend.data.model.UserOrder
import com.lightning.androidfrontend.data.model.UserOrdersRes
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.placeholderColor
import com.lightning.androidfrontend.ui.components.OrderCard
import com.lightning.androidfrontend.ui.components.RestaurantCard
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrdersScreen(homeScreenViewModel: HomeScreenViewModel,
                 navController:NavHostController
) {
    val previousCustomerOrderState by homeScreenViewModel.previousCustomerOrderState.collectAsState()
    val previousCustomerOrderResponse by homeScreenViewModel.previousCustomerOrderResponse.collectAsState()
    val currentCustomerOrderState by homeScreenViewModel.currentCustomerOrderState.collectAsState()
    val currentCustomerOrderResponse by homeScreenViewModel.currentCustomerOrderResponse.collectAsState()
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = now.format(formatter)

    val listStateCurrentOrders = rememberLazyListState()
    var itemsCurrentOrders by remember { mutableStateOf(listOf<UserOrder>()) }
    var offsetCurrentOrders by remember { mutableStateOf(formatted) }
    val limitCurrentOrders = 10
    var isLoadingCurrentOrders by remember { mutableStateOf(false) }

    val listStatePreviousOrders = rememberLazyListState()
    var itemsPreviousOrders by remember { mutableStateOf(listOf<UserOrder>()) }
    var offsetPreviousOrders by remember { mutableStateOf(formatted) }
    val limitPreviousOrders = 10
    var isLoadingPreviousOrders by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier.background(color = Color.White)
    ) {




        LaunchedEffect(currentCustomerOrderState) {
            when (currentCustomerOrderState) {
                is CurrentCustomerOrderState.Success -> {
                    val newItems: List<UserOrder> = currentCustomerOrderResponse?.order.orEmpty()

                    if (offsetCurrentOrders == formatted) {
                        itemsCurrentOrders = newItems
                    } else {

                        itemsCurrentOrders += newItems // تحميل إضافي

                    }
                    isLoadingCurrentOrders = false
                }

                else -> {}
            }
        }
        LaunchedEffect(listStateCurrentOrders) {
            snapshotFlow { listStateCurrentOrders.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { index ->

                    if ((index == itemsCurrentOrders.lastIndex && !isLoadingCurrentOrders) || index == null) {
                        isLoadingCurrentOrders = true
                        if (itemsCurrentOrders.lastIndex > 0)
                            offsetCurrentOrders = itemsCurrentOrders.last().created_at

                        homeScreenViewModel.currentCustomerOrder(
                            dateOffset = offsetCurrentOrders,
                            limit = limitCurrentOrders,
                        )


                    }
                }
        }

        LazyColumn(state = listStateCurrentOrders) {
            if (itemsCurrentOrders.lastIndex < 100) {
                items(itemsCurrentOrders) { item ->
                    OrderCard(
                        order = item,
                        navController = navController,
                        homeScreenViewModel = homeScreenViewModel,
                        orderId = item.order_id,
                        orderStatus = item.status.toString(),
                        storeName = item.store_name_ar,
                        orderType = item.orders_type.toString(),
                        createdAt = item.created_at,
                        totalPrice = item.amount + item.delivery_fee
                    )
                }
            }
        }
    }


    LaunchedEffect(previousCustomerOrderState) {
        when (previousCustomerOrderState) {
            is PreviousCustomerOrderState.Success -> {
                val newItems: List<UserOrder> = previousCustomerOrderResponse?.order.orEmpty()

                if (offsetPreviousOrders == formatted) {
                    itemsPreviousOrders = newItems
                } else {

                    itemsPreviousOrders += newItems // تحميل إضافي

                }
                isLoadingPreviousOrders = false
            }

            else -> {}
        }
    }
    LaunchedEffect(listStatePreviousOrders) {
        snapshotFlow { listStatePreviousOrders.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                Log.d("debugTag", "index $index ")

                if ((index == itemsPreviousOrders.lastIndex && !isLoadingPreviousOrders) || index == null) {
                    isLoadingPreviousOrders = true
                    if (itemsPreviousOrders.lastIndex > 0)
                        offsetPreviousOrders = itemsPreviousOrders.last().created_at


                    homeScreenViewModel.previousCustomerOrder(
                        dateOffset = offsetPreviousOrders,
                        limit = limitPreviousOrders,
                    )

                }
            }
    }

    LazyColumn( state = listStatePreviousOrders) {
        if (itemsPreviousOrders.lastIndex < 100) {
            items(itemsPreviousOrders) { item ->

                OrderCard(
                    order = item,
                    navController = navController,
                    homeScreenViewModel = homeScreenViewModel,
                    orderId = item.order_id,
                    orderStatus = item.status.toString(),
                    storeName = item.store_name_ar,
                    orderType = item.orders_type.toString(),
                    createdAt = item.created_at,
                    totalPrice = item.amount + item.delivery_fee
                )
            }
        }
    }

    

}