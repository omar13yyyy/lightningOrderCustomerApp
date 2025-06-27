package com.lightning.androidfrontend.ui.screens.home


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
import com.lightning.androidfrontend.data.model.OrderItem
import com.lightning.androidfrontend.data.model.OrdersType
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.theme.white
import com.lightning.androidfrontend.ui.screens.FilledButton
import com.lightning.androidfrontend.ui.screens.authentication.LoginState
import com.lightning.androidfrontend.utils.priceText


@Composable
fun Cart(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    locationViewModel:LocationViewModel
    ) {

    val context = LocalContext.current as Activity
    val getCartStore by homeScreenViewModel.getCartStore.collectAsState()


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
            ItemSection(
                onDecreaseClick = { },
                onIncreaseClick = { },
                homeScreenViewModel = homeScreenViewModel
            )
        }

        item {
            CouponBar(homeScreenViewModel=homeScreenViewModel)
        }
        item {
            DeliverySection(homeScreenViewModel=homeScreenViewModel,
                locationViewModel=locationViewModel)
        }
        item {
            getCartStore?.let {
                BillSection(

                    homeScreenViewModel=homeScreenViewModel,
                    deliveryFee = it.delivery_price
                )
            }
        }
    }
}




@Composable
fun BillSection(
    homeScreenViewModel: HomeScreenViewModel,
    deliveryFee: Double
) {
    val context = LocalContext.current

    val list = homeScreenViewModel.orderItems
    val storeOrder = homeScreenViewModel.storeOrder
    val getCouponDetailsResponse by homeScreenViewModel.getCouponDetailsResponse.collectAsState()
    val sendUserOrderState by homeScreenViewModel.sendUserOrderState.collectAsState()
    val sendUserOrderResponse by homeScreenViewModel.sendUserOrderResponse.collectAsState()
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
                        Text(text = "مبلغ المشتريات:")
                        Text(text = "مبلغ التوصيل:")
                        Text(text = "الاجمالي:")
                    }
                    Column {
                        if(getCouponDetailsResponse == null) {
                            Text(text = priceText(sumTotalOrderPrice(list)))
                            Text(text = priceText(deliveryFee))
                            Text(text = priceText(sumTotalOrderPrice(list) + deliveryFee))
                        }
                        else{
                            val annotatedSales = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        textDecoration=  TextDecoration.LineThrough
                                    )
                                ) {
                                    append(" ${sumTotalOrderPrice(list)} ")
                                }
                                append("${priceText( sumTotalOrderPrice(list)* getCouponDetailsResponse!!.discount_percentage)} ")

                            }
                            val annotateddelivery = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        textDecoration=  TextDecoration.LineThrough
                                    )
                                ) {
                                    append(" ${sumTotalOrderPrice(list)} ")
                                }
                                append("${priceText(deliveryFee* getCouponDetailsResponse!!.delivery_discount_percentage)} ")

                            }
                            val annotatedTotal = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        textDecoration=  TextDecoration.LineThrough
                                    )
                                ) {
                                    append(" ${sumTotalOrderPrice(list)+sumTotalOrderPrice(list)} ")
                                }
                                append("${priceText(deliveryFee* getCouponDetailsResponse!!.delivery_discount_percentage+
                                        sumTotalOrderPrice(list)* getCouponDetailsResponse!!.discount_percentage)} ")

                            }
                            Text(text = annotatedSales)
                            Text(text = annotateddelivery)
                            Text(text = annotatedTotal)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    when (sendUserOrderState) {
                        is SendUserOrderState.Idle , is SendUserOrderState.Error-> {
                            FilledButton(
                                text = "اطلب الان",
                                modifier = Modifier.padding(horizontal = 34.dp),
                                enabled = true
                            ) {
                                homeScreenViewModel.sendUserOrder()
                            }
                        }
                        is SendUserOrderState.Loading -> {
                            FilledButton(
                                text = "طلبك قيد المعالجة",
                                modifier = Modifier.padding(horizontal = 34.dp),
                                enabled = false
                            ){}                }
                        is SendUserOrderState.Success -> {
                            if(sendUserOrderResponse != null)
                            if(sendUserOrderResponse!!.success){
                            Toast.makeText(context, "تمت الموافقة على طلبك تابعه عن طريق قائمة الطلبات", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "تعذر معالجة طلبك الرجاء المحاولة بعد عدة دقائق", Toast.LENGTH_SHORT).show()

                            }

                        }

                    }

                }
            }
        }
    }
}


