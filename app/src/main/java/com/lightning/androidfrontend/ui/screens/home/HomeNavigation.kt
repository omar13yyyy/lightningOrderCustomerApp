package com.lightning.androidfrontend.ui.screens.home


import LocationMapScreen
import MoreDetailsScreen
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.data.model.CategoriesWithTags
import com.lightning.androidfrontend.data.model.CategoryWithTags
import com.lightning.androidfrontend.data.model.GetCategoryTagsRes
import com.lightning.androidfrontend.data.model.GetStoreCategoriesRes
import com.lightning.androidfrontend.data.model.LocationEntry
import com.lightning.androidfrontend.data.repository.UserRepository.VisitorRepository
import com.lightning.androidfrontend.navigation.ROUTES
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.orange
import com.lightning.androidfrontend.theme.placeholderColor
import com.lightning.androidfrontend.utils.LocationPreferences
import com.lightning.androidfrontend.utils.LocationPreferences.getLatLon
import com.lightning.androidfrontend.utils.loadLocations
import com.lightning.androidfrontend.utils.saveLocations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class HomeScreens {
    MENU,
    OFFERS,
    HOME,
    PROFILE,
    MORE,
    SEARCH,
    CHOOSE_LOCATION_SCREEN,
    LOCATION_ON_MAP_SCREEN,
    STORE_DETAILS_SCREEN,
    PRODUCT_DETAILS_SCREEN,
    CART,
    ORDERS,
    ORDER_DETAILS
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeNavigation(navScreensController: NavController) {
    val locationViewModel =LocationViewModel()

    var firstOpen by remember { mutableStateOf(true) }


    val selectedItem = remember { mutableStateOf(HomeScreens.HOME) }
    val navController = rememberAnimatedNavController()
    var locationCode by remember { mutableStateOf("false") }

    val context = LocalContext.current
    val activity = context as? Activity
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    var checkLocation by remember { mutableStateOf(false) }
    var checkLocationCode by remember { mutableStateOf(false) }




        LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }


        val homeScreenViewModel = remember {HomeScreenViewModel(visitorRepository = VisitorRepository(),
            context=context,   locationCode=locationViewModel.selectedLocationCode.value,latitude = locationViewModel.selectedLocation.value?.latitude
                ?: 0.0,longitude= locationViewModel.selectedLocation.value?.longitude
                ?: 0.0
        )}
    val hideBottomMenu by homeScreenViewModel.hideBottomMenu.collectAsState()

    var size = if (!hideBottomMenu) 0.93f else 1.0f
    Box(modifier = Modifier.fillMaxSize()) {
            //-> Main Container ~90% of the screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(size)
            ) {

        HomeNavHost(navHostController = navController,
            homeScreenViewModel=homeScreenViewModel,
            locationViewModel = locationViewModel)

                //-> Navigate when the selected item changes..
                if(!firstOpen){
               // navController.popBackStack()
                navController.navigate(route = selectedItem.value.name) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(route = selectedItem.value.name) {
                        saveState = true
                    }
                }

                }
            }
            if(!hideBottomMenu){

                //-> Bottom Bar ~10% of the screen
            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.13f)
                    .align(Alignment.BottomCenter),
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.size(70.dp),
                        backgroundColor = if (selectedItem.value == HomeScreens.HOME) orange else placeholderColor,
                        onClick = {firstOpen =false
                            selectedItem.value = HomeScreens.HOME },
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.592f),
                        cutoutShape = RoundedCornerShape(50),
                        backgroundColor = Color.White,
                        elevation = 20.dp,
                        contentPadding = PaddingValues(all = 5.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BottomNavigationItem(selected = selectedItem.value == HomeScreens.ORDERS,
                                onClick = {firstOpen =false
                                    selectedItem.value = HomeScreens.ORDERS },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_menu),
                                        contentDescription = "menu",
                                        tint = if (selectedItem.value == HomeScreens.ORDERS) orange else placeholderColor
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Orders",
                                        style = navItemTextStyle(selectedItem.value == HomeScreens.ORDERS)
                                    )
                                })

                            BottomNavigationItem(selected = selectedItem.value == HomeScreens.SEARCH,
                                onClick = {firstOpen =false
                                    selectedItem.value = HomeScreens.SEARCH },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_shopping_bag),
                                        contentDescription = "search",
                                        tint = if (selectedItem.value == HomeScreens.SEARCH) orange else placeholderColor
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Search",
                                        style = navItemTextStyle(selectedItem.value == HomeScreens.SEARCH)
                                    )
                                })

                            BottomNavigationItem(selected = selectedItem.value == HomeScreens.PROFILE,
                                onClick = {firstOpen =false
                                    selectedItem.value = HomeScreens.PROFILE },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_profile),
                                        contentDescription = "profile",
                                        tint = if (selectedItem.value == HomeScreens.PROFILE) orange else placeholderColor
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Profile",
                                        style = navItemTextStyle(selectedItem.value == HomeScreens.PROFILE)
                                    )
                                })

                            BottomNavigationItem(selected = selectedItem.value == HomeScreens.MORE,
                                onClick = {firstOpen =false
                                    selectedItem.value = HomeScreens.MORE },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_more),
                                        contentDescription = "more",
                                        tint = if (selectedItem.value == HomeScreens.MORE) orange else placeholderColor
                                    )
                                },
                                label = {
                                    Text(
                                        text = "More",
                                        style = navItemTextStyle(selectedItem.value == HomeScreens.MORE)
                                    )
                                })
                        }
                    }
                }
            ) {}
        }
    }
}

