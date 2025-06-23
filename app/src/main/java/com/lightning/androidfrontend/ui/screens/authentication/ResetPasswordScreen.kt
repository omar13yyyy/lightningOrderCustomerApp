package com.lightning.androidfrontend.ui.screens.authentication

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lightning.androidfrontend.navigation.ROUTES
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.ui.screens.AppTextField
import com.lightning.androidfrontend.ui.screens.FilledButton


@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun ResetPasswordScreen(
    authViewModel: AuthViewModel = viewModel(),
    navigateToPhoneVerificationScreen: () -> Unit,
    navController: NavHostController,
) {
    var phoneNumber by remember { mutableStateOf("") }
    val restConfirmationPasswordState by authViewModel.restConfirmationState.collectAsState()
    val restConfirmationPasswordResponse by authViewModel.restPasswordResponse.collectAsState()

    MyAppTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Reset Password",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Please enter your phone to receive a link to  create a new password via phone",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryFontColor,
                    fontFamily = metropolisFontFamily,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 60.dp)
            )
            Spacer(modifier = Modifier.height(60.dp))
            AppTextField(
                hint = "Your Phone",
                action = ImeAction.Done,
                keyboardType = KeyboardType.Phone,
                value = phoneNumber
            ){
                phoneNumber=it
            }
            Spacer(modifier = Modifier.height(34.dp))
            when (restConfirmationPasswordState) {
                is RestConfirmationState.Idle , is RestConfirmationState.Error-> {
                    FilledButton(
                        text = "Send",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = true
                    ) {
                        if (phoneNumber != null) {
                                authViewModel.restconfirmation(phoneNumber=phoneNumber)

                        }
                    }
                }
                is RestConfirmationState.Loading -> {
                    FilledButton(
                        text = "loading...",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = false
                    ){}                }
                is RestConfirmationState.Success -> {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("phoneNumber", phoneNumber)
                    navController.navigate(ROUTES.EMAIL_VERIFICATION_SCREEN.name)
                }

            }



        }
    }
}