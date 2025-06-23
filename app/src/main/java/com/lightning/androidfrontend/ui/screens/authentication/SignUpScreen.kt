package com.lightning.androidfrontend.ui.screens.authentication

import TokenManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lightning.androidfrontend.navigation.ROUTES
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.ui.screens.AppTextField
import com.lightning.androidfrontend.ui.screens.FilledButton
import com.lightning.androidfrontend.ui.screens.Footer
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel = viewModel(),
                 navController:NavHostController) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val dateDialogState = rememberMaterialDialogState()
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    var password by remember { mutableStateOf("") }
    val phoneNumber = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("phoneNumber")

    val code = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("code")
    val registerState by authViewModel.registerState.collectAsState()
    val registerResponse by authViewModel.registerResponse.collectAsState()
    MyAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Sign Up",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = primaryFontColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Add your details to sign up",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = secondaryFontColor,
                )
            )

            Spacer(modifier = Modifier.height(36.dp))
            AppTextField(hint = "fullName",
                    value =fullName ,
                onValueChange = { fullName = it }
            )
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(hint = "email",
                value =email ,
                onValueChange = { email = it })
            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(hint = "address",
                value =address ,
                onValueChange = { address = it })
            Spacer(modifier = Modifier.height(28.dp))



            MaterialDialog(dialogState = dateDialogState, buttons = {
                positiveButton("OK")
                negativeButton("Cancel")
            }) {
                datepicker { date ->
                    selectedDate.value = date
                }
            }
            Button(onClick = { dateDialogState.show() }) {
                Text("Select date")
            }

            Spacer(modifier = Modifier.height(28.dp))
            AppTextField(hint = "Password", keyboardType = KeyboardType.Password,
                value =password ,
                onValueChange = { password = it })
            Spacer(modifier = Modifier.height(28.dp))

            Spacer(modifier = Modifier.height(28.dp))


                when (registerState) {
                    is RegisterState.Idle , is RegisterState.Error-> {
                        FilledButton(
                            text = "sign up",
                            modifier = Modifier.padding(horizontal = 34.dp),
                            enabled = true
                        ) {
                            if (phoneNumber != null) {
                                if (code != null) {
                                    authViewModel.register(fullName=fullName,email=email, password = password,phoneNumber=phoneNumber, code = code, address = address, birthDate = selectedDate.value.toString())
                                }
                            }
                        }
                    }
                    is RegisterState.Loading -> {
                        FilledButton(
                            text = "loading...",
                            modifier = Modifier.padding(horizontal = 34.dp),
                            enabled = false
                        ){}                }
                    is RegisterState.Success -> {
                        //navController.navigate(route = ROUTES.SIGN_IN_SCREEN.name)
                        val token = registerResponse?.token
                        if(token != null) {
                            val tokenManager = TokenManager(context)
                            tokenManager.saveToken(token)
                            navController.navigate(ROUTES.HOME_SCREEN.name)
                        }
                    }

                }


            Spacer(modifier = Modifier.height(28.dp))
            Footer(
                text = "Already have an Account?",
                textButton = "Login",
            ) {
                navController.navigate(ROUTES.SIGN_IN_SCREEN.name)
              //  navigateToSignInScreen()
            }
        }

    }

}
