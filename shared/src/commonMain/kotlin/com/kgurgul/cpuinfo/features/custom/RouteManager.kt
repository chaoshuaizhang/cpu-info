package com.kgurgul.cpuinfo.features.custom

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object RouteManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _routeMap = mutableMapOf<String, INavPage>()
    val routeMap: Map<String, INavPage> = _routeMap

    private val routePushChannel = Channel<String>()

    val routePushFlow = routePushChannel.receiveAsFlow()

    fun register(page: INavPage) {
        _routeMap[page.route] = page
    }

    fun push(route: String) {
        scope.launch {
            routePushChannel.send(route)
        }
    }

    fun startDestination() = routeMap.keys.first()

}
