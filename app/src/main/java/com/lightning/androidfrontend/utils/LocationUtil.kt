package com.lightning.androidfrontend.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lightning.androidfrontend.data.model.LocationEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine


class LocationUtil(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationResult: (Location?) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 5000
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                onLocationResult(location)
            }
            .addOnFailureListener {
                onLocationResult(null)
            }
    }
}
object LocationPreferences {
    private val LAT = doublePreferencesKey("lat")
    private val LON = doublePreferencesKey("lon")
    private val LOCATION_CODE = stringPreferencesKey("location")

    val Context.dataStore by preferencesDataStore("locationPrefs")

    fun save(context: Context, lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs[LAT] = lat
                prefs[LON] = lon
                prefs[LOCATION_CODE] = encodeToQuadrants(lat, lon)
            }
        }
    }

    fun getLatLon(context: Context): Flow<Pair<Double?, Double?>> {
        return context.dataStore.data.map { preferences ->
            val lat = preferences[LAT]
            val lon = preferences[LON]
            Pair(lat, lon)
        }
    }

    fun getLocationCode(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[LOCATION_CODE]
        }
    }


suspend fun getCurrentLocation(context: Context): Location? = suspendCancellableCoroutine { cont ->
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    val cancellationToken = CancellationTokenSource()

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

    }
    fusedClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        cancellationToken.token
    ).addOnSuccessListener { location ->
        cont.resume(location) {}
    }.addOnFailureListener { e ->
        cont.resume(null) {}
    }

    cont.invokeOnCancellation {
        cancellationToken.cancel()
    }
}
}
fun encodeToQuadrants(latitude: Double, longitude: Double, precision: Int = 20): String {
    var latMin = -90.0
    var latMax = 90.0
    var lonMin = -180.0
    var lonMax = 180.0
    val quadrantCode = StringBuilder()

    for (i in 0 until precision) {
        val latMid = (latMin + latMax) / 2.0
        if (latitude > latMid) {
            quadrantCode.append("1") // شمال
            latMin = latMid
        } else {
            quadrantCode.append("3") // جنوب
            latMax = latMid
        }

        val lonMid = (lonMin + lonMax) / 2.0
        if (longitude > lonMid) {
            quadrantCode.append("2") // شرق
            lonMin = lonMid
        } else {
            quadrantCode.append("4") // غرب
            lonMax = lonMid
        }
    }

    val compressQuadrantCode = StringBuilder()
    for (i in 0 until quadrantCode.length step 2) {
        val pair = "${quadrantCode[i]}${quadrantCode[i + 1]}"
        when (pair) {
            "13" -> compressQuadrantCode.append("1")
            "24" -> compressQuadrantCode.append("2")
            "34" -> compressQuadrantCode.append("3")
            "12" -> compressQuadrantCode.append("4") // أصلًا شرط "34" مكرر، عدلتها لـ "12" لتغطية حالة أخرى محتملة
        }
    }

    return quadrantCode.toString() // أو يمكن إرجاع `compressQuadrantCode.toString()` لو أردت النسخة المضغوطة
}
fun saveLocations(context: Context, list: List<LocationEntry>) {
    val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    val json = Gson().toJson(list)
    editor.putString("locations_list", json)
    editor.apply()
}
fun loadLocations(context: Context): List<LocationEntry> {
    val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val json = sharedPrefs.getString("locations_list", null) ?: return emptyList()
    val type = object : TypeToken<List<LocationEntry>>() {}.type
    return Gson().fromJson(json, type)
}