package com.everything.time.ui.screen

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.everything.time.R
import com.everything.time.ui.screen.navigation.isJavaTab
import com.everything.time.ui.screen.navigation.isKotlinTab
import com.everything.time.ui.screen.navigation.javaDestination
import com.everything.time.ui.screen.navigation.kotlinDestination
import com.everything.time.ui.screen.navigation.navigateToJavaTab
import com.everything.time.ui.screen.navigation.navigateToKotlinTab
import com.everything.time.ui.screen.navigation.rootStartingRoute
import com.everything.time.ui.screen.viewmodel.RootAction
import com.everything.time.ui.screen.viewmodel.RootEvent
import com.everything.time.ui.screen.viewmodel.RootViewModel
import com.everything.time.ui.theme.Typography
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun RootScreen(
    viewModel: RootViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    lifecycleOwner: Lifecycle = LocalLifecycleOwner.current.lifecycle,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel
            .eventFlow
            .filter { _ -> lifecycleOwner.currentState.isAtLeast(Lifecycle.State.RESUMED) }
            .onEach { event ->
                when (event) {
                    RootEvent.NavigateToJavaTab -> navController.navigateToJavaTab()
                    RootEvent.NavigateToKotlinTab -> navController.navigateToKotlinTab()
                }
            }
            .launchIn(this)
    }
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.titleMedium,
                    )
                },
            )
        },
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(
                                image = ImageVector.vectorResource(
                                    id = R.drawable.ic_launcher_foreground,
                                ),
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(size = 48.dp),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.java),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    selected = navController.isJavaTab,
                    onClick = { viewModel.sendAction(RootAction.JavaTabClick) },
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(
                                image = ImageVector.vectorResource(
                                    id = R.drawable.ic_launcher_foreground,
                                ),
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(size = 48.dp),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.kotlin),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    selected = navController.isKotlinTab,
                    onClick = { viewModel.sendAction(RootAction.KotlinTabClick) },
                )
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = rootStartingRoute(),
            modifier = Modifier
                .padding(paddingValues = it)
                .consumeWindowInsets(paddingValues = it)
        ) {
            javaDestination()
            kotlinDestination()
        }
    }
}