@Composable
fun CartItemCard(
    cartItem: OrderItem,
    onRemove :(OrderItem) ->Unit,
    itemIncrease :() ->Unit,
    itemdiscrease :() ->Unit

) {
    var showHint by remember { mutableStateOf(false) }

    var state by remember { mutableStateOf(cartItem.count) }
    var item by remember { mutableStateOf(cartItem) }

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
            Icon(
                imageVector = Icons.Filled.RemoveCircle,
                contentDescription = null,
                modifier = Modifier
                    .padding(3.dp, 0.dp)
                    .size(20.dp)
                    .clickable { onRemove(cartItem) }
            )
            Text(
                text = item.item_name,
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
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(3.dp, 0.dp)
                            .size(20.dp)
                            .clickable {
                                if (state != 0) {
                                    state--
                                    itemdiscrease()
                                    item = item.copy()
                                }
                            }
                    )
                    Text(text = state.toString())
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(3.dp, 0.dp)
                            .size(20.dp)
                            .clickable {
                                state++
                                itemIncrease()
                                item = item.copy()
                                Log.d("debugTag", "itemCardCount ${item.count}")

                            }
                    )
                }
            }
        }

        // السعر
        Text(
            text = priceText(sumTotalItemPrice(item)/item.count) + "للقطعة الواحدة",
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
                Text(text = cartItem.size_name)
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
fun CouponBar(
            homeScreenViewModel:HomeScreenViewModel
) {
    val getCartStore by homeScreenViewModel.getCartStore.collectAsState()
    val getStoreAppliedCoupon by homeScreenViewModel.getStoreAppliedCoupon.collectAsState()
    val getCouponDetailsState by homeScreenViewModel.getCouponDetailsState.collectAsState()
    val getCouponDetailsResponse by homeScreenViewModel.getCouponDetailsResponse.collectAsState()

    var text by remember { mutableStateOf("") }
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd,

            ) {


            when (getCouponDetailsState) {


              is  GetCouponDetailsState.Idle , is GetCouponDetailsState.Error-> {

                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            if(getCartStore?.couponCode != null)
                                Text(
                                    text =  "${getCartStore?.couponCode}تملك كود خصم ؟ اكتبه هنا جرب ",
                                    modifier = Modifier.alpha(0.5f)
                                )
                            else
                                Text(
                                    text =  "تملك كود خصم ؟ اكتبه هنا ",
                                    modifier = Modifier.alpha(0.5f)

                                )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 16.dp, shape = CircleShape),

                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            cursorColor = Color.White,

                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )
                    Row {
                        Text(
                            text = "APPLY",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                getCartStore?.let { homeScreenViewModel.getCouponDetails(storeId= it.store_id, couponCode =text) }

                            },
                            color = colors.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }


               is GetCouponDetailsState.Loading -> {


                }

                is GetCouponDetailsState.Success -> {
                }
            }
        }
    }
}

@Composable
fun DeliverySection(
    homeScreenViewModel:HomeScreenViewModel,
    locationViewModel:LocationViewModel
) {
    val selectedLocation by locationViewModel.selectedLocation.collectAsState()
    val getCartStore by homeScreenViewModel.getCartStore.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {


        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(16.dp),

        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    getCartStore?.let { Text(text = it.title) }
                    selectedLocation?.let {
                        Text(
                            text = it.name,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                    IconButton(onClick = {


                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "edit location",
                            modifier = Modifier.alpha(0.5f),
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(text = "الوقت التقديري:")
                    }
                    Column {

                        Text(text = getCartStore?.preparation_time.toString() + "د")
                    }


                }

            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemSection(
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel
) {
    val list = homeScreenViewModel.orderItems  // لا تستخدم remember

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
                if (list.isNotEmpty()) {
                    FlowColumn(modifier = Modifier.fillMaxWidth()) {
                        list.forEachIndexed { index, orderItem ->
                            CartItemCard(
                                cartItem = orderItem,
                                onRemove = {
                                    homeScreenViewModel.orderItems.removeAt(index)
                                },
                                itemIncrease = {
                                    list[index] = list[index].copy(count = list[index].count + 1)
                                },
                                itemdiscrease = {
                                    if (list[index].count > 0)
                                        list[index] = list[index].copy(count = list[index].count - 1)
                                },
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

                val inputValue = remember { mutableStateOf("") }
                val hintState = remember {
                    mutableStateOf(true)
                }

                Box {
                    BasicTextField(
                        value = inputValue.value,
                        onValueChange = { inputValue.value = it
                                        homeScreenViewModel.storeOrder.delivery_note =it
                                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .onFocusChanged {
                                if (inputValue.value == "") {
                                    hintState.value = !it.isFocused
                                }
                            }
                    )

                    if (hintState.value) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "اكتب ملاحظة للسائق ",
                                modifier = Modifier.alpha(0.5f),
                            )
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Back",
                                modifier = Modifier.alpha(0.5f),
                            )

                        }
                    }
                }
            }
        }

    }
}
fun sumTotalOrderPrice(list: SnapshotStateList<OrderItem>): Double {
    var total = 0.0
    list.forEach { item ->
        total += sumTotalItemPrice(item)
    }
    return total
}