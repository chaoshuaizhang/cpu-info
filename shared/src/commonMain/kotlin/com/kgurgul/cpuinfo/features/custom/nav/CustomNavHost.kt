package com.kgurgul.cpuinfo.features.custom.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.savedstate.SavedState
import co.touchlab.kermit.Logger
import com.kgurgul.cpuinfo.features.custom.RouteManager
import com.kgurgul.cpuinfo.features.custom.Routes
import com.kgurgul.cpuinfo.features.custom.pages.JobGroup
import com.kgurgul.cpuinfo.features.custom.pages.main.MainPage
import kotlin.collections.forEach
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
@Composable
fun CustomNavHost(navHostController: NavHostController = rememberNavController()) {
    LaunchedEffect(Unit) {
        launch {
            RouteManager.routePushFlow.collect {
                val topLevelNavOptions = navOptions {
                    // Pop up to the start destination of the graph to avoid building up a large stack of destinations on the back stack as users select items
//                    popUpTo(navHostController.graph.findStartDestination().id) {
//                        saveState = true
//                        inclusive = true
//                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                navHostController.navigate(
                    route = it.route,
                    navOptions = topLevelNavOptions,
                )
            }
        }
    }
    RouteManager.register(MainPage())
    RouteManager.register(JobGroup())
    NavHost(
        navController = navHostController,
        startDestination = Routes.Main
    ) {
        RouteManager.routeMap.forEach { nav ->
            nav.value.invoke(this, navHostController)
        }
    }
}

class CustomNavType : NavType<Logger>(false) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: Logger
    ) {
        TODO("Not yet implemented")
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): Logger? {
        TODO("Not yet implemented")
    }

    override fun parseValue(value: String): Logger {
        TODO("Not yet implemented")
    }
}
