package com.lightning.androidfrontend.ui.screens.authentication

import TokenManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.data.model.UserLoginParams
import com.lightning.androidfrontend.data.repository.UserRepository.VisitorRepository
import com.lightning.androidfrontend.ui.screens.AppTextField
import com.lightning.androidfrontend.ui.screens.ButtonWithImage
import com.lightning.androidfrontend.ui.screens.FilledButton
import com.lightning.androidfrontend.ui.screens.Footer
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.blue
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.red
import com.lightning.androidfrontend.theme.secondaryFontColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    authViewModel: AuthViewModel = viewModel(),
    navigateToResetPasswordScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit
) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by authViewModel.loginState.collectAsState()
    val loginRes by authViewModel.loginResponse.collectAsState()
    MyAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Add your details to login",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = secondaryFontColor,
                    fontWeight = FontWeight.Medium,
                    fontFamily = metropolisFontFamily
                )
            )
            Spacer(modifier = Modifier.height(36.dp))
            AppTextField(
                hint = "Your phoneNumber",
                keyboardType = KeyboardType.Phone,
                value = phoneNumber,
                onValueChange = { phoneNumber = it }
            )
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                keyboardType = KeyboardType.Password,

                action = ImeAction.Done
            )
            Spacer(modifier = Modifier.height(28.dp))
            when (loginState) {
                is LoginState.Idle , is LoginState.Error-> {
                    FilledButton(
                        text = "Sign In",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = true
                    ) {
                        Log.d("debugTag", "try login")
                        authViewModel.login(phoneNumber, password)
                    }
                }
                is LoginState.Loading -> {
                    FilledButton(
                        text = "loading...",
                        modifier = Modifier.padding(horizontal = 34.dp),
                        enabled = false
                    ){}                }
                is LoginState.Success -> {
                    Log.d("debugTag", "login")
                    var token: String = loginRes?.token ?: "null"
                    val tokenManager = TokenManager(context)
                    tokenManager.saveToken(token)
                    val token2 = tokenManager.getToken()
                    Log.d("debugTag", "Token is: $token2")
                    if (token != "null")
                        navigateToHomeScreen()
                }

            }

            Spacer(modifier = Modifier.height(24.dp))



            TextButton(onClick = {
                navigateToResetPasswordScreen()
            }) {
                Text(
                    "Forgot your password?",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = secondaryFontColor,
                        fontFamily = metropolisFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

            Spacer(modifier = Modifier.height(28.dp))
            ButtonWithImage(
                text = "Login with Google",
                image = R.drawable.ic_google,
                modifier = Modifier.padding(horizontal = 34.dp),
                color = red
            ) {}
            Spacer(modifier = Modifier.height(28.dp))
            Footer(
                text = "Don't have an Account?",
                textButton = "Sign Up",
            ) {
                navigateToSignUpScreen()
            }
        }
    }
}






