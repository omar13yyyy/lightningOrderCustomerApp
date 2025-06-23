package com.lightning.androidfrontend.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lightning.androidfrontend.data.model.LocationEntry
import com.lightning.androidfrontend.navigation.ROUTES
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.utils.LocationPreferences
import com.lightning.androidfrontend.utils.encodeToQuadrants
import com.lightning.androidfrontend.utils.loadLocations
import com.lightning.androidfrontend.utils.saveLocations
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.maplibre.android.geometry.LatLng

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseLocationScreen(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel(),
    navController: NavController,
) {

    val colors = LocalAppColors.current

    val context = LocalContext.current

    var options by remember { mutableStateOf(loadLocations(context = context)) }

    var selectedOption by remember { mutableStateOf<LocationEntry?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var newOptionText by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<Location?>(null) }
    val scope = rememberCoroutineScope()

        if (!isGpsEnabled(context)) {
            Toast.makeText(context, "الرجاء تشغيل GPS", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "GPS يعمل ✅", Toast.LENGTH_SHORT).show()
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.chooseLocationColor
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("اختر موقعك", style = MaterialTheme.typography.titleLarge, color = Color.Black)

                // Dropdown list
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },

                ) {
                    TextField(
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = colors.chooseLocationButtonColor,

                        ),
                        value = selectedOption?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("موقعي", color = Color.Black)  },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.name) },
                                onClick = {
                                    selectedOption = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    value = newOptionText,
                    onValueChange = { newOptionText = it },
                    label = { Text("او اكتب اسم جديد لموقعك الحالي", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (newOptionText.isNotBlank()) {
                                scope.launch {
                                    val loc = LocationPreferences.getCurrentLocation(context = context)
                                    if (loc != null) {
                                        options = options + (
                                                LocationEntry(
                                                    name = newOptionText,
                                                    longitude = loc.longitude,
                                                    latitude = loc.latitude,
                                                    selected = false
                                                )
                                                )
                                        newOptionText = ""
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "تعذر تحديد الموقع الحالي",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    ) {
                        Text("إضافة موقعي الحالي")
                    }

                    Button(
                        onClick = {
                            navController.popBackStack()

                            navController.navigate(HomeScreens.LOCATION_ON_MAP_SCREEN.name)
                        }
                    ) {
                        Text("الذهاب الى الخريطة")
                    }
                }
                Button(
                    onClick = {
                            // إطلاق coroutine لجلب الموقع بشكل غير متزامن
                            scope.launch {
                                val loc = LocationPreferences.getCurrentLocation(context = context)
                                if (loc != null) {


                                    locationViewModel.setSelectedLocation(LocationEntry(
                                        name = "Current",
                                        longitude = loc.longitude,
                                        latitude = loc.latitude,
                                        selected = true
                                    ))
                                    homeScreenViewModel.setLocation(LocationEntry(
                                        name = "Current",
                                        longitude = loc.longitude,
                                        latitude = loc.latitude,
                                        selected = true
                                    ))
                                    homeScreenViewModel.changeHideBottomMenu()
                                    navController.popBackStack()

                                    navController.navigate(HomeScreens.HOME.name)

                                }
                            }

                    }
                ) {
                    Text("موقعي الحالي")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        options.forEach { it.selected = false }
                        selectedOption?.let {
                            it.selected = true
                            locationViewModel.setSelectedLocation(it)
                            //homeScreenViewModel.getStoresCategories()
                           // homeScreenViewModel.getNearStores(limit = 20, offset = 0)
                            homeScreenViewModel.setLocation(it)
                            saveLocations(context, options)
                            homeScreenViewModel.changeHideBottomMenu()
                            navController.popBackStack()
                            navController.navigate(HomeScreens.HOME.name)
                        }
                    }
                ) {
                    Text("تأكيد")
                }
            }
        }
    }
}

fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}