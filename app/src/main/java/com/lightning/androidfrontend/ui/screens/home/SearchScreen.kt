package com.lightning.androidfrontend.ui.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.data.model.CategoriesWithTags
import com.lightning.androidfrontend.data.model.CategoryWithTags
import com.lightning.androidfrontend.data.model.GetCategoryTagsRes
import com.lightning.androidfrontend.data.model.GetStoreCategoriesParams
import com.lightning.androidfrontend.data.model.StoresData
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.theme.gray
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.orange
import com.lightning.androidfrontend.theme.placeholderColor
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.ui.components.RestaurantCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable

fun SearchScreen(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    val categories by homeScreenViewModel.categoriesWithTags.collectAsState()

    LaunchedEffect(Unit) {
        homeScreenViewModel.loadCategoriesWithTags()
    }

    val selectedCategory by homeScreenViewModel.selectedCategory.collectAsState()

    var selectedTag by remember { mutableStateOf<GetCategoryTagsRes?>(null) }

    val tagClicked by homeScreenViewModel.tagClicked.collectAsState()

    val categoryClicked by homeScreenViewModel.categoryClicked.collectAsState()
    val searched by homeScreenViewModel.searched.collectAsState()

    val getStoresByCategoryState by homeScreenViewModel.getStoresByCategoryState.collectAsState()
    val getStoresByCategoryResponse by homeScreenViewModel.getStoresByCategoryResponse.collectAsState()

    val getStoresByTagState by homeScreenViewModel.getStoresByTagState.collectAsState()
    val geStoresByTagResponse by homeScreenViewModel.geStoresByTagResponse.collectAsState()

    val searchForStoreState by homeScreenViewModel.searchForStoreState.collectAsState()
    val searchForStoreRespone by homeScreenViewModel.searchForStoreResponse.collectAsState()

    val listStateCategory = rememberLazyListState()
    var itemsCategory by remember { mutableStateOf(listOf<StoresData>()) }
    var offsetCategory by remember { mutableStateOf(0) }
    val limitCategory = 10
    var isLoadingCategory by remember { mutableStateOf(false) }


    val listStateTags = rememberLazyListState()
    var itemsTags by remember { mutableStateOf(listOf<StoresData>()) }
    var offsetTags by remember { mutableStateOf(0) }
    val limitTags = 10
    var isLoadingTags by remember { mutableStateOf(false) }

    val listStateSearch = rememberLazyListState()
    var itemsSearch by remember { mutableStateOf(listOf<StoresData>()) }
    var offsetSearch by remember { mutableStateOf(0) }
    val limitSearch = 10
    var isLoadingSearch by remember { mutableStateOf(false) }

    var searchChangeState by remember { mutableStateOf(0) }


    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(isPressed) {
        if (isPressed) {
            homeScreenViewModel.setTagClicked(false)
            homeScreenViewModel.setCategoryClicked(false)
            homeScreenViewModel.setSearchedClicked(false)
            Log.d("debugTag", "box is clicked")
        }
    }
    Column(
        modifier
            .background(colors.background)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.size(14.dp))
        // storeDetailsScreen()

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
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
                    text = "ابحث عن متجر حسب الاسم",
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
                     homeScreenViewModel.setTagClicked(false)
                     homeScreenViewModel.setCategoryClicked(false)
                    homeScreenViewModel.setSearchedClicked(true)

                    itemsSearch = listOf()
                    //searchChangeState += searchChangeState + 1
                    keyboardController?.hide()
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

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (categories != null) {


                items(categories!!.categories) { category ->


                            Button(
                                onClick = {


                                    homeScreenViewModel.setTagClicked(false)
                                    homeScreenViewModel.setCategoryClicked(true)
                                    homeScreenViewModel.setSelectedCategory(category)
                                    homeScreenViewModel.getNearStoresByCategory(
                                        10,
                                        0,
                                        category.categories.category_id
                                    )


                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = /*if (selectedCategory == category) Color.Gray else*/ Color.LightGray
                                ),
                                shape = RoundedCornerShape(20.dp),
                            ) {
                                Text(text = category.categories.category_name)
                            }
                        }

            }
        }
        selectedCategory?.let { category ->
            Spacer(modifier = Modifier.height(2.dp))

            LazyRow() {
                items(category.tags) { tag ->

                    Button(
                        onClick = {
                            selectedTag = tag

                            homeScreenViewModel.setTagClicked(true)
                            homeScreenViewModel.setCategoryClicked(false)

                            homeScreenViewModel.getNearStoresByTag(
                                limit = 10,
                                offset = 0,
                                tagId = tag.tag_id
                            )
                            itemsTags = listOf()
                        },
                    ) {
                        Text(text = tag.tag_name)
                    }
                }

            }
        }

        if (!tagClicked && categoryClicked) {


            LaunchedEffect(getStoresByCategoryState) {
                when (getStoresByCategoryState) {
                    is GetStoresByCategoryState.Success -> {
                        val newItems = (getStoresByCategoryResponse?.trendStores.orEmpty() +
                                getStoresByCategoryResponse?.stores.orEmpty())

                        if (offsetCategory == 0) {
                            itemsCategory = newItems // أول تحميل
                        } else {
                            itemsCategory += newItems // تحميل إضافي
                        }

                        isLoadingCategory = false
                    }

                    else -> {}
                }
            }

            LaunchedEffect(listStateCategory) {
                snapshotFlow { listStateCategory.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { index ->
                        if (index == itemsCategory.lastIndex && !isLoadingCategory) {
                            isLoadingCategory = true
                            offsetCategory += itemsCategory.lastIndex
                            selectedCategory?.categories?.let {
                                homeScreenViewModel.getNearStoresByCategory(
                                    offset = offsetCategory,
                                    limit = limitCategory,
                                    categoryId = it.category_id
                                )
                            }
                        }
                    }
            }

            LazyColumn(state = listStateCategory) {
                items(itemsCategory) { item ->
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

        if (tagClicked && !categoryClicked) {

            LaunchedEffect(getStoresByTagState) {
                when (getStoresByTagState) {
                    is GetStoresByTagState.Success -> {
                        val newItems = (geStoresByTagResponse?.trendStores.orEmpty() +
                                geStoresByTagResponse?.stores.orEmpty())

                        if (offsetTags == 0) {
                            itemsTags = newItems // أول تحميل
                        } else {
                            itemsTags += newItems // تحميل إضافي
                        }

                        isLoadingTags = false
                    }

                    else -> {}
                }
            }

            LaunchedEffect(listStateTags) {
                snapshotFlow { listStateTags.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { index ->
                        if (index == itemsTags.lastIndex && !isLoadingTags) {
                            isLoadingTags = true
                            offsetTags += itemsTags.lastIndex
                            selectedTag?.let {
                                homeScreenViewModel.getNearStoresByTag(
                                    offset = offsetTags,
                                    limit = limitTags,
                                    tagId = it.tag_id
                                )
                            }
                        }
                    }
            }

            LazyColumn(state = listStateTags) {
                items(itemsTags) { item ->
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
        if (!tagClicked && !categoryClicked && searched) {


            LaunchedEffect(searchForStoreState) {
                when (searchForStoreState) {
                    is SearchForStoreState.Success -> {
                        val newItems = (searchForStoreRespone?.trendStores.orEmpty() +
                                searchForStoreRespone?.stores.orEmpty())

                        if (offsetSearch == 0) {
                            itemsSearch = newItems // أول تحميل
                        } else {
                            itemsSearch += newItems // تحميل إضافي
                        }

                        isLoadingSearch = false
                    }

                    else -> {}
                }
            }

            LaunchedEffect(listStateSearch) {
                snapshotFlow { listStateSearch.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { index ->
                        Log.d("debugTag", "index $index ")
                        Log.d("debugTag", "itemsSearch.lastIndex ${itemsSearch.lastIndex} ")

                        if ((index == itemsSearch.lastIndex && !isLoadingSearch) || index == null) {
                            isLoadingSearch = true
                            if (itemsSearch.lastIndex > 0)
                                offsetSearch += itemsSearch.lastIndex

                            homeScreenViewModel.searchForStore(
                                offset = offsetSearch,
                                limit = limitSearch,
                                storeName = searchText
                            )

                        }
                    }
            }

            LazyColumn(state = listStateSearch) {
                items(itemsSearch) { item ->
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
}
