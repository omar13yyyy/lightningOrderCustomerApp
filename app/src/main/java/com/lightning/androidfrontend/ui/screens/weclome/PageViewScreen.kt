package com.lightning.androidfrontend.ui.screens.weclome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.orange
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.ui.components.PageItem
import com.lightning.androidfrontend.ui.screens.BorderButton
import com.lightning.androidfrontend.ui.screens.FilledButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageViewScreen(navigateToWelcomeScreen: () -> Unit){
    val pagerState = rememberPagerState(0, 0F, { 0});
    val scope = rememberCoroutineScope()
    val list = arrayOf(
        com.lightning.androidfrontend.ui.components.PageItem(
            image = R.drawable.ic_page_1,
            title = "Find Food You Love",
            subTitle = "Discover the best foods from over 1,000 restaurants and fast delivery to your doorstep"
        ),
        com.lightning.androidfrontend.ui.components.PageItem(
            image = R.drawable.ic_page_2,
            title = "Fast Delivery",
            subTitle = "Fast food delivery to your home, office wherever you are"
        ),
        com.lightning.androidfrontend.ui.components.PageItem(
            image = R.drawable.ic_page_3,
            title = "Live Tracking",
            subTitle = "Real time tracking of your food on the app once you placed the order"
        )
    )
    MyAppTheme {
        HorizontalPager(
            //beyondBoundsPageCount =list.size,
            state = pagerState
        ) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = list[index].image), contentDescription = null)
                Spacer(modifier = Modifier.height(30.dp))
                Indicator(count = list.size, index = index)
                Spacer(modifier = Modifier.height(35.dp))
                Text(
                    text = list[index].title,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = metropolisFontFamily,
                        color = primaryFontColor
                    )
                )
                Spacer(modifier = Modifier.height(33.dp))
                Text(
                    text = list[index].subTitle,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = metropolisFontFamily,
                        color = secondaryFontColor,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 45.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                FilledButton(modifier = Modifier.padding(horizontal = 34.dp), text = "Next") {
                    scope.launch {
                        if (index < list.size - 1) {
                            pagerState.animateScrollToPage(index + 1)
                        } else {
                            navigateToWelcomeScreen()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                BorderButton(modifier = Modifier.padding(horizontal = 34.dp), text = "Skip", color = secondaryFontColor) {
                    navigateToWelcomeScreen()
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
@Composable
fun Indicator(count: Int, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (i == index) orange else Color.LightGray)
            )
            Spacer(modifier = Modifier.size(5.dp))
        }
    }
}
