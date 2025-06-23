package com.lightning.androidfrontend.ui.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lightning.androidfrontend.data.model.MenuCategory
import com.lightning.androidfrontend.data.model.MenuData
import com.lightning.androidfrontend.data.model.MenuItem
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.ui.components.RestaurantCardDetails
import com.lightning.androidfrontend.utils.priceText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@RequiresApi(35)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun storeDetailsScreen(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    navController: NavController
) {
    val colors = LocalAppColors.current

    val getSelectedStore by homeScreenViewModel.getSelectedStore.collectAsState()
    val getStoreProductsState by homeScreenViewModel.getStoreProductsState.collectAsState()
    val getMenuCategoryItems by homeScreenViewModel.getMenuCategoryItems.collectAsState()
    val listStateOuter = rememberLazyListState()
    val listStateInner = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        navController.popBackStack()
        homeScreenViewModel.setStoreProductsState(GetStoreProductsState.Idle)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        TopAppBar(
            title = { getSelectedStore?.let { Text(it.title) } },
            actions = {
                IconButton(onClick = { /* TODO: Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = null)
                }
            }
        )
        Scaffold(
            modifier = Modifier.background(color = colors.background),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                        navController.navigate(route = HomeScreens.CART.name) {
                            launchSingleTop =
                                true
                            restoreState =
                                true
                            popUpTo(route = HomeScreens.CART.name) {
                            }
                        }

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            content = { padding ->
                // محتوى الشاشة
                Box(
                    modifier = Modifier.background(color = colors.background)
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                }

        LazyColumn(state = listStateOuter,
            modifier = Modifier.fillMaxSize()) {
            item {
                Column(modifier = Modifier.height(400.dp)){
            getSelectedStore?.let { RestaurantCardDetails(it) }
                }
            }
            item {
                Spacer(modifier = Modifier.size(18.dp))
            }
            item {


                Spacer(modifier = Modifier.size(18.dp))
            }
            when (getStoreProductsState) {
                is GetStoreProductsState.Idle -> {
                    if (!homeScreenViewModel.getStoreCategoryHasRequested.value)
                        getSelectedStore?.let { homeScreenViewModel.getStoreProducts(it.store_id) }
                    homeScreenViewModel.getStoreCategoryHasRequested.value = true
                }

                is GetStoreProductsState.Error -> {}

                is GetStoreProductsState.Loading -> {
                    item {
                        StoresCategoriesSkeleton()
                    }
                }

                is GetStoreProductsState.Success -> {
                    getMenuCategoryItems?.let {
                        stickyHeader {
                        MenuCategory(
                            menu = it,
                            homeScreenViewModel = homeScreenViewModel,
                            navController = navController,
                            listStateInner = listStateInner,
                            listStateOuter =listStateOuter,
                            coroutineScope=coroutineScope)
                        }
                        item {
                            RestaurantMenuScreen(
                                menu = it,
                                homeScreenViewModel = homeScreenViewModel,
                                navController = navController,
                                listStateInner = listStateInner,

                            )
                        }
                    }
                }
            }
            }
        })
    }
}


@RequiresApi(35)
@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@Composable
fun RestaurantMenuScreen(
    menu: List<MenuCategory>,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel,
    listStateInner : LazyListState,

    ) {
    val colors = LocalAppColors.current
    val initMenu = remember(menu) {
        val bestSalesItems = mutableListOf<MenuItem>()
        val notEmptyCategory = mutableListOf<MenuCategory>()

        menu.forEach { category ->
            if (category.items.isNotEmpty()) {
                notEmptyCategory.add(category)
                category.items.forEach { item ->
                    if (item.is_best_seller) {
                        bestSalesItems.add(item)
                    }
                }
            }
        }

        val finalMenu = mutableListOf<MenuCategory>()

        if (bestSalesItems.isNotEmpty()) {
            finalMenu.add(
                MenuCategory(
                    name = "الأكثر مبيعًا",
                    order = -1,
                    category_id = "0",
                    items = bestSalesItems
                )
            )
        }

        finalMenu.addAll(notEmptyCategory)
        finalMenu
    }



    LazyColumn(state = listStateInner,modifier = Modifier
        .fillMaxWidth()
        .height(800.dp) // ⬅️ مهم جدًا تحديد الارتفاع
    ){
        itemsIndexed(initMenu, key = { _, item -> item.category_id }) { index, category ->

                Column {
                    // عنوان الفئة
                    Text(
                        text = category.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        letterSpacing = 0.5.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontFamily = metropolisFontFamily),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }

                // عناصر الفئة
                category.items.forEach { item ->
                    var externalPrice: Double = 99999999.0
                    var showHint by remember { mutableStateOf(false) }
                    item.sizes.forEach { size ->
                        if (externalPrice > size.price)
                            externalPrice = size.price
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onProductClicked(
                                    navController = navController,
                                    homeScreenViewModel = homeScreenViewModel,
                                    product = item
                                )
                            }
                            .border(
                                1.dp,
                                color = colors.productDetailsColorLighter,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colors.background,
                            ),
                            onClick = {
                                onProductClicked(
                                    navController = navController,
                                    homeScreenViewModel = homeScreenViewModel,
                                    product = item
                                )
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .height(IntrinsicSize.Min)
                            ) {
                                AsyncImage(
                                    model = item.image_url,
                                    contentDescription = item.name,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = item.description,
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = priceText(externalPrice),
                                        fontSize = 14.sp,
                                        color = Color(0xFF388E3C),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                }

            }
        }
    }





@Composable
fun MenuData.getCurrencySymbol(): String {
    return "ل.س"
}

fun onProductClicked(
    product: MenuItem,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    homeScreenViewModel.setSelectedProduct(product)
    navController.navigate(route = HomeScreens.PRODUCT_DETAILS_SCREEN.name) {
        launchSingleTop =
            true           // لا يعيد إنشاء الشاشة إذا هي موجودة في القمة
        restoreState =
            true             // يستعيد حالة الشاشة المحفوظة (مثل scroll position، المتغيرات، ...)
        popUpTo(route = HomeScreens.PRODUCT_DETAILS_SCREEN.name) {  // يحدد نقطة العودة
            saveState = true            // يحفظ حالة هذه الشاشة عند الخروج منها
        }
    }
}

@RequiresApi(35)
@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@Composable
fun MenuCategory(
    menu: List<MenuCategory>,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel,
    listStateInner : LazyListState,
    listStateOuter : LazyListState,

    coroutineScope :CoroutineScope
) {
    val colors = LocalAppColors.current
    val initMenu = remember(menu) {
        val bestSalesItems = mutableListOf<MenuItem>()
        val notEmptyCategory = mutableListOf<MenuCategory>()

        menu.forEach { category ->
            if (category.items.isNotEmpty()) {
                notEmptyCategory.add(category)
                category.items.forEach { item ->
                    if (item.is_best_seller) {
                        bestSalesItems.add(item)
                    }
                }
            }
        }

        val finalMenu = mutableListOf<MenuCategory>()

        if (bestSalesItems.isNotEmpty()) {
            finalMenu.add(
                MenuCategory(
                    name = "الأكثر مبيعًا",
                    order = -1,
                    category_id = "-1",
                    items = bestSalesItems
                )
            )
        }

        finalMenu.addAll(notEmptyCategory)
        finalMenu
    }
    val categoryIndexMap = remember(initMenu) {
        val indexMap = mutableMapOf<String, Int>()
        var index = 0
        for (category in initMenu) {
            indexMap[category.category_id] = index
            index++ // للفئة
           // index += category.items.size // للعناصر
        }
        indexMap
    }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFA500)) // لون برتقالي (Orange)
                .padding(8.dp)
    ) {
        items(initMenu) { category ->
            Button(
                onClick = {
                    categoryIndexMap[category.category_id]?.let { index ->
                        coroutineScope.launch {
                            listStateOuter.animateScrollToItem(3)
                            Log.d("debugTag", "listStateOuter index : $index")

                            listStateInner.animateScrollToItem(index=index )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(text = category.name)
            }
        }

}
}
