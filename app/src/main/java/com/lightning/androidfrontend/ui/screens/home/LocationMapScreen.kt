import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.lightning.androidfrontend.data.model.LocationEntry
import com.lightning.androidfrontend.navigation.ROUTES
import com.lightning.androidfrontend.ui.screens.home.HomeScreenViewModel
import com.lightning.androidfrontend.ui.screens.home.HomeScreens
import com.lightning.androidfrontend.ui.screens.home.LocationViewModel
import com.lightning.androidfrontend.utils.LocationPreferences
import com.lightning.androidfrontend.utils.saveLocations
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationMapScreen(
    homeScreenViewModel:HomeScreenViewModel,
    locationViewModel: LocationViewModel = viewModel(),
     navController : NavController
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    var currentLatLng by remember { mutableStateOf<LatLng?>(null) }
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        currentLatLng = latLng
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(latLng, 16f),
                                durationMs = 1000
                            )
                        }
                    }
                }
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = permissionState.status.isGranted),
        )

        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = "دبوس الموقع",
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
                .zIndex(1f),
            tint = Color.Red
        )
        /*
        go to my location button
        FloatingActionButton(
            onClick = {
                if (permissionState.status.isGranted) {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                        .addOnSuccessListener { location ->
                            location?.let {
                                val latLng = LatLng(it.latitude, it.longitude)
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(latLng, 16f),
                                        durationMs = 1000
                                    )
                                }
                            }
                        }
                } else {
                    permissionState.launchPermissionRequest()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "موقعي")

        }
        */
        // زر تأكيد الموقع
        Button(
             onClick  = {
                val selectedLatLng = cameraPositionState.position.target
                locationViewModel.setSelectedLocation(LocationEntry(name = "Current", selected = true,
                    latitude = selectedLatLng.latitude, longitude = selectedLatLng.longitude))
                 saveLocations(context = context, listOf(LocationEntry(name = "Current", selected = true,
                     latitude = selectedLatLng.latitude, longitude = selectedLatLng.longitude)))
                 homeScreenViewModel.setLocation(LocationEntry(name = "Current", selected = true,
                     latitude = selectedLatLng.latitude, longitude = selectedLatLng.longitude))
                 homeScreenViewModel.changeHideBottomMenu()
                 navController.popBackStack()

                 navController.navigate(HomeScreens.HOME.name)
             },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {
            Text("تأكيد الموقع")
        }
    }
}
