package com.kgurgul.cpuinfo.features.custom

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

}

data class RouteEntity<T : Any>(val route: T)