private fun navItemTextStyle(isSelected: Boolean): TextStyle {
    return TextStyle(
        color = if (isSelected) orange else placeholderColor,
        fontSize = 12.sp,
        fontFamily = metropolisFontFamily,
        fontWeight = FontWeight.Medium
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun HomeNavHost(
    navHostController: NavHostController,
    homeScreenViewModel :HomeScreenViewModel = viewModel(),
    locationViewModel : LocationViewModel = viewModel()
) {
    val context = LocalContext.current
    AnimatedNavHost(navController = navHostController, startDestination = HomeScreens.CHOOSE_LOCATION_SCREEN.name) {

        composable(
            route = HomeScreens.HOME.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            HomeScreen(
                homeScreenViewModel= homeScreenViewModel,
                navController = navHostController,
                        locationViewModel=locationViewModel
            )
        }
        composable(
            route = HomeScreens.CART.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            Cart(
                homeScreenViewModel= homeScreenViewModel,
                navController = navHostController,
                locationViewModel=locationViewModel
            )
        }
        composable(
            route = HomeScreens.ORDERS.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            OrdersScreen(
                homeScreenViewModel= homeScreenViewModel,
                navController =  navHostController

            )
        }
        composable(
            route = HomeScreens.ORDER_DETAILS.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            OrderDetails(
                navController=navHostController,
                homeScreenViewModel=homeScreenViewModel
                )
        }

        composable(
            route = HomeScreens.SEARCH.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            SearchScreen(homeScreenViewModel =homeScreenViewModel,
                navController = navHostController)
        }
        composable(
            route = HomeScreens.PRODUCT_DETAILS_SCREEN.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            ProductDetailsScreen(homeScreenViewModel =homeScreenViewModel,
                navController = navHostController)
        }
        composable(
            route = HomeScreens.STORE_DETAILS_SCREEN.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            storeDetailsScreen(homeScreenViewModel=homeScreenViewModel,
                navController = navHostController)
        }

        composable(
            route = HomeScreens.PROFILE.name,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            ProfileScreen()
        }

        composable(
            route = HomeScreens.MORE.name,
            popEnterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            MoreScreen {content ->
                navHostController.currentBackStackEntry?.arguments?.putSerializable("content", content)
                navHostController.navigate("MoreDetailsScreen/{content}")
            }
        }

        composable(
            route = "MoreDetailsScreen/{content}",
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            val content = navHostController.previousBackStackEntry?.arguments?.getSerializable("content") as com.lightning.androidfrontend.ui.components.More?
            if (content != null) {
                MoreDetailsScreen(content)
            }
        }
        composable(route = HomeScreens.CHOOSE_LOCATION_SCREEN.name) {
            ChooseLocationScreen(
                homeScreenViewModel=homeScreenViewModel,
                locationViewModel = locationViewModel,
                navController = navHostController,


            )
        }
        composable(route = HomeScreens.LOCATION_ON_MAP_SCREEN.name) {
            LocationMapScreen (
                homeScreenViewModel=homeScreenViewModel,
                locationViewModel = locationViewModel,
                navController = navHostController
            )
        }

    }
}