package com.lightning.androidfrontend.ui.screens.home


import NetworkClient.gson
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.FlowColumn

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.google.gson.reflect.TypeToken
import com.lightning.androidfrontend.data.model.OrderItem
import com.lightning.androidfrontend.data.model.OrdersType
import com.lightning.androidfrontend.data.model.ResolvedOrderItem
import com.lightning.androidfrontend.data.model.TotalResolved
import com.lightning.androidfrontend.data.model.UserOrdersRes
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.theme.white
import com.lightning.androidfrontend.ui.screens.FilledButton
import com.lightning.androidfrontend.ui.screens.authentication.LoginState
import com.lightning.androidfrontend.utils.priceText


@Composable
fun OrderDetails(
    navController: NavHostController,
    homeScreenViewModel:HomeScreenViewModel
    ) {

    val context = LocalContext.current as Activity
    val selectedOrderDetails by homeScreenViewModel.selectedOrderDetails.collectAsState()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFE8E7E7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
            }
        }

        item {
            val type = object : TypeToken<TotalResolved>() {}.type

            val json :TotalResolved = gson.fromJson(selectedOrderDetails?.order_details_text, type)

            OrderItemSection( orderItems = json.orderAR ,
                json.deliveryNote
                )
        }


        item {
            selectedOrderDetails?.let {
                OrderBillSection(
                    deliveryFee = it.delivery_fee,
                    couponCode = it.coupon_code,
                    totalPrice = it.amount
                )
            }
        }
    }
}




@Composable
fun OrderBillSection(
    deliveryFee: Double,
    couponCode :String,
    totalPrice :Double,
) {
    val context = LocalContext.current


    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(16.dp),
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "تفاصيل الفاتورة", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = "كوبون ")
                        Text(text = "مبلغ المشتريات:")
                        Text(text = "مبلغ التوصيل:")
                        Text(text = "الاجمالي:")
                    }
                    Column {
                            Text(text = if (couponCode != "null") couponCode else " لا يوجد كوبون")

                            Text(text = priceText(totalPrice))
                            Text(text = priceText(deliveryFee))
                            Text(text = priceText(totalPrice + deliveryFee))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

        }
    }
}
    }


@Composable
fun OrderCartItemCard(
    cartItem: ResolvedOrderItem,

) {
    var showHint by remember { mutableStateOf(false) }


    val colors = LocalAppColors.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // الاسم
        Row(
            modifier = Modifier
                .weight(1f) // <-- حل المشكلة هنا
                .clickable { showHint = !showHint },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = cartItem.itemName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // الكمية
        Column(
            modifier = Modifier.weight(0.5f), // توزيع المساحة
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.border(
                    BorderStroke(1.dp, Color.Black.copy(0.5f)),
                    MaterialTheme.shapes.medium
                ),
                shape = MaterialTheme.shapes.medium,
                color = Color.White,
                contentColor = colors.primary
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = cartItem.count.toString())


                }
            }
        }

        // السعر
        Text(
            text = priceText(sumTotalResolvedItemPrice(cartItem)/cartItem.count) + "للقطعة الواحدة",
            modifier = Modifier.weight(0.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )


    }
    if (showHint) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = colors.productDetailsColorLighter,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(text = cartItem.sizeName)
                cartItem.modifiers.forEach {
                    modifier->
                    Text(text = modifier.title+ " : ")
                    modifier.items.forEach {
                        modifierItems->
                        Text(text = modifierItems.name)

                    }
                }

            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCouponBar(
) {
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd,

            ) {


            Text(
                text =  "كود الخصم هو ",
                modifier = Modifier.alpha(0.5f)
            )
            Text(
                text =  "كود الخصم هو ",
                modifier = Modifier.alpha(0.5f)
            )


        }
    }
}



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderItemSection(
    orderItems:List<ResolvedOrderItem>,
    deliveryNote : String

) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(16.dp),
        ) {
            Column(Modifier.padding(16.dp)) {
                if (orderItems.isNotEmpty()) {
                    FlowColumn(modifier = Modifier.fillMaxWidth()) {
                        orderItems.forEachIndexed { index, orderItem ->
                            OrderCartItemCard(
                                cartItem = orderItem,

                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(text = "Empty")
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 8.dp)
                )

                Box {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ملاحظة السائق: $deliveryNote",
                                modifier = Modifier.alpha(0.5f),
                            )



                    }
                }
            }
        }

    }
}
fun sumTotalResolvedItemPrice(orderItem :ResolvedOrderItem): Double {

    var modifiersPrice :Double =0.0

    orderItem.modifiers.forEach {
            modifier ->
        modifier.items.forEach {
                modifierItem ->
            modifiersPrice += modifiersPrice
        }
    }
    return (modifiersPrice+ orderItem.sizePrice) * orderItem.count

}