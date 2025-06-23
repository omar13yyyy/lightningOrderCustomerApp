package com.lightning.androidfrontend.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.LatLng
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.data.model.GetStoreCategoriesRes
import com.lightning.androidfrontend.data.model.StoresData
import com.lightning.androidfrontend.data.repository.UserRepository.VisitorRepository
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.theme.gray
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.orange
import com.lightning.androidfrontend.theme.placeholderColor
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.ui.components.OptionDialog
import com.lightning.androidfrontend.ui.components.RestaurantCard
import com.lightning.androidfrontend.ui.screens.SearchField
import com.lightning.androidfrontend.utils.loadLocations
import com.lightning.androidfrontend.utils.saveLocations

val testList2 = listOf(
    com.lightning.androidfrontend.ui.components.PopularRestaurant(
        name = "Minute by tuk tuk",
        rate = 4.9f,
        rateCount = 124,
        foodKind = "Western Food",
        coverImage = R.drawable.restaurent_a
    ),
    com.lightning.androidfrontend.ui.components.PopularRestaurant(
        name = "Café de Noir",
        rate = 4.8f,
        rateCount = 137,
        foodKind = "Italian Food",
        coverImage = R.drawable.restaurent_b
    ),
    com.lightning.androidfrontend.ui.components.PopularRestaurant(
        name = "Bakes by Tella",
        rate = 4.7f,
        rateCount = 125,
        foodKind = "French Food",
        coverImage = R.drawable.restaurent_c
    ),
)

