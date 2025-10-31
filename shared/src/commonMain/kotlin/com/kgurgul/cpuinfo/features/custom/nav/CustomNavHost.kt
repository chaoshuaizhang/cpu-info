package com.kgurgul.cpuinfo.features.custom.nav

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.savedstate.SavedState
import co.touchlab.kermit.Logger
import com.kgurgul.cpuinfo.features.custom.CustomNavigationBar
import com.kgurgul.cpuinfo.features.custom.RouteManager
import com.kgurgul.cpuinfo.features.custom.Routes
import com.kgurgul.cpuinfo.features.custom.pages.JobGroup
import com.kgurgul.cpuinfo.features.custom.pages.main.HomePage2
import com.kgurgul.cpuinfo.features.custom.viewmodel.MainViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import org.koin.compose.viewmodel.koinViewModel

expect fun moveAppToBackground()

@OptIn(InternalSerializationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomNavHost(
    navHostController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        launch {
            RouteManager.routePushFlow.collect {
                navHostController.navigate(route = it.route) {
                    if (RouteManager.isMainPageRoutes(navHostController.previousBackStackEntry?.destination?.route)
                        && RouteManager.isMainPageRoutes(it.route::class.qualifiedName)
                    ) {
                        // Pop up to the start destination of the graph to avoid building up a large stack of destinations on the back stack as users select items
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            }
        }

        launch {
            RouteManager.routePopFlow.collect {
                if (RouteManager.ifMoveAppToBackground(navHostController.currentDestination?.route)) {
                    moveAppToBackground()
                    return@collect
                }
                it?.route?.let {
                    navHostController.popBackStack(it, true)
                } ?: run {
                    navHostController.popBackStack()
                }
            }
        }
    }

    RouteManager.register(HomePage2())
    RouteManager.register(JobGroup())

    Scaffold(
        bottomBar = {
            CustomNavigationBar(tabs = mainViewModel.mainPageTabs, navHostController) {}
        }
    ) {
        CompositionLocalProvider(LocalNavHostController provides navHostController) {
            NavHost(
                navController = navHostController,
                startDestination = Routes.Home.Home
            ) {
                RouteManager.routeMap.forEach { nav ->
                    nav.value.invoke(this, navHostController)
                }
            }
        }
    }
}

val LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    error("NavHostController not provided")
}
