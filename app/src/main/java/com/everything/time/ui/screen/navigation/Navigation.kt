package com.everything.time.ui.screen.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.everything.time.ui.screen.JavaScreen
import com.everything.time.ui.screen.KotlinScreen
import kotlinx.serialization.Serializable

@Serializable
private data object JavaRoute

@Serializable
private data object KotlinRoute

fun rootStartingRoute(): Any = JavaRoute

fun NavGraphBuilder.javaDestination() {
    composable<JavaRoute> {
        JavaScreen()
    }
}

fun NavGraphBuilder.kotlinDestination() {
    composable<KotlinRoute> {
        KotlinScreen()
    }
}

val NavController.isJavaTab: Boolean
    get() = currentDestination?.hasRoute(JavaRoute::class) == true

fun NavController.navigateToJavaTab() {
    if (isJavaTab) return
    navigate(
        route = JavaRoute,
        navOptions = navOptions {
            popUpTo(id = graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        },
    )
}

val NavController.isKotlinTab: Boolean
    get() = currentDestination?.hasRoute(KotlinRoute::class) == true

fun NavController.navigateToKotlinTab() {
    if (isKotlinTab) return
    navigate(
        route = KotlinRoute,
        navOptions = navOptions {
            popUpTo(id = graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        },
    )
}
