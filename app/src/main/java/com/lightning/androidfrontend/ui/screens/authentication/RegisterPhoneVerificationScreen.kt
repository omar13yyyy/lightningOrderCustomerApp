
package com.lightning.androidfrontend.ui.screens.authentication

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lightning.androidfrontend.navigation.ROUTES
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.ui.screens.AppTextField
import com.lightning.androidfrontend.ui.screens.FilledButton
import com.lightning.androidfrontend.ui.screens.Footer
import com.lightning.androidfrontend.ui.screens.VerifyTextField
import com.lightning.androidfrontend.viewmodels.ResetPasswordViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterPhoneVerificationScreen(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val checkCodeValidityState by authViewModel.checkCodeValidityState.collectAsState()
    val checkCodeValidityResponse by authViewModel.checkCodeValidityResponse.collectAsState()
    val resetPasswordViewModel = viewModel<ResetPasswordViewModel>()
//    val snackbarHostState = remember { SnackbarHostState() }
    var sucsses by remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }

    val phoneNumber = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("phoneNumber")

    MyAppTheme {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                modifier = Modifier.padding(horizontal = 50.dp),
                text = "We have sent an OTP to your Mobile",
                style = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = metropolisFontFamily,
                    color = primaryFontColor,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Please check your mobile number $phoneNumber continue to reset your password",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryFontColor,
                    fontFamily = metropolisFontFamily,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 50.dp)
            )
            Spacer(modifier = Modifier.height(54.dp))

            AppTextField(hint = "Enter Code", value = code){
                code = it
            }

            Spacer(modifier = Modifier.height(36.dp))
            when (checkCodeValidityState) {
                is CheckCodeValidityState.Idle , is CheckCodeValidityState.Error-> {
                    FilledButton(
                        text = "Send",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = true
                    ) {
                        if (phoneNumber != null) {
                            authViewModel.checkCodeValidity(phoneNumber=phoneNumber,code = code)

                        }
                    }
                }
                is CheckCodeValidityState.Loading -> {
                    FilledButton(
                        text = "loading...",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = false
                    ){}
                }
                is CheckCodeValidityState.Success -> {
                    if(checkCodeValidityResponse?.success!!){
                    navController.currentBackStackEntry?.savedStateHandle?.set("phoneNumber", phoneNumber)
                    navController.currentBackStackEntry?.savedStateHandle?.set("code", code)
                    navController.navigate(ROUTES.SIGN_UP_SCREEN.name)
                }
                }

            }

            Spacer(modifier = Modifier.height(32.dp))
            Footer(text = "Didn't Receive?", textButton = "Click Here") {

            }
        }

    }

}