val testList4 = listOf(
    com.lightning.androidfrontend.ui.components.PopularRestaurant(
        name = "Mulberry Pizza by Josh",
        rate = 4.9f,
        rateCount = 124,
        foodKind = "Western Food",
        coverImage = R.drawable.item_a
    ),
    com.lightning.androidfrontend.ui.components.PopularRestaurant(
        name = "Barita",
        rate = 4.8f,
        rateCount = 137,
        foodKind = "Italian Food",
        coverImage = R.drawable.item_b

    ),
    com.lightning.androidfrontend.ui.components.PopularRestaurant(
        name = "Pizza Rush Hour",
        rate = 4.7f,
        rateCount = 125,
        foodKind = "French Food",
        coverImage = R.drawable.item_c
    ),
)

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    navController: NavController,
    locationViewModel: LocationViewModel = viewModel()

) {

    val colors = LocalAppColors.current
    val getStoreCategoryState by homeScreenViewModel.getStoreCategoryState.collectAsState()
    val getStoreCategoryResponse by homeScreenViewModel.getStoreCategoryResponse.collectAsState()
    val getNearStoresState by homeScreenViewModel.getNearStoresState.collectAsState()
    val getNearStoresResponse by homeScreenViewModel.getNearStoresResponse.collectAsState()
    val getStoresByCategoryState by homeScreenViewModel.getStoresByCategoryState.collectAsState()
    val getStoresByCategoryResponse by homeScreenViewModel.getStoresByCategoryResponse.collectAsState()
    val context = LocalContext.current


    val selectedLocation by locationViewModel.selectedLocation.collectAsState()

    var showDialog by remember { mutableStateOf(false) }


    var options by remember { mutableStateOf(loadLocations(context = context)) }
    var searchText by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(isPressed) {
        if (isPressed) {

            navController.navigate(route = HomeScreens.SEARCH.name) {
                launchSingleTop = true
                restoreState = true
                popUpTo(route = HomeScreens.SEARCH.name) {
                    saveState = true
                }

            }
        }
    }

    if (showDialog) {
        OptionDialog(
            options = options,
            onAddOption = { newOpt -> options = options + newOpt },
            onDismiss = { showDialog = false },
            onOptionSelected = { entry ->
                entry.selected = true
                locationViewModel.setSelectedLocation(entry)
                homeScreenViewModel.setLocation(entry)
                homeScreenViewModel.getStoresCategories()
                homeScreenViewModel.getNearStores(limit = 20, offset = 0)


            },
            onOptionSave = { entrys ->
                saveLocations(context, entrys)
            }
        )
    }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location Icon",
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDialog = true }
                            .padding(16.dp)
                    ) {
                        Row {
                            Text(
                                text = "Location",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Location Drop Down",
                                tint = Color.Black
                            )
                        }
                        selectedLocation?.let {
                            Text(
                                text = it.name,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }
        }
        //--> Top Section
        item {
            TopBar("OMAR")

        }
        item {
            //TopBar("Mahmoud")

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                value = searchText,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = gray,
                    cursorColor = orange,
                    disabledLabelColor = gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                interactionSource = interactionSource,
                onValueChange = {
                    searchText = it
                },
                placeholder = {
                    Text(
                        text = "ابحث عن متجرك المفضل",
                        style = TextStyle(
                            color = placeholderColor,
                            fontSize = 14.sp,
                            fontFamily = metropolisFontFamily
                        )
                    )
                },
                shape = RoundedCornerShape(28.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                    }
                ),
                singleLine = true,
                textStyle = TextStyle(
                    color = primaryFontColor,
                    fontSize = 15.sp,
                    fontFamily = metropolisFontFamily
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search icon",
                        modifier = Modifier.padding(start = 14.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.size(18.dp))
            when (getStoreCategoryState) {
                is GetStoreCategoryState.Idle -> {
                    if (!homeScreenViewModel.getStoreCategoryHasRequested.value) {
                        homeScreenViewModel.getStoresCategories()
                        homeScreenViewModel.getStoreCategoryHasRequested.value = true
                    } else {

                        StoresCategories(
                            list = getStoreCategoryResponse,
                            navController = navController
                        )

                    }
                }

                is GetStoreCategoryState.Error -> {}

                is GetStoreCategoryState.Loading -> {
                    StoresCategoriesSkeleton()
                }

                is GetStoreCategoryState.Success -> {
                    StoresCategories(list = getStoreCategoryResponse, navController = navController)

                }

            }



            Spacer(modifier = Modifier.size(25.dp))
        }

        //--> PopularRestaurants Section
        item {
            SectionHeader(sectionName = "Popular Restaurants") {

                navController.navigate(route = HomeScreens.SEARCH.name) {
                    launchSingleTop = true           // لا يعيد إنشاء الشاشة إذا هي موجودة في القمة
                    restoreState =
                        true             // يستعيد حالة الشاشة المحفوظة (مثل scroll position، المتغيرات، ...)
                    popUpTo(route = HomeScreens.SEARCH.name) {  // يحدد نقطة العودة
                        saveState = true            // يحفظ حالة هذه الشاشة عند الخروج منها
                    }
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
        }
        when (getNearStoresState) {
            is GetNearStoresState.Idle -> {

                if (!homeScreenViewModel.getNearStoresHasRequested.value) {
                    homeScreenViewModel.getNearStores(limit = 20, offset = 0)
                    homeScreenViewModel.getNearStoresHasRequested.value = true;
                }

            }

            is GetNearStoresState.Error -> {}
            is GetNearStoresState.Loading -> {
                items(3) { StoresCategoriesSkeleton() }

            }

            is GetNearStoresState.Success -> {
                getNearStoresResponse?.let {
                    items(it.trendStores) { item: StoresData ->
                        RestaurantCard(item) { storeData ->
                            onResturanClicked(
                                storeData = storeData,
                                navController = navController,
                                homeScreenViewModel = homeScreenViewModel
                            )
                        }
                    }
                    items(it.stores) { item: StoresData ->
                        RestaurantCard(item) { storeData ->
                            onResturanClicked(
                                storeData = storeData,
                                navController = navController,
                                homeScreenViewModel = homeScreenViewModel
                            )
                        }
                    }
                }
            }

        }


        //--> MostPopular Section


        //--> ResentItems Section
        item {
            SectionHeader(sectionName = "Recent Items") {}
            Spacer(modifier = Modifier.size(8.dp))
        }
        items(testList4) { item ->
            RecentItem(item)
        }
    }
}

@Composable
fun TopBar(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Hello $userName!",
            style = TextStyle(
                color = primaryFontColor,
                fontSize = 20.sp,
                fontFamily = metropolisFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_cart),
            contentDescription = null,
            tint = primaryFontColor
        )
    }
}

@Composable
fun StoresCategories(list: List<GetStoreCategoriesRes?>, navController: NavController) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        item { Spacer(modifier = Modifier.size(12.dp)) }
        items(list) { item ->
            StoresCategory(item = item) {
                if (item != null) {

                    navController.navigate(route = HomeScreens.SEARCH.name) {
                        launchSingleTop =
                            true           // لا يعيد إنشاء الشاشة إذا هي موجودة في القمة
                        restoreState =
                            true             // يستعيد حالة الشاشة المحفوظة (مثل scroll position، المتغيرات، ...)
                        popUpTo(route = HomeScreens.SEARCH.name) {  // يحدد نقطة العودة
                            saveState = true            // يحفظ حالة هذه الشاشة عند الخروج منها
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.size(12.dp)) }

    }
}

@Composable
fun StoresCategory(
    item: GetStoreCategoriesRes?,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(enabled = item != null && onClick != null) {
                onClick?.invoke()
            }
            .padding(4.dp) // هامش خارجي بسيط
    ) {
        if (item != null) {
            AsyncImage(
                modifier = Modifier
                    .width(110.dp)
                    .aspectRatio(1f)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp)),
                model = item.category_image,
                contentDescription = item.category_name,
                contentScale = ContentScale.Crop
            )

            Text(
                text = item.category_name,
                style = TextStyle(
                    color = primaryFontColor,
                    fontSize = 14.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}

@Composable
fun StoresCategoriesSkeleton() {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        item { Spacer(modifier = Modifier.size(12.dp)) }

        items(4) {
            StoresCategorySkeleton()
        }

        item { Spacer(modifier = Modifier.size(12.dp)) }

    }
}

@Composable
fun StoresCategorySkeleton() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(110.dp)
                .aspectRatio(1f)
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray.copy(alpha = 0.3f)) // شكل الصورة أثناء التحميل
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(14.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = 0.3f)) // شكل النص أثناء التحميل
        )
    }
}

