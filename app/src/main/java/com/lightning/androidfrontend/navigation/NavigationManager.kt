package com.lightning.androidfrontend.navigation

import LocationMapScreen
import TokenManager
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.lightning.androidfrontend.data.repository.UserRepository.VisitorRepository
import com.lightning.androidfrontend.ui.home.CheckoutScreen
import com.lightning.androidfrontend.ui.screens.weclome.*
import com.lightning.androidfrontend.ui.screens.authentication.*
import com.lightning.androidfrontend.ui.screens.home.*

enum class ROUTES {
    SPLASH_SCREEN,
    PAGE_VIEW_SCREEN,
    WELCOME_SCREEN,
    SIGN_IN_SCREEN,
    SIGN_UP_SCREEN,
    RESET_PASSWORD_SCREEN,
    EMAIL_VERIFICATION_SCREEN,
    CHANGE_PASSWORD_SCREEN,
    HOME_SCREEN,
    CHECKOUT_SCREEN,
    REGISTER_CHECK_PHONE_NUMBER_SCREEN,
    REGISTER_PHONE_NUMBER_VERIFICATION_SCREEN,


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun MyNavHost(navHostController: NavHostController) {
    val context = LocalContext.current
   // var startDestination = ROUTES.WELCOME_SCREEN.name
    var startDestination = ROUTES.HOME_SCREEN.name

    val tokenManager = TokenManager(context)
    if(tokenManager.getToken()!=null){
        startDestination=ROUTES.HOME_SCREEN.name
    }

    AnimatedNavHost(
        navController = navHostController,
//        startDestination = setStartDestination()
        startDestination = startDestination
    ) {
        composable(
            route = ROUTES.SPLASH_SCREEN.name,
            exitTransition = {
                fadeOut(tween(400)) + slideOutVertically(
                    tween(400),
                    targetOffsetY = { -400 })
            }) {
            SplashScreen(
                navigateToPageView = {
                    navHostController.popBackStack()
                    navHostController.navigate(route = ROUTES.PAGE_VIEW_SCREEN.name)
                }
            )
        }

        composable(route = ROUTES.PAGE_VIEW_SCREEN.name,
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -300 }) + fadeOut(
                    tween(300)
                )
            }) {
            PageViewScreen(
                navigateToWelcomeScreen = {
                    navHostController.popBackStack()
                    navHostController.navigate(route = ROUTES.WELCOME_SCREEN.name)
                }
            )
        }

        composable(route = ROUTES.WELCOME_SCREEN.name,
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 300 }) + fadeIn(
                    tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -300 }) + fadeIn(
                    tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -300 }) + fadeOut(
                    tween(300)
                )
            }) {
            WelcomeScreen(
                navigateToSignIn = {
                    navHostController.navigate(route = ROUTES.SIGN_IN_SCREEN.name)
                },
                navigateToSignUp = {
                    navHostController.navigate(route = ROUTES.REGISTER_CHECK_PHONE_NUMBER_SCREEN.name)
                })
        }

        composable(route = ROUTES.SIGN_IN_SCREEN.name,
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -300 }) + fadeIn(
                    tween(300)
                )
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 300 }) + fadeIn(
                    tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -300 }) + fadeOut(
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { 300 }) + fadeOut(
                    tween(300)
                )
            }) {
            SignInScreen(
                navigateToResetPasswordScreen = {
                navHostController.navigate(route = ROUTES.RESET_PASSWORD_SCREEN.name)

            }, navigateToSignUpScreen = {
                navHostController.navigate(route = ROUTES.REGISTER_CHECK_PHONE_NUMBER_SCREEN.name) {
                    popUpTo(route = ROUTES.WELCOME_SCREEN.name)
                }
            }, navigateToHomeScreen = {
                navHostController.navigate(route = ROUTES.HOME_SCREEN.name)
            }, authViewModel = AuthViewModel(visitorRepository = VisitorRepository( )))
        }

        composable(route = ROUTES.SIGN_UP_SCREEN.name) {
            SignUpScreen (authViewModel = AuthViewModel(visitorRepository = VisitorRepository( )),
                navController = navHostController)
        }


        composable(route = ROUTES.RESET_PASSWORD_SCREEN.name,
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -300 }) + fadeIn(
                    tween(300)
                )
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 300 }) + fadeIn(
                    tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -300 }) + fadeOut(
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { 300 }) + fadeOut(
                    tween(300)
                )
            }) {
            ResetPasswordScreen(
                authViewModel = AuthViewModel(visitorRepository = VisitorRepository( )),
               navigateToPhoneVerificationScreen = { navHostController.navigate(route = ROUTES.EMAIL_VERIFICATION_SCREEN.name) },
                navController = navHostController
            )


        }
        composable(route = ROUTES.REGISTER_CHECK_PHONE_NUMBER_SCREEN.name,
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -300 }) + fadeIn(
                    tween(300)
                )
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 300 }) + fadeIn(
                    tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -300 }) + fadeOut(
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { 300 }) + fadeOut(
                    tween(300)
                )
            }) {
            RegisterCheckPhoneNumberScreen(
                authViewModel = AuthViewModel(visitorRepository = VisitorRepository( )),
                navController = navHostController
            )


        }
        composable(route = ROUTES.EMAIL_VERIFICATION_SCREEN.name,
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -300 }) + fadeIn(
                    tween(300)
                )
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 300 }) + fadeIn(
                    tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -300 }) + fadeOut(
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { 300 }) + fadeOut(
                    tween(300)
                )
            }) {
            PhoneVerificationScreen (authViewModel = AuthViewModel(visitorRepository = VisitorRepository( )),navController = navHostController , navigateToChangePasswordScreen = { navHostController.navigate(route = ROUTES.CHANGE_PASSWORD_SCREEN.name) })

        }
        composable(route = ROUTES.REGISTER_PHONE_NUMBER_VERIFICATION_SCREEN.name,
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -300 }) + fadeIn(
                    tween(300)
                )
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 300 }) + fadeIn(
                    tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -300 }) + fadeOut(
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { 300 }) + fadeOut(
                    tween(300)
                )
            }) {
            RegisterPhoneVerificationScreen (authViewModel = AuthViewModel(visitorRepository = VisitorRepository( )),navController = navHostController ,
                )

        }
        composable(route = ROUTES.CHANGE_PASSWORD_SCREEN.name,
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 300 }) + fadeIn(
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { 300 }) + fadeOut(
                    tween(300)
                )
            }) {
            ChangePasswordScreen(authViewModel = AuthViewModel(visitorRepository = VisitorRepository( )), navController = navHostController)
        }

        composable(route = ROUTES.HOME_SCREEN.name) {
            HomeNavigation(navScreensController = navHostController
            )
        }

        composable(route = ROUTES.CHECKOUT_SCREEN.name) {
            CheckoutScreen()
        }
    }
}

private fun setStartDestination(): String{

    return if (Build.VERSION.SDK_INT >= 31){
        ROUTES.PAGE_VIEW_SCREEN.name
    }else{
        ROUTES.SPLASH_SCREEN.name
    }
}

