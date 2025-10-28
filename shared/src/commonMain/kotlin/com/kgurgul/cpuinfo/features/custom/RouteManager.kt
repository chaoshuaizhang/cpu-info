package com.kgurgul.cpuinfo.features.custom

import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object RouteManager {

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _routeMap = mutableMapOf<String, INavPage>()
    val routeMap: Map<String, INavPage> = _routeMap

    val routePushChannel = Channel<RouteEntity<Any>>()

    val routePushFlow = routePushChannel.receiveAsFlow()

    fun register(page: INavPage) {
        if (_routeMap.contains(page.route)) return
        _routeMap[page.route] = page
    }

    inline fun <reified T : Any> push(route: String, param: T) {
        scope.launch {
            routePushChannel.send(RouteEntity(route, param, T::class))
        }
    }

    fun startDestination() = routeMap.keys.first()

}

data class RouteEntity<T : Any>(
    val route: String,
    val param: T,
    val clazz: KClass<out Any>
)
