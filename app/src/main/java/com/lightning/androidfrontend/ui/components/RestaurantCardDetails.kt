package com.lightning.androidfrontend.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.data.model.StoresData
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.utils.priceText
import com.lightning.androidfrontend.utils.getCurrency

@Composable
fun RestaurantCardDetails(
    storesData: StoresData
    // navigator: DestinationsNavigator
) {
    val colors = LocalAppColors.current


    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = colors.onPrimary
        ),

        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 6.dp)
            .fillMaxWidth(),
        onClick = {

        }
    ) {
        Column {
            ImageLargeCardDetails(
                coverImageUrl = storesData.cover_image_url,
                discountPercentage = storesData.discount_value_percentage,
                deliveryDiscountPercentage = storesData.delivery_discount_percentage,
                couponMinOrder = storesData.coupon_min_order_value,
                couponCode = storesData.couponCode,
            )
            ContentLargeCardDetails(
                storeName = storesData.title,
                logoImageUrl = storesData.logo_image_url,
                storeTags = storesData.tags,
                deliveryTimeInMins = storesData.preparation_time,
                deliveryDistanceInKms = storesData.distance_km,
                minOrderPrice = storesData.min_order_price,
                status = storesData.status,
                rating = storesData.rating_previous_day,
                ratersNumber = storesData.number_of_raters,
                deliveryPrice = storesData.delivery_price,
                isHaveCoupon = false,
                isHaveCouponDelivery=false,
            )
        }
    }
}

@Composable
fun ImageLargeCardDetails(
    coverImageUrl: String,
    discountPercentage: Double?,
    deliveryDiscountPercentage: Double?,
    couponMinOrder: Double?,
    couponCode: String?,
) {
    var isFavourite by remember {
        mutableStateOf(false)
    }


    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .height((LocalConfiguration.current.screenWidthDp / 1.8).dp)
            .fillMaxWidth()
            .background(Color.DarkGray)
    ) {
        AsyncImage(
            model = coverImageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds

        )


        Column {

            Column(
                modifier = Modifier.height((LocalConfiguration.current.screenWidthDp / 1.5).dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .alpha(0.6f)
                            .background(Color.Black)

                    )
                    {

                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { isFavourite = !isFavourite },
                        contentAlignment = Alignment.Center
                    ) {

                    }
                }
                Row(
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                            contentColor = colors.onPrimary
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp, horizontal = 7.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.finance_percentage),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(15.dp)
                            )
                            if(couponCode !=null)
                            Column {
                                Text(
                                    text = "${discountPercentage.toString()}% OFF",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .alpha(0.8f)
                                        .padding(vertical = 5.dp, horizontal = 5.dp),
                                )
                                Text(
                                    text = "${deliveryDiscountPercentage.toString()}% OFF",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .alpha(0.8f)
                                        .padding(vertical = 5.dp, horizontal = 5.dp),
                                )
                                Text(
                                    text = "min${couponMinOrder.toString()}",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .alpha(0.8f)
                                        .padding(vertical = 5.dp, horizontal = 5.dp),
                                )
                            }
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                            contentColor = colors.onPrimary
                        ),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 3.dp, horizontal = 4.dp)
                        ) {


                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun ContentLargeCardDetails(
    storeName: String,
    logoImageUrl: String,
    storeTags: List<String>,
    deliveryTimeInMins: Int,
    deliveryDistanceInKms: Double,
    minOrderPrice: Double,
    status: String,
    rating: Double,
    ratersNumber: Int,
    deliveryPrice:Double,
    isHaveCoupon: Boolean,
    isHaveCouponDelivery: Boolean,

    ) {
    Column {

        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = logoImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(40.dp)


                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(storeName, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                    color = Color.Black)
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                Text(
                    text = "${rating} (${ratersNumber})",
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                storeTags.joinToString(separator = "-"),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFFFA000)
                )

                Text(
                    text = "$deliveryDistanceInKms",
                    fontSize = 13.sp,
                    color = Color.Black
                )
                VerticalDivider()
                Icon(    painter = painterResource(id = R.drawable.delivery), modifier = Modifier.size(13.dp), contentDescription = null, tint = Color.Gray)

                if(!isHaveCouponDelivery)
                    Text(
                        text = priceText(deliveryPrice),
                        fontSize = 13.sp,
                        color = Color.Black,
                    )

                else
                    Text(
                        text = priceText(deliveryPrice),
                        fontSize = 13.sp,
                        color = Color.Black,
                        textDecoration = TextDecoration.LineThrough
                    )
                VerticalDivider()
                Icon(    painter = painterResource(id = R.drawable.delivery), modifier = Modifier.size(13.dp), contentDescription = null, tint = Color.Gray)

                Text(
                    text = "${deliveryTimeInMins}د",
                    fontSize = 13.sp,
                    color = Color.Black
                )
                VerticalDivider()
                if(!isHaveCoupon)
                    Text(
                        text = priceText(minOrderPrice),

                        color = Color(0xFFFFA000),
                        fontWeight = FontWeight.Bold
                    )
                else Text(
                    text = priceText(minOrderPrice),
                    color = Color(0xFFFFA000),
                    fontWeight = FontWeight.Bold,textDecoration = TextDecoration.LineThrough
                )
                VerticalDivider()

                Text("مفتوح", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
            }
        }

    }
    }


