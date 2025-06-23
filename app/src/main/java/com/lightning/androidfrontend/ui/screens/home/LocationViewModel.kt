package com.lightning.androidfrontend.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.android.libraries.maps.model.LatLng
import com.lightning.androidfrontend.data.model.LocationEntry
import com.lightning.androidfrontend.utils.encodeToQuadrants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationViewModel : ViewModel() {
    private val _selectedLocation = MutableStateFlow<LocationEntry?>(LocationEntry(name = "Default", longitude = 0.0, latitude = 0.0, selected = false))
    val selectedLocation: StateFlow<LocationEntry?> = _selectedLocation
    private val _selectedLocationCode = MutableStateFlow<String>("")
    val selectedLocationCode: StateFlow<String> = _selectedLocationCode

    fun setSelectedLocation(locationEntry: LocationEntry) {
        _selectedLocation.value = locationEntry
        _selectedLocationCode.value= encodeToQuadrants(latitude=locationEntry.latitude, longitude = locationEntry.longitude)
    }



}
