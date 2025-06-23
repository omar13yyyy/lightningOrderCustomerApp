package com.lightning.androidfrontend.ui.components

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lightning.androidfrontend.data.model.LocationEntry
import com.lightning.androidfrontend.utils.LocationPreferences
import com.lightning.androidfrontend.utils.encodeToQuadrants
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.maplibre.android.geometry.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionDialog(
    options: List<LocationEntry>,
    onAddOption: (LocationEntry) -> Unit,
    onDismiss: () -> Unit,
    onOptionSelected: (LocationEntry) -> Unit,
    onOptionSave: (List<LocationEntry>) -> Unit
) {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf<LocationEntry?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var newOptionText by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<Location?>(null) }
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("اختر موقعك") },
        text = {
            Column {
                // Dropdown list
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedOption?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("موقعي") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor()
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

                Spacer(Modifier.height(16.dp))

                // Add new option
                OutlinedTextField(
                    value = newOptionText,
                    onValueChange = { newOptionText = it },
                    label = { Text("او اكتب اسم جديد لموقعك الحالي") },
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (newOptionText.isNotBlank()) {
                            // إطلاق coroutine لجلب الموقع بشكل غير متزامن
                            scope.launch {
                                val loc = LocationPreferences.getCurrentLocation(context = context)
                                if (loc != null) {
                                    onAddOption(
                                        LocationEntry(
                                            name = newOptionText,
                                            longitude = loc.longitude,
                                            latitude = loc.latitude,
                                            selected = false
                                        )
                                    )

                                    newOptionText = ""


                                } else {
                                    Toast.makeText(context, "تعذر تحديد الموقع الحالي", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                ) {
                    Text("إضافة موقعي الحالي")
                }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    options.forEach {
                        option ->
                        option.selected =false
                    }
                    selectedOption?.let { it .selected =true
                        onOptionSelected(it) }
                    onOptionSave(options)
                    onDismiss()
                },
                enabled = selectedOption != null
            ) {
                Text("تأكيد")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
