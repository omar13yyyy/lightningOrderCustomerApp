package com.lightning.androidfrontend.ui.components.more

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.orange
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.ui.components.More
import com.lightning.androidfrontend.ui.screens.AppTopBar

class About(override var icon: Int = 0, override var name: String = "") :
    com.lightning.androidfrontend.ui.components.More {

    override fun setObjectToSend(): com.lightning.androidfrontend.ui.components.More {
        return com.lightning.androidfrontend.ui.components.more.About()
    }

    @Composable
    override fun setContent() {
        val list = listOf(
            "Your orders has been picked up Your orders has been picked up Your orders has been picked upYour orders has been picked up",
            "Your orders has been picked up Your orders has been picked up Your orders has been picked upYour orders has been picked up",
            "Your orders has been picked up Your orders has been picked up Your orders has been picked upYour orders has been picked up",
            "Your orders has been picked up Your orders has been picked up Your orders has been picked upYour orders has been picked up",
        )
        MyAppTheme() {
            Column {
                AppTopBar(title = "About Us")
                Spacer(modifier = Modifier.height(15.dp))
                LazyColumn{
                    items(list) { item ->
                        Column() {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 20.dp)) {
                                Icon(painter = painterResource(id = R.drawable.ic_circle), contentDescription = "", tint = orange)
                                Spacer(modifier = Modifier.width(30.dp))
                                Text(item, style = TextStyle(fontSize = 15.sp, fontFamily = metropolisFontFamily, color = primaryFontColor))
                            }
                        }
                    }
                }
            }
        }
    }
}