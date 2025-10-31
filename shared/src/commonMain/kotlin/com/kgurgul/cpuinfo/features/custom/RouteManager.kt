package com.kgurgul.cpuinfo.features.custom

import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object RouteManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _routeMap = mutableMapOf<Routes, INavPage>()
    val routeMap: Map<Routes, INavPage> = _routeMap

    private val routePushChannel = Channel<RouteEntity<out Any>>()

    private val routePopChannel = Channel<RouteEntity<out Any>?>()

    val routePushFlow = routePushChannel.receiveAsFlow()

    val routePopFlow = routePopChannel.receiveAsFlow()

    fun register(page: INavPage) {
        if (_routeMap.contains(page.route)) return
        _routeMap[page.route] = page
    }

    fun <T : Routes> push(route: RouteEntity<T>) {
        scope.launch {
            routePushChannel.send(route)
        }
    }

    fun pop(route: RouteEntity<out Routes>? = null) {
        scope.launch {
            routePopChannel.send(route)
        }
    }

    /*
    * 退出app，不杀死，只压后台
    * */
    fun ifMoveAppToBackground(route: String?): Boolean {
        return when (route) {
            Routes.Home.Home::class.qualifiedName,
            Routes.Owner.Owner::class.qualifiedName -> true

            else -> false
        }
    }

    fun isMainPageRoutes(route: String?): Boolean {
        return when (route) {
            Routes.Home::class.qualifiedName,
            Routes.Home.Home::class.qualifiedName,
            Routes.Owner::class.qualifiedName -> true
            Routes.Owner.Owner::class.qualifiedName -> true

            else -> false
        }
    }

}

data class RouteEntity<T : Any>(val route: T)