@Composable
fun SectionHeader(sectionName: String, viewAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sectionName,
            style = TextStyle(
                color = primaryFontColor,
                fontSize = 20.sp,
                fontFamily = metropolisFontFamily,
                fontWeight = FontWeight.Medium
            )
        )
        TextButton(onClick = viewAll) {
            Text(
                "View More",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = orange,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun PopularRestaurantItem(item: com.lightning.androidfrontend.ui.components.PopularRestaurant) {
    val itemRate = item.rate.toString().substring(0, 3)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
            painter = painterResource(id = item.coverImage),
            contentDescription = item.name,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp),
            text = item.name,
            style = TextStyle(
                color = primaryFontColor,
                fontSize = 16.sp,
                fontFamily = metropolisFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.ic_star),
                contentDescription = "rating",
                tint = orange
            )
            Text(
                text = " $itemRate ",
                style = TextStyle(
                    color = orange,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )
            Text(
                text = " (${item.rateCount} ratings)  ",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(orange)
            )
            Text(
                text = "  ${item.foodKind} ",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )
        }

    }
}

@Composable
fun MostPopular(list: List<com.lightning.androidfrontend.ui.components.PopularRestaurant>) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        item { Spacer(modifier = Modifier.size(12.dp)) }
        items(list) { item ->
            MostPopularItem(item, modifier = Modifier.fillParentMaxWidth(0.8f))
        }
        item { Spacer(modifier = Modifier.size(12.dp)) }

    }
}

@Composable
fun MostPopularItem(
    item: com.lightning.androidfrontend.ui.components.PopularRestaurant,
    modifier: Modifier = Modifier
) {
    val itemRate = item.rate.toString().substring(0, 3)
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .clip(RoundedCornerShape(10.dp)),
            painter = painterResource(id = item.coverImage),
            contentDescription = item.name,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .padding(top = 6.dp),
            text = item.name,
            style = TextStyle(
                color = primaryFontColor,
                fontSize = 16.sp,
                fontFamily = metropolisFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = " Café  ",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(orange)
            )
            Text(
                text = "  ${item.foodKind} ",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )

            Icon(
                painterResource(id = R.drawable.ic_star),
                contentDescription = "rating",
                tint = orange
            )
            Text(
                text = " $itemRate ",
                style = TextStyle(
                    color = orange,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )
        }

    }
}

@Composable
fun RecentItem(item: com.lightning.androidfrontend.ui.components.PopularRestaurant) {
    val itemRate = item.rate.toString().substring(0, 3)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        Image(
            modifier = Modifier
                .width(110.dp)
                .padding(end = 20.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp)),
            painter = painterResource(id = item.coverImage),
            contentDescription = item.name,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = item.name,
                style = TextStyle(
                    color = primaryFontColor,
                    fontSize = 16.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "${item.foodKind} ",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )
            Spacer(modifier = Modifier.size(4.dp))
            Row {
                Icon(
                    painterResource(id = R.drawable.ic_star),
                    contentDescription = "rating",
                    tint = orange
                )
                Text(
                    text = " $itemRate ",
                    style = TextStyle(
                        color = orange,
                        fontSize = 13.sp,
                        fontFamily = metropolisFontFamily
                    )
                )
                Text(
                    text = " (${item.rateCount} ratings)  ",
                    style = TextStyle(
                        color = secondaryFontColor,
                        fontSize = 13.sp,
                        fontFamily = metropolisFontFamily
                    )
                )
            }
        }
    }
}

fun onResturanClicked(
    storeData: StoresData,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    homeScreenViewModel.setSelectedStore(storeData)
    homeScreenViewModel.getStoreCategoryHasRequested.value = false
    navController.navigate(route = HomeScreens.STORE_DETAILS_SCREEN.name) {
        launchSingleTop =
            true           // لا يعيد إنشاء الشاشة إذا هي موجودة في القمة
        restoreState =
            true             // يستعيد حالة الشاشة المحفوظة (مثل scroll position، المتغيرات، ...)
        popUpTo(route = HomeScreens.STORE_DETAILS_SCREEN.name) {  // يحدد نقطة العودة
            saveState = true            // يحفظ حالة هذه الشاشة عند الخروج منها
        }
    }
}
