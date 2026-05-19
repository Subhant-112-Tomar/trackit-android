package com.trackit.trackit.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trackit.trackit.ui.screens.DashboardScreen
import com.trackit.trackit.ui.screens.LoginScreen
import com.trackit.trackit.ui.screens.RegisterScreen
import com.trackit.trackit.utils.TokenManager
import com.trackit.trackit.viewmodel.ApplicationViewModel
import com.trackit.trackit.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
}

@Composable
fun TrackItNavigation(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token by tokenManager.token.collectAsState(initial = null)

    val authViewModel: AuthViewModel = viewModel()
    val appViewModel: ApplicationViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (!token.isNullOrEmpty()) Screen.Dashboard.route
        else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = appViewModel,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}