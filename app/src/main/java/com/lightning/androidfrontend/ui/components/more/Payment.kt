package com.lightning.androidfrontend.ui.components.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.theme.gray
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.orange
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.ui.components.More
import com.lightning.androidfrontend.ui.screens.*

data class PaymentItem(val image: Int, val num: String)

class Payment(override var icon: Int = 0, override var name: String = "") :
    com.lightning.androidfrontend.ui.components.More {

    override fun setObjectToSend(): com.lightning.androidfrontend.ui.components.More {
        return Payment()
    }

    @Composable
    override fun setContent() {
        val list = listOf(
            PaymentItem(R.drawable.visa, "123 456   789"),
            PaymentItem(R.drawable.visa, "123 456   789"),
            PaymentItem(R.drawable.visa, "123 456   789"),
        )
        Column {
            AppTopBar(title = "Your Payments", backIcon = true)
            Text("Customize your payment method", modifier = Modifier.padding(20.dp), style = TextStyle(color = primaryFontColor, fontWeight = FontWeight.Bold, fontFamily = metropolisFontFamily, fontSize = 15.sp))
            Divider(modifier = Modifier.padding(start = 25.dp, end = 20.dp, bottom = 20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp)
                    .background(gray), contentAlignment = Alignment.Center
            ) {
                LazyColumn {
                    items(list) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Image(
                                painter = painterResource(id = item.image),
                                contentDescription = null,
                                modifier = Modifier.size(55.dp, 40.dp)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(text = item.num)
                            Spacer(modifier = Modifier.fillMaxWidth(0.5f))
                            BorderButton(
                                text = "Delete",
                                modifier = Modifier.size(85.dp, 30.dp),
                                fontSize = 13
                            ) {}
                        }
                        Divider()
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .height(35.dp)
                    .padding(horizontal = 20.dp)
            )
            ButtonWithImage(
                text = "Add Another Credit Card",
                image = R.drawable.ic_add,
                color = orange,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {}
        }

    }
}