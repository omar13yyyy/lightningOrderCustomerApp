package com.lightning.androidfrontend.ui.screens.authentication


import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lightning.androidfrontend.navigation.ROUTES
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.ui.screens.AppTextField
import com.lightning.androidfrontend.ui.screens.FilledButton
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun ChangePasswordScreen(

    authViewModel: AuthViewModel = viewModel(),
    navController :NavHostController

) {
    val phoneNumber = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("phoneNumber")

    val code = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("code")

    val restPasswordState by authViewModel.restPasswordState.collectAsState()
    val restPasswordResponse by authViewModel.restPasswordResponse.collectAsState()
    var password by remember { mutableStateOf("") }
    var Confirm by remember { mutableStateOf("") }

    MyAppTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "New Password",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 60.dp),
                text = "Please enter your phone to receive a" +
                        "link to  create a new password via phone",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryFontColor,
                    fontFamily = metropolisFontFamily,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(36.dp))
            AppTextField(hint = "New Password", keyboardType = KeyboardType.Password, value = password){
                Confirm = it
            }
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(
                hint = "Confirm Password",
                keyboardType = KeyboardType.Password,
                action = ImeAction.Done,
                value = Confirm,
            ){
                Confirm = it
            }
            Spacer(modifier = Modifier.height(28.dp))
            when (restPasswordState) {
                is RestPasswordState.Idle , is RestPasswordState.Error-> {
                    FilledButton(
                        text = "Reset Password",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = true
                    ) {
                        if (phoneNumber != null) {
                            if (code != null) {
                                authViewModel.restPassword(phoneNumber=phoneNumber, code = code, password = password)
                            }
                        }
                    }
                }
                is RestPasswordState.Loading -> {
                    FilledButton(
                        text = "loading...",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = false
                    ){}                }
                is RestPasswordState.Success -> {
                    navController.navigate(route = ROUTES.SIGN_IN_SCREEN.name)
                }

            }
        }
    }
}