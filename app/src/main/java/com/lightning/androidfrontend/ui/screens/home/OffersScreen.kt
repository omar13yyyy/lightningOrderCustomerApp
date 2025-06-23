package com.lightning.androidfrontend.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.ui.screens.FilledButton
import com.lightning.androidfrontend.ui.screens.AppTopBar

@Composable
fun OffersScreen(){
    MyAppTheme() {
        Column() {
            AppTopBar(title = "Latest Offers", backIcon = false)
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "Find discounts, \nOffers special meals and more!",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = metropolisFontFamily,
                    color = secondaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(17.dp))
            FilledButton(text = "Check Offers", modifier = Modifier
                .height(35.dp)
                .width(160.dp)
                .padding(horizontal = 20.dp),
                fontSize = 12) {}
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(testList2) { item ->
                    PopularRestaurantItem(item)
                }
            }
        }
    }
}